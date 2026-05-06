package model;

public class Credentials {
    // Variabili final: una volta create non possono essere modificate (SonarCloud adora l'immutabilità per la sicurezza)
    private final String email;
    private final String password;

    // Costruttore per quando carichiamo dal DB senza voler esporre la password
    public Credentials(String email) {
        this.email = email;
        this.password = null;
    }

    // Costruttore completo per login/registrazione
    public Credentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}