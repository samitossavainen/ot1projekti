package com.mokkikodit.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class RaportitController {

    @FXML
    private VBox root;           // koko raportit-näkymä

    @FXML
    private VBox varausRapCard;  // VARAUSRAPORTTI-kortti

    @FXML
    public void initialize() {
        varausRapCard.setOnMouseClicked(this::openVarausRaportti);
    }

    private void openVarausRaportti(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/varaukset_raportti.fxml")
            );
            Parent varausRaporttiView = loader.load();

            root.getChildren().setAll(varausRaporttiView);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
