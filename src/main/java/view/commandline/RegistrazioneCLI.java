package view.commandline;

import beans.CredenzialiBean;
import beans.UtenteBean;
import controller.RegistrazioneController;
import exceptions.MailAlreadyExistsException;
import java.util.Scanner;

public class RegistrazioneCLI {

    private Scanner scanner;
    private RegistrazioneController registrazioneController;

    public RegistrazioneCLI() {
        this.scanner = new Scanner(System.in);
        // Ipotizzando che tu abbia creato un RegistrazioneController
        this.registrazioneController = new RegistrazioneController();
    }

    public void start() {
        stampaIntestazione();

        try {
            String nome = richiediInput("Nome: ");
            String cognome = richiediInput("Cognome: ");
            String telefono = richiediInput("Numero di telefono: "); // Nuovo campo richiesto!

            String email = richiediInput("Email: ");
            if (!email.contains("@") || !email.contains(".")) {
                System.out.println("❌ Formato email non valido. Registrazione annullata.");
                return;
            }

            String password = richiediInput("Password: ");

            // 1. Creiamo il Bean delle Credenziali
            CredenzialiBean credenziali = new CredenzialiBean(email, password);

            // 2. Creiamo il Bean dell'Utente (ora combacia perfettamente con i parametri del tuo costruttore)
            UtenteBean nuovoUtente = new UtenteBean(credenziali, nome, cognome, telefono);

            // 3. Chiamiamo il controller per salvarlo
            registrazioneController.registerUser(nuovoUtente);

            System.out.println("\n✅ Registrazione completata con successo!");
            System.out.println("Ora puoi effettuare il login con le tue nuove credenziali.");

        } catch (MailAlreadyExistsException e) {
            System.out.println("\n❌ Errore: L'indirizzo email inserito è già registrato nel sistema.");
        } catch (Exception e) {
            System.out.println("\n❌ Si è verificato un errore imprevisto: " + e.getMessage());
        }
    }

    // Metodo di utilità per gestire gli input obbligatori ed evitare che l'utente lasci i campi vuoti
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
        System.out.println("   REGISTRAZIONE NUOVO UTENTE     ");
        System.out.println("==================================");
    }
}