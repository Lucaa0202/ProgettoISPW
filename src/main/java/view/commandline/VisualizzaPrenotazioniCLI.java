package view.commandline;

import beans.PrenotazioneBean;
import beans.UtenteBean;
import controller.PrenotazioneController;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VisualizzaPrenotazioniCLI {

    private UtenteBean passeggeroLoggato;
    private PrenotazioneController controller;
    private Scanner scanner;

    public VisualizzaPrenotazioniCLI(UtenteBean passeggeroLoggato) {
        this.passeggeroLoggato = passeggeroLoggato;
        this.controller = new PrenotazioneController();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        stampaIntestazione();

        List<PrenotazioneBean> miePrenotazioni = new ArrayList<>();

        try {
            // Chiamiamo il metodo che avevi già preparato nel Controller!
            controller.recuperaPrenotazioniPasseggero(passeggeroLoggato.getCredenziali().getEmail(), miePrenotazioni);

            if (miePrenotazioni.isEmpty()) {
                System.out.println("Non hai ancora inviato nessuna richiesta di prenotazione.");
                System.out.println("Vai su 'Cerca un passaggio' per trovare il tuo primo viaggio!");
            } else {
                System.out.println("Ecco lo stato delle tue richieste:\n");

                for (int i = 0; i < miePrenotazioni.size(); i++) {
                    PrenotazioneBean p = miePrenotazioni.get(i);

                    // Mettiamo il simbolo in base allo stato
                    String simbolo = p.getStato() == utilities.enums.StatoPrenotazione.IN_ATTESA ? "⏳" :
                            (p.getStato() == utilities.enums.StatoPrenotazione.CONFERMATA ? "✅" : "❌");

                    // Stampiamo i dettagli essenziali
                    System.out.println(simbolo + " Viaggio ID: " + p.getIdViaggio() +
                            " | Stato: " + p.getStato() +
                            " | Effettuata il: " + p.getDataPrenotazione().toLocalDate());
                }
            }

        } catch (Exception e) {
            System.out.println("\n❌ Errore durante il recupero delle prenotazioni: " + e.getMessage());
        }

        System.out.println("\nPremi Invio per tornare alla Home...");
        scanner.nextLine();
    }

    private void stampaIntestazione() {
        System.out.println("\n==================================");
        System.out.println("      I MIEI VIAGGI PRENOTATI     ");
        System.out.println("==================================");
    }
}