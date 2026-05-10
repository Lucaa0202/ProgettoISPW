package view.gui; // Assicurati che questo corrisponda alla tua cartella

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
// IMPORTANTE: Lascia che l'IDE importi in automatico la tua classe Session corretta
import utilities.other.mappers.Session;

import java.io.IOException;

public abstract class CommonGUI {

    // La sessione protetta sarà visibile a tutte le classi "figlie" (es. LoginGUI, DashboardGUI)
    protected Session session;

    public CommonGUI(Session session) {
        this.session = session;
    }

    // Il "motore" per cambiare qualsiasi schermata
    protected void cambiaSchermata(MouseEvent event, String fileFxml, CommonGUI prossimoController) {
        try {
            // Cerca il file grafico (FXML) nelle risorse
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fileFxml));

            // Inietta il controller PRIMA di mostrare la grafica (come nel progetto di riferimento)
            loader.setControllerFactory(c -> prossimoController);

            Parent root = loader.load();

            // Recupera la finestra (Stage) attuale a partire dal bottone che l'utente ha cliccato
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Errore critico nel caricamento della schermata: " + fileFxml);
        }
    }

    // --- METODI DI NAVIGAZIONE RAPIDA ---
    // Man mano che creiamo le finestre, aggiungeremo qui i metodi per raggiungerle

    protected void vaiALogin(MouseEvent event) {
        // Ipotizzando che i file FXML li metterai nella cartella risorse: src/main/resources/view/login.fxml
        // cambiaSchermata(event, "/view/login.fxml", new LoginGUI(this.session));
    }
}
