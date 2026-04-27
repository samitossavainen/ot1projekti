package com.mokkikodit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class UusiVarausController {

    @FXML
    private void cancel(ActionEvent event) {
        close(event);
    }

    @FXML
    private void create(ActionEvent event) {
        // Tässä EI vielä luoda varausta
        close(event);
    }

    private void close(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}