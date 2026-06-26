package view.gui;

import beans.UtenteBean;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import utilities.other.mappers.Session;
import patterns.observer.Observer;
import patterns.observer.PrenotazioneManagerConcreteSubject;
import model.Prenotazione;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class UtenteHomepageGUI extends CommonGUI implements Observer {

    // La memoria che ricorda l'identità esatta delle notifiche già mostrate
    private static final Set<String> notificheGiaMostrate = new HashSet<>();

    public UtenteHomepageGUI(Session session) {
        super(session);
        PrenotazioneManagerConcreteSubject.getInstance().addObserver(this);
        this.update();
    }

    // =========================================================================
    // METODO DEL PATTERN OBSERVER (Multi-Notifica Aggregata e Anti-Fantasma)
    // =========================================================================
    @Override
    public void update() {
        List<Prenotazione> listaRichieste = PrenotazioneManagerConcreteSubject.getInstance().getPrenotazioniReq();

        if (listaRichieste.isEmpty()) return;

        UtenteBean utenteLoggato = (UtenteBean) session.getUser();
        if (utenteLoggato == null) return;

        String emailLoggata = utenteLoggato.getCredenziali().getEmail();

        try {
            dao.ViaggioDAO viaggioDAO = new dao.full.sql.ViaggioDAOSQL();

            StringBuilder messaggiNotifica = new StringBuilder();
            int contatoreNuove = 0;

            for (Prenotazione richiesta : listaRichieste) {

                // 1. IL KILLER DEI FANTASMI: Se la richiesta è già stata Accettata o Rifiutata, saltala!
                if (richiesta.getStato() != utilities.enums.StatoPrenotazione.IN_ATTESA) {
                    continue;
                }

                // 2. Creiamo una firma unica
                String firmaNotifica = richiesta.getEmailPasseggero() + "-" + richiesta.getIdViaggio();

                // 3. Se l'abbiamo già mostrata in questa sessione, saltiamola!
                if (notificheGiaMostrate.contains(firmaNotifica)) {
                    continue;
                }

                model.Viaggio viaggio = viaggioDAO.recuperaViaggio(richiesta.getIdViaggio());

                // 4. Se sono io il guidatore e NON il passeggero
                if (viaggio.getEmailGuidatore().equals(emailLoggata) &&
                        !emailLoggata.equals(richiesta.getEmailPasseggero())) {

                    // Registriamo che l'abbiamo vista
                    notificheGiaMostrate.add(firmaNotifica);

                    // Aggiungiamo un punto elenco al nostro pop-up
                    messaggiNotifica.append("- ").append(richiesta.getEmailPasseggero())
                            .append(" (Viaggio verso: ").append(viaggio.getDestinazione()).append(")\n");
                    contatoreNuove++;
                }
            }

            // Se abbiamo raccolto almeno UNA notifica valida, mostriamo il pop-up
            if (contatoreNuove > 0) {
                final String testoPopUp = messaggiNotifica.toString();
                final int totale = contatoreNuove;

                Platform.runLater(() -> {
                    Alert alertNotifica = new Alert(Alert.AlertType.INFORMATION);
                    alertNotifica.setTitle("Notifica di Sistema - Carpooling");
                    alertNotifica.setHeaderText("🔔 Hai " + totale + " nuova/e richiesta/e in sospeso!");

                    // TESTO AGGIORNATO CON "I MIEI VIAGGI"
                    alertNotifica.setContentText(testoPopUp + "\nVai nella sezione 'I Miei Viaggi' per rispondere.");
                    alertNotifica.show();
                });
            }
        } catch (Exception e) {
            System.err.println("Errore durante la notifica: " + e.getMessage());
        }
    }

    // =========================================================================
    // I TUOI METODI ORIGINALI
    // =========================================================================

    @FXML
    protected void vaiAggiungiVeicolo(MouseEvent event) {
        cambiaSchermata(event, "/org/example/view/aggiungiVeicolo.fxml", new AggiungiVeicoloGUI(session));
    }

    @FXML
    protected void cercaPassaggio(MouseEvent event) {
        cambiaSchermata(event, "/org/example/view/ricercaViaggi.fxml", new RisultatiRicercaGUI(session));
    }

    @FXML
    protected void offriPassaggio(MouseEvent event) {
        cambiaSchermata(event, "/org/example/view/offriPassaggio.fxml", new OffriPassaggioGUI(session));
    }

    @FXML
    protected void iMieiViaggi(MouseEvent event) {
        cambiaSchermata(event, "/org/example/view/iMieiViaggi.fxml", new IMieiViaggiGUI(session));
    }

    @FXML
    protected void eseguiLogout(MouseEvent event) {
        // Svuotiamo la memoria al logout per il prossimo utente
        notificheGiaMostrate.clear();
        session.setUser(null);
        cambiaSchermata(event, "/org/example/view/login.fxml", new LoginGUI(session));
    }
}