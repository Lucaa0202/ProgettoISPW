package model;

import utilities.enums.StatoPrenotazione;
import java.time.LocalDateTime;

public class Prenotazione {
    private int idPrenotazione;
    private Utente passeggero;
    private Viaggio viaggio;
    private StatoPrenotazione stato; // Usiamo l'Enum
    private LocalDateTime dataPrenotazione; // Data moderna

    public Prenotazione(int idPrenotazione, Utente passeggero, Viaggio viaggio, StatoPrenotazione stato, LocalDateTime dataPrenotazione) {
        this.idPrenotazione = idPrenotazione;
        this.passeggero = passeggero;
        this.viaggio = viaggio;
        this.stato = stato;
        this.dataPrenotazione = dataPrenotazione;
    }

    // Costruttore senza ID per le nuove prenotazioni
    public Prenotazione(Utente passeggero, Viaggio viaggio, StatoPrenotazione stato, LocalDateTime dataPrenotazione) {
        this.passeggero = passeggero;
        this.viaggio = viaggio;
        this.stato = stato;
        this.dataPrenotazione = dataPrenotazione;
    }

    // --- GETTER E SETTER ---
    public int getIdPrenotazione() { return idPrenotazione; }
    public void setIdPrenotazione(int idPrenotazione) { this.idPrenotazione = idPrenotazione; }

    public Utente getPasseggero() { return passeggero; }
    public void setPasseggero(Utente passeggero) { this.passeggero = passeggero; }

    public Viaggio getViaggio() { return viaggio; }
    public void setViaggio(Viaggio viaggio) { this.viaggio = viaggio; }

    public StatoPrenotazione getStato() { return stato; }
    public void setStato(StatoPrenotazione stato) { this.stato = stato; }

    public LocalDateTime getDataPrenotazione() { return dataPrenotazione; }
    public void setDataPrenotazione(LocalDateTime dataPrenotazione) { this.dataPrenotazione = dataPrenotazione; }
}