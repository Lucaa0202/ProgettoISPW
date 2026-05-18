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

public class LoginGUI extends CommonGUI {

    // COSTRUTTORE: Deve accettare SOLO la sessione
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

            // 1. Salviamo l'utente loggato nella sessione
            session.setUser(utenteBean);

            // 2. IL GRANDE SALTO VERSO LA HOMEPAGE!
            cambiaSchermata(event, "/org/example/view/utenteHomepage.fxml", new UtenteHomepageGUI(session));

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
        // Usa il motore di CommonGUI per cambiare pagina andando alla registrazione
        cambiaSchermata(event, "/org/example/view/registrazione.fxml", new RegistrationGUI(session));
    }
}