package dao;

import model.Prenotazione;
import exceptions.NoResultException;
import exceptions.DbOperationException;
import java.util.List;

public interface PrenotazioneDAO {
    // Crea una nuova richiesta di prenotazione
    void inserisciPrenotazione(Prenotazione prenotazione) throws DbOperationException;

    // Trova tutte le prenotazioni fatte da uno specifico utente (passeggero)
    List<Prenotazione> trovaPrenotazioniPasseggero(String emailPasseggero) throws NoResultException;

    // Trova le prenotazioni ricevute per un viaggio specifico (utile per il guidatore)
    List<Prenotazione> trovaPrenotazioniPerViaggio(int idViaggio) throws NoResultException;

    // Aggiorna lo stato della prenotazione (es. da IN_ATTESA a CONFERMATA)
    void aggiornaStatoPrenotazione(Prenotazione prenotazione) throws DbOperationException;
}
