package model;

import utilities.enums.StatoInvito;

public class Invito {
    private int idInvito;
    private Viaggio viaggio;
    private Utente passeggero;
    private StatoInvito stato;

    public Invito(int idInvito, Viaggio viaggio, Utente passeggero, StatoInvito stato) {
        this.idInvito = idInvito;
        this.viaggio = viaggio;
        this.passeggero = passeggero;
        this.stato = stato;
    }

    public Invito(Viaggio viaggio, Utente passeggero, StatoInvito stato) {
        this.viaggio = viaggio;
        this.passeggero = passeggero;
        this.stato = stato;
    }

    // --- GETTER E SETTER ---
    public int getIdInvito() { return idInvito; }
    public void setIdInvito(int idInvito) { this.idInvito = idInvito; }

    public Viaggio getViaggio() { return viaggio; }
    public void setViaggio(Viaggio viaggio) { this.viaggio = viaggio; }

    public Utente getPasseggero() { return passeggero; }
    public void setPasseggero(Utente passeggero) { this.passeggero = passeggero; }

    public StatoInvito getStato() { return stato; }
    public void setStato(StatoInvito stato) { this.stato = stato; }
}