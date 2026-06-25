package view.gui;

import beans.PrenotazioneBean;
import beans.UtenteBean;
import beans.ViaggioBean;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import utilities.other.mappers.Session;

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
    // Le colonne dipendono da come è fatto il tuo PrenotazioneBean,
    // qui mettiamo le base: ID Viaggio e Stato
    @FXML private TableColumn<PrenotazioneBean, String> idViaggioPrenCol;
    @FXML private TableColumn<PrenotazioneBean, String> statoPrenCol;

    @FXML
    public void initialize() {
        // 1. Diciamo alle colonne dove prendere i dati testuali
        partenzaOffCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPartenza()));
        arrivoOffCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getArrivo()));

        // --- CORREZIONE APPLICATA QUI ---
        // Ora usiamo getDataOraPartenza() invece di getData()
        dataOffCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDataOraPartenza().toString()));

        // 2. Iniettiamo il bottone speciale "Vedi Richieste" nella colonna Void
        vediRichiesteBtnCol.setCellFactory(param -> createGestioneCell("Gestisci"));

        // Facciamo lo stesso per la tabella prenotati
        idViaggioPrenCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getIdViaggio())));
        statoPrenCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStato().toString()));

        // 3. Carichiamo i dati dal DB (Simulato: qui chiamerai i tuoi controller)
        caricaDatiNelleTabelle();
    }

    // ==========================================================
    // IL SEGRETO DEL TUO AMICO: INIEZIONE DEL BOTTONE IN TABELLA
    // ==========================================================
    private TableCell<ViaggioBean, Void> createGestioneCell(String buttonText) {
        return new TableCell<>() {
            private final Button button = createButton(buttonText);

            private Button createButton(String buttonText) {
                Button btn = new Button(buttonText);
                btn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-cursor: hand;");
                btn.setOnMouseClicked(event -> {
                    // Magia: prendiamo il Viaggio della riga esatta in cui l'utente ha cliccato!
                    ViaggioBean viaggioSelezionato = getTableView().getItems().get(getIndex());

                    // Vai alla pagina delle richieste per QUESTO viaggio (la faremo al prossimo step)
                    System.out.println("Apro le richieste per il viaggio verso " + viaggioSelezionato.getArrivo());
                    // cambiaSchermata(event, "/org/example/view/gestioneRichieste.fxml", new GestioneRichiesteGUI(session, viaggioSelezionato));
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

    private void caricaDatiNelleTabelle() {
        UtenteBean utenteLoggato = (UtenteBean) session.getUser();
        String miaEmail = utenteLoggato.getCredenziali().getEmail();

        // QUI ANDRANNO LE CHIAMATE AI CONTROLLER (DA IMPLEMENTARE IN FUTURO)
        // List<ViaggioBean> mieiViaggi = viaggioController.getViaggiDi(miaEmail);
        // List<PrenotazioneBean> miePrenotazioni = prenotazioneController.getPrenotazioniDi(miaEmail);
    }

    @FXML
    protected void tornaIndietro(MouseEvent event) {
        cambiaSchermata(event, "/org/example/view/utenteHomepage.fxml", new UtenteHomepageGUI(session));
    }
}