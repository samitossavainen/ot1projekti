package com.mokkikodit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class MainApp extends Application {

    // Ikkunoiden koon määrittämiseen käytettävät vakiot
    private static final double WINDOW_WIDTH = 1100;
    private static final double WINDOW_HEIGHT = 750;
    private static final double MIN_WIDTH = 900;
    private static final double MIN_HEIGHT = 650;

    @Override
    public void start(Stage stage) throws Exception {

        /*
         * Lataa pää-FXML-tiedosto.
         * Objects.requireNonNull() estää mahdollisen NullPointerException-virheen.
         * jos resurssipolku on virheellinen tai tiedosto puuttuu.
         */
        URL fxmlLocation = getClass().getResource("/fxml/main.fxml");

        if (fxmlLocation == null) {
            throw new IllegalStateException("FXML file not found: /fxml/main.fxml");
        }

        FXMLLoader loader = new FXMLLoader(fxmlLocation);

        // Luo kohtaus ladatusta FXML-tiedostosta
        Scene scene = new Scene(loader.load());

        /*
         * Ladataan CSS-tyylitiedosto
         */
        URL cssLocation = getClass().getResource("/css/styles.css");

        if (cssLocation != null) {
            scene.getStylesheets().add(cssLocation.toExternalForm());
        } else {
            System.err.println("CSS file not found: /css/styles.css");
        }

        // Määritä sovelluksen ikkuna
        stage.setTitle("Mökkikodit oy");
        stage.setScene(scene);

        // Ikkunan alkuperäinen koko
        stage.setWidth(WINDOW_WIDTH);
        stage.setHeight(WINDOW_HEIGHT);

        // Ikkunan pienin sallittu koko
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);

        // Avaa ikkuna
        stage.show();
    }

    /**
     * Sovelluksen aloituskohta.
     * JavaFX-ajoympäristö toimittaa args-parametrin.
     */
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        launch(args);
    }
}