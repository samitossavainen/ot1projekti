package com.mokkikodit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import com.mokkikodit.logiikka.VarausService;
import com.mokkikodit.mallit.*;

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

    @FXML
    private void create(ActionEvent event) {

        Asiakas a = new Asiakas(
                new Random().nextInt(10000),
                asiakasField.getText(),
                "demo@mail.com",
                "000"
        );

        Mokki m = new Mokki(
                new Random().nextInt(10000),
                mokkiField.getText(),
                4,
                100.0
        );

        Varaus v = new Varaus(
                new Random().nextInt(10000),
                a,
                m,
                alkuDatePicker.getValue(),
                loppuDatePicker.getValue(),
                "VARATTU"
        );

        varausService.addVaraus(v);

        close(event);
    }

    @FXML
    private void cancel(ActionEvent event) {
        close(event);
    }

    private void close(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}