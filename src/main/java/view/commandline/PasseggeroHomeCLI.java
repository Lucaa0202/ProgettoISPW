package view.commandline;

import beans.UtenteBean;
import java.util.Scanner;

public class PasseggeroHomeCLI {

    private UtenteBean passeggeroLoggato;
    private Scanner scanner;

    public PasseggeroHomeCLI(UtenteBean passeggeroLoggato) {
        this.passeggeroLoggato = passeggeroLoggato;
        this.scanner = new Scanner(System.in);
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
                        // AGGIORNATO: Avvia la schermata di ricerca e prenotazione viaggi
                        RicercaViaggiCLI ricercaCli = new RicercaViaggiCLI(passeggeroLoggato);
                        ricercaCli.start();
                    }
                    case 2 -> {
                        // IL COMING SOON È FINITO! 🚀
                        VisualizzaPrenotazioniCLI miePrenotazioniCli = new VisualizzaPrenotazioniCLI(passeggeroLoggato);
                        miePrenotazioniCli.start();;
                    }
                    case 0 -> {
                        System.out.println("\nLogout in corso... Buon viaggio, " + passeggeroLoggato.getNome() + "!");
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
        System.out.println(" 🧳 HOME PASSEGGERO - " + passeggeroLoggato.getNome().toUpperCase());
        System.out.println("==================================");
    }

    private void mostraMenu() {
        System.out.println("1. Cerca e prenota un viaggio");
        System.out.println("2. Visualizza i tuoi viaggi prenotati");
        System.out.println("0. Logout");
        System.out.print("Scegli un'opzione: ");
    }
}
