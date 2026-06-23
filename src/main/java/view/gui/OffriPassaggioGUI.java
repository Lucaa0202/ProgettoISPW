package view.gui;

import beans.VeicoloBean;
import beans.ViaggioBean;
import controller.GestioneViaggioController;
import exceptions.NoResultException;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import utilities.other.mappers.Session;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OffriPassaggioGUI extends CommonGUI {

    public OffriPassaggioGUI(Session session) {
        super(session);
    }

    private final GestioneViaggioController gestioneViaggioController = new GestioneViaggioController();

    // Adesso partenza e arrivo sono ComboBox!
    @FXML private ComboBox<String> partenza;
    @FXML private ComboBox<String> arrivo;
    @FXML private DatePicker dataPartenza;
    @FXML private TextField oraPartenza;
    @FXML private TextField posti;
    @FXML private TextField prezzo;
    @FXML private ComboBox<String> tendinaVeicoli;
    @FXML private Text messaggio;

    @FXML
    public void initialize() {
        tendinaVeicoli.getItems().clear();
        partenza.getItems().clear();
        arrivo.getItems().clear();

        // 1. Popoliamo gli snodi principali per il Carpooling
        String[] macroZone = {
                "Città Universitaria",
                "Stazione Termini",
                "Stazione Tiburtina",
                "Piazza Bologna",
                "San Giovanni",
                "EUR Magliana",
                "Roma Nord",
                "Roma Sud"
        };
        partenza.getItems().addAll(macroZone);
        arrivo.getItems().addAll(macroZone);

        // 2. Caricamento dinamico dei veicoli dell'utente dal DB
        try {
            String emailUtente = session.getUser().getCredenziali().getEmail();
            List<VeicoloBean> veicoliUtente = new ArrayList<>();
            gestioneViaggioController.recuperaVeicoliUtente(emailUtente, veicoliUtente);

            for (VeicoloBean v : veicoliUtente) {
                String voce = v.getTarga() + " - " + v.getMarca() + " " + v.getModello();
                tendinaVeicoli.getItems().add(voce);
            }

            if (!tendinaVeicoli.getItems().isEmpty()) {
                tendinaVeicoli.getSelectionModel().selectFirst();
            }

        } catch (NoResultException e) {
            tendinaVeicoli.setPromptText("Nessuna auto registrata");
        } catch (Exception e) {
            mostraErrore("Errore durante il caricamento dei veicoli.");
        }
    }

    @FXML
    private void salvaViaggio(MouseEvent event) {
        messaggio.setVisible(false);
        try {
            // Controllo rigoroso sulle nuove tendine
            String partenzaScelta = partenza.getValue();
            String arrivoScelto = arrivo.getValue();

            if (partenzaScelta == null || arrivoScelto == null) {
                throw new Exception("Seleziona una zona di partenza e di arrivo.");
            }
            if (partenzaScelta.equals(arrivoScelto)) {
                throw new Exception("La partenza e l'arrivo non possono coincidere.");
            }

            LocalDate data = dataPartenza.getValue();
            if (data == null) throw new Exception("Seleziona una data.");

            LocalTime ora = LocalTime.parse(oraPartenza.getText(), DateTimeFormatter.ofPattern("HH:mm"));
            LocalDateTime dataOraCompleta = LocalDateTime.of(data, ora);

            int postiDisponibili = Integer.parseInt(posti.getText());
            double prezzoScelto = Double.parseDouble(prezzo.getText());

            String veicoloSelezionato = tendinaVeicoli.getValue();
            if (veicoloSelezionato == null) throw new Exception("Seleziona un veicolo.");
            String targa = veicoloSelezionato.split(" ")[0];

            String email = session.getUser().getCredenziali().getEmail();

            // Creazione del Bean con i dati sicuri
            ViaggioBean viaggioBean = new ViaggioBean(0, email, partenzaScelta, arrivoScelto, dataOraCompleta, postiDisponibili, prezzoScelto, targa);

            gestioneViaggioController.creaViaggio(viaggioBean);

            messaggio.setFill(javafx.scene.paint.Color.GREEN);
            messaggio.setText("Viaggio creato con successo!");
            messaggio.setVisible(true);

        } catch (java.time.format.DateTimeParseException e) {
            mostraErrore("Formato ora non valido. Usa HH:mm (es. 14:30)");
        } catch (NumberFormatException e) {
            mostraErrore("Posti e prezzo devono essere numeri validi.");
        } catch (Exception e) {
            e.printStackTrace();
            mostraErrore(e.getMessage());
        }
    }

    private void mostraErrore(String text) {
        messaggio.setFill(javafx.scene.paint.Color.RED);
        messaggio.setText(text);
        messaggio.setVisible(true);
    }

    @FXML
    private void tornaIndietro(MouseEvent event) {
        cambiaSchermata(event, "/org/example/view/utenteHomepage.fxml", new UtenteHomepageGUI(session));
    }
}
