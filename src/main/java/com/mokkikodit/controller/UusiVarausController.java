package com.mokkikodit.controller;

import com.mokkikodit.logiikka.VarausService;
import com.mokkikodit.mallit.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Random;

public class UusiVarausController {

    @FXML private TextField asiakasField;
    @FXML private TextField mokkiField;
    @FXML private DatePicker alkuDatePicker;
    @FXML private DatePicker loppuDatePicker;

    private VarausService varausService;

    public void setVarausService(VarausService service) {
        this.varausService = service;
    }

    // 🔥 BUTTON: "Vahvista"
    @FXML
    private void vahvista(ActionEvent event) {
        saveVaraus();
        close(event);
    }

    private void saveVaraus() {

        if (varausService == null) {
            throw new IllegalStateException("VarausService not set!");
        }

        Varaus v = new Varaus(
                new Random().nextInt(10000),
                new Asiakas(
                        new Random().nextInt(10000),
                        asiakasField.getText(),
                        "demo@mail.com",
                        "000"
                ),
                new Mokki(
                        new Random().nextInt(10000),
                        mokkiField.getText(),
                        4,
                        100.0
                ),
                alkuDatePicker.getValue(),
                loppuDatePicker.getValue(),
                "VARATTU"
        );

        varausService.addVaraus(v);
    }

    @FXML
    private void cancel(ActionEvent event) {
        close(event);
    }

    private void close(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();
        stage.close();
    }
}