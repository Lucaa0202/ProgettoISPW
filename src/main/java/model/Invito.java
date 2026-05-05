package model;

public class Invito {
    private int idInvito;
    private Viaggio viaggio;
    private Utente passeggero; // L'utente che riceve l'invito
    private String stato; // es: PENDENTE, ACCETTATO, RIFIUTATO

    public Invito(int idInvito, Viaggio viaggio, Utente passeggero, String stato) {
        this.idInvito = idInvito;
        this.viaggio = viaggio;
        this.passeggero = passeggero;
        this.stato = stato;
    }

    public Invito() {}

    public int getIdInvito() { return idInvito; }
    public void setIdInvito(int idInvito) { this.idInvito = idInvito; }

    public Viaggio getViaggio() { return viaggio; }
    public void setViaggio(Viaggio viaggio) { this.viaggio = viaggio; }

    public Utente getPasseggero() { return passeggero; }
    public void setPasseggero(Utente passeggero) { this.passeggero = passeggero; }

    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }
}