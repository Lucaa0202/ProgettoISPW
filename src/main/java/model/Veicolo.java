package model;

public class Veicolo {
    private String targa;
    private String marca;
    private String modello;
    private int annoImmatricolazione;
    private Utente proprietario; // Oggetto Utente invece della stringa email

    public Veicolo(String targa, String marca, String modello, int annoImmatricolazione, Utente proprietario) {
        this.targa = targa;
        this.marca = marca;
        this.modello = modello;
        this.annoImmatricolazione = annoImmatricolazione;
        this.proprietario = proprietario;
    }

    public Veicolo() {}

    public String getTarga() { return targa; }
    public void setTarga(String targa) { this.targa = targa; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModello() { return modello; }
    public void setModello(String modello) { this.modello = modello; }

    public int getAnnoImmatricolazione() { return annoImmatricolazione; }
    public void setAnnoImmatricolazione(int annoImmatricolazione) { this.annoImmatricolazione = annoImmatricolazione; }

    public Utente getProprietario() { return proprietario; }
    public void setProprietario(Utente proprietario) { this.proprietario = proprietario; }
}