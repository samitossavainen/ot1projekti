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

    // UI-komponentit varauksen luontiin
    @FXML
    private ComboBox<Asiakas> asiakasComboBox;
    @FXML
    private ComboBox<Mokki> mokkiComboBox;
    @FXML
    private DatePicker alkuDatePicker;
    @FXML
    private DatePicker loppuDatePicker;

    // Varauspalvelu (liiketoimintalogiikka)
    private VarausService varausService;

    // Repositoriot asiakas- ja mökkidatan hakemiseen
    private final AsiakasRepository asiakasRepo =
            new AsiakasRepository();

    private final MokkiRepository mokkiRepo =
            new MokkiRepository();

    // Callback kun varaus on luotu
    private Runnable onVarausCreated;

    // Onnistumismerkintä
    private boolean varausLisatty = false;


    // Asetetaan palvelu ulkopuolelta (dependency injection)
    public void setVarausService(VarausService varausService) {
        this.varausService = varausService;
    }

    // Asetetaan callback, jota kutsutaan onnistuneen varauksen jälkeen
    public void setOnVarausCreated(Runnable onVarausCreated) {
        this.onVarausCreated = onVarausCreated;
    }


    @FXML
    public void initialize() {

        // Ladataan asiakkaat comboboxiin
        asiakasComboBox.setItems(
                FXCollections.observableArrayList(asiakasRepo.findAllAvailable())
        );

        // Ladataan mökit comboboxiin
        mokkiComboBox.setItems(
                FXCollections.observableArrayList(
                        mokkiRepo.findAllAvailable()
                )
        );
    }


    @FXML
    private void vahvista(ActionEvent event) {

        try {
            // Tarkistetaan syötteiden validius
            validateInputs();

            // Haetaan valitut arvot UI:sta
            Asiakas asiakas = asiakasComboBox.getValue();
            Mokki mokki = mokkiComboBox.getValue();

            // Luodaan uusi varausolio
            Varaus v = new Varaus();
            v.setAsiakasEmail(asiakas.getSapo());
            v.setMokkiId(mokki.getMokkiId());
            v.setAlkuPvm(alkuDatePicker.getValue());
            v.setLoppuPvm(loppuDatePicker.getValue());
            v.setTila("aktiivinen");
            v.setKokonaissumma(0.0);

            // Tallennetaan varaus palvelun kautta
            varausService.addVaraus(v);

            varausLisatty = true;

            // Kutsutaan callbackia jos sellainen on asetettu
            if (onVarausCreated != null){
                onVarausCreated.run();
            }

            // Näytetään onnistumisviesti
            DialogUtil.showInfo("Varaus luotu onnistuneesti!");

            // Suljetaan ikkuna
            close(event);

        } catch (IllegalArgumentException e) {
            // Käyttäjän syötevirhe
            DialogUtil.showError(e.getMessage());
        } catch (Exception e) {
            // Yleinen virhetilanne
            DialogUtil.showError("Virhe varauksen luonnissa.");
            e.printStackTrace();
        }
    }


    // Syötteiden validointi ennen tallennusta
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


    // Peruuta-nappi
    @FXML
    private void cancel(ActionEvent event) {
        close(event);
    }

    // Sulkee ikkunan
    private void close(ActionEvent event) {

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.close();
    }

    // Palauttaa tiedon onnistuneesta lisäyksestä
    public boolean isVarausLisatty() {
        return varausLisatty;
    }
}