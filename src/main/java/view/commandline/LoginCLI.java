package view.commandline;

import beans.CredenzialiBean;
import beans.UtenteBean;
import controller.LoginController;
import exceptions.WrongEmailOrPasswordException;
import exceptions.NoResultException;
import exceptions.LoginAndRegistrationException;
import java.util.Scanner;

public class LoginCLI {

    private Scanner scanner;
    private LoginController loginController;

    public LoginCLI() {
        this.scanner = new Scanner(System.in);
        this.loginController = new LoginController();
    }

    public void start() {
        boolean running = true;

        while (running) {
            stampaIntestazione();

            System.out.print("Email: ");
            String email = scanner.nextLine().trim();

            System.out.print("Password: ");
            String password = scanner.nextLine().trim();

            try {
                // 1. Creiamo il bean delle sole credenziali
                CredenzialiBean credenziali = new CredenzialiBean(email, password);

                // 2. Controllo le credenziali (Metodo VOID come in BodyBuilding)
                // Se la password è sbagliata, lancia l'eccezione e salta subito al blocco 'catch'
                loginController.login(credenziali);

                // 3. Se il codice arriva a questa riga, il login è andato bene!
                // Ora creiamo l'UtenteBean vuoto e lo facciamo "riempire" dal controller
                UtenteBean utenteLoggato = new UtenteBean();
                utenteLoggato.setCredenziali(credenziali);

                // Metodo VOID che popola nome, cognome e telefono dentro l'oggetto utenteLoggato
                loginController.retrieveUtente(utenteLoggato);

                System.out.println("\n✅ Login effettuato con successo! Ciao " + utenteLoggato.getNome() + "!");

                // Scegliamo in che modalità entrare
                scegliModalitaHome(utenteLoggato);

                // Chiudiamo il ciclo al ritorno dalla Home
                running = false;

            } catch (WrongEmailOrPasswordException | NoResultException e) {
                System.out.println("\n❌ Errore: Email o password errati. Riprova.");
                System.out.println("Premi Invio per riprovare, oppure digita 'esci' per chiudere il programma.");
                if (scanner.nextLine().trim().equalsIgnoreCase("esci")) {
                    running = false;
                }
            } catch (LoginAndRegistrationException e) {
                System.out.println("\n❌ Errore di sistema durante il login: " + e.getMessage());
                running = false;
            }
        }
    }

    private void scegliModalitaHome(UtenteBean utenteLoggato) {
        boolean sceltaValida = false;
        while (!sceltaValida) {
            System.out.println("\nCome vuoi utilizzare l'app oggi?");
            System.out.println("1. Modalità GUIDATORE (Offri passaggi, gestisci auto)");
            System.out.println("2. Modalità PASSEGGERO (Cerca passaggi, prenota)");
            System.out.print("Scegli un'opzione: ");

            String scelta = scanner.nextLine().trim();

            if (scelta.equals("1")) {
                GuidatoreHomeCLI homeGuidatore = new GuidatoreHomeCLI(utenteLoggato);
                homeGuidatore.start();
                sceltaValida = true;
            } else if (scelta.equals("2")) {
                PasseggeroHomeCLI homePasseggero = new PasseggeroHomeCLI(utenteLoggato);
                homePasseggero.start();
                sceltaValida = true;
            } else {
                System.out.println("Scelta non valida. Inserisci 1 o 2.");
            }
        }
    }

    private void stampaIntestazione() {
        System.out.println("\n==================================");
        System.out.println("       CARPOOLING APP - LOGIN     ");
        System.out.println("==================================");
    }
}
