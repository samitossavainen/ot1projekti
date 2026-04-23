package com.mokkikodit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;

public class MainController {

    @FXML private Button btnVaraukset;
    @FXML private Button btnMokit;
    @FXML private Button btnAsiakkaat;
    @FXML private Button btnLaskutus;

    @FXML private StackPane contentArea;

    @FXML
    public void initialize() {

        showVaraukset();

        btnVaraukset.setOnAction(e -> showVaraukset());
        btnMokit.setOnAction(e -> showMokit());
        btnAsiakkaat.setOnAction(e -> showAsiakkaat());
        btnLaskutus.setOnAction(e -> showLaskutus());
    }

    private void showVaraukset() {
        setContent("Varaukset (oletus)");
    }

    private void showMokit() {
        setContent("Mökit");
    }

    private void showAsiakkaat() {
        setContent("Asiakkaat");
    }

    private void showLaskutus() {
        setContent("Laskutus");
    }

    private void setContent(String text) {
        contentArea.getChildren().clear();
        contentArea.getChildren().add(new Label(text));
    }
}