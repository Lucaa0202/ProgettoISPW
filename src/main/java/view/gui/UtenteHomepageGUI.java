package view.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import utilities.other.mappers.Session;

// 1. Importiamo le classi necessarie per il Pattern Observer
import patterns.observer.Observer;
import patterns.observer.PrenotazioneManagerConcreteSubject;
import model.Prenotazione;

// 2. Estendiamo CommonGUI e implementiamo l'interfaccia Observer
public class UtenteHomepageGUI extends CommonGUI implements Observer {

    public UtenteHomepageGUI(Session session) {
        super(session);
        // 3. Iscriviamo la Homepage al Subject per "ascoltare" le notifiche
        PrenotazioneManagerConcreteSubject.getInstance().addObserver(this);
    }

    // =========================================================================
    // METODO DEL PATTERN OBSERVER (Scatta automaticamente)
    // =========================================================================
    @Override
    public void update() {
        java.util.List<Prenotazione> listaRichieste = PrenotazioneManagerConcreteSubject.getInstance().getPrenotazioniReq();

        if (!listaRichieste.isEmpty()) {
            Prenotazione ultimaArrivata = listaRichieste.get(listaRichieste.size() - 1);

            // Scopriamo chi è l'utente attualmente loggato nella sessione
            beans.UtenteBean utenteLoggato = (beans.UtenteBean) session.getUser();
            String emailLoggata = utenteLoggato.getCredenziali().getEmail();

            // IL FILTRO DI SICUREZZA:
            // Mostriamo il pop-up SOLO se l'utente loggato NON è il passeggero che ha appena prenotato
            if (!emailLoggata.equals(ultimaArrivata.getEmailPasseggero())) {

                // Platform.runLater assicura che il pop-up venga disegnato correttamente da JavaFX
                javafx.application.Platform.runLater(() -> {
                    Alert alertNotifica = new Alert(Alert.AlertType.INFORMATION);
                    alertNotifica.setTitle("Notifica di Sistema - Carpooling");
                    alertNotifica.setHeaderText("🔔 Nuova Prenotazione Ricevuta!");
                    alertNotifica.setContentText("Il passeggero " + ultimaArrivata.getEmailPasseggero() +
                            " ha appena prenotato un posto per il viaggio con ID: " + ultimaArrivata.getIdViaggio() + "!");
                    alertNotifica.showAndWait();
                });
            }
        }
    }

    // =========================================================================
    // I TUOI METODI ORIGINALI (Intatti)
    // =========================================================================

    @FXML
    protected void vaiAggiungiVeicolo(MouseEvent event) {
        cambiaSchermata(event, "/org/example/view/aggiungiVeicolo.fxml", new AggiungiVeicoloGUI(session));
    }

    @FXML
    protected void cercaPassaggio(MouseEvent event) {
        cambiaSchermata(event, "/org/example/view/ricercaViaggi.fxml", new RisultatiRicercaGUI(session));
    }

    @FXML
    protected void offriPassaggio(MouseEvent event) {
        cambiaSchermata(event, "/org/example/view/offriPassaggio.fxml", new OffriPassaggioGUI(session));
    }

    @FXML
    protected void iMieiViaggi(MouseEvent event) {
        System.out.println("Vado alla pagina: Storico Viaggi...");
    }

    @FXML
    protected void eseguiLogout(MouseEvent event) {
        session.setUser(null);
        cambiaSchermata(event, "/org/example/view/login.fxml", new LoginGUI(session));
    }
}
