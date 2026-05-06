package model;

import utilities.enums.StatoPrenotazione;
import java.time.LocalDateTime;

public class Prenotazione {
    private int idPrenotazione;
    private String emailPasseggero; // Al posto dell'oggetto Utente
    private int idViaggio;
    private StatoPrenotazione stato; // Usiamo l'Enum
    private LocalDateTime dataPrenotazione; // Data moderna

    public Prenotazione(int idPrenotazione, String emailPasseggero, int idViaggio, StatoPrenotazione stato, LocalDateTime dataPrenotazione) {
        this.idPrenotazione = idPrenotazione;
        this.emailPasseggero = emailPasseggero;
        this.idViaggio = idViaggio;
        this.stato = stato;
        this.dataPrenotazione = dataPrenotazione;
    }

    // Costruttore senza ID per le nuove prenotazioni
    public Prenotazione(String emailPasseggero, int idViaggio, StatoPrenotazione stato, LocalDateTime dataPrenotazione) {
        this.emailPasseggero = emailPasseggero;
        this.idViaggio = idViaggio;
        this.stato = stato;
        this.dataPrenotazione = dataPrenotazione;
    }

    // --- GETTER E SETTER ---
    public int getIdPrenotazione() { return idPrenotazione; }
    public void setIdPrenotazione(int idPrenotazione) { this.idPrenotazione = idPrenotazione; }

    public String getEmailPasseggero() { return emailPasseggero; }
    public void setEmailPasseggero(String emailPasseggero) { this.emailPasseggero = emailPasseggero; }

    public int getIdViaggio() { return idViaggio; }
    public void setIdViaggio(int idViaggio) { this.idViaggio = idViaggio; }

    public StatoPrenotazione getStato() { return stato; }
    public void setStato(StatoPrenotazione stato) { this.stato = stato; }

    public LocalDateTime getDataPrenotazione() { return dataPrenotazione; }
    public void setDataPrenotazione(LocalDateTime dataPrenotazione) { this.dataPrenotazione = dataPrenotazione; }
}