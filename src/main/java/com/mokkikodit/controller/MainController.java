package com.mokkikodit.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainController {

    @FXML private StackPane contentArea;
    @FXML private Button btnVaraukset;
    @FXML private Button btnMokit;
    @FXML private Button btnAsiakkaat;
    @FXML private Button btnLaskut;
    @FXML private Button btnRaportit;

    @FXML
    public void initialize() {
        showVaraukset();
    }

    @FXML
    private void showVaraukset() {
        loadView("/fxml/varaukset.fxml");
        setActive(btnVaraukset);
    }

    @FXML
    private void showMokit() {
        loadView("/fxml/mokit.fxml");
        setActive(btnMokit);
    }

    @FXML
    private void showAsiakkaat() {
        loadView("/fxml/asiakkaat.fxml");
        setActive(btnAsiakkaat);
    }

    @FXML
    private void showLaskutus() {
        loadView("/fxml/laskut.fxml");
        setActive(btnLaskut);
    }

    @FXML
    private void showRaportit() {
        loadView("/fxml/raportit.fxml");
        setActive(btnRaportit);
    }

    private void loadView(String fxmlPath) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
            contentArea.getChildren().clear();
        }
    }
    private void setActive(Button activeButton) {

        btnVaraukset.getStyleClass().remove("nav-button-active");
        btnMokit.getStyleClass().remove("nav-button-active");
        btnAsiakkaat.getStyleClass().remove("nav-button-active");
        btnLaskut.getStyleClass().remove("nav-button-active");
        btnRaportit.getStyleClass().remove("nav-button-active");

        activeButton.getStyleClass().add("nav-button-active");
    }
}