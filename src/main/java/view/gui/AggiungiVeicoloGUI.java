package view.gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import beans.VeicoloBean;
import controller.GestioneVeicoloController;
import exceptions.EmptyFieldException; // Assicurati di avere questa o usane una simile
import utilities.other.mappers.Session;

public class AggiungiVeicoloGUI extends CommonGUI {

    public AggiungiVeicoloGUI(Session session) {
        super(session);
    }

    // Qui chiamiamo in causa il famoso "Controller Applicativo"
    private final GestioneVeicoloController gestioneVeicoloController = new GestioneVeicoloController();

    @FXML
    private TextField marca;
    @FXML
    private TextField modello;
    @FXML
    private TextField targa;
    @FXML
    private TextField posti;
    @FXML
    private Text messaggio; // Per gli errori o i successi

    @FXML
    private void salvaVeicolo(MouseEvent event) {
        messaggio.setVisible(false);
        try {
            // 1. Controllo base che non ci siano campi vuoti
            if (marca.getText().isEmpty() || modello.getText().isEmpty() ||
                    targa.getText().isEmpty() || posti.getText().isEmpty()) {
                throw new EmptyFieldException("Compila tutti i campi!");
            }

            // 2. Prepariamo il "pacchetto" (Bean) con i dati letti dalla grafica
            // 2 e 3. Prepariamo il Bean usando il TUO costruttore esatto: (targa, marca, modello, postiTotali, email)
            int numeroPosti = Integer.parseInt(posti.getText());
            String emailUtente = session.getUser().getCredenziali().getEmail();

            VeicoloBean veicoloBean = new VeicoloBean(targa.getText(), marca.getText(), modello.getText(), numeroPosti, emailUtente);

            // 4. Passiamo la patata bollente al Controller Applicativo
            gestioneVeicoloController.aggiungiVeicolo(veicoloBean);

            // 5. Successo!
            messaggio.setFill(javafx.scene.paint.Color.GREEN);
            messaggio.setText("Veicolo salvato con successo!");
            messaggio.setVisible(true);

            // (Opzionale) Cambiamo pagina tornando alla Home
            // cambiaSchermata(event, "/org/example/view/utenteHomepage.fxml", new UtenteHomepageGUI(session));

        } catch (NumberFormatException e) {
            mostraErrore("I posti devono essere un numero intero.");
        } catch (EmptyFieldException e) {
            mostraErrore(e.getMessage());
        } catch (Exception e) {
            mostraErrore("Errore di sistema. " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void mostraErrore(String text) {
        messaggio.setFill(javafx.scene.paint.Color.RED);
        messaggio.setText(text);
        messaggio.setVisible(true);
    }

    @FXML
    private void tornaIndietro(MouseEvent event) {
        // Torna alla Dashboard
        cambiaSchermata(event, "/org/example/view/utenteHomepage.fxml", new UtenteHomepageGUI(session));
    }
}
