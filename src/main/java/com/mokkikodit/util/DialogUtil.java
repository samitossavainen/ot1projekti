package com.mokkikodit.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DialogUtil {

    public static boolean confirm(Stage owner, String title, String header, String content) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.initOwner(owner);
        alert.initModality(Modality.WINDOW_MODAL);

        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        ButtonType yes = new ButtonType("Kyllä");
        ButtonType no = new ButtonType("Ei");

        alert.getButtonTypes().setAll(yes, no);

        return alert.showAndWait()
                .filter(response -> response == yes)
                .isPresent();
    }

}