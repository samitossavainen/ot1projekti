package com.mokkikodit.controller;

import javafx.fxml.FXML;

public class VarausRaporttiController {

    @FXML
    private void goBack() {

        MainController.getInstance().showRaportit();
    }
}