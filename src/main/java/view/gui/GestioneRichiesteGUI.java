package view.gui;

import beans.PrenotazioneBean;
import beans.ViaggioBean;
import beans.InvitoBean;
import controller.PrenotazioneController;
import controller.InvitoController;
import exceptions.NoResultException;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import patterns.observer.Observer;
import patterns.observer.PrenotazioneManagerConcreteSubject;
import utilities.other.mappers.Session;

import java.util.List;
import java.util.Optional;

public class GestioneRichiesteGUI extends CommonGUI implements Observer {

    private final ViaggioBean viaggioSelezionato;
    private final PrenotazioneController prenotazioneController;
    private final InvitoController invitoController; // Aggiunto il controller degli inviti

    @FXML private TableView<PrenotazioneBean> tableRichieste;
    @FXML private TableColumn<PrenotazioneBean, String> passeggeroCol;
    @FXML private TableColumn<PrenotazioneBean, Void> accettaCol;
    @FXML private TableColumn<PrenotazioneBean, Void> rifiutaCol;

    public GestioneRichiesteGUI(Session session, ViaggioBean viaggioSelezionato) {
        super(session);
        this.viaggioSelezionato = viaggioSelezionato;
        this.prenotazioneController = new PrenotazioneController();
        this.invitoController = new InvitoController(); // Inizializzato

        PrenotazioneManagerConcreteSubject.getInstance().addObserver(this);
    }

    @FXML
    public void initialize() {
        passeggeroCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmailPasseggero()));
        accettaCol.setCellFactory(param -> createButtonCell("Accetta", true));
        rifiutaCol.setCellFactory(param -> createButtonCell("Rifiuta", false));

        caricaRichieste();
    }

    private void caricaRichieste() {
        try {
            List<PrenotazioneBean> richieste = prenotazioneController.caricaRichiestePerViaggio(viaggioSelezionato.getIdViaggio());
            javafx.collections.ObservableList<PrenotazioneBean> lista = javafx.collections.FXCollections.observableList(richieste);
            tableRichieste.setItems(lista);
        } catch (Exception e) {
            System.err.println("Nessuna richiesta trovata: " + e.getMessage());
        }
    }

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
                    if (prenotazione.getStato() == utilities.enums.StatoPrenotazione.IN_ATTESA) {
                        setGraphic(button);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        };
    }

    private void gestisciPrenotazione(PrenotazioneBean prenotazione, boolean isAccept) {
        try {
            if (isAccept) {
                boolean autoPiena = prenotazioneController.accettaPrenotazione(prenotazione);
                if (autoPiena) {
                    mostraInfo("Viaggio al completo!", "Siccome tutti i posti sono occupati, le richieste rimanenti sono state rifiutate in automatico dal sistema.");
                } else {
                    mostraInfo("Richiesta Accettata", "Hai accettato a bordo " + prenotazione.getEmailPasseggero() + "!");
                }
            } else {
                prenotazioneController.rifiutaPrenotazione(prenotazione);
                mostraInfo("Richiesta Rifiutata", "Hai rifiutato la richiesta di " + prenotazione.getEmailPasseggero() + ".");
            }
            caricaRichieste();
        } catch (Exception e) {
            mostraErrore("Si è verificato un problema: " + e.getMessage());
        }
    }

    // =========================================================================
    // NUOVO METODO: GESTISCE IL POP-UP PER INVITARE UN PASSEGGERO
    // =========================================================================
    @FXML
    private void apriPopUpInvito(MouseEvent event) {
        // 1. Controllo base: l'auto ha ancora posti?
        if (viaggioSelezionato.getPostiDisponibili() <= 0) {
            mostraErrore("Non puoi invitare nessuno, l'auto è già piena!");
            return;
        }

        try {
            beans.UtenteBean utente = (beans.UtenteBean) session.getUser();
            String emailGuidatore = utente.getCredenziali().getEmail();

            // 2. Chiediamo al Controller le email dello storico
            // NUOVA RIGA: passiamo anche l'ID del viaggio per far funzionare il filtro!
            List<String> storico = invitoController.recuperaStoricoPasseggeri(emailGuidatore, viaggioSelezionato.getIdViaggio());

            // 3. Disegniamo il Pop-up con la tendina
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Invita Passeggero");
            dialog.setHeaderText("Scegli chi invitare tra i tuoi vecchi passeggeri:");

            ButtonType btnInvita = new ButtonType("Invita", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(btnInvita, ButtonType.CANCEL);

            ComboBox<String> tendina = new ComboBox<>();
            tendina.getItems().addAll(storico);
            tendina.setPromptText("Seleziona un'email...");
            tendina.setPrefWidth(250);
            dialog.getDialogPane().setContent(tendina);

            // Evitiamo che il guidatore possa cliccare "Invita" senza aver scelto nessuno
            dialog.getDialogPane().lookupButton(btnInvita).setDisable(true);
            tendina.valueProperty().addListener((obs, oldVal, newVal) -> {
                dialog.getDialogPane().lookupButton(btnInvita).setDisable(newVal == null);
            });

            // Catturiamo il risultato
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == btnInvita) {
                    return tendina.getValue();
                }
                return null;
            });

            // 4. Se ha scelto qualcuno, salviamo l'invito nel Database!
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String emailScelta = result.get();
                // Assicurati che StatoInvito.PENDING sia scritto esattamente come nel tuo Enum
                InvitoBean nuovoInvito = new InvitoBean(viaggioSelezionato.getIdViaggio(), emailScelta, utilities.enums.StatoInvito.PENDING);

                invitoController.inviaInvito(nuovoInvito);
                mostraInfo("Invito Inviato!", "Hai invitato " + emailScelta + " a viaggiare con te.");
            }

        } catch (NoResultException e) {
            // Scatta se la lista storico è vuota (non ha mai viaggiato con nessuno)
            mostraInfo("Storico Vuoto", "Non hai nessun passeggero nel tuo storico da poter invitare.");
        } catch (Exception e) {
            e.printStackTrace();
            mostraErrore("Errore di sistema: " + e.getMessage());
        }
    }

    // --- Metodi di utilità per non ripetere il codice degli Alert ---
    private void mostraInfo(String titolo, String messaggio) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }

    private void mostraErrore(String messaggio) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }

    @Override
    public void update() {
        javafx.application.Platform.runLater(this::caricaRichieste);
    }

    @FXML
    protected void tornaIndietro(MouseEvent event) {
        cambiaSchermata(event, "/org/example/view/iMieiViaggi.fxml", new IMieiViaggiGUI(session));
    }
}
