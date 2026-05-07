package utilities.other.mappers;

import beans.ViaggioBean;
import model.Viaggio;
import utilities.enums.StatoViaggio;

public class ViaggioMapper implements BeanAndModelMapper<ViaggioBean, Viaggio> {

    @Override
    public Viaggio fromBeanToModel(ViaggioBean bean) {
        // Dobbiamo rispettare l'ordine esatto del tuo costruttore:
        // (int idViaggio, String partenza, String destinazione, LocalDateTime dataOra,
        //  int postiDisponibili, double prezzo, StatoViaggio stato, String emailGuidatore)

        return new Viaggio(
                bean.getIdViaggio(),
                bean.getPartenza(),
                bean.getArrivo(),           // Mappa 'arrivo' del Bean su 'destinazione' del Model
                bean.getDataOraPartenza(),  // Mappa 'dataOraPartenza' su 'dataOra'
                bean.getPostiDisponibili(),
                bean.getContributoCondiviso(), // Mappa 'contributo' su 'prezzo'
                StatoViaggio.ATTIVO,        // Valore di default (cambialo se il tuo Enum è diverso)
                bean.getEmailGuidatore()
        );
    }

    @Override
    public ViaggioBean fromModelToBean(Viaggio model) {
        // Qui mappiamo dal Model verso la "valigetta" (Bean) per la grafica:
        // (int id, String email, String partenza, String arrivo, LocalDateTime data, int posti, double contributo)

        return new ViaggioBean(
                model.getIdViaggio(),
                model.getEmailGuidatore(),
                model.getPartenza(),
                model.getDestinazione(),    // Dal Model alla grafica
                model.getDataOra(),
                model.getPostiDisponibili(),
                model.getPrezzo()           // Dal Model alla grafica
        );
    }
}