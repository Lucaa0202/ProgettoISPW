package controller;

import beans.PrenotazioneBean;
import dao.PrenotazioneDAO;
import exceptions.DbOperationException;
import exceptions.NoResultException;
import model.Prenotazione;
import patterns.factory.BeanAndModelMapperFactory;
import patterns.factory.FactoryDAO;
import patterns.observer.PrenotazioneManagerConcreteSubject;

import java.util.ArrayList;
import java.util.List;

public class PrenotazioneController {

    private final BeanAndModelMapperFactory factory;
    private final PrenotazioneDAO prenotazioneDAO;

    public PrenotazioneController() {
        this.factory = BeanAndModelMapperFactory.getInstance();
        // Assicurati che in FactoryDAO ci sia il metodo getPrenotazioneDAO()
        this.prenotazioneDAO = FactoryDAO.getPrenotazioneDAO();
    }

    // 1. IL PASSEGGERO INVIA LA PRENOTAZIONE
    public void inviaRichiestaPrenotazione(beans.PrenotazioneBean prenotazioneBean) throws exceptions.DbOperationException {
        // Convertiamo il Bean in Model per passarlo alla logica di business
        model.Prenotazione prenotazione = factory.fromBeanToModel(prenotazioneBean, beans.PrenotazioneBean.class);

        try {
            // 1. Salviamo nel Database
            prenotazioneDAO.inserisciPrenotazione(prenotazione);

            // 2. PATTERN OBSERVER: Avvisiamo la grafica del guidatore in tempo reale!
            patterns.observer.PrenotazioneManagerConcreteSubject.getInstance().addPrenotazioneReq(prenotazione);

        } catch (Exception e) {
            throw new exceptions.DbOperationException("Errore durante l'invio della prenotazione: " + e.getMessage());
        }
    }

    // 2. IL GUIDATORE RIFIUTA/ELIMINA LA PRENOTAZIONE
    // Esatta copia logica del suo deleteReservationReq
    public void eliminaRichiestaPrenotazione(PrenotazioneBean prenotazioneBean) throws DbOperationException {
        Prenotazione prenotazione = factory.fromBeanToModel(prenotazioneBean, PrenotazioneBean.class);

        try {
            // CORREZIONE: Passiamo l'ID invece dell'oggetto completo
            prenotazioneDAO.eliminaPrenotazione(prenotazione.getIdPrenotazione());

            // PATTERN OBSERVER: Rimane uguale
            PrenotazioneManagerConcreteSubject subject = PrenotazioneManagerConcreteSubject.getInstance();
            subject.removePrenotazioneReq(prenotazione);

        } catch (Exception e) {
            throw new DbOperationException("Errore durante l'eliminazione della prenotazione: " + e.getMessage());
        }
    }

    // =================================================================================
    // 3. NUOVO METODO: IL GUIDATORE VISUALIZZA LE RICHIESTE IN SOSPESO (Stile BodyBuilding)
    // =================================================================================
    public List<PrenotazioneBean> caricaRichiestePerViaggio(int idViaggio) throws NoResultException {
        // 1. Peschiamo dal Database usando il tuo DAO
        List<Prenotazione> listaDalDB = prenotazioneDAO.trovaPrenotazioniPerViaggio(idViaggio);

        // 2. [IL SEGRETO DELL'OBSERVER] Carichiamo la memoria del Subject!
        PrenotazioneManagerConcreteSubject.getInstance().loadPrenotazioni(listaDalDB);

        // 3. Trasformiamo i Model in Bean per passarli alla Grafica (usando il tuo Mapper)
        List<PrenotazioneBean> listaBean = new ArrayList<>();
        for (Prenotazione p : listaDalDB) {
            listaBean.add(factory.fromModelToBean(p, Prenotazione.class));
        }

        return listaBean;
    }

