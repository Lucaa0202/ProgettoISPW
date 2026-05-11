package view.gui;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import beans.CredenzialiBean;
import beans.UtenteBean;
import controller.LoginController;
import exceptions.EmptyFieldException;
import exceptions.WrongEmailOrPasswordException;
import utilities.other.mappers.Session;
import view.gui.CommonGUI;
import view.gui.RegistrationGUI;

public class LoginGUI extends CommonGUI {

    // COSTRUTTORE: Deve accettare SOLO la sessione per ora
    public LoginGUI(Session session) {
        super(session);
    }

    private final LoginController loginController = new LoginController();

    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private Text error;

    @FXML
    private void eseguiLogin(MouseEvent event) {
        error.setVisible(false);
        try {
            validateFields();

            CredenzialiBean credenzialiBean = new CredenzialiBean(email.getText(), password.getText());
            loginController.login(credenzialiBean);

            UtenteBean utenteBean = new UtenteBean();
            utenteBean.setCredenziali(credenzialiBean);
            loginController.retrieveUtente(utenteBean);

            session.setUser(utenteBean);
            System.out.println("Login OK! Benvenuto " + utenteBean.getNome());

            // Per ora stampiamo solo, poi aggiungeremo il cambio schermata

        } catch (WrongEmailOrPasswordException | EmptyFieldException e) {
            error.setText(e.getMessage());
            error.setVisible(true);
        } catch (Exception e) {
            error.setText("Errore di sistema.");
            error.setVisible(true);
            e.printStackTrace();
        }
    }

    private void validateFields() throws EmptyFieldException {
        if (email.getText().isEmpty() || password.getText().isEmpty()) {
            throw new EmptyFieldException("Compila tutti i campi.");
        }
    }
    @FXML
    private void vaiARegistrazione(MouseEvent event) {
        // Usa il motore di CommonGUI per cambiare pagina!
        // NOTA: controlla che il percorso "/org/example/view/registrazione.fxml" sia corretto
        cambiaSchermata(event, "/org/example/view/registrazione.fxml", new RegistrationGUI(session));
    }
}