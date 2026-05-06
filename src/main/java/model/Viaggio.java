package model;

import utilities.enums.StatoViaggio;
import java.time.LocalDateTime;

public class Viaggio {
    private int idViaggio;
    private String partenza;
    private String destinazione;
    private LocalDateTime dataOra; // Niente più String!
    private int postiDisponibili;
    private double prezzo;
    private StatoViaggio stato;    // Usiamo l'Enum super sicuro
    private Utente guidatore;

    public Viaggio(int idViaggio, String partenza, String destinazione, LocalDateTime dataOra,
                   int postiDisponibili, double prezzo, StatoViaggio stato, Utente guidatore) {
        this.idViaggio = idViaggio;
        this.partenza = partenza;
        this.destinazione = destinazione;
        this.dataOra = dataOra;
        this.postiDisponibili = postiDisponibili;
        this.prezzo = prezzo;
        this.stato = stato;
        this.guidatore = guidatore;
    }

    public Viaggio(String partenza, String destinazione, LocalDateTime dataOra,
                   int postiDisponibili, double prezzo, StatoViaggio stato, Utente guidatore) {
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

    public LocalDateTime getDataOra() { return dataOra; }
    public void setDataOra(LocalDateTime dataOra) { this.dataOra = dataOra; }

    public int getPostiDisponibili() { return postiDisponibili; }
    public void setPostiDisponibili(int postiDisponibili) { this.postiDisponibili = postiDisponibili; }

    public double getPrezzo() { return prezzo; }
    public void setPrezzo(double prezzo) { this.prezzo = prezzo; }

    public StatoViaggio getStato() { return stato; }
    public void setStato(StatoViaggio stato) { this.stato = stato; }

    public Utente getGuidatore() { return guidatore; }
    public void setGuidatore(Utente guidatore) { this.guidatore = guidatore; }
}