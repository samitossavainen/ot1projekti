package com.mokkikodit.controller;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class RaportitController {

    @FXML
    private VBox varausRapCard;
    @FXML
    private VBox mokkiRapCard;
    @FXML
    private VBox asiakasRapCard;
    @FXML
    private VBox laskuRapCard;

    @FXML
    public void initialize() {
        varausRapCard.setOnMouseClicked(this::openVarausRaportti);
        mokkiRapCard.setOnMouseClicked(this::openMokkiRaportti);
        asiakasRapCard.setOnMouseClicked(this::openAsiakasRaportti);
        laskuRapCard.setOnMouseClicked(this::openLaskutRaportti);
    }

    private void openVarausRaportti(MouseEvent event) {
        MainController.getInstance()
                .showCustomView("/fxml/varaukset_raportti.fxml");
    }

    private void openMokkiRaportti(MouseEvent event) {
        MainController.getInstance()
                .showCustomView("/fxml/mokit_raportti.fxml");
    }

    private void openAsiakasRaportti(MouseEvent event) {
        MainController.getInstance()
                .showCustomView("/fxml/asiakas_raportti.fxml");
    }

    private void openLaskutRaportti(MouseEvent event) {
        MainController.getInstance()
                .showCustomView("/fxml/laskut_raportti.fxml");
    }

}