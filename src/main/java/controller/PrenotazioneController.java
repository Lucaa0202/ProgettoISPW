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
    public void inviaRichiestaPrenotazione(PrenotazioneBean prenotazioneBean) throws DbOperationException {
        Prenotazione prenotazione = factory.fromBeanToModel(prenotazioneBean, PrenotazioneBean.class);

        try {
            // 1. Salviamo nel Database (Assicurati che il tuo DAO abbia inserisciPrenotazione o simile)
            prenotazioneDAO.inserisciPrenotazione(prenotazione);

            // 2. PATTERN OBSERVER: Avvisiamo la grafica del guidatore in tempo reale!
            PrenotazioneManagerConcreteSubject subject = PrenotazioneManagerConcreteSubject.getInstance();
            subject.addPrenotazioneReq(prenotazione);

        } catch (Exception e) {
            throw new DbOperationException("Errore durante l'invio della prenotazione: " + e.getMessage());
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

    // Metodo utility del tuo amico per creare un Bean (createReservationBean)
    public PrenotazioneBean creaPrenotazioneBean(Prenotazione prenotazione) {
        return factory.fromModelToBean(prenotazione, Prenotazione.class);
    }
}