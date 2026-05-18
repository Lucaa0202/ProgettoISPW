package view.gui;


import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import utilities.other.mappers.Session;

// Estendiamo CommonGUI esattamente come faceva il tuo amico
public class UtenteHomepageGUI extends CommonGUI {

    public UtenteHomepageGUI(Session session) {
        super(session);
    }

    // --- I METODI PER I BOTTONI DEL CARPOOLING ---

    @FXML
    protected void cercaPassaggio(MouseEvent event) {
        System.out.println("Vado alla pagina: Cerca Passaggio...");
        // Qui poi aggiungeremo il cambio scena
    }

    @FXML
    protected void offriPassaggio(MouseEvent event) {
        // IL SALTO ALLA PAGINA DEL VEICOLO!
        // Assicurati che il percorso del file fxml sia quello corretto
        cambiaSchermata(event, "/org/example/view/aggiungiVeicolo.fxml", new AggiungiVeicoloGUI(session));
    }

    @FXML
    protected void iMieiViaggi(MouseEvent event) {
        System.out.println("Vado alla pagina: Storico Viaggi...");
    }

    @FXML
    protected void eseguiLogout(MouseEvent event) {
        // Il tuo amico lo chiamava goToLoginAndRegister, noi andiamo dritti al login
        session.setUser(null);
        cambiaSchermata(event, "/org/example/view/login.fxml", new LoginGUI(session));
    }
}
