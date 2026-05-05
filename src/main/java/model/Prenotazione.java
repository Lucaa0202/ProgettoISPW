package model;

public class Prenotazione {
    private int idPrenotazione;
    private Utente passeggero;
    private Viaggio viaggio;
    private String stato; // es: IN_ATTESA, CONFERMATA, RIFIUTATA
    private String dataPrenotazione; // Usiamo String per semplicità, come nel progetto di riferimento

    public Prenotazione(int idPrenotazione, Utente passeggero, Viaggio viaggio, String stato, String dataPrenotazione) {
        this.idPrenotazione = idPrenotazione;
        this.passeggero = passeggero;
        this.viaggio = viaggio;
        this.stato = stato;
        this.dataPrenotazione = dataPrenotazione;
    }

    public Prenotazione() {}

    public int getIdPrenotazione() { return idPrenotazione; }
    public void setIdPrenotazione(int idPrenotazione) { this.idPrenotazione = idPrenotazione; }

    public Utente getPasseggero() { return passeggero; }
    public void setPasseggero(Utente passeggero) { this.passeggero = passeggero; }

    public Viaggio getViaggio() { return viaggio; }
    public void setViaggio(Viaggio viaggio) { this.viaggio = viaggio; }

    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }

    public String getDataPrenotazione() { return dataPrenotazione; }
    public void setDataPrenotazione(String dataPrenotazione) { this.dataPrenotazione = dataPrenotazione; }
}