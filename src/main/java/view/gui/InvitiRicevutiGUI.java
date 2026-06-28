package view.gui;

import beans.InvitoBean;
import controller.InvitoController;
import dao.ViaggioDAO;
import dao.full.sql.ViaggioDAOSQL;
import exceptions.NoResultException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import utilities.enums.StatoInvito;
import utilities.other.mappers.Session;

import java.util.ArrayList;
import java.util.List;

public class InvitiRicevutiGUI extends CommonGUI {

    private final InvitoController invitoController = new InvitoController();
    private final ViaggioDAO viaggioDAO = new ViaggioDAOSQL();

    @FXML private TableView<InvitoBean> tabellaInviti;
    @FXML private TableColumn<InvitoBean, String> colonnaDettagliViaggio;
    @FXML private TableColumn<InvitoBean, Void> colonnaAccetta;
    @FXML private TableColumn<InvitoBean, Void> colonnaRifiuta;
    @FXML private Label messaggioVuoto;

    public InvitiRicevutiGUI(Session session) {
        super(session);
    }

    @FXML
    public void initialize() {
        // Configuriamo la colonna testuale: recupera dinamicamente le info del viaggio!
        colonnaDettagliViaggio.setCellValueFactory(cellData -> {
            try {
                model.Viaggio v = viaggioDAO.recuperaViaggio(cellData.getValue().getIdViaggio());
                return new SimpleStringProperty(v.getPartenza() + " ➔ " + v.getDestinazione() + " (Data: " + v.getDataOra() + ")");
            } catch (Exception e) {
                return new SimpleStringProperty("Dettagli viaggio non disponibili (ID: " + cellData.getValue().getIdViaggio() + ")");
            }
        });

        // Configuriamo i due bottoni
        colonnaAccetta.setCellFactory(param -> creaBottone("Accetta", true));
        colonnaRifiuta.setCellFactory(param -> creaBottone("Rifiuta", false));

        caricaInviti();
    }

    private void caricaInviti() {
        try {
            beans.UtenteBean utente = (beans.UtenteBean) session.getUser();
            String miaEmail = utente.getCredenziali().getEmail();

            List<InvitoBean> lista = new ArrayList<>();
            invitoController.recuperaInvitiRicevuti(miaEmail, lista);

            ObservableList<InvitoBean> dati = FXCollections.observableList(lista);
            tabellaInviti.setItems(dati);
            messaggioVuoto.setVisible(false);
            tabellaInviti.setVisible(true);

        } catch (NoResultException e) {
            tabellaInviti.getItems().clear();
            tabellaInviti.setVisible(false);
            messaggioVuoto.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TableCell<InvitoBean, Void> creaBottone(String testo, boolean isAccept) {
        return new TableCell<>() {
            private final Button btn = new Button(testo);
            {
                btn.setStyle(isAccept ? "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-cursor: hand; -fx-font-weight: bold;"
                        : "-fx-background-color: #F44336; -fx-text-fill: white; -fx-cursor: hand; -fx-font-weight: bold;");

                btn.setOnMouseClicked(event -> {
                    InvitoBean invitoSelezionato = getTableView().getItems().get(getIndex());
                    gestisciRisposta(invitoSelezionato, isAccept);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        };
    }

    private void gestisciRisposta(InvitoBean invito, boolean isAccept) {
        try {
            StatoInvito statoRisposta = isAccept ? StatoInvito.ACCETTATO : StatoInvito.RIFIUTATO;
            invitoController.rispondiInvito(invito, statoRisposta);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(isAccept ? "Invito Accettato" : "Invito Rifiutato");
            alert.setHeaderText(null);

            if(isAccept) {
                alert.setContentText("Hai accettato l'invito! La tua prenotazione è stata confermata in automatico.");
            } else {
                alert.setContentText("Hai rifiutato l'invito al viaggio.");
            }
            alert.showAndWait();

            // Ricarichiamo la tabella (farà sparire l'invito a cui abbiamo appena risposto)
            caricaInviti();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText(null);
            alert.setContentText("Si è verificato un errore: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    protected void tornaIndietro(MouseEvent event) {
        cambiaSchermata(event, "/org/example/view/utenteHomepage.fxml", new UtenteHomepageGUI(session));
    }
}