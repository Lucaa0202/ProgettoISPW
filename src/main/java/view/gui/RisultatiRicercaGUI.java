package view.gui;

import beans.ViaggioBean;
import controller.RicercaViaggiController;
import exceptions.NoResultException;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import utilities.other.mappers.Session;

import java.util.ArrayList;
import java.util.List;

public class RisultatiRicercaGUI extends CommonGUI {

    public RisultatiRicercaGUI(Session session) {
        super(session);
    }

    private final RicercaViaggiController ricercaController = new RicercaViaggiController();

    @FXML private ComboBox<String> tendinaPartenza;
    @FXML private ComboBox<String> tendinaArrivo;

    @FXML private TableView<ViaggioBean> tabellaViaggi;
    @FXML private TableColumn<ViaggioBean, String> colonnaGuidatore;

    // NUOVA COLONNA PER IL BOTTONE
    @FXML private TableColumn<ViaggioBean, Void> colonnaRecensioniBtn;

    @FXML private TableColumn<ViaggioBean, String> colonnaPartenza;
    @FXML private TableColumn<ViaggioBean, String> colonnaDestinazione;
    @FXML private TableColumn<ViaggioBean, String> colonnaDataOra;
    @FXML private TableColumn<ViaggioBean, Integer> colonnaPosti;
    @FXML private TableColumn<ViaggioBean, Double> colonnaPrezzo;
    @FXML private TableColumn<ViaggioBean, Void> colonnaBottone;
    @FXML private Text messaggio;

    @FXML
    public void initialize() {
        String[] macroZone = {
                "Città Universitaria", "Stazione Termini", "Stazione Tiburtina",
                "Piazza Bologna", "San Giovanni", "EUR Magliana", "Roma Nord", "Roma Sud"
        };
        tendinaPartenza.getItems().addAll(macroZone);
        tendinaArrivo.getItems().addAll(macroZone);
    }

    @FXML
    private void eseguiRicerca(MouseEvent event) {
        messaggio.setVisible(false);
        String partenza = tendinaPartenza.getValue();
        String arrivo = tendinaArrivo.getValue();

        if (partenza == null || arrivo == null) {
            mostraMessaggio("Seleziona sia la partenza che la destinazione.", javafx.scene.paint.Color.RED);
            return;
        }

        try {
            beans.UtenteBean utenteLoggato = (beans.UtenteBean) session.getUser();
            String miaEmail = utenteLoggato.getCredenziali().getEmail();

            List<ViaggioBean> risultati = new ArrayList<>();
            ricercaController.cercaViaggi(risultati, partenza, arrivo, miaEmail);

            mostraViaggi(risultati);
            mostraMessaggio("Ricerca completata!", javafx.scene.paint.Color.GREEN);

        } catch (NoResultException e) {
            tabellaViaggi.getItems().clear();
            mostraMessaggio(e.getMessage(), javafx.scene.paint.Color.RED);
        } catch (Exception e) {
            e.printStackTrace();
            mostraMessaggio("Errore di sistema durante la ricerca.", javafx.scene.paint.Color.RED);
        }
    }

