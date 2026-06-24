package beans;

import utilities.enums.StatoPrenotazione;
import java.time.LocalDateTime;

public class PrenotazioneBean {
    private int idPrenotazione;
    private int idViaggio;
    private String emailPasseggero;
    private StatoPrenotazione stato;
    private LocalDateTime dataPrenotazione; // <--- AGGIUNTO

    public PrenotazioneBean(int idPrenotazione, int idViaggio, String emailPasseggero, StatoPrenotazione stato, LocalDateTime dataPrenotazione) {
        this.idPrenotazione = idPrenotazione;
        this.idViaggio = idViaggio;
        this.emailPasseggero = emailPasseggero;
        this.stato = stato;
        this.dataPrenotazione = dataPrenotazione;
    }

    public PrenotazioneBean() {}

    // --- GETTER E SETTER ---
    public int getIdPrenotazione() { return idPrenotazione; }
    public void setIdPrenotazione(int idPrenotazione) { this.idPrenotazione = idPrenotazione; }

    public int getIdViaggio() { return idViaggio; }
    public void setIdViaggio(int idViaggio) { this.idViaggio = idViaggio; }

    public String getEmailPasseggero() { return emailPasseggero; }
    public void setEmailPasseggero(String emailPasseggero) { this.emailPasseggero = emailPasseggero; }

    public StatoPrenotazione getStato() { return stato; }
    public void setStato(StatoPrenotazione stato) { this.stato = stato; }

    // --- NUOVI METODI PER LA DATA ---
    public LocalDateTime getDataPrenotazione() { return dataPrenotazione; }
    public void setDataPrenotazione(LocalDateTime dataPrenotazione) { this.dataPrenotazione = dataPrenotazione; }
}