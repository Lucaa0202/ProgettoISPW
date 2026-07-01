package view.commandline;

import beans.PrenotazioneBean;
import beans.UtenteBean;
import beans.ViaggioBean;
import controller.PrenotazioneController;
import controller.RicercaViaggiController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class RicercaViaggiCLI {

    private Scanner scanner;
    private UtenteBean passeggeroLoggato;
    private RicercaViaggiController ricercaViaggiController;
    private PrenotazioneController prenotazioneController;
    private List<ViaggioBean> viaggiDisponibili;

    // LE STESSE IDENTICHE ZONE DELLA GUI E DEL GUIDATORE
    private final List<String> MACRO_ZONE = Arrays.asList(
            "Città Universitaria", "Stazione Termini", "Stazione Tiburtina",
            "Piazza Bologna", "San Giovanni", "EUR Magliana", "Roma Nord", "Roma Sud"
    );

    public RicercaViaggiCLI(UtenteBean passeggeroLoggato) {
        this.passeggeroLoggato = passeggeroLoggato;
        this.scanner = new Scanner(System.in);
        this.ricercaViaggiController = new RicercaViaggiController();
        this.prenotazioneController = new PrenotazioneController();
        this.viaggiDisponibili = new ArrayList<>();
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
                        String partenza = selezionaZona("Partenza", MACRO_ZONE);
                        String arrivo = selezionaZona("Arrivo", MACRO_ZONE);
                        cercaViaggi(partenza, arrivo);
                    }
                    case 2 -> prenotaViaggio();
                    case 3 -> running = false;
                    default -> System.out.println("❌ Scelta non valida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Errore: Inserisci un numero.");
            }
        }
    }

    private String selezionaZona(String tipo, List<String> listaZone) {
        System.out.println("\nSeleziona la macro zona di " + tipo + ":");
        for (int i = 0; i < listaZone.size(); i++) {
            System.out.println((i + 1) + ". " + listaZone.get(i));
        }

        int scelta = -1;
        while (scelta < 1 || scelta > listaZone.size()) {
            System.out.print("Scegli un numero: ");
            try {
                scelta = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Numero non valido.");
            }
        }
        return listaZone.get(scelta - 1);
    }

    private void cercaViaggi(String partenza, String arrivo) {
        try {
            ricercaViaggiController.cercaViaggi(
                    viaggiDisponibili,
                    partenza,
                    arrivo,
                    passeggeroLoggato.getCredenziali().getEmail()
            );

            if (viaggiDisponibili.isEmpty()) {
                System.out.println("\nNessun viaggio trovato per questa tratta.");
                return;
            }

            System.out.println("\n--- VIAGGI DISPONIBILI ---");
            for (int i = 0; i < viaggiDisponibili.size(); i++) {
                ViaggioBean v = viaggiDisponibili.get(i);
                System.out.println((i + 1) + ". " + v.getPartenza() + " -> " + v.getArrivo() +
                        " | " + v.getDataOraPartenza() +
                        " | Prezzo: €" + v.getContributoCondiviso() +
                        " | Posti: " + v.getPostiDisponibili() +
                        " | Guidatore: " + v.getEmailGuidatore());
            }
            System.out.println("--------------------------");

        } catch (Exception e) {
            System.out.println("Errore durante la ricerca: " + e.getMessage());
        }
    }

    private void prenotaViaggio() {
        if (viaggiDisponibili == null || viaggiDisponibili.isEmpty()) {
            System.out.println("\nDevi prima effettuare una ricerca e trovare dei viaggi!");
            return;
        }

        System.out.print("\nInserisci il numero del viaggio che vuoi prenotare: ");
        try {
            int indice = Integer.parseInt(scanner.nextLine().trim()) - 1;

            if (indice >= 0 && indice < viaggiDisponibili.size()) {
                ViaggioBean viaggioScelto = viaggiDisponibili.get(indice);

                if (viaggioScelto.getPostiDisponibili() <= 0) {
                    System.out.println("❌ Spiacenti, questo viaggio è al completo.");
                    return;
                }

                PrenotazioneBean nuovaPrenotazione = new PrenotazioneBean();
                nuovaPrenotazione.setIdViaggio(viaggioScelto.getIdViaggio());
                nuovaPrenotazione.setEmailPasseggero(passeggeroLoggato.getCredenziali().getEmail());
                nuovaPrenotazione.setStato(utilities.enums.StatoPrenotazione.IN_ATTESA);

                // Ricordati di impostare la data, come facevi nella GUI!
                nuovaPrenotazione.setDataPrenotazione(java.time.LocalDateTime.now());

                prenotazioneController.inviaRichiestaPrenotazione(nuovaPrenotazione);

                System.out.println("\n✅ Richiesta inviata con successo!");
                System.out.println("Il guidatore riceverà una notifica. Attendi la sua approvazione.");

            } else {
                System.out.println("Indice non valido.");
            }
        } catch (Exception e) {
            System.out.println("Errore durante la prenotazione: " + e.getMessage());
        }
    }

    private void stampaIntestazione() {
        System.out.println("\n==================================");
        System.out.println("       CERCA UN PASSAGGIO         ");
        System.out.println("==================================");
    }

    private void mostraMenu() {
        System.out.println("\n1. Cerca viaggi");
        System.out.println("2. Prenota un viaggio dalla lista");
        System.out.println("3. Torna alla Home");
        System.out.print("Scegli un'opzione: ");
    }
}