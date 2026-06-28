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
    public List<String> recuperaStoricoPasseggeri(String emailGuidatore) throws NoResultException {
        // Delega direttamente al DAO per ottenere la lista di stringhe (email)
        return invitoDAO.trovaStoricoPasseggeri(emailGuidatore);
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
    public void rispondiInvito(InvitoBean invitoBean, StatoInvito risposta) throws DbOperationException {
        try {
            // 1. Aggiorniamo lo stato dell'invito nel DB
            invitoDAO.rispondiInvito(invitoBean.getIdInvito(), risposta);

            // 2. SE ACCETTA: Dobbiamo creare una prenotazione confermata
            if (risposta == StatoInvito.ACCETTATO) {
                PrenotazioneController pController = new PrenotazioneController();

                PrenotazioneBean nuovaPrenotazione = new PrenotazioneBean();
                nuovaPrenotazione.setIdViaggio(invitoBean.getIdViaggio());
                nuovaPrenotazione.setEmailPasseggero(invitoBean.getEmailPasseggero());
                // Forziamo lo stato in CONFERMATA direttamente
                nuovaPrenotazione.setStato(StatoPrenotazione.CONFERMATA);
                nuovaPrenotazione.setDataPrenotazione(java.time.LocalDateTime.now());

                // Salviamo la prenotazione.
                // NOTA: il metodo inviaRichiestaPrenotazione normalmente scala anche i posti!
                pController.inviaRichiestaPrenotazione(nuovaPrenotazione);
            }

            // 3. Rimuoviamo la notifica dall'Observer
            Invito InvitoModel = factory.fromBeanToModel(invitoBean, InvitoBean.class);
            InvitoManagerConcreteSubject subject = InvitoManagerConcreteSubject.getInstance();
            subject.removeInvito(InvitoModel);

        } catch (Exception e) {
            throw new DbOperationException("Errore durante l'elaborazione della risposta all'invito: " + e.getMessage());
        }
    }
}