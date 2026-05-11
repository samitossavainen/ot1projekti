package com.mokkikodit.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DialogUtil {

    // INFO DIALOG
    public static void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // ERROR DIALOG
    public static void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Virhe");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // CONFIRM DIALOG
    public static boolean confirm(Stage owner, String title, String header, String content) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.initOwner(owner);
        alert.initModality(Modality.WINDOW_MODAL);

        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        ButtonType yes = new ButtonType("Kyllä");
        ButtonType no = new ButtonType("Ei");

        alert.getButtonTypes().setAll(no, yes);

        return alert.showAndWait()
                .filter(response -> response == yes)
                .isPresent();
    }
}
