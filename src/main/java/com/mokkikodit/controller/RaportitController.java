package com.mokkikodit.controller;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class RaportitController {

    // Raporttikortit (UI-elementit), joita klikataan eri raportteihin siirtymiseksi
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

        // Asetetaan klikattavat toiminnot jokaiselle raporttikortille
        varausRapCard.setOnMouseClicked(this::openVarausRaportti);
        mokkiRapCard.setOnMouseClicked(this::openMokkiRaportti);
        asiakasRapCard.setOnMouseClicked(this::openAsiakasRaportti);
        laskuRapCard.setOnMouseClicked(this::openLaskutRaportti);
    }

    // Avaa varausraporttinäkymän
    private void openVarausRaportti(MouseEvent event) {
        MainController.getInstance()
                .showCustomView("/fxml/varaukset_raportti.fxml");
    }

    // Avaa mökkiraporttinäkymän
    private void openMokkiRaportti(MouseEvent event) {
        MainController.getInstance()
                .showCustomView("/fxml/mokit_raportti.fxml");
    }

    // Avaa asiakasraporttinäkymän
    private void openAsiakasRaportti(MouseEvent event) {
        MainController.getInstance()
                .showCustomView("/fxml/asiakas_raportti.fxml");
    }

    // Avaa laskuraporttinäkymän
    private void openLaskutRaportti(MouseEvent event) {
        MainController.getInstance()
                .showCustomView("/fxml/laskut_raportti.fxml");
    }
}