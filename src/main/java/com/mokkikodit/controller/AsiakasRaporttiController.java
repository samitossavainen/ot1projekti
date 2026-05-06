package com.mokkikodit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class AsiakasRaporttiController {

    @FXML private ComboBox<?> asiakasComboBox;
    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneLabel;

    // Paluu napista raportti näkymään
    @FXML
    private void goBack() {
        MainController.getInstance().showRaportit();
    }
}
