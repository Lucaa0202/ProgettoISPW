package model;

public class Recensione {
    private int idRecensione;
    private Utente recensore; // Chi scrive la recensione
    private Utente recensito; // Chi riceve la recensione
    private Viaggio viaggio;  // Il viaggio di riferimento
    private int voto;         // Da 1 a 5
    private String commento;
    private String dataRecensione;

    public Recensione(int idRecensione, Utente recensore, Utente recensito, Viaggio viaggio, int voto, String commento, String dataRecensione) {
        this.idRecensione = idRecensione;
        this.recensore = recensore;
        this.recensito = recensito;
        this.viaggio = viaggio;
        this.voto = voto;
        this.commento = commento;
        this.dataRecensione = dataRecensione;
    }

    public Recensione() {}

    // Getters and Setters
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

    public String getDataRecensione() { return dataRecensione; }
    public void setDataRecensione(String dataRecensione) { this.dataRecensione = dataRecensione; }
}