package model;

import java.time.LocalDateTime;

public class Recensione {
    private int idRecensione;
    private Utente recensore;
    private Utente recensito;
    private Viaggio viaggio;
    private int voto;
    private String commento;
    private LocalDateTime dataRecensione;

    public Recensione(int idRecensione, Utente recensore, Utente recensito, Viaggio viaggio, int voto, String commento, LocalDateTime dataRecensione) {
        this.idRecensione = idRecensione;
        this.recensore = recensore;
        this.recensito = recensito;
        this.viaggio = viaggio;
        this.voto = voto;
        this.commento = commento;
        this.dataRecensione = dataRecensione;
    }

    public Recensione(Utente recensore, Utente recensito, Viaggio viaggio, int voto, String commento, LocalDateTime dataRecensione) {
        this.recensore = recensore;
        this.recensito = recensito;
        this.viaggio = viaggio;
        this.voto = voto;
        this.commento = commento;
        this.dataRecensione = dataRecensione;
    }

    // --- GETTER E SETTER ---
    public int getIdRecensione() { return idRecensione; }
    public void setIdRecensione(int idRecensione) { this.idRecensione = idRecensione; }

    public Utente getRecensore() { return recensore; }
    public void setRecensore(Utente recensore) { this.recensore = recensore; }

    public Utente getRecensito() { return recensito; }
    public void setRecensito(Utente recensito) { this.recensito = recensito; }

    public Viaggio getViaggio() { return viaggio; }
    public void setViaggio(Viaggio viaggio) { this.viaggio = viaggio; }

    public int getVoto() { return voto; }
    public void setVoto(int voto) { this.voto = voto; }

    public String getCommento() { return commento; }
    public void setCommento(String commento) { this.commento = commento; }

    public LocalDateTime getDataRecensione() { return dataRecensione; }
    public void setDataRecensione(LocalDateTime dataRecensione) { this.dataRecensione = dataRecensione; }
}