package view.gui;


import beans.ViaggioBean;
import controller.GestioneViaggioController;
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

public class OffriPassaggioGUI extends CommonGUI {

    public OffriPassaggioGUI(Session session) {
        super(session);
    }

    private final GestioneViaggioController gestioneViaggioController = new GestioneViaggioController();

    @FXML private TextField partenza;
    @FXML private TextField arrivo;
    @FXML private DatePicker dataPartenza;
    @FXML private TextField oraPartenza;
    @FXML private TextField posti;
    @FXML private TextField prezzo;
    @FXML private ComboBox<String> tendinaVeicoli; // Qui caricheremo le targhe
    @FXML private Text messaggio;

    @FXML
    public void initialize() {
        // TODO: Appena apriamo la pagina, dovremmo caricare le targhe dal DB nella tendina.
        // Per ora mettiamo dei dati finti (stub) per vedere se la grafica funziona!
        tendinaVeicoli.getItems().addAll("AB123CD - Fiat Panda", "EF456GH - Ford Fiesta");
    }

    @FXML
    private void salvaViaggio(MouseEvent event) {
        messaggio.setVisible(false);
        try {
            // 1. Uniamo Data e Ora in un LocalDateTime
            LocalDate data = dataPartenza.getValue();
            if (data == null) throw new Exception("Seleziona una data.");

            // Format per l'orario (HH:mm)
            LocalTime ora = LocalTime.parse(oraPartenza.getText(), DateTimeFormatter.ofPattern("HH:mm"));
            LocalDateTime dataOraCompleta = LocalDateTime.of(data, ora);

            // 2. Parsiamo numeri
            int postiDisponibili = Integer.parseInt(posti.getText());
            double prezzoScelto = Double.parseDouble(prezzo.getText());

            // 3. Recuperiamo la targa dalla tendina (es: taglia la stringa "AB123CD - Fiat Panda")
            String veicoloSelezionato = tendinaVeicoli.getValue();
            if (veicoloSelezionato == null) throw new Exception("Seleziona un veicolo.");
            String targa = veicoloSelezionato.split(" ")[0]; // Prende solo la prima parola (la targa)

            // 4. Creiamo il Bean (assicurati di aver aggiunto la targa al tuo ViaggioBean!)
            // L'idViaggio mettiamo 0 perché lo genererà automaticamente MySQL (AUTO_INCREMENT)
            String email = session.getUser().getCredenziali().getEmail();
            ViaggioBean viaggioBean = new ViaggioBean(0, email, partenza.getText(), arrivo.getText(), dataOraCompleta, postiDisponibili, prezzoScelto, targa);

            // 5. Passiamo al controller applicativo (che creeremo al prossimo passo)
            gestioneViaggioController.creaViaggio(viaggioBean);

            messaggio.setFill(javafx.scene.paint.Color.GREEN);
            messaggio.setText("Viaggio creato con successo!");
            messaggio.setVisible(true);

        } catch (java.time.format.DateTimeParseException e) {
            mostraErrore("Formato ora non valido. Usa HH:mm (es. 14:30)");
        } catch (NumberFormatException e) {
            mostraErrore("Posti e prezzo devono essere numeri validi.");
        } catch (Exception e) {
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
