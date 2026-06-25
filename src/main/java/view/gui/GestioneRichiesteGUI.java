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

    // Il costruttore ora accetta anche il Viaggio cliccato!
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
            // Usa il metodo che avevamo preparato nel PrenotazioneController!
            List<PrenotazioneBean> richieste = prenotazioneController.caricaRichiestePerViaggio(viaggioSelezionato.getIdViaggio());

            javafx.collections.ObservableList<PrenotazioneBean> lista = javafx.collections.FXCollections.observableList(richieste);
            tableRichieste.setItems(lista);
        } catch (Exception e) {
            System.err.println("Nessuna richiesta trovata: " + e.getMessage());
        }
    }

    // --- IL METODO MAGICO DEL TUO AMICO ---
    private TableCell<PrenotazioneBean, Void> createButtonCell(String buttonText, boolean isAccept) {
        return new TableCell<>() {
            private final Button button = createButton(buttonText, isAccept);

            private Button createButton(String buttonText, boolean isAccept) {
                Button btn = new Button(buttonText);
                if(isAccept) {
                    btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-cursor: hand;");
                } else {
                    btn.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-cursor: hand;");
                }

                btn.setOnMouseClicked(event -> {
                    PrenotazioneBean prenotationeSelezionata = getTableView().getItems().get(getIndex());
                    gestisciPrenotazione(prenotationeSelezionata, isAccept);
                });
                return btn;
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(button);
                }
            }
        };
    }

    private void gestisciPrenotazione(PrenotazioneBean prenotazione, boolean isAccept) {
        if (isAccept) {
            System.out.println("Hai ACCETTATO la prenotazione di: " + prenotazione.getEmailPasseggero());
            // Qui andrà: prenotazioneController.accettaPrenotazione(...)
        } else {
            System.out.println("Hai RIFIUTATO la prenotazione di: " + prenotazione.getEmailPasseggero());
            // Qui andrà: prenotazioneController.rifiutaPrenotazione(...)
        }
        caricaRichieste(); // Ricarica la tabella per aggiornarla
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