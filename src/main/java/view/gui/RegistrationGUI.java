package view.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import beans.CredenzialiBean;
import beans.UtenteBean;
import controller.UserRegistrationController;
import exceptions.EmptyFieldException;
import utilities.other.mappers.Session;

import java.time.LocalDate;

public class RegistrationGUI extends CommonGUI {

    // Costruttore: riceve la sessione come abbiamo fatto per il Login
    public RegistrationGUI(Session session) {
        super(session);
    }

    // Elementi grafici collegati al file FXML tramite fx:id
    @FXML
    private TextField nome;
    @FXML
    private TextField cognome;
    @FXML
    private TextField email;
    @FXML
    private TextField telefono;
    @FXML
    private PasswordField password;
    @FXML
    private ChoiceBox<String> genere;
    @FXML
    private DatePicker dataNascita;
    @FXML
    private Text error;

    private final UserRegistrationController registrationController = new UserRegistrationController();

    @FXML
    public void initialize() {
        // Inizializziamo la tendina del genere con le opzioni
        ObservableList<String> opzioniGenere = FXCollections.observableArrayList(
                "Maschio", "Femmina", "Altro"
        );
        genere.setItems(opzioniGenere);
    }

    @FXML
    protected void eseguiRegistrazione(MouseEvent event) {
        error.setVisible(false); // Nascondiamo eventuali errori precedenti

        try {
            // 1. Validazione campi (metodo sotto)
            checkFields();

            // 2. Controllo formato email
            if (isValidMail(email.getText())) {
                return;
            }

            // 3. Impacchettiamo i dati nei Bean
            CredenzialiBean credenzialiBean = new CredenzialiBean(email.getText(), password.getText());
            UtenteBean utenteBean = new UtenteBean();
            utenteBean.setCredenziali(credenzialiBean);
            utenteBean.setNome(nome.getText());
            utenteBean.setCognome(cognome.getText());
            utenteBean.setTelefono(telefono.getText());
            // Se il tuo UtenteBean supporta anche genere e data:
            // utenteBean.setGenere(genere.getValue());
            // utenteBean.setDataNascita(dataNascita.getValue());

            // 4. Chiamiamo il controller per salvare l'utente nel DB
            registrationController.registraUtente(utenteBean);

            // 5. Successo! Mostriamo un avviso
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successo");
            alert.setHeaderText(null);
            alert.setContentText("Registrazione completata! Ora puoi effettuare l'accesso.");
            alert.showAndWait();

            // TODO: Qui aggiungeremo il cambio scena per tornare al Login
            // goToLogin(event);

        } catch (EmptyFieldException e) {
            error.setText(e.getMessage());
            error.setVisible(true);
        } catch (Exception e) {
            error.setText("Errore durante la registrazione. Riprova.");
            error.setVisible(true);
            e.printStackTrace();
        }
    }

    // Controlla che nessun campo sia stato lasciato bianco
    private void checkFields() throws EmptyFieldException {
        if (nome.getText().isEmpty() || cognome.getText().isEmpty() ||
                email.getText().isEmpty() || password.getText().isEmpty() ||
                genere.getValue() == null || dataNascita.getValue() == null) {

            throw new EmptyFieldException("Tutti i campi sono obbligatori.");
        }
        if(telefono.getText().isEmpty()) {
            throw new EmptyFieldException("Inserisci il numero di telefono.");
        }

        // Controllo aggiuntivo: non deve essere un bambino o una data futura
        if (dataNascita.getValue().isAfter(LocalDate.now().minusYears(14))) {
            throw new EmptyFieldException("Devi avere almeno 14 anni per iscriverti.");
        }
    }

    // Semplice controllo regex per l'email
    private boolean isValidMail(String mail) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!mail.matches(emailRegex)) {
            error.setText("Formato email non valido.");
            error.setVisible(true);
            return true;
        }
        return false;
    }
}