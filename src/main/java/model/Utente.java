package model;

import java.time.LocalDateTime;

public class Utente {
    private Credentials credentials; // Contiene email e password
    private String nome;
    private String cognome;
    private String telefono;
    private LocalDateTime dataRegistrazione;

    // Costruttore completo (usato quando estraiamo i dati completi dal DB)
    public Utente(Credentials credentials, String nome, String cognome, String telefono, LocalDateTime dataRegistrazione) {
        this.credentials = credentials;
        this.nome = nome;
        this.cognome = cognome;
        this.telefono = telefono;
        this.dataRegistrazione = dataRegistrazione;
    }

    // Costruttore per le nuove registrazioni (dataRegistrazione è autogenerata dal DB)
    public Utente(Credentials credentials, String nome, String cognome, String telefono) {
        this.credentials = credentials;
        this.nome = nome;
        this.cognome = cognome;
        this.telefono = telefono;
    }

    // --- Getters e Setters ---

    public Credentials getCredentials() { return credentials; }
    public void setCredentials(Credentials credentials) { this.credentials = credentials; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public LocalDateTime getDataRegistrazione() { return dataRegistrazione; }
    public void setDataRegistrazione(LocalDateTime dataRegistrazione) { this.dataRegistrazione = dataRegistrazione; }
}