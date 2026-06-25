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

    // Elementi di Ricerca
    @FXML private ComboBox<String> tendinaPartenza;
    @FXML private ComboBox<String> tendinaArrivo;

    // Elementi della Tabella
    @FXML private TableView<ViaggioBean> tabellaViaggi;
    @FXML private TableColumn<ViaggioBean, String> colonnaPartenza;
    @FXML private TableColumn<ViaggioBean, String> colonnaDestinazione;
    @FXML private TableColumn<ViaggioBean, String> colonnaDataOra;
    @FXML private TableColumn<ViaggioBean, Integer> colonnaPosti;
    @FXML private TableColumn<ViaggioBean, Double> colonnaPrezzo;
    @FXML private TableColumn<ViaggioBean, Void> colonnaBottone;
    @FXML private Text messaggio;

    @FXML
    public void initialize() {
        // Popoliamo le tendine con gli stessi hub strategici di Roma
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
            // --- INIZIO MODIFICA: Recuperiamo l'email dalla sessione ---
            // (Uso i percorsi completi per evitare problemi di import, ma se li hai già importati puoi toglierli)
            beans.UtenteBean utenteLoggato = (beans.UtenteBean) session.getUser();
            String miaEmail = utenteLoggato.getCredenziali().getEmail();
            // --- FINE MODIFICA ---

            List<ViaggioBean> risultati = new ArrayList<>();

            // Passiamo la nostra email come quarto parametro!
            ricercaController.cercaViaggi(risultati, partenza, arrivo, miaEmail);

            // Se va tutto a buon fine, stampiamo la lista
            mostraViaggi(risultati);
            mostraMessaggio("Ricerca completata!", javafx.scene.paint.Color.GREEN);

        } catch (NoResultException e) {
            tabellaViaggi.getItems().clear(); // Svuota la tabella se non ci sono risultati
            mostraMessaggio(e.getMessage(), javafx.scene.paint.Color.RED);
        } catch (Exception e) {
            e.printStackTrace();
            mostraMessaggio("Errore di sistema durante la ricerca.", javafx.scene.paint.Color.RED);
        }
    }

    public void mostraViaggi(List<ViaggioBean> viaggiTrovati) {
        ObservableList<ViaggioBean> listaViaggi = FXCollections.observableList(viaggiTrovati);

        colonnaPartenza.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPartenza()));
        colonnaDestinazione.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getArrivo()));
        colonnaDataOra.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDataOraPartenza().toString()));
        colonnaPosti.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPostiDisponibili()));
        colonnaPrezzo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getContributoCondiviso()));

        colonnaBottone.setCellFactory(param -> creaBottonePrenota("Prenota"));
        tabellaViaggi.setItems(listaViaggi);
    }

    private TableCell<ViaggioBean, Void> creaBottonePrenota(String testoBottone) {
        return new TableCell<>() {
            private final Button btn = new Button(testoBottone);
            {
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

            // Recuperiamo la mail dell'utente loggato
            beans.UtenteBean utenteLoggato = (beans.UtenteBean) session.getUser();
            String emailPasseggero = utenteLoggato.getCredenziali().getEmail();

            // Prepariamo il Bean della prenotazione
            beans.PrenotazioneBean prenotazioneBean = new beans.PrenotazioneBean();
            prenotazioneBean.setEmailPasseggero(emailPasseggero);
            prenotazioneBean.setIdViaggio(viaggioBean.getIdViaggio());
            prenotazioneBean.setStato(utilities.enums.StatoPrenotazione.convertIntToState(2));
            prenotazioneBean.setDataPrenotazione(java.time.LocalDateTime.now());

            // Inviamo al controller
            prenotationController.inviaRichiestaPrenotazione(prenotazioneBean);

            // --- FINESTRA DI SUCCESSO (ALERT) ---
            Alert alertConferma = new Alert(Alert.AlertType.INFORMATION);
            alertConferma.setTitle("Prenotazione Confermata");
            alertConferma.setHeaderText(null);
            alertConferma.setContentText("Hai prenotato con successo il viaggio verso " + viaggioBean.getArrivo() + "!\nI posti disponibili sono stati aggiornati.");
            alertConferma.showAndWait(); // Il programma si mette in pausa qui finché l'utente non clicca OK

            // L'utente ha cliccato OK, ora ricarichiamo la tabella per mostrare il -1 sui posti
            eseguiRicerca(null);

        } catch (Exception e) {
            e.printStackTrace();
            // --- FINESTRA DI ERRORE (ALERT) ---
            Alert alertErrore = new Alert(Alert.AlertType.ERROR);
            alertErrore.setTitle("Errore di Prenotazione");
            alertErrore.setHeaderText(null);
            alertErrore.setContentText("Impossibile prenotare: " + e.getMessage());
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