    public void mostraViaggi(List<ViaggioBean> viaggiTrovati) {
        ObservableList<ViaggioBean> listaViaggi = FXCollections.observableList(viaggiTrovati);

        colonnaGuidatore.setCellValueFactory(cellData -> {
            String email = cellData.getValue().getEmailGuidatore();
            try {
                dao.RecensioneDAO rDao = new dao.full.sql.RecensioneDAOSQL();
                double media = rDao.calcolaMediaVoti(email);
                String mediaFormattata = String.format("%.1f", media);
                return new SimpleStringProperty(email + " (" + mediaFormattata + " ⭐)");
            } catch (NoResultException e) {
                return new SimpleStringProperty(email + " (Nessuna recensione)");
            } catch (Exception e) {
                return new SimpleStringProperty(email);
            }
        });

        // ASSOCIAMO IL BOTTONE "LEGGI" ALLA NUOVA COLONNA
        colonnaRecensioniBtn.setCellFactory(param -> creaBottoneLeggiRecensioni("Leggi"));

        colonnaPartenza.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPartenza()));
        colonnaDestinazione.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getArrivo()));
        colonnaDataOra.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDataOraPartenza().toString()));
        colonnaPosti.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPostiDisponibili()));
        colonnaPrezzo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getContributoCondiviso()));

        colonnaBottone.setCellFactory(param -> creaBottonePrenota("Prenota"));
        tabellaViaggi.setItems(listaViaggi);
    }

    // ==========================================================
    // METODO PER CREARE IL BOTTONE "LEGGI"
    // ==========================================================
    private TableCell<ViaggioBean, Void> creaBottoneLeggiRecensioni(String testo) {
        return new TableCell<>() {
            private final Button btn = new Button(testo);
            {
                btn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 11px;");
                btn.setOnMouseClicked(event -> {
                    ViaggioBean viaggioScelto = getTableView().getItems().get(getIndex());
                    mostraPopUpRecensioni(viaggioScelto.getEmailGuidatore());
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else setGraphic(btn);
            }
        };
    }

    // ==========================================================
    // POP-UP CHE MOSTRA I COMMENTI
    // ==========================================================
    private void mostraPopUpRecensioni(String emailGuidatore) {
        try {
            controller.RecensioneController rc = new controller.RecensioneController();
            List<beans.RecensioneBean> listaRecensioni = new ArrayList<>();
            rc.visualizzaRecensioniRicevute(emailGuidatore, listaRecensioni);

            // Creazione della finestra di dialogo
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Recensioni del Guidatore");
            dialog.setHeaderText("Cosa dicono di " + emailGuidatore);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

            // Contenitore per impilare i vari commenti
            javafx.scene.layout.VBox vbox = new javafx.scene.layout.VBox(10);
            vbox.setStyle("-fx-padding: 10;");

            // Per ogni recensione, creiamo un blocchetto grafico
            for(beans.RecensioneBean r : listaRecensioni) {
                Label lblVoto = new Label("⭐ Voto: " + r.getVoto() + "/5  (Da: " + r.getEmailAutore() + ")");
                lblVoto.setStyle("-fx-font-weight: bold; -fx-text-fill: #FF9800;");

                Label lblCommento = new Label("\"" + r.getCommento() + "\"");
                lblCommento.setWrapText(true);
                lblCommento.setStyle("-fx-font-style: italic;");

                javafx.scene.layout.VBox singolaRecensione = new javafx.scene.layout.VBox(3, lblVoto, lblCommento);
                singolaRecensione.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5; -fx-padding: 8; -fx-background-color: #ffffff;");
                vbox.getChildren().add(singolaRecensione);
            }

            // Aggiungiamo uno ScrollPane nel caso le recensioni siano tante
            ScrollPane scrollPane = new ScrollPane(vbox);
            scrollPane.setFitToWidth(true);
            scrollPane.setPrefSize(400, 300);

            dialog.getDialogPane().setContent(scrollPane);
            dialog.showAndWait();

        } catch (NoResultException e) {
            // Se scatta questa eccezione, significa che non ci sono recensioni
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Nessuna Recensione");
            alert.setHeaderText(null);
            alert.setContentText("Questo guidatore non ha ancora ricevuto commenti.");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TableCell<ViaggioBean, Void> creaBottonePrenota(String testoBottone) {
        return new TableCell<>() {
            private final Button btn = new Button(testoBottone);
            {
                btn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-cursor: hand; -fx-font-weight: bold;");
                btn.setOnMouseClicked(event -> {
                    ViaggioBean viaggioScelto = getTableView().getItems().get(getIndex());
                    gestisciPrenotazione(viaggioScelto);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else setGraphic(btn);
            }
        };
    }

    private void gestisciPrenotazione(ViaggioBean viaggioBean) {
        try {
            controller.PrenotazioneController prenotationController = new controller.PrenotazioneController();
            beans.UtenteBean utenteLoggato = (beans.UtenteBean) session.getUser();
            String emailPasseggero = utenteLoggato.getCredenziali().getEmail();

            beans.PrenotazioneBean prenotazioneBean = new beans.PrenotazioneBean();
            prenotazioneBean.setEmailPasseggero(emailPasseggero);
            prenotazioneBean.setIdViaggio(viaggioBean.getIdViaggio());
            prenotazioneBean.setStato(utilities.enums.StatoPrenotazione.IN_ATTESA);
            prenotazioneBean.setDataPrenotazione(java.time.LocalDateTime.now());

            prenotationController.inviaRichiestaPrenotazione(prenotazioneBean);

            Alert alertConferma = new Alert(Alert.AlertType.INFORMATION);
            alertConferma.setTitle("Richiesta Inviata");
            alertConferma.setHeaderText(null);
            alertConferma.setContentText("Hai inviato con successo la richiesta per il viaggio verso " + viaggioBean.getArrivo() + "!\nAttendi che il guidatore accetti la tua prenotazione.");
            alertConferma.showAndWait();

            eseguiRicerca(null);

        } catch (Exception e) {
            e.printStackTrace();
            Alert alertErrore = new Alert(Alert.AlertType.ERROR);
            alertErrore.setTitle("Errore di Prenotazione");
            alertErrore.setHeaderText(null);
            alertErrore.setContentText("Impossibile inviare la richiesta: " + e.getMessage());
            alertErrore.showAndWait();
        }
    }

    private void mostraMessaggio(String testo, javafx.scene.paint.Color colore) {
        messaggio.setFill(colore);
        messaggio.setText(testo);
        messaggio.setVisible(true);
    }

    @FXML
    private void tornaIndietro(MouseEvent event) {
        cambiaSchermata(event, "/org/example/view/utenteHomepage.fxml", new UtenteHomepageGUI(session));
    }
}