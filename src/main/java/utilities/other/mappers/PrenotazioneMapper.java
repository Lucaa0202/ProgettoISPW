package utilities.other.mappers;

import beans.PrenotazioneBean;
import model.Prenotazione;
import java.time.LocalDateTime;

public class PrenotazioneMapper implements BeanAndModelMapper<PrenotazioneBean, Prenotazione> {

    @Override
    public Prenotazione fromBeanToModel(PrenotazioneBean bean) {
        // Seguiamo l'ordine esatto richiesto dal tuo Model:
        // (String emailPasseggero, int idViaggio, StatoPrenotazione stato, LocalDateTime data)
        Prenotazione prenotazione = new Prenotazione(
                bean.getEmailPasseggero(),
                bean.getIdViaggio(),
                bean.getStato(),
                bean.getDataPrenotazione() // <-- CORREZIONE: Usiamo la data che arriva dalla grafica!
        );

        // Visto che l'ID della prenotazione non è nel costruttore del Model, lo impostiamo col setter
        prenotazione.setIdPrenotazione(bean.getIdPrenotazione());

        return prenotazione;
    }

    @Override
    public PrenotazioneBean fromModelToBean(Prenotazione model) {
        // Qui l'ordine del Bean (aggiornato) è:
        // (int idPrenotazione, int idViaggio, String email, StatoPrenotazione, LocalDateTime dataPrenotazione)
        return new PrenotazioneBean(
                model.getIdPrenotazione(),
                model.getIdViaggio(),
                model.getEmailPasseggero(),
                model.getStato(),
                model.getDataPrenotazione() // <-- CORREZIONE: Aggiunto il quinto parametro!
        );
    }
}