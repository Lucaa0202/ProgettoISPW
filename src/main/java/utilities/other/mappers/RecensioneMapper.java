package utilities.other.mappers;

import beans.RecensioneBean;
import model.Recensione;
import java.time.LocalDateTime;

public class RecensioneMapper implements BeanAndModelMapper<RecensioneBean, Recensione> {

    @Override
    public Recensione fromBeanToModel(RecensioneBean bean) {
        // Usiamo il costruttore COMPLETO del Model Recensione (7 parametri):
        // 1. idRecensione
        // 2. emailRecensore (da emailAutore del bean)
        // 3. emailRecensito (da emailDestinatario del bean)
        // 4. idViaggio
        // 5. voto
        // 6. commento
        // 7. dataRecensione
        return new Recensione(
                bean.getIdRecensione(),
                bean.getEmailAutore(),
                bean.getEmailDestinatario(),
                bean.getIdViaggio(),
                bean.getVoto(),
                bean.getCommento(),
                LocalDateTime.now() // Impostiamo la data attuale
        );
    }

    @Override
    public RecensioneBean fromModelToBean(Recensione model) {
        // Percorso inverso: dal Model creiamo il Bean per la grafica
        return new RecensioneBean(
                model.getIdRecensione(),
                model.getVoto(),
                model.getCommento(),
                model.getEmailRecensore(), // Usiamo il getter corretto del Model
                model.getEmailRecensito(), // Usiamo il getter corretto del Model
                model.getIdViaggio()
        );
    }
}