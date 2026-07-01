package view.commandline;

import beans.PrenotazioneBean;
import beans.UtenteBean;
import controller.PrenotazioneController;
import patterns.observer.Observer;

import java.util.List;
import java.util.Scanner;

public class GestionePrenotazioniCLI implements Observer {

    private UtenteBean guidatoreLoggato;
    private PrenotazioneController controller;
    private List<PrenotazioneBean> richieste;
    private Scanner scanner;
    private int idViaggioSelezionato;

    public GestionePrenotazioniCLI(UtenteBean guidatoreLoggato) {
        this.guidatoreLoggato = guidatoreLoggato;
        this.controller = new PrenotazioneController();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        stampaIntestazione();

        System.out.print("Inserisci l'ID del viaggio da gestire: ");
        try {
            this.idViaggioSelezionato = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("❌ Errore: ID non valido. Torno alla Home.");
            return;
        }

        caricaRichieste();

        while (running) {
            mostraMenu();

            try {
                int scelta = Integer.parseInt(scanner.nextLine().trim());

                switch (scelta) {
                    case 1 -> visualizzaRichieste();
                    case 2 -> gestisciRichiesta(true);  // Accetta
                    case 3 -> gestisciRichiesta(false); // Rifiuta
                    case 4 -> {
                        System.out.println("Torno alla Home...");
                        running = false;
                    }
                    default -> System.out.println("❌ Errore: Scelta non valida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Errore: Inserisci un numero valido.");
            }
        }
    }

    private void caricaRichieste() {
        try {
            this.richieste = controller.caricaRichiestePerViaggio(idViaggioSelezionato);
        } catch (Exception e) {
            System.out.println("\nNessuna richiesta trovata per questo viaggio.");
        }
    }

    private void visualizzaRichieste() {
        if (richieste == null || richieste.isEmpty()) {
            System.out.println("\nNessuna richiesta in sospeso per il viaggio " + idViaggioSelezionato);
            return;
        }

        System.out.println("\n--- LISTA RICHIESTE PASSEGGERI ---");
        for (int i = 0; i < richieste.size(); i++) {
            PrenotazioneBean p = richieste.get(i);

            // Un piccolo tocco estetico: mettiamo un simbolo in base allo stato
            String simbolo = p.getStato() == utilities.enums.StatoPrenotazione.IN_ATTESA ? "⏳" :
                    (p.getStato() == utilities.enums.StatoPrenotazione.CONFERMATA ? "✅" : "❌");

            System.out.println((i + 1) + ". " + simbolo + " Da: " + p.getEmailPasseggero() +
                    " | Stato: " + p.getStato());
        }
        System.out.println("----------------------------------");
    }

    private void gestisciRichiesta(boolean accetta) {
        visualizzaRichieste();
        if (richieste == null || richieste.isEmpty()) return;

        System.out.print("\nInserisci il numero della richiesta da elaborare: ");
        try {
            int indice = Integer.parseInt(scanner.nextLine().trim()) - 1;

            if (indice >= 0 && indice < richieste.size()) {
                PrenotazioneBean selezionata = richieste.get(indice);

                // =========================================================
                // 1. IL BLOCCO LOGICO (GUARD CONDITION)
                // =========================================================
                if (selezionata.getStato() != utilities.enums.StatoPrenotazione.IN_ATTESA) {
                    System.out.println("\n⚠️ AZIONE BLOCCATA: Non puoi modificare questa richiesta!");
                    System.out.println("La prenotazione è già stata gestita (Stato attuale: " + selezionata.getStato() + ").");
                    return; // Interrompe il metodo e torna al menu
                }

                // 2. ESECUZIONE DELLA SCELTA
                if (accetta) {
                    boolean autoPiena = controller.accettaPrenotazione(selezionata);
                    System.out.println("\n✅ Hai ACCETTATO il passeggero a bordo!");

                    // Sfruttiamo la logica dell'overbooking che avevi preparato!
                    if (autoPiena) {
                        System.out.println("🚗 L'AUTO È ORA PIENA! Tutte le altre richieste in attesa sono state rifiutate in automatico.");
                    }
                } else {
                    controller.rifiutaPrenotazione(selezionata);
                    System.out.println("\n❌ Hai RIFIUTATO la richiesta.");
                }

                // Ricarichiamo la lista dal database per vedere i cambiamenti effettivi
                caricaRichieste();
            } else {
                System.out.println("Indice non valido.");
            }
        } catch (Exception e) {
            System.out.println("Errore durante l'elaborazione: " + e.getMessage());
        }
    }

    private void stampaIntestazione() {
        System.out.println("\n==================================");
        System.out.println("   GESTIONE PRENOTAZIONI DI " + guidatoreLoggato.getNome().toUpperCase());
        System.out.println("==================================");
    }

    private void mostraMenu() {
        System.out.println("\n1. Visualizza richieste");
        System.out.println("2. Accetta un passeggero");
        System.out.println("3. Rifiuta un passeggero");
        System.out.println("4. Indietro (Torna alla Home)");
        System.out.print("Scegli un'opzione: ");
    }

    @Override
    public void update() {
        System.out.println("\n[NOTIFICA OBSERVER] È arrivata una nuova prenotazione!");
        caricaRichieste();
    }
}