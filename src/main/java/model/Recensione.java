package model;

import java.time.LocalDateTime;

public class Recensione {
    private int idRecensione;
    private String emailRecensore; // Appiattito
    private String emailRecensito; // Appiattito
    private int idViaggio;         // Appiattito
    private int voto;
    private String commento;
    private LocalDateTime dataRecensione;

    // Costruttore CON ID (dal Database)
    public Recensione(int idRecensione, String emailRecensore, String emailRecensito, int idViaggio, int voto, String commento, LocalDateTime dataRecensione) {
        this.idRecensione = idRecensione;
        this.emailRecensore = emailRecensore;
        this.emailRecensito = emailRecensito;
        this.idViaggio = idViaggio;
        this.voto = voto;
        this.commento = commento;
        this.dataRecensione = dataRecensione;
    }

    // Costruttore SENZA ID (Nuova recensione creata dall'App)
    public Recensione(String emailRecensore, String emailRecensito, int idViaggio, int voto, String commento, LocalDateTime dataRecensione) {
        this.emailRecensore = emailRecensore;
        this.emailRecensito = emailRecensito;
        this.idViaggio = idViaggio;
        this.voto = voto;
        this.commento = commento;
        this.dataRecensione = dataRecensione;
    }

    // --- GETTER E SETTER ---
    public int getIdRecensione() { return idRecensione; }
    public void setIdRecensione(int idRecensione) { this.idRecensione = idRecensione; }

    public String getEmailRecensore() { return emailRecensore; }
    public void setEmailRecensore(String emailRecensore) { this.emailRecensore = emailRecensore; }

    public String getEmailRecensito() { return emailRecensito; }
    public void setEmailRecensito(String emailRecensito) { this.emailRecensito = emailRecensito; }

    public int getIdViaggio() { return idViaggio; }
    public void setIdViaggio(int idViaggio) { this.idViaggio = idViaggio; }

    public int getVoto() { return voto; }
    public void setVoto(int voto) { this.voto = voto; }

    public String getCommento() { return commento; }
    public void setCommento(String commento) { this.commento = commento; }

    public LocalDateTime getDataRecensione() { return dataRecensione; }
    public void setDataRecensione(LocalDateTime dataRecensione) { this.dataRecensione = dataRecensione; }
}