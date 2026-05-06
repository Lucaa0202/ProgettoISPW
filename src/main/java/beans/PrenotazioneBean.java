package beans;

import utilities.enums.StatoPrenotazione;

public class PrenotazioneBean {
    private int idPrenotazione;
    private int idViaggio;
    private String emailPasseggero;
    private StatoPrenotazione stato;

    public PrenotazioneBean(int idPrenotazione, int idViaggio, String emailPasseggero, StatoPrenotazione stato) {
        this.idPrenotazione = idPrenotazione;
        this.idViaggio = idViaggio;
        this.emailPasseggero = emailPasseggero;
        this.stato = stato;
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
}