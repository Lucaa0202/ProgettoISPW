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
import view.commandline.LoginCLI; // <--- IMPORTANTE: Importa la nostra CLI

import java.io.IOException;
import java.util.Scanner;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        MapperRegistration.registerMappers();

        Scanner scanner = new Scanner(System.in);
        boolean validInput = false;

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

    public void graphicInterface(Stage stage) throws IOException {
        Session session = new Session();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/view/login.fxml"));
        loader.setControllerFactory(c -> new LoginGUI(session));

        Parent rootParent = loader.load();
        Scene scene = new Scene(rootParent);

        stage.setTitle("Carpooling App");
        stage.setScene(scene);
        stage.setResizable(false);

        stage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                event.consume();
                Platform.exit();
            }
        });

        stage.show();
    }

    public void showMenu() {
        System.out.println(" ");
        System.out.println("-------------- Carpooling App --------------");
        System.out.println("Scegli l'interfaccia da utilizzare:");
        System.out.println("1. Interfaccia grafica (JavaFX)");
        System.out.println("2. Interfaccia a riga di comando (CLI)");
        System.out.print("Scelta: ");
    }

    // --- ORA IL TUO MAIN È COLLEGATO ALLA CLI ---
    public void interfaceCLI() {
        LoginCLI loginCLI = new LoginCLI();
        loginCLI.start();
        System.out.println("Arrivederci!");
    }
}