package view.gui;

import beans.PrenotazioneBean;
import beans.UtenteBean;
import beans.ViaggioBean;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import utilities.other.mappers.Session;

import java.util.List;

public class IMieiViaggiGUI extends CommonGUI {

    public IMieiViaggiGUI(Session session) {
        super(session);
    }

    // ==========================================================
    // TABELLA 1: I VIAGGI CHE OFFRO (GUIDATORE)
    // ==========================================================
    @FXML private TableView<ViaggioBean> tableOfferti;
    @FXML private TableColumn<ViaggioBean, String> partenzaOffCol;
    @FXML private TableColumn<ViaggioBean, String> arrivoOffCol;
    @FXML private TableColumn<ViaggioBean, String> dataOffCol;
    @FXML private TableColumn<ViaggioBean, Void> vediRichiesteBtnCol;

    // ==========================================================
    // TABELLA 2: I VIAGGI CHE HO PRENOTATO (PASSEGGERO)
    // ==========================================================
    @FXML private TableView<PrenotazioneBean> tablePrenotati;
    @FXML private TableColumn<PrenotazioneBean, String> idViaggioPrenCol;
    @FXML private TableColumn<PrenotazioneBean, String> statoPrenCol;

    // NUOVA COLONNA PER IL BOTTONE RECENSIONE
    @FXML private TableColumn<PrenotazioneBean, Void> recensioneBtnCol;

