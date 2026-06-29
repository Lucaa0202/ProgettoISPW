package controller;

import beans.InvitoBean;
import beans.PrenotazioneBean;
import dao.InvitoDAO;
import exceptions.DbOperationException;
import exceptions.NoResultException;
import model.Invito;
import utilities.enums.StatoInvito;
import utilities.enums.StatoPrenotazione;
import patterns.factory.BeanAndModelMapperFactory;
import patterns.factory.FactoryDAO;
import patterns.observer.InvitoManagerConcreteSubject;

import java.util.ArrayList;
import java.util.List;

public class InvitoController {

    private final BeanAndModelMapperFactory factory;
    private final InvitoDAO invitoDAO;

    public InvitoController() {
        this.factory = BeanAndModelMapperFactory.getInstance();
        this.invitoDAO = FactoryDAO.getInvitoDAO();
    }

    // =========================================================================
    // 1. IL GUIDATORE RECUPERA LO STORICO (Per la tendina)
    // =========================================================================
    // =========================================================================
    // 1. IL GUIDATORE RECUPERA LO STORICO (Ora con Filtro Anti-Doppioni!)
    // =========================================================================
    public List<String> recuperaStoricoPasseggeri(String emailGuidatore, int idViaggioAttuale) throws NoResultException {
        // Prendiamo tutto lo storico dal DAO
        List<String> storico = invitoDAO.trovaStoricoPasseggeri(emailGuidatore);

        // Filtro: Rimuoviamo chi ha già una prenotazione per questo specifico viaggio!
        try {
            dao.PrenotazioneDAO pDao = new dao.full.sql.PrenotazioneDAOSQL();
            List<model.Prenotazione> giaPrenotati = pDao.trovaPrenotazioniPerViaggio(idViaggioAttuale);
            for (model.Prenotazione p : giaPrenotati) {
                storico.remove(p.getEmailPasseggero()); // Se c'è, lo toglie dalla lista
            }
        } catch (NoResultException e) {
            // Nessuno è ancora prenotato, perfetto, non rimuoviamo nessuno.
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (storico.isEmpty()) {
            throw new NoResultException("Nessun passeggero disponibile. (Lo storico è vuoto o sono già tutti invitati/prenotati per questo viaggio).");
        }
        return storico;
    }

    // =========================================================================
    // 2. IL GUIDATORE INVIA UN INVITO
    // =========================================================================
    public void inviaInvito(InvitoBean invitoBean) throws DbOperationException {
        Invito invito = factory.fromBeanToModel(invitoBean, InvitoBean.class);

        try {
            invitoDAO.creaInvito(invito);

            // Avvisiamo l'Observer
            InvitoManagerConcreteSubject subject = InvitoManagerConcreteSubject.getInstance();
            subject.addInvito(invito);

        } catch (Exception e) {
            throw new DbOperationException("Errore durante l'invio dell'invito: " + e.getMessage());
        }
    }

    // =========================================================================
    // 3. IL PASSEGGERO VEDE I SUOI INVITI
    // =========================================================================
    public void recuperaInvitiRicevuti(String emailPasseggero, List<InvitoBean> invitiBeanList) throws NoResultException {
        invitiBeanList.clear();
        try {
            List<Invito> invitiModel = invitoDAO.trovaInvitiRicevuti(emailPasseggero);

            // Filtriamo: vogliamo mostrare al passeggero solo gli inviti in attesa di risposta
            for (Invito i : invitiModel) {
                if (i.getStato() == StatoInvito.PENDING) {
                    InvitoBean iBean = factory.fromModelToBean(i, Invito.class);
                    invitiBeanList.add(iBean);
                }
            }

            if (invitiBeanList.isEmpty()) {
                throw new NoResultException("Non hai nessun nuovo invito in sospeso.");
            }

        } catch (NoResultException e) {
            throw e;
        } catch (Exception e) {
            throw new NoResultException("Errore nel recupero degli inviti: " + e.getMessage());
        }
    }

    // =========================================================================
    // 4. IL PASSEGGERO RISPONDE (Magia dell'accettazione)
    // =========================================================================
    // =========================================================================
    // 4. IL PASSEGGERO RISPONDE (Con scalata automatica e prenotazione SILENZIOSA)
    // =========================================================================
    // =========================================================================
    // 4. IL PASSEGGERO RISPONDE (Con prenotazione silenziosa e UPDATE forzato)
    // =========================================================================
    public void rispondiInvito(InvitoBean invitoBean, StatoInvito risposta) throws DbOperationException {
        try {
            invitoDAO.rispondiInvito(invitoBean.getIdInvito(), risposta);

            if (risposta == StatoInvito.ACCETTATO) {
                dao.ViaggioDAO viaggioDAO = new dao.full.sql.ViaggioDAOSQL();
                model.Viaggio viaggio = viaggioDAO.recuperaViaggio(invitoBean.getIdViaggio());

                if (viaggio.getPostiDisponibili() <= 0) {
                    throw new DbOperationException("Ops! Nel frattempo l'auto si è riempita.");
                }

                PrenotazioneBean nuovaPrenotazione = new PrenotazioneBean();
                nuovaPrenotazione.setIdViaggio(invitoBean.getIdViaggio());
                nuovaPrenotazione.setEmailPasseggero(invitoBean.getEmailPasseggero());
                nuovaPrenotazione.setStato(utilities.enums.StatoPrenotazione.CONFERMATA);
                nuovaPrenotazione.setDataPrenotazione(java.time.LocalDateTime.now());

                model.Prenotazione pModel = factory.fromBeanToModel(nuovaPrenotazione, PrenotazioneBean.class);
                dao.PrenotazioneDAO pDao = new dao.full.sql.PrenotazioneDAOSQL();

                // 1. Inseriamo la prenotazione (Il DB probabilmente la forza a IN_ATTESA)
                pDao.inserisciPrenotazione(pModel);

                // 2. --- FIX: AGGIORNAMENTO FORZATO ---
                // Ripeschiamo la prenotazione appena creata per avere il suo ID reale
                // e le cambiamo lo stato con un Update diretto nel DB!
                List<model.Prenotazione> lista = pDao.trovaPrenotazioniPerViaggio(invitoBean.getIdViaggio());
                for (model.Prenotazione p : lista) {
                    if (p.getEmailPasseggero().equals(invitoBean.getEmailPasseggero())) {
                        p.setStato(utilities.enums.StatoPrenotazione.CONFERMATA);
                        pDao.aggiornaStatoPrenotazione(p); // Forza il DB a mettere CONFERMATA!
                        break;
                    }
                }

                // 3. Scaliamo il posto dall'auto
                viaggioDAO.aggiornaPostiDisponibili(viaggio.getIdViaggio(), viaggio.getPostiDisponibili() - 1);
            }

            // Rimuoviamo la notifica dell'invito
            Invito invitoModel = factory.fromBeanToModel(invitoBean, InvitoBean.class);
            InvitoManagerConcreteSubject subject = InvitoManagerConcreteSubject.getInstance();
            subject.removeInvito(invitoModel);

        } catch (Exception e) {
            throw new DbOperationException(e.getMessage());
        }
    }
}