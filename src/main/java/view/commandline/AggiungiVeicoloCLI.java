package view.commandline;

import beans.UtenteBean;
import beans.VeicoloBean;
import controller.GestioneVeicoloController; // IL TUO CONTROLLER ESATTO

import java.util.Scanner;

public class AggiungiVeicoloCLI {

    private Scanner scanner;
    private UtenteBean guidatoreLoggato;
    private GestioneVeicoloController veicoloController; // AGGIORNATO

    public AggiungiVeicoloCLI(UtenteBean guidatoreLoggato) {
        this.guidatoreLoggato = guidatoreLoggato;
        this.scanner = new Scanner(System.in);
        this.veicoloController = new GestioneVeicoloController(); // AGGIORNATO
    }

    public void start() {
        stampaIntestazione();

        try {
            System.out.println("Inserisci i dati della tua vettura:");

            String targa = richiediInput("Targa (es. AB123CD): ");
            String marca = richiediInput("Marca (es. Fiat): ");
            String modello = richiediInput("Modello (es. Panda): ");

            VeicoloBean nuovoVeicolo = new VeicoloBean();
            nuovoVeicolo.setTarga(targa);
            nuovoVeicolo.setMarca(marca);
            nuovoVeicolo.setModello(modello);

            // In base a come hai chiamato il setter dell'email nel VeicoloBean,
            // potrebbe essere setEmailProprietario o setEmailGuidatore.
            nuovoVeicolo.setEmailProprietario(guidatoreLoggato.getCredenziali().getEmail());

            // CHIAMATA AL TUO CONTROLLER
            veicoloController.aggiungiVeicolo(nuovoVeicolo);

            System.out.println("\n✅ Veicolo " + marca + " " + modello + " (" + targa + ") registrato con successo!");
            System.out.println("Ora puoi utilizzarlo per offrire nuovi passaggi.");

        } catch (Exception e) {
            System.out.println("\n❌ Errore durante la registrazione del veicolo: " + e.getMessage());
            e.printStackTrace(); // Utile per vedere se ci sono problemi col DB
        }

        System.out.println("\nPremi Invio per tornare alla Home...");
        scanner.nextLine();
    }

    private String richiediInput(String messaggio) {
        System.out.print(messaggio);
        String input = scanner.nextLine().trim();
        while (input.isEmpty()) {
            System.out.print("Il campo non può essere vuoto. " + messaggio);
            input = scanner.nextLine().trim();
        }
        return input;
    }

    private void stampaIntestazione() {
        System.out.println("\n==================================");
        System.out.println("       AGGIUNGI UN VEICOLO        ");
        System.out.println("==================================");
    }
}