    @FXML
    public void initialize() {
        // 1. Diciamo alle colonne dove prendere i dati testuali
        partenzaOffCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPartenza()));
        arrivoOffCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getArrivo()));
        dataOffCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDataOraPartenza().toString()));
        vediRichiesteBtnCol.setCellFactory(param -> createGestioneCell("Gestisci"));

        // Tabella prenotati
        idViaggioPrenCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getIdViaggio())));
        statoPrenCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStato().toString()));

        // Iniezione della cella dinamica per la recensione
        recensioneBtnCol.setCellFactory(param -> createRecensioneCell("Recensisci"));

        // 3. Carichiamo i dati dal DB
        caricaDatiNelleTabelle();
    }

    private void caricaDatiNelleTabelle() {
        UtenteBean utenteLoggato = (UtenteBean) session.getUser();
        String miaEmail = utenteLoggato.getCredenziali().getEmail();

        try {
            controller.GestioneViaggioController viaggioController = new controller.GestioneViaggioController();
            List<ViaggioBean> mieiViaggi = new java.util.ArrayList<>();
            viaggioController.recuperaViaggiGuidatore(miaEmail, mieiViaggi);
            javafx.collections.ObservableList<ViaggioBean> offertiList = javafx.collections.FXCollections.observableList(mieiViaggi);
            tableOfferti.setItems(offertiList);
        } catch (Exception e) {
            System.err.println("Nessun viaggio offerto trovato.");
        }

        try {
            controller.PrenotazioneController prenotazioneController = new controller.PrenotazioneController();
            List<PrenotazioneBean> miePrenotazioni = new java.util.ArrayList<>();
            prenotazioneController.recuperaPrenotazioniPasseggero(miaEmail, miePrenotazioni);
            javafx.collections.ObservableList<PrenotazioneBean> prenotatiList = javafx.collections.FXCollections.observableList(miePrenotazioni);
            tablePrenotati.setItems(prenotatiList);
        } catch (Exception e) {
            System.err.println("Nessuna prenotazione trovata.");
        }
    }

    // ==========================================================
    // METODO ORIGINALE (Guidatore)
    // ==========================================================
    private TableCell<ViaggioBean, Void> createGestioneCell(String buttonText) {
        return new TableCell<>() {
            private final Button button = createButton(buttonText);
            private Button createButton(String buttonText) {
                Button btn = new Button(buttonText);
                btn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-cursor: hand;");
                btn.setOnMouseClicked(event -> {
                    ViaggioBean viaggioSelezionato = getTableView().getItems().get(getIndex());
                    cambiaSchermata(event, "/org/example/view/gestioneRichieste.fxml", new GestioneRichiesteGUI(session, viaggioSelezionato));
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

    // ==========================================================
    // NUOVO METODO: INIEZIONE BOTTONE RECENSIONE (Passeggero)
    // ==========================================================
    private TableCell<PrenotazioneBean, Void> createRecensioneCell(String buttonText) {
        return new TableCell<>() {
            private final Button button = createButton(buttonText);

            private Button createButton(String buttonText) {
                Button btn = new Button(buttonText);
                btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-cursor: hand; -fx-font-weight: bold;");
                btn.setOnMouseClicked(event -> {
                    PrenotazioneBean prenotazione = getTableView().getItems().get(getIndex());
                    apriFinestraRecensione(prenotazione);
                });
                return btn;
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView() == null || getTableView().getItems().size() <= getIndex()) {
                    setGraphic(null);
                } else {
                    PrenotazioneBean p = getTableView().getItems().get(getIndex());
                    UtenteBean utenteLoggato = (UtenteBean) session.getUser();

                    try {
                        // Recuperiamo il viaggio dal DB per avere la data e l'email del guidatore
                        dao.ViaggioDAO vDao = new dao.full.sql.ViaggioDAOSQL();
                        model.Viaggio viaggio = vDao.recuperaViaggio(p.getIdViaggio());

                        controller.RecensioneController rc = new controller.RecensioneController();

                        // Passiamo al "Metodo Magico" tutti i dati per decidere se mostrare il bottone
                        // QUI È DOVE HO CORRETTO L'ERRORE (getDataOra invece di getDataOraPartenza)
                        boolean canReview = rc.puoLasciareRecensione(
                                utenteLoggato.getCredenziali().getEmail(),
                                p.getIdViaggio(),
                                viaggio.getDataOra(),
                                p.getStato()
                        );

                        if (canReview) {
                            setGraphic(button);
                        } else {
                            setGraphic(null); // Lascia la cella vuota
                        }
                    } catch (Exception e) {
                        setGraphic(null);
                    }
                }
            }
        };
    }

    // ==========================================================
    // POP-UP PER LASCIARE LA RECENSIONE
    // ==========================================================
    private void apriFinestraRecensione(PrenotazioneBean prenotazione) {
        try {
            // Recuperiamo i dati del destinatario (il guidatore)
            dao.ViaggioDAO vDao = new dao.full.sql.ViaggioDAOSQL();
            model.Viaggio viaggio = vDao.recuperaViaggio(prenotazione.getIdViaggio());
            String emailGuidatore = viaggio.getEmailGuidatore(); // Assicurati che il tuo model Viaggio abbia questo getter
            String emailPasseggero = ((UtenteBean) session.getUser()).getCredenziali().getEmail();

            // Creazione del Pop-up (Dialog)
            Dialog<beans.RecensioneBean> dialog = new Dialog<>();
            dialog.setTitle("Lascia una Recensione");
            dialog.setHeaderText("Valuta il tuo viaggio con " + emailGuidatore);

            ButtonType inviaButtonType = new ButtonType("Invia Recensione", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(inviaButtonType, ButtonType.CANCEL);

            // Menu a tendina per i voti
            ComboBox<Integer> votoBox = new ComboBox<>();
            votoBox.getItems().addAll(1, 2, 3, 4, 5);
            votoBox.setValue(5); // Voto di default

            // Area di testo per il commento
            TextArea commentoArea = new TextArea();
            commentoArea.setPromptText("Scrivi qui come ti sei trovato...");
            commentoArea.setPrefRowCount(3);

            javafx.scene.layout.VBox vbox = new javafx.scene.layout.VBox(10);
            vbox.getChildren().addAll(new Label("Voto (Stelline):"), votoBox, new Label("Commento:"), commentoArea);
            dialog.getDialogPane().setContent(vbox);

            // Cattura i dati quando si clicca "Invia"
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == inviaButtonType) {
                    return new beans.RecensioneBean(
                            0, // ID fittizio, lo genera il DB
                            votoBox.getValue(),
                            commentoArea.getText(),
                            emailPasseggero,
                            emailGuidatore,
                            prenotazione.getIdViaggio()
                    );
                }
                return null;
            });

            // Mostra la finestra e attendi
            dialog.showAndWait().ifPresent(recensioneBean -> {
                try {
                    controller.RecensioneController rc = new controller.RecensioneController();
                    rc.lasciaRecensione(recensioneBean);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successo");
                    alert.setHeaderText(null);
                    alert.setContentText("Recensione salvata con successo!");
                    alert.showAndWait();

                    // Ricarichiamo le tabelle: il bottone sparirà magicamente perché il DB confermerà la recensione!
                    caricaDatiNelleTabelle();
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Errore");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void tornaIndietro(MouseEvent event) {
        cambiaSchermata(event, "/org/example/view/utenteHomepage.fxml", new UtenteHomepageGUI(session));
    }
}