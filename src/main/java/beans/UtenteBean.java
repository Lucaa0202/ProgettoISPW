package beans;

public class UtenteBean {
    private CredenzialiBean credenziali; // <-- Composizione, proprio come il tuo amico!
    private String nome;
    private String cognome;

    // Costruttore
    public UtenteBean(CredenzialiBean credenziali, String nome, String cognome) {
        this.credenziali = credenziali;
        this.nome = nome;
        this.cognome = cognome;
    }

    // Costruttore vuoto (può servire per alcuni framework grafici)
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
}
