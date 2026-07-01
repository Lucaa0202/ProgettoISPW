package dao.demo.shared;

import model.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SharedResources {

    // Singleton per garantire che ci sia una sola istanza della classe
    private static SharedResources instance = null;

    // --- MAPPE CONDIVISE (Le "Tabelle" del database in RAM) ---

    // Entità principali (Thread-safe)
    private final Map<String, Utente> utenti = new ConcurrentHashMap<>(); // Chiave: email
    private final Map<Integer, Viaggio> viaggi = new ConcurrentHashMap<>(); // Chiave: idViaggio
    private final Map<Integer, Prenotazione> prenotazioni = new ConcurrentHashMap<>(); // Chiave: idPrenotazione
    private final Map<String, Veicolo> veicoli = new ConcurrentHashMap<>(); // Chiave: targa
    private final Map<Integer, Pagamento> pagamenti = new ConcurrentHashMap<>(); // Chiave: idPagamento
    private final Map<Integer, Invito> inviti = new ConcurrentHashMap<>(); // Chiave: idInvito
    private final Map<Integer, Recensione> recensioni = new ConcurrentHashMap<>(); // Chiave: idRecensione

    // Relazioni 1:N (Opzionali, utili se vuoi velocizzare le ricerche senza scorrere tutta la mappa principale)
    private final Map<String, List<Prenotazione>> prenotazioniPerUtente = new HashMap<>(); // Chiave: email passeggero
    private final Map<Integer, List<Prenotazione>> prenotazioniPerViaggio = new HashMap<>(); // Chiave: idViaggio

    private SharedResources() {
    }

    // Metodo per ottenere l'istanza Singleton
    public static synchronized SharedResources getInstance() {
        if(instance == null){
            instance = new SharedResources();
        }
        return instance;
    }

    // --- GETTER PER LE MAPPE CONDIVISE ---

    public Map<String, Utente> getUtenti() {
        return utenti;
    }

    public Map<Integer, Viaggio> getViaggi() {
        return viaggi;
    }

    public Map<Integer, Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    public Map<String, Veicolo> getVeicoli() {
        return veicoli;
    }

    public Map<Integer, Pagamento> getPagamenti() {
        return pagamenti;
    }

    public Map<Integer, Invito> getInviti() {
        return inviti;
    }

    public Map<Integer, Recensione> getRecensioni() {
        return recensioni;
    }

    public Map<String, List<Prenotazione>> getPrenotazioniPerUtente() {
        return prenotazioniPerUtente;
    }

    public Map<Integer, List<Prenotazione>> getPrenotazioniPerViaggio() {
        return prenotazioniPerViaggio;
    }
}
