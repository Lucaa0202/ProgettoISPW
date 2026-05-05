package model;

public class Viaggio {
    private int idViaggio; // Autogenerato dal DB
    private String partenza;
    private String destinazione;
    private String dataOra; // Usiamo String per semplicità, oppure java.time.LocalDateTime
    private int postiDisponibili;
    private double prezzo;
    private String stato;

    // Al posto della semplice stringa "email_guidatore", inseriamo l'oggetto Utente
    private Utente guidatore;

    // Costruttore completo
    public Viaggio(int idViaggio, String partenza, String destinazione, String dataOra,
                   int postiDisponibili, double prezzo, String stato, Utente guidatore) {
        this.idViaggio = idViaggio;
        this.partenza = partenza;
        this.destinazione = destinazione;
        this.dataOra = dataOra;
        this.postiDisponibili = postiDisponibili;
        this.prezzo = prezzo;
        this.stato = stato;
        this.guidatore = guidatore;
    }

    // Costruttore senza ID (usato quando creiamo un viaggio nuovo prima di salvarlo nel DB)
    public Viaggio(String partenza, String destinazione, String dataOra,
                   int postiDisponibili, double prezzo, String stato, Utente guidatore) {
        this.partenza = partenza;
        this.destinazione = destinazione;
        this.dataOra = dataOra;
        this.postiDisponibili = postiDisponibili;
        this.prezzo = prezzo;
        this.stato = stato;
        this.guidatore = guidatore;
    }

    // --- GETTER E SETTER ---
    public int getIdViaggio() { return idViaggio; }
    public void setIdViaggio(int idViaggio) { this.idViaggio = idViaggio; }

    public String getPartenza() { return partenza; }
    public void setPartenza(String partenza) { this.partenza = partenza; }

    public String getDestinazione() { return destinazione; }
    public void setDestinazione(String destinazione) { this.destinazione = destinazione; }

    public String getDataOra() { return dataOra; }
    public void setDataOra(String dataOra) { this.dataOra = dataOra; }

    public int getPostiDisponibili() { return postiDisponibili; }
    public void setPostiDisponibili(int postiDisponibili) { this.postiDisponibili = postiDisponibili; }

    public double getPrezzo() { return prezzo; }
    public void setPrezzo(double prezzo) { this.prezzo = prezzo; }

    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }

    public Utente getGuidatore() { return guidatore; }
    public void setGuidatore(Utente guidatore) { this.guidatore = guidatore; }
}