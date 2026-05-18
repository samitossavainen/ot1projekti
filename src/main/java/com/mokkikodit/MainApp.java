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
         * Lataa pää-FXML-tiedosto sovelluksen käyttöliittymää varten.
         * Objects.requireNonNull() -tyyppinen tarkistus varmistaa,
         * että resurssi löytyy ennen käyttöä (ei tule NullPointerException-virhettä).
         */
        URL fxmlLocation = getClass().getResource("/fxml/main.fxml");

        if (fxmlLocation == null) {
            throw new IllegalStateException("FXML file not found: /fxml/main.fxml");
        }

        FXMLLoader loader = new FXMLLoader(fxmlLocation);

        // Luodaan Scene ladatun FXML-rakenteen perusteella
        Scene scene = new Scene(loader.load());

        /*
         * Ladataan CSS-tyylitiedosto käyttöliittymän ulkoasun määrittämiseksi
         */
        URL cssLocation = getClass().getResource("/css/styles.css");

        if (cssLocation != null) {
            scene.getStylesheets().add(cssLocation.toExternalForm());
        } else {
            System.err.println("CSS file not found: /css/styles.css");
        }

        // Asetetaan sovelluksen pääikkunan otsikko
        stage.setTitle("Mökkikodit Oy");

        // Asetetaan Scene ikkunaan
        stage.setScene(scene);

        // Määritetään ikkunan oletuskoko
        stage.setWidth(WINDOW_WIDTH);
        stage.setHeight(WINDOW_HEIGHT);

        // Määritetään ikkunan minimikoko
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);

        // Näytetään sovelluksen ikkuna
        stage.show();
    }

    /**
     * Sovelluksen käynnistyspiste (JavaFX entry point).
     * launch(args) käynnistää JavaFX-sovelluksen elinkaaren.
     */
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        launch(args);
    }
}