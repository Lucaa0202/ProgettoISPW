package model;

public class Utente {
    private String email;
    private String nome;
    private String cognome;
    private String password;
    private String telefono;

    // Costruttore
    public Utente(String email, String nome, String cognome, String password, String telefono) {
        this.email = email;
        this.nome = nome;
        this.cognome = cognome;
        this.password = password;
        this.telefono = telefono;
    }

    // Costruttore vuoto (spesso utile quando si estraggono i dati dal DB)
    public Utente() {}

    // Getters e Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}