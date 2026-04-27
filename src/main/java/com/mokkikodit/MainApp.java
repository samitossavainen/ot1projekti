package com.mokkikodit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/main.fxml")
        );

        Scene scene = new Scene(loader.load());

        //CSS tyylit
        scene.getStylesheets().add(
                getClass().getResource("/css/styles.css").toExternalForm()
        );

        stage.setTitle("Mökkikodit");
        stage.setScene(scene);

        stage.setWidth(1100);
        stage.setHeight(750);

        stage.setMinWidth(900);
        stage.setMinHeight(650);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}