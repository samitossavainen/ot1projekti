package com.mokkikodit.controller;

import javafx.fxml.FXML;

public class LaskutRaporttiController {

    // Paluu napista raportti näkymään
    @FXML
    private void goBack() {
        MainController.getInstance().showRaportit();
    }
}