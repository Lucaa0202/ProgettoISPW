package view.gui;

import beans.PrenotazioneBean;
import beans.ViaggioBean;
import controller.PrenotazioneController;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import patterns.observer.Observer;
import patterns.observer.PrenotazioneManagerConcreteSubject;
import utilities.other.mappers.Session;

import java.util.List;

public class GestioneRichiesteGUI extends CommonGUI implements Observer {

    private final ViaggioBean viaggioSelezionato;
    private final PrenotazioneController prenotazioneController;

    // Colonne della tabella
    @FXML private TableView<PrenotazioneBean> tableRichieste;
    @FXML private TableColumn<PrenotazioneBean, String> passeggeroCol;
    @FXML private TableColumn<PrenotazioneBean, Void> accettaCol;
    @FXML private TableColumn<PrenotazioneBean, Void> rifiutaCol;

    // Il costruttore accetta anche il Viaggio cliccato!
    public GestioneRichiesteGUI(Session session, ViaggioBean viaggioSelezionato) {
        super(session);
        this.viaggioSelezionato = viaggioSelezionato;
        this.prenotazioneController = new PrenotazioneController();

        // Iscriviamo la pagina all'Observer!
        PrenotazioneManagerConcreteSubject.getInstance().addObserver(this);
    }

    @FXML
    public void initialize() {
        // Configuriamo le colonne
        passeggeroCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmailPasseggero()));

        accettaCol.setCellFactory(param -> createButtonCell("Accetta", true));
        rifiutaCol.setCellFactory(param -> createButtonCell("Rifiuta", false));

        // Carichiamo le prenotazioni per QUESTO viaggio
        caricaRichieste();
    }

    private void caricaRichieste() {
        try {
            // Usa il metodo preparato nel PrenotazioneController!
            List<PrenotazioneBean> richieste = prenotazioneController.caricaRichiestePerViaggio(viaggioSelezionato.getIdViaggio());

            javafx.collections.ObservableList<PrenotazioneBean> lista = javafx.collections.FXCollections.observableList(richieste);
            tableRichieste.setItems(lista);
        } catch (Exception e) {
            System.err.println("Nessuna richiesta trovata: " + e.getMessage());
        }
    }

    // --- IL METODO MAGICO AGGIORNATO ---
    private TableCell<PrenotazioneBean, Void> createButtonCell(String buttonText, boolean isAccept) {
        return new TableCell<>() {
            private final Button button = createButton(buttonText, isAccept);

            private Button createButton(String buttonText, boolean isAccept) {
                Button btn = new Button(buttonText);
                btn.setStyle(isAccept ? "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-cursor: hand;"
                        : "-fx-background-color: #F44336; -fx-text-fill: white; -fx-cursor: hand;");

                btn.setOnMouseClicked(event -> {
                    PrenotazioneBean prenotazioneSelezionata = getTableView().getItems().get(getIndex());
                    gestisciPrenotazione(prenotazioneSelezionata, isAccept);
                });
                return btn;
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView() == null || getTableView().getItems().size() <= getIndex()) {
                    setGraphic(null);
                } else {
                    PrenotazioneBean prenotazione = getTableView().getItems().get(getIndex());
                    // Mostriamo il bottone SOLO se la prenotazione è IN_ATTESA
                    if (prenotazione.getStato() == utilities.enums.StatoPrenotazione.IN_ATTESA) {
                        setGraphic(button);
                    } else {
                        setGraphic(null); // Fa scomparire i bottoni se è accettata o rifiutata!
                    }
                }
            }
        };
    }

    private void gestisciPrenotazione(PrenotazioneBean prenotazione, boolean isAccept) {
        try {
            if (isAccept) {
                // Catturiamo il boolean dal controller
                boolean autoPiena = prenotazioneController.accettaPrenotazione(prenotazione);

                if (autoPiena) {
                    // --- FINESTRA OVERBOOKING (ULTIMO POSTO) ---
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Viaggio al completo!");
                    alert.setHeaderText(null);
                    alert.setContentText("Siccome tutti i posti sono occupati, le richieste rimanenti sono state rifiutate in automatico dal sistema.");
                    alert.showAndWait();
                } else {
                    // --- FINESTRA DI SUCCESSO ACCETTAZIONE CLASSICA ---
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Richiesta Accettata");
                    alert.setHeaderText(null);
                    alert.setContentText("Hai accettato a bordo " + prenotazione.getEmailPasseggero() + "!");
                    alert.showAndWait();
                }
            } else {
                prenotazioneController.rifiutaPrenotazione(prenotazione);
                // --- FINESTRA DI CONFERMA RIFIUTO ---
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Richiesta Rifiutata");
                alert.setHeaderText(null);
                alert.setContentText("Hai rifiutato la richiesta di " + prenotazione.getEmailPasseggero() + ".");
                alert.showAndWait();
            }
            caricaRichieste(); // Ricarica la tabella, facendo sparire i bottoni all'istante
        } catch (Exception e) {
            System.err.println("Errore durante la gestione: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText(null);
            alert.setContentText("Si è verificato un problema: " + e.getMessage());
            alert.showAndWait();
        }
    }

    // METODO OBSERVER (Scatta se qualcuno si prenota mentre stai guardando questa pagina)
    @Override
    public void update() {
        javafx.application.Platform.runLater(() -> {
            caricaRichieste();
        });
    }

    @FXML
    protected void tornaIndietro(MouseEvent event) {
        cambiaSchermata(event, "/org/example/view/iMieiViaggi.fxml", new IMieiViaggiGUI(session));
    }
}