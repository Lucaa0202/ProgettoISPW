package view.commandline;

import beans.UtenteBean;
import controller.PrenotazioneController; // Importiamo il controller
import java.util.Scanner;

public class GuidatoreHomeCLI {

    private UtenteBean guidatoreLoggato;
    private Scanner scanner;
    private PrenotazioneController prenotazioneController; // Aggiunto

    public GuidatoreHomeCLI(UtenteBean guidatoreLoggato) {
        this.guidatoreLoggato = guidatoreLoggato;
        this.scanner = new Scanner(System.in);
        this.prenotazioneController = new PrenotazioneController(); // Inizializzato
    }

    public void start() {
        boolean running = true;

        while (running) {
            stampaIntestazione();
            mostraMenu();

            try {
                int scelta = Integer.parseInt(scanner.nextLine().trim());

                switch (scelta) {
                    case 1 -> {
                        GestionePrenotazioniCLI gestioneCli = new GestionePrenotazioniCLI(guidatoreLoggato);
                        gestioneCli.start();
                    }
                    case 2 -> {
                        AggiungiVeicoloCLI aggiungiVeicoloCli = new AggiungiVeicoloCLI(guidatoreLoggato);
                        aggiungiVeicoloCli.start();
                    }
                    case 3 -> {
                        InserisciViaggioCLI inserisciCli = new InserisciViaggioCLI(guidatoreLoggato);
                        inserisciCli.start();
                    }
                    case 0 -> {
                        System.out.println("\nLogout in corso... A presto, " + guidatoreLoggato.getNome() + "!");
                        running = false;
                    }
                    default -> System.out.println("❌ Scelta non valida. Riprova.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Errore: Inserisci un numero valido.");
            }
        }
    }

    private void stampaIntestazione() {
        System.out.println("\n==================================");
        System.out.println(" 🚗 HOME GUIDATORE - " + guidatoreLoggato.getNome().toUpperCase());
        System.out.println("==================================");
    }

    private void mostraMenu() {
        // --- LA MAGIA DELLA NOTIFICA ---
        // Se hai implementato il metodo nel DAO, decommenta le due righe sotto:
        // int notifiche = prenotazioneController.contaRichiesteInAttesa(guidatoreLoggato.getCredenziali().getEmail());
        // if (notifiche > 0) {
        //     System.out.println("🔔 ATTENZIONE: Hai " + notifiche + " nuove richieste di prenotazione da gestire!\n");
        // }

        System.out.println("1. Gestisci prenotazioni ricevute 🔔");
        System.out.println("2. Aggiungi un nuovo veicolo");
        System.out.println("3. Inserisci un nuovo viaggio");
        System.out.println("0. Logout");
        System.out.print("Scegli un'opzione: ");
    }
}