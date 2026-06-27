package org.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import utilities.other.mappers.MapperRegistration;
import utilities.other.mappers.Session;
import view.gui.LoginGUI;

import java.io.IOException;
import java.util.Scanner;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // 1. Registrazione dei mappers
         MapperRegistration.registerMappers();

        Scanner scanner = new Scanner(System.in);
        boolean validInput = false;

        // Loop per far scegliere all'utente l'interfaccia
        while (!validInput) {
            try {
                showMenu();
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        graphicInterface(stage);
                        validInput = true;
                        break;
                    case 2:
                        interfaceCLI();
                        validInput = true;
                        break;
                    default:
                        System.err.println("Scelta non valida. Riprova.");
                }
            } catch (Exception e) {
                System.err.println("Errore di input o di avvio: " + e.getMessage());
                scanner.nextLine();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    // --- METODO PER AVVIARE LA GRAFICA (GUI) ---q
    public void graphicInterface(Stage stage) throws IOException {
        // Inizializza la Sessione
        Session session = new Session();

        // NOTA: Il tuo amico usa FXMLPathConfig per leggere i percorsi da un file properties.
        // Per ora usiamo il percorso diretto al nostro Login per fare il test. Lo aggiungeremo dopo.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/view/login.fxml"));

        // Inietta il controller esattamente come fa lui
        loader.setControllerFactory(c -> new LoginGUI(session));

        Parent rootParent = loader.load();
        Scene scene = new Scene(rootParent);

        stage.setTitle("Carpooling App"); // Titolo personalizzato
        stage.setScene(scene);
        stage.setResizable(false);

        // Aggiunge un filtro per chiudere l'app premendo ESCAPE (Stile del tuo amico)
        stage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                event.consume();
                Platform.exit();
            }
        });

        stage.show();
    }

    // --- METODO PER MOSTRARE IL MENU NEL TERMINALE ---
    public void showMenu() {
        System.out.println(" ");
        System.out.println("-------------- Carpooling App --------------");
        System.out.println("Scegli l'interfaccia da utilizzare:");
        System.out.println("1. Interfaccia grafica (JavaFX)");
        System.out.println("2. Interfaccia a riga di comando (CLI)");
        System.out.print("Scelta: ");
    }

    // --- METODO PER AVVIARE IL TERMINALE (CLI) ---
    public void interfaceCLI() {
        System.out.println("Avvio interfaccia CLI in corso...");



        System.out.println("Arrivederci!");
    }
}