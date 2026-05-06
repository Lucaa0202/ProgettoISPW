package beans; // Sostituisci con il tuo pacchetto corretto

public class CredenzialiBean {
    // Usiamo final per l'email come buona pratica di immutabilità dell'identità
    private final String email;
    private String password;

    // Costruttore completo (per il login)
    public CredenzialiBean(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Costruttore solo email (utile se devi passare solo l'identità senza la password in giro per l'app)
    public CredenzialiBean(String email) {
        this.email = email;
        this.password = null;
    }

    // --- GETTER E SETTER ---
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}