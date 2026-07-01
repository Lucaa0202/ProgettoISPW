package view.commandline;

import beans.UtenteBean;
import beans.VeicoloBean;
import beans.ViaggioBean;
import controller.GestioneViaggioController;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class InserisciViaggioCLI {

    private Scanner scanner;
    private UtenteBean guidatoreLoggato;
    private GestioneViaggioController controller;

    private final List<String> MACRO_ZONE = Arrays.asList(
            "Città Universitaria", "Stazione Termini", "Stazione Tiburtina",
            "Piazza Bologna", "San Giovanni", "EUR Magliana", "Roma Nord", "Roma Sud"
    );

    public InserisciViaggioCLI(UtenteBean guidatoreLoggato) {
        this.guidatoreLoggato = guidatoreLoggato;
        this.scanner = new Scanner(System.in);
        this.controller = new GestioneViaggioController();
    }

    public void start() {
        stampaIntestazione();

        // 1. RECUPERO VEICOLI E CONTROLLO DI BLOCCO
        List<VeicoloBean> veicoliUtente = new ArrayList<>();
        try {
            controller.recuperaVeicoliUtente(guidatoreLoggato.getCredenziali().getEmail(), veicoliUtente);
        } catch (Exception e) {
            System.out.println("❌ Errore durante il controllo dei veicoli: " + e.getMessage());
            return; // Interrompe ed esce
        }

        if (veicoliUtente.isEmpty()) {
            System.out.println("\n⚠️ ATTENZIONE: Non hai ancora registrato nessun veicolo!");
            System.out.println("Devi prima aggiungere un veicolo al tuo profilo per poter offrire un passaggio.");
            System.out.println("\nPremi Invio per tornare alla Home...");
            scanner.nextLine();
            return; // Ritorna alla GuidatoreHomeCLI
        }

        try {
            System.out.println("Compila i dettagli del tuo nuovo viaggio:");

            String partenza = selezionaDaLista("partenza", MACRO_ZONE);

            List<String> zoneArrivo = new ArrayList<>(MACRO_ZONE);
            zoneArrivo.remove(partenza);
            String arrivo = selezionaDaLista("arrivo", zoneArrivo);

            System.out.print("Numero di posti disponibili: ");
            int posti = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Contributo per passeggero (es. 15.50): ");
            double contributo = Double.parseDouble(scanner.nextLine().trim());

            // 2. SELEZIONE GUIDATA DELLA TARGA (Niente più input libero!)
            String targaScelta = selezionaVeicolo(veicoliUtente);

            LocalDateTime dataOra = null;
            while (dataOra == null) {
                System.out.print("Data e ora partenza (formato AAAA-MM-GGTHH:MM, es. 2026-07-15T08:30): ");
                try {
                    dataOra = LocalDateTime.parse(scanner.nextLine().trim());
                } catch (DateTimeParseException e) {
                    System.out.println("❌ Formato data non valido. Ricorda la 'T' tra data e ora. Riprova.");
                }
            }

            ViaggioBean nuovoViaggio = new ViaggioBean();
            nuovoViaggio.setPartenza(partenza);
            nuovoViaggio.setArrivo(arrivo);
            nuovoViaggio.setPostiDisponibili(posti);
            nuovoViaggio.setContributoCondiviso(contributo);
            nuovoViaggio.setTargaVeicolo(targaScelta); // Usiamo la targa selezionata
            nuovoViaggio.setDataOraPartenza(dataOra);
            nuovoViaggio.setEmailGuidatore(guidatoreLoggato.getCredenziali().getEmail());

            controller.creaViaggio(nuovoViaggio);

            System.out.println("\n✅ Viaggio " + partenza + " -> " + arrivo + " creato con successo!");

        } catch (NumberFormatException e) {
            System.out.println("\n❌ Errore: Formato numerico non valido per posti o prezzo.");
        } catch (Exception e) {
            System.out.println("\n❌ Errore durante la creazione del viaggio: " + e.getMessage());
            e.printStackTrace(); // Utile per vedere i dettagli in rosso su IntelliJ
        }

        System.out.println("\nPremi Invio per tornare alla Home...");
        scanner.nextLine();
    }

    private String selezionaDaLista(String tipo, List<String> opzioni) {
        System.out.println("\nScegli la zona di " + tipo + ":");
        for (int i = 0; i < opzioni.size(); i++) {
            System.out.println((i + 1) + ". " + opzioni.get(i));
        }
        int scelta = -1;
        while (scelta < 1 || scelta > opzioni.size()) {
            System.out.print("Scelta: ");
            try {
                scelta = Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Numero non valido.");
            }
        }
        return opzioni.get(scelta - 1);
    }

    // NUOVO METODO: Fa scegliere il veicolo dalla lista reale del DB
    // NUOVO METODO: Ricerca per targa digitata
    private String selezionaVeicolo(List<VeicoloBean> veicoli) {
        System.out.println("\nI tuoi veicoli disponibili:");
        for (VeicoloBean v : veicoli) {
            // Mostriamo l'elenco con un trattino invece che col numero
            System.out.println("- Targa: " + v.getTarga());
        }

        String targaScelta = null;
        boolean targaTrovata = false;

        while (!targaTrovata) {
            System.out.print("Digita la targa del veicolo che vuoi utilizzare: ");
            String input = scanner.nextLine().trim();

            // Cicliamo i veicoli dell'utente per vedere se la targa inserita coincide
            for (VeicoloBean v : veicoli) {
                if (v.getTarga().equalsIgnoreCase(input)) {
                    targaScelta = v.getTarga(); // Prendiamo la targa esatta dal DB
                    targaTrovata = true;
                    break;
                }
            }

            if (!targaTrovata) {
                System.out.println("❌ Targa non riconosciuta. Assicurati di digitarla esattamente come scritta sopra.");
            }
        }

        return targaScelta;
    }

    private void stampaIntestazione() {
        System.out.println("\n==================================");
        System.out.println("      OFFRI UN NUOVO PASSAGGIO    ");
        System.out.println("==================================");
    }
}