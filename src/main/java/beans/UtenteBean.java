package beans;

public class UtenteBean {
    private CredenzialiBean credenziali;
    private String nome;
    private String cognome;
    private String telefono; // <--- AGGIUNTO

    // Costruttore con parametri (aggiornato per includere telefono)
    public UtenteBean(CredenzialiBean credenziali, String nome, String cognome, String telefono) {
        this.credenziali = credenziali;
        this.nome = nome;
        this.cognome = cognome;
        this.telefono = telefono; // <--- AGGIUNTO
    }

    // Costruttore vuoto
    public UtenteBean() {}

    // --- GETTER E SETTER ---
    public CredenzialiBean getCredenziali() {
        return credenziali;
    }

    public void setCredenziali(CredenzialiBean credenziali) {
        this.credenziali = credenziali;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    // --- NUOVI METODI PER IL TELEFONO ---
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
