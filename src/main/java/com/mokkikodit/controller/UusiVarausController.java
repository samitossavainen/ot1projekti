package com.mokkikodit.controller;

import com.mokkikodit.logiikka.VarausService;
import com.mokkikodit.mallit.Varaus;
import com.mokkikodit.util.DialogUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class UusiVarausController {

    @FXML private TextField asiakasField;
    @FXML private TextField mokkiField;
    @FXML private DatePicker alkuDatePicker;
    @FXML private DatePicker loppuDatePicker;

    private VarausService varausService = new VarausService();

    @FXML
    private void vahvista(ActionEvent event) {

        try {
            validateInputs();

            Varaus v = new Varaus(
                    0, // DB handles ID
                    Integer.parseInt(asiakasField.getText()),
                    Integer.parseInt(mokkiField.getText()),
                    alkuDatePicker.getValue(),
                    loppuDatePicker.getValue(),
                    "VARATTU"
            );

            varausService.addVaraus(v);

            DialogUtil.showInfo("Varaus luotu onnistuneesti!");
            close(event);

        } catch (IllegalArgumentException e) {
            DialogUtil.showError(e.getMessage());
        } catch (Exception e) {
            DialogUtil.showError("Virhe varauksen luonnissa.");
            e.printStackTrace();
        }
    }

    private void validateInputs() {

        if (asiakasField.getText().isEmpty() ||
                mokkiField.getText().isEmpty()) {
            throw new IllegalArgumentException("Täytä kaikki kentät.");
        }

        try {
            Integer.parseInt(asiakasField.getText());
            Integer.parseInt(mokkiField.getText());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Asiakas- ja mökki-ID pitää olla numero.");
        }

        LocalDate alku = alkuDatePicker.getValue();
        LocalDate loppu = loppuDatePicker.getValue();

        if (alku == null || loppu == null) {
            throw new IllegalArgumentException("Valitse päivämäärät.");
        }

        if (!loppu.isAfter(alku)) {
            throw new IllegalArgumentException("Loppupäivän tulee olla alkupäivän jälkeen.");
        }
    }

    private void close(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();
        stage.close();
    }
}