package beans;

import utilities.enums.StatoInvito;

public class InvitoBean {
    private int idInvito;
    private int idViaggio;
    private String emailPasseggero;
    private StatoInvito stato;

    // Costruttore completo
    public InvitoBean(int idInvito, int idViaggio, String emailPasseggero, StatoInvito stato) {
        this.idInvito = idInvito;
        this.idViaggio = idViaggio;
        this.emailPasseggero = emailPasseggero;
        this.stato = stato;
    }

    // Costruttore per creare un nuovo invito (l'ID lo deciderà il DB)
    public InvitoBean(int idViaggio, String emailPasseggero, StatoInvito stato) {
        this.idViaggio = idViaggio;
        this.emailPasseggero = emailPasseggero;
        this.stato = stato;
    }

    public InvitoBean() {}

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