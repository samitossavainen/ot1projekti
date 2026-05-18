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

    // Pääsisältöalue, johon eri näkymät ladataan
    @FXML private StackPane contentArea;

    // Navigaatiopainikkeet eri näkymille
    @FXML private Button btnVaraukset;
    @FXML private Button btnMokit;
    @FXML private Button btnAsiakkaat;
    @FXML private Button btnLaskut;
    @FXML private Button btnRaportit;

    // Staattinen instanssi (käytetään esim. näkymän vaihtoon muualta)
    private static MainController instance;

    @FXML
    public void initialize() {
        // Tallennetaan kontrollin instanssi ja avataan oletusnäkymä
        instance = this;
        showVaraukset();
    }

    // Näytetään varaukset-näkymä
    @FXML
    private void showVaraukset() {
        loadView("/fxml/varaukset.fxml");
        setActive(btnVaraukset);
    }

    // Näytetään mökit-näkymä
    @FXML
    private void showMokit() {
        loadView("/fxml/mokit.fxml");
        setActive(btnMokit);
    }

    // Näytetään asiakkaat-näkymä
    @FXML
    private void showAsiakkaat() {
        loadView("/fxml/asiakkaat.fxml");
        setActive(btnAsiakkaat);
    }

    // Näytetään laskutus-näkymä
    @FXML
    private void showLaskutus() {
        loadView("/fxml/laskut.fxml");
        setActive(btnLaskut);
    }

    // Näytetään raportit-näkymä
    @FXML
    void showRaportit() {
        loadView("/fxml/raportit.fxml");
        setActive(btnRaportit);
    }

    /**
     * Lataa FXML-näkymän ja asettaa sille tarvittavat service-riippuvuudet.
     * Tämä toimii eräänlaisena "manuaalisena dependency injectionina".
     */
    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource(fxmlPath));

            // Ladataan näkymä FXML-tiedostosta
            Parent view = loader.load();

            // Haetaan kyseisen näkymän controller
            Object controller = loader.getController();

            // VarausController tarvitsee useita servicejä
            if (controller instanceof VarausController) {
                VarausController vc = (VarausController) controller;

                vc.setVarausService(
                        new VarausService(new VarausRepository())
                );
                vc.setAsiakasService(
                        new AsiakasService(new AsiakasRepository())
                );
                vc.setMokkiService(
                        new MokkiService(new MokkiRepository())
                );
            }

            // AsiakasController saa asiakas-palvelun
            if (controller instanceof AsiakasController) {
                ((AsiakasController) controller).setAsiakasService(
                        new AsiakasService(new AsiakasRepository())
                );
            }

            // MokkiController saa mökki-palvelun
            if (controller instanceof MokkiController) {
                ((MokkiController) controller).setMokkiService(
                        new MokkiService(new MokkiRepository())
                );
            }

            // LaskutController saa lasku- ja varauspalvelun
            if (controller instanceof LaskutController) {

                VarausService vs = new VarausService(new VarausRepository());

                LaskuService ls = new LaskuService(
                        new LaskuRepository(),
                        vs
                );

                ((LaskutController) controller).setLaskuService(ls);
            }

            // Laskutusraportti käyttää laskupalvelua
            if (controller instanceof LaskutRaporttiController) {

                VarausService vs = new VarausService(new VarausRepository());

                ((LaskutRaporttiController) controller).setLaskuService(
                        new LaskuService(new LaskuRepository(), vs)
                );
            }

            // Mökkiraportti tarvitsee useita palveluja
            if (controller instanceof MokkiRaporttiController) {

                VarausService vs = new VarausService(new VarausRepository());

                ((MokkiRaporttiController) controller).setMokkiService(
                        new MokkiService(new MokkiRepository())
                );

                ((MokkiRaporttiController) controller).setLaskuService(
                        new LaskuService(new LaskuRepository(), vs)
                );

                ((MokkiRaporttiController) controller).setVarausService(vs);
            }

            // Asiakasraportti tarvitsee asiakas- ja varauspalvelut
            if (controller instanceof AsiakasRaporttiController) {
                ((AsiakasRaporttiController) controller).setAsiakasService(
                        new AsiakasService(new AsiakasRepository())
                );
                ((AsiakasRaporttiController) controller).setVarausService(
                        new VarausService(new VarausRepository())
                );
            }

            // Varausraportti tarvitsee asiakas-, varaus- ja mökkipalvelut
            if (controller instanceof VarausRaporttiController) {
                ((VarausRaporttiController) controller).setAsiakasService(
                        new AsiakasService(new AsiakasRepository())
                );
                ((VarausRaporttiController) controller).setVarausService(
                        new VarausService(new VarausRepository())
                );
                ((VarausRaporttiController) controller).setMokkiService(
                        new MokkiService(new MokkiRepository())
                );
            }

            // Asetetaan ladattu näkymä näkyväksi pääalueelle
            contentArea.getChildren().setAll(view);

        } catch (IOException e) {
            e.printStackTrace();
            contentArea.getChildren().clear();
        }
    }

    /**
     * Korostaa aktiivisen navigaatiopainikkeen UI:ssa.
     */
    private void setActive(Button activeButton) {

        // Poistetaan aktiivisuus kaikista painikkeista
        btnVaraukset.getStyleClass().remove("nav-button-active");
        btnMokit.getStyleClass().remove("nav-button-active");
        btnAsiakkaat.getStyleClass().remove("nav-button-active");
        btnLaskut.getStyleClass().remove("nav-button-active");
        btnRaportit.getStyleClass().remove("nav-button-active");

        // Lisätään aktiivinen tyyli valitulle painikkeelle
        activeButton.getStyleClass().add("nav-button-active");
    }

    // Palauttaa tämän controllerin instanssin muualta käytettäväksi
    public static MainController getInstance() {
        return instance;
    }

    // Mahdollistaa mukautetun näkymän lataamisen raporttinäkymän kontekstissa
    public void showCustomView(String fxmlPath) {
        loadView(fxmlPath);
        setActive(btnRaportit); // pysyy Raportit-tilassa
    }

}