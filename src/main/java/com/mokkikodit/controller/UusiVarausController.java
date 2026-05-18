package com.mokkikodit.controller;

import com.mokkikodit.logiikka.VarausService;
import com.mokkikodit.mallit.Asiakas;
import com.mokkikodit.mallit.Mokki;
import com.mokkikodit.mallit.Varaus;
import com.mokkikodit.tietokanta.AsiakasRepository;
import com.mokkikodit.tietokanta.MokkiRepository;
import com.mokkikodit.util.DialogUtil;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.time.LocalDate;

public class UusiVarausController {

    @FXML
    private ComboBox<Asiakas> asiakasComboBox;
    @FXML
    private ComboBox<Mokki> mokkiComboBox;
    @FXML
    private DatePicker alkuDatePicker;
    @FXML
    private DatePicker loppuDatePicker;
    private VarausService varausService;

    private final AsiakasRepository asiakasRepo =
            new AsiakasRepository();

    private final MokkiRepository mokkiRepo =
            new MokkiRepository();

    private Runnable onVarausCreated;

    private boolean varausLisatty = false;


    public void setVarausService(VarausService varausService) {
        this.varausService = varausService;
    }

    public void setOnVarausCreated(Runnable onVarausCreated) {
        this.onVarausCreated = onVarausCreated;
    }


    @FXML
    public void initialize() {

        // Asiakkaiden sähköpostit
        asiakasComboBox.setItems(
                FXCollections.observableArrayList(asiakasRepo.findAllAvailable())
        );

        // Mökit
        mokkiComboBox.setItems(
                FXCollections.observableArrayList(
                        mokkiRepo.findAllAvailable()
                )
        );
    }


    @FXML
    private void vahvista(ActionEvent event) {

        try {
            validateInputs();

            Asiakas asiakas = asiakasComboBox.getValue();
            Mokki mokki = mokkiComboBox.getValue();

            Varaus v = new Varaus();
            v.setAsiakasEmail(asiakas.getSapo());
            v.setMokkiId(mokki.getMokkiId());
            v.setAlkuPvm(alkuDatePicker.getValue());
            v.setLoppuPvm(loppuDatePicker.getValue());
            v.setTila("aktiivinen");
            v.setKokonaissumma(0.0);

            varausService.addVaraus(v);
            varausLisatty = true;

            if (onVarausCreated != null){
                onVarausCreated.run();
            }

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

        if (varausService == null) {
            throw new IllegalStateException(
                    "VarausService ei ole asetettu!"
            );
        }

        if (asiakasComboBox.getValue() == null ||
                mokkiComboBox.getValue() == null) {

            throw new IllegalArgumentException(
                    "Valitse asiakas ja mökki."
            );
        }

        LocalDate alku = alkuDatePicker.getValue();
        LocalDate loppu = loppuDatePicker.getValue();

        if (alku == null || loppu == null) {

            throw new IllegalArgumentException(
                    "Valitse päivämäärät."
            );
        }

        if (!loppu.isAfter(alku)) {

            throw new IllegalArgumentException(
                    "Loppupäivän tulee olla alkupäivän jälkeen."
            );
        }
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

    public boolean isVarausLisatty() {
        return varausLisatty;
    }
}