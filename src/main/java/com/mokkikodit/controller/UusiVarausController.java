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

    // Injected instead of new
    private VarausService varausService;

    @FXML
    private void cancel(ActionEvent event) {
        close(event);
    }

    public void setVarausService(VarausService varausService) {
        this.varausService = varausService;
    }

    // =========================
    // CONFIRM
    // =========================
    @FXML
    private void vahvista(ActionEvent event) {

        try {
            validateInputs();

            Varaus v = new Varaus(
                    0,
                    Integer.parseInt(asiakasField.getText().trim()),
                    Integer.parseInt(mokkiField.getText().trim()),
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

    // =========================
    // VALIDATION
    // =========================
    private void validateInputs() {

        if (varausService == null) {
            throw new IllegalStateException("VarausService ei ole asetettu!");
        }

        if (asiakasField.getText().trim().isEmpty() ||
                mokkiField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Täytä kaikki kentät.");
        }

        // numeric check
        try {
            Integer.parseInt(asiakasField.getText().trim());
            Integer.parseInt(mokkiField.getText().trim());
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

    // =========================
    // CLOSE WINDOW
    // =========================
    private void close(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();
        stage.close();
    }
}