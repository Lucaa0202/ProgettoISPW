package model;

import utilities.enums.StatoInvito;

public class Invito {
    private int idInvito;
    private int idViaggio;           // <-- Appiattito!
    private String emailPasseggero;  // <-- Appiattito!
    private StatoInvito stato;

    // Costruttore CON ID (dal Database)
    public Invito(int idInvito, int idViaggio, String emailPasseggero, StatoInvito stato) {
        this.idInvito = idInvito;
        this.idViaggio = idViaggio;
        this.emailPasseggero = emailPasseggero;
        this.stato = stato;
    }

    // Costruttore SENZA ID (Nuovo invito creato)
    public Invito(int idViaggio, String emailPasseggero, StatoInvito stato) {
        this.idViaggio = idViaggio;
        this.emailPasseggero = emailPasseggero;
        this.stato = stato;
    }

    // --- GETTER E SETTER ---
    public int getIdInvito() { return idInvito; }
    public void setIdInvito(int idInvito) { this.idInvito = idInvito; }

    public int getIdViaggio() { return idViaggio; }
    public void setIdViaggio(int idViaggio) { this.idViaggio = idViaggio; }

    public String getEmailPasseggero() { return emailPasseggero; }
    public void setEmailPasseggero(String emailPasseggero) { this.emailPasseggero = emailPasseggero; }

    public StatoInvito getStato() { return stato; }
    public void setStato(StatoInvito stato) { this.stato = stato; }
}