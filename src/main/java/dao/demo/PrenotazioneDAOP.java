package dao.demo;

import dao.PrenotazioneDAO;
import dao.demo.shared.SharedResources;
import model.Prenotazione;
import utilities.enums.StatoPrenotazione; // Assicurati che l'import corrisponda al tuo enum
import exceptions.NoResultException;
import exceptions.DbOperationException;

import java.util.ArrayList;
import java.util.List;

public class PrenotazioneDAOP implements PrenotazioneDAO {

    @Override
    public void eliminaPrenotazione(int idPrenotazione) throws DbOperationException {
        // Se la prenotazione non esiste nella mappa, potremmo lanciare un'eccezione
        if (!SharedResources.getInstance().getPrenotazioni().containsKey(idPrenotazione)) {
            throw new DbOperationException("Errore: Prenotazione con ID " + idPrenotazione + " non trovata in memoria.");
        }
        SharedResources.getInstance().getPrenotazioni().remove(idPrenotazione);
    }

    @Override
    public void rifiutaAltreRichieste(int idViaggio) throws DbOperationException {
        boolean trovate = false;
        // Scorro tutte le prenotazioni in memoria
        for (Prenotazione p : SharedResources.getInstance().getPrenotazioni().values()) {
            // Se appartengono al viaggio e sono in attesa, le rifiuto
            // NOTA: adatta "IN_ATTESA" e "RIFIUTATA" in base ai nomi esatti del tuo Enum!
            if (p.getIdViaggio() == idViaggio && p.getStato() == StatoPrenotazione.IN_ATTESA) {
                p.setStato(StatoPrenotazione.RIFIUTATA);
                trovate = true;
            }
        }
        if (!trovate) {
            System.out.println("Nessuna altra richiesta in attesa da rifiutare per il viaggio " + idViaggio);
        }
    }

    @Override
    public void inserisciPrenotazione(Prenotazione prenotazione) throws DbOperationException {
        if (prenotazione == null) {
            throw new DbOperationException("Impossibile inserire una prenotazione nulla.");
        }
        SharedResources.getInstance().getPrenotazioni().put(prenotazione.getIdPrenotazione(), prenotazione);
    }

    @Override
    public List<Prenotazione> trovaPrenotazioniPasseggero(String emailPasseggero) throws NoResultException {
        List<Prenotazione> trovate = new ArrayList<>();
        for (Prenotazione p : SharedResources.getInstance().getPrenotazioni().values()) {
            if (p.getEmailPasseggero().equals(emailPasseggero)) {
                trovate.add(p);
            }
        }
        if (trovate.isEmpty()) {
            throw new NoResultException("Nessuna prenotazione trovata per il passeggero: " + emailPasseggero);
        }
        return trovate;
    }

    @Override
    public List<Prenotazione> trovaPrenotazioniPerViaggio(int idViaggio) throws NoResultException {
        List<Prenotazione> trovate = new ArrayList<>();
        for (Prenotazione p : SharedResources.getInstance().getPrenotazioni().values()) {
            if (p.getIdViaggio() == idViaggio) {
                trovate.add(p);
            }
        }
        if (trovate.isEmpty()) {
            throw new NoResultException("Nessuna prenotazione trovata per il viaggio con ID: " + idViaggio);
        }
        return trovate;
    }

    @Override
    public void aggiornaStatoPrenotazione(Prenotazione prenotazione) throws DbOperationException {
        Prenotazione p = SharedResources.getInstance().getPrenotazioni().get(prenotazione.getIdPrenotazione());
        if (p == null) {
            throw new DbOperationException("Errore: Impossibile aggiornare. Prenotazione non trovata in memoria.");
        }
        // Aggiorno lo stato in memoria referenziando l'oggetto recuperato
        p.setStato(prenotazione.getStato());
    }
}