package patterns.observer;


import model.Prenotazione;
import java.util.ArrayList;
import java.util.List;

public class PrenotazioneManagerConcreteSubject extends Subject {

    private static PrenotazioneManagerConcreteSubject instance = null;
    private List<Prenotazione> reservationsReq = new ArrayList<>(); // Nome lista uguale al suo

    private PrenotazioneManagerConcreteSubject() {}

    // Singleton - Identico al suo
    public static PrenotazioneManagerConcreteSubject getInstance() {
        if (instance == null) {
            instance = new PrenotazioneManagerConcreteSubject();
        }
        return instance;
    }

    // Lui lo chiama addReservationReq, noi addPrenotazioneReq
    public void addPrenotazioneReq(Prenotazione prenotazioneReq) {
        reservationsReq.add(prenotazioneReq);
        notifyObservers();
    }

    // Ecco la parte "critica": la rimozione con lo stesso stile logico del suo
    public void removePrenotazioneReq(Prenotazione prenotazione) {
        reservationsReq.removeIf(p ->
                p.getEmailPasseggero().equals(prenotazione.getEmailPasseggero()) &&
                        // Convertiamo in Stringa per un confronto antiproiettile!
                        String.valueOf(p.getIdViaggio()).equals(String.valueOf(prenotazione.getIdViaggio()))
        );
        notifyObservers();
    }

    public List<Prenotazione> getPrenotazioniReq() {
        return reservationsReq;
    }

    // Identico al suo loadReservations
    public void loadPrenotazioni(List<Prenotazione> prenotazioni) {
        this.reservationsReq = prenotazioni;
    }
}