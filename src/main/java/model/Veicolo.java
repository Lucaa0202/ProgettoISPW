package model;

public class Veicolo {
    private String targa;
    private String marca;
    private String modello;
    private int annoImmatricolazione;
    private String emailProprietario; // <-- Appiattito! Niente più oggetto Utente

    // Un solo costruttore: la targa la sappiamo fin dall'inizio, non la genera il database
    public Veicolo(String targa, String marca, String modello, int annoImmatricolazione, String emailProprietario) {
        this.targa = targa;
        this.marca = marca;
        this.modello = modello;
        this.annoImmatricolazione = annoImmatricolazione;
        this.emailProprietario = emailProprietario;
    }

    // --- GETTER E SETTER ---
    public String getTarga() { return targa; }
    public void setTarga(String targa) { this.targa = targa; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModello() { return modello; }
    public void setModello(String modello) { this.modello = modello; }

    public int getAnnoImmatricolazione() { return annoImmatricolazione; }
    public void setAnnoImmatricolazione(int annoImmatricolazione) { this.annoImmatricolazione = annoImmatricolazione; }

    public String getEmailProprietario() { return emailProprietario; }
    public void setEmailProprietario(String emailProprietario) { this.emailProprietario = emailProprietario; }
}