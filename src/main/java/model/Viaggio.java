package model;

import utilities.enums.StatoViaggio;
import java.time.LocalDateTime;

public class Viaggio {
    private int idViaggio;
    private String partenza;
    private String destinazione;
    private LocalDateTime dataOra;
    private int postiDisponibili;
    private double prezzo;
    private StatoViaggio stato;
    private String emailGuidatore;
    // --- NUOVO ATTRIBUTO ---
    private String targaVeicolo;

    // Costruttore COMPLETO (usato quando leggiamo dal Database)
    public Viaggio(int idViaggio, String partenza, String destinazione, LocalDateTime dataOra,
                   int postiDisponibili, double prezzo, StatoViaggio stato, String emailGuidatore, String targaVeicolo) {
        this.idViaggio = idViaggio;
        this.partenza = partenza;
        this.destinazione = destinazione;
        this.dataOra = dataOra;
        this.postiDisponibili = postiDisponibili;
        this.prezzo = prezzo;
        this.stato = stato;
        this.emailGuidatore = emailGuidatore;
        this.targaVeicolo = targaVeicolo;
    }

    // Costruttore SENZA ID (usato quando creiamo un viaggio nuovo, l'ID lo mette MySQL)
    public Viaggio(String partenza, String destinazione, LocalDateTime dataOra,
                   int postiDisponibili, double prezzo, StatoViaggio stato, String emailGuidatore, String targaVeicolo) {
        this.partenza = partenza;
        this.destinazione = destinazione;
        this.dataOra = dataOra;
        this.postiDisponibili = postiDisponibili;
        this.prezzo = prezzo;
        this.stato = stato;
        this.emailGuidatore = emailGuidatore;
        this.targaVeicolo = targaVeicolo;
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

    public String getEmailGuidatore() { return emailGuidatore; }
    public void setEmailGuidatore(String emailGuidatore) { this.emailGuidatore = emailGuidatore; }

    public String getTargaVeicolo() { return targaVeicolo; }
    public void setTargaVeicolo(String targaVeicolo) { this.targaVeicolo = targaVeicolo; }
}