    public boolean accettaPrenotazione(beans.PrenotazioneBean prenotazione) throws Exception {
        dao.PrenotazioneDAO prenotaDAO = new dao.full.sql.PrenotazioneDAOSQL();
        dao.ViaggioDAO viaggioDAO = new dao.full.sql.ViaggioDAOSQL();

        // 1. Cambiamo lo stato in CONFERMATA
        model.Prenotazione pModel = factory.fromBeanToModel(prenotazione, beans.PrenotazioneBean.class);
        pModel.setStato(utilities.enums.StatoPrenotazione.CONFERMATA);
        prenotaDAO.aggiornaStatoPrenotazione(pModel);

        // 2. Scaliamo il posto dall'auto!
        model.Viaggio v = viaggioDAO.recuperaViaggio(prenotazione.getIdViaggio());
        int postiRimasti = v.getPostiDisponibili() - 1;
        viaggioDAO.aggiornaPostiDisponibili(v.getIdViaggio(), postiRimasti);

        // 3. Rimuoviamo QUESTA specifica richiesta dalle notifiche
        patterns.observer.PrenotazioneManagerConcreteSubject.getInstance().removePrenotazioneReq(pModel);

        System.out.println("Prenotazione confermata e posto scalato!");

        // 4. LA MAGIA DELL'OVERBOOKING
        if (postiRimasti == 0) {
            System.out.println("Ultimo posto assegnato! Chiusura del volo...");

            // Rifiutiamo sul Database tutte le altre richieste pendenti per questo viaggio
            prenotaDAO.rifiutaAltreRichieste(v.getIdViaggio());

            // Ripuliamo istantaneamente la memoria dell'Observer da eventuali "fantasmi"
            // che stavano ancora aspettando per questo specifico viaggio
            patterns.observer.PrenotazioneManagerConcreteSubject.getInstance().getPrenotazioniReq().removeIf(
                    p -> p.getIdViaggio() == v.getIdViaggio()
            );

            System.out.println("Tutte le altre richieste in attesa sono state rifiutate in automatico!");
            return true; // Comunica alla GUI che la macchina è piena
        }

        return false; // Comunica alla GUI che ci sono ancora posti liberi
    }

    public void rifiutaPrenotazione(beans.PrenotazioneBean prenotazione) throws Exception {
        dao.PrenotazioneDAO prenotaDAO = new dao.full.sql.PrenotazioneDAOSQL();

        // 1. Cambiamo lo stato in RIFIUTATA usando la Factory! I posti in auto non vengono toccati
        model.Prenotazione pModel = factory.fromBeanToModel(prenotazione, beans.PrenotazioneBean.class);
        pModel.setStato(utilities.enums.StatoPrenotazione.RIFIUTATA);
        prenotaDAO.aggiornaStatoPrenotazione(pModel);

        // 2. PATTERN OBSERVER: Rimuoviamo la richiesta dalle notifiche!
        patterns.observer.PrenotazioneManagerConcreteSubject.getInstance().removePrenotazioneReq(pModel);

        System.out.println("Prenotazione rifiutata e notifica Observer rimossa.");
    }

    // --- NUOVO METODO: Recupera i viaggi in cui sono PASSEGGERO ---
    public void recuperaPrenotazioniPasseggero(String email, List<beans.PrenotazioneBean> prenotazioniBeanList) throws Exception {

        dao.PrenotazioneDAO prenotazioneDAO = new dao.full.sql.PrenotazioneDAOSQL();

        // Chiamiamo il metodo del DAO che avevamo sistemato prima!
        List<model.Prenotazione> prenotazioniModel = prenotazioneDAO.trovaPrenotazioniPasseggero(email);

        utilities.other.mappers.PrenotazioneMapper mapper = new utilities.other.mappers.PrenotazioneMapper();
        for (model.Prenotazione prenotazione : prenotazioniModel) {
            prenotazioniBeanList.add(mapper.fromModelToBean(prenotazione));
        }
    }

    // Metodo utility del tuo amico per creare un Bean (createReservationBean)
    public PrenotazioneBean creaPrenotazioneBean(Prenotazione prenotazione) {
        return factory.fromModelToBean(prenotazione, Prenotazione.class);
    }
}