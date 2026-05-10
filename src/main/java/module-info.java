module carpoolingproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires java.xml.crypto;

    // 1. Apri la cartella dove tieni i file .java della grafica (es. LoginGUI.java)
    // Se la cartella si chiama 'view.gui' scrivi così:
    opens view.gui to javafx.fxml;
    exports view.gui;

    // 2. Apri le altre cartelle. CONTROLLA SE HANNO LA 'S' FINALE
    exports controller;
    opens controller to javafx.fxml;

    exports model;
    opens model to javafx.fxml;

    // ESEMPIO: Se la cartella si chiama 'beans' (con la S), scrivi 'beans'
    exports beans;
    opens beans to javafx.fxml;

    exports exceptions;
    opens exceptions to javafx.fxml;

    // 3. Esporta il pacchetto che contiene il Main.java
    // Se Main.java è dentro la cartella 'main', scrivi:
    exports org.example;
}