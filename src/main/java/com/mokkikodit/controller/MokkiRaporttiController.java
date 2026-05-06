package com.mokkikodit.controller;

import javafx.fxml.FXML;

public class MokkiRaporttiController {

    // Paluu napista raportti näkymään
    @FXML
    private void goBack() {
        MainController.getInstance().showRaportit();
    }
}
