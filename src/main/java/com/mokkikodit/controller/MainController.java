package com.mokkikodit.controller;

import com.mokkikodit.logiikka.AsiakasService;
import com.mokkikodit.logiikka.LaskuService;
import com.mokkikodit.logiikka.MokkiService;
import com.mokkikodit.tietokanta.AsiakasRepository;
import com.mokkikodit.tietokanta.LaskuRepository;
import com.mokkikodit.tietokanta.MokkiRepository;
import com.mokkikodit.logiikka.VarausService;
import com.mokkikodit.tietokanta.VarausRepository;

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

    private static MainController instance;

    @FXML
    public void initialize() {
        instance = this;
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
    void showRaportit() {
        loadView("/fxml/raportit.fxml");
        setActive(btnRaportit);
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();

            Object controller = loader.getController();

            if (controller instanceof VarausController) {
                ((VarausController) controller).setVarausService(
                        new VarausService(new VarausRepository())
                );
            }

            if (controller instanceof AsiakasController) {
                ((AsiakasController) controller).setAsiakasService(
                        new AsiakasService(new AsiakasRepository())
                );
            }

            if (controller instanceof MokkiController) {
                ((MokkiController) controller).setMokkiService(
                        new MokkiService(new MokkiRepository())
                );
            }

            if (controller instanceof LaskutController) {
                ((LaskutController) controller).setLaskuService(
                        new LaskuService(new LaskuRepository())
                );
            }

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
    public static MainController getInstance() {
        return instance;
    }

    public void showCustomView(String fxmlPath) {
        loadView(fxmlPath);
        setActive(btnRaportit); // pysyy Raportit-tilassa
    }

}