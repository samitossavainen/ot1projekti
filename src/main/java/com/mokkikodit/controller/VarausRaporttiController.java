package com.mokkikodit.controller;

import com.mokkikodit.logiikka.AsiakasService;
import com.mokkikodit.logiikka.MokkiService;
import com.mokkikodit.logiikka.VarausService;

import com.mokkikodit.mallit.Asiakas;
import com.mokkikodit.mallit.Mokki;
import com.mokkikodit.mallit.Varaus;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import javafx.fxml.FXML;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDate;

public class VarausRaporttiController {

    // -------------------------------------------------
    // SUODATTIMET (käyttäjän raporttisuodatukset)
    // -------------------------------------------------

    @FXML private DatePicker alkuDatePicker;
    @FXML private DatePicker loppuDatePicker;

    @FXML private ComboBox<String> tilaComboBox;
    @FXML private ComboBox<String> mokkiComboBox;
    @FXML private ComboBox<String> asiakasComboBox;

    // -------------------------------------------------
    // TAULU (raportin päätaulu varauksille)
    // -------------------------------------------------

    @FXML private TableView<Varaus> tableRaportti;

    @FXML private TableColumn<Varaus, Number> varausIdCol;
    @FXML private TableColumn<Varaus, String> mokkiCol;
    @FXML private TableColumn<Varaus, String> asiakasCol;
    @FXML private TableColumn<Varaus, LocalDate> alkuCol;
    @FXML private TableColumn<Varaus, LocalDate> loppuCol;
    @FXML private TableColumn<Varaus, String> tilaCol;
    @FXML private TableColumn<Varaus, Double> summaCol;

    // -------------------------------------------------
    // YHTEENVETO (raportin laskennalliset tunnusluvut)
    // -------------------------------------------------

    @FXML private Label yhteensaLabel;
    @FXML private Label varatutLabel;
    @FXML private Label perututLabel;
    @FXML private Label kokonaissummaLabel;

    // -------------------------------------------------
    // SERVICE-PALVELUT (liiketoimintalogiikan rajapinnat)
    // -------------------------------------------------

    private VarausService varausService;
    private AsiakasService asiakasService;
    private MokkiService mokkiService;

    // -------------------------------------------------
    // DATA (kaikki varaukset + suodatettu näkymä)
    // -------------------------------------------------

    private final ObservableList<Varaus> varaukset =
            FXCollections.observableArrayList();

    private FilteredList<Varaus> filteredVaraukset;

    // -------------------------------------------------
    // ALOITA (INITIALIZE) - näkymän alustus
    // -------------------------------------------------

    @FXML
    public void initialize() {

        // Asetetaan taulun sarakkeet
        setupTable();

        // Luodaan suodatettu lista (oletuksena kaikki näkyy)
        filteredVaraukset = new FilteredList<>(varaukset, v -> true);

        // Liitetään taulu suodatettuun dataan
        tableRaportti.setItems(filteredVaraukset);
        tableRaportti.setSelectionModel(null);
        tableRaportti.setFocusTraversable(false);

        // Asetetaan suodattimien tapahtumakuuntelijat
        setupFilters();
    }

    // -------------------------------------------------
    // SERVICE-PALVELUT (setterit kontrollereiden injektointiin)
    // -------------------------------------------------

    public void setVarausService(VarausService service) {
        this.varausService = service;

        refreshTable();
    }

    public void setAsiakasService(AsiakasService service) {
        this.asiakasService = service;
    }

    public void setMokkiService(MokkiService service) {
        this.mokkiService = service;

        loadMokit();
    }

    // -------------------------------------------------
    // TAULUN ASETTAMINEN (sarakkeiden datamappaus)
    // -------------------------------------------------

    private void setupTable() {

        varausIdCol.setCellValueFactory(data ->
                new SimpleIntegerProperty(
                        data.getValue().getVarausId()
                ));

        mokkiCol.setCellValueFactory(data -> {

            Mokki m = null;

            if (mokkiService != null) {
                m = mokkiService.haeIdlla(
                        data.getValue().getMokkiId()
                );
            }

            String nimi = (m != null)
                    ? m.getNimi()
                    : "-";

            return new SimpleStringProperty(nimi);
        });

        asiakasCol.setCellValueFactory(data -> {

            Asiakas a = null;

            if (asiakasService != null) {
                a = asiakasService.hae(
                        data.getValue().getAsiakasEmail()
                );
            }

            String nimi = (a != null)
                    ? a.getNimi() + " ("+a.getSapo()+")"
                    : data.getValue().getAsiakasEmail();

            return new SimpleStringProperty(nimi);
        });

        alkuCol.setCellValueFactory(data ->
                new SimpleObjectProperty<>(
                        data.getValue().getAlkuPvm()
                ));

        loppuCol.setCellValueFactory(data ->
                new SimpleObjectProperty<>(
                        data.getValue().getLoppuPvm()
                ));

        tilaCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        formatTila(data.getValue().getTila())
                ));

        summaCol.setCellValueFactory(data ->
                new SimpleDoubleProperty(
                        data.getValue().getKokonaissumma()
                ).asObject());
    }

    // -------------------------------------------------
    // SUODATTIMET (käyttäjän valinnat ja rajaukset)
    // -------------------------------------------------

    private void setupFilters() {

        tilaComboBox.getItems().addAll(
                "Kaikki",
                "Aktiivinen",
                "Maksettu",
                "Peruutettu"
        );

        tilaComboBox.setValue("Kaikki");

        // Kaikki suodattimet päivittävät näkymän muutoksessa
        tilaComboBox.setOnAction(e -> applyFilters());

        mokkiComboBox.setOnAction(e -> applyFilters());

        asiakasComboBox.setOnAction(e -> applyFilters());

        alkuDatePicker.setOnAction(e -> applyFilters());

        loppuDatePicker.setOnAction(e -> applyFilters());
    }

    private void applyFilters() {

        filteredVaraukset.setPredicate(v -> {

            // -------------------------
            // PÄIVÄMÄÄRÄSUODATIN
            // -------------------------

            LocalDate alku = alkuDatePicker.getValue();
            LocalDate loppu = loppuDatePicker.getValue();

            if (alku != null &&
                    v.getLoppuPvm().isBefore(alku)) {

                return false;
            }

            if (loppu != null &&
                    v.getAlkuPvm().isAfter(loppu)) {

                return false;
            }

            // -------------------------
            // TILASUODATIN
            // -------------------------

            String tila = tilaComboBox.getValue();

            if (tila != null &&
                    !tila.equals("Kaikki") &&
                    !v.getTila().equalsIgnoreCase(tila)) {

                return false;
            }

            // -------------------------
            // MÖKKISUODATIN
            // -------------------------

            String mokki = mokkiComboBox.getValue();

            if (mokki != null &&
                    !mokki.equals("Kaikki")) {

                Mokki m = mokkiService.haeIdlla(v.getMokkiId());

                if (m == null ||
                        !m.getNimi().equalsIgnoreCase(mokki)) {

                    return false;
                }
            }

            // -------------------------
            // ASIAKASSUODATIN
            // -------------------------

            String asiakas = asiakasComboBox.getValue();

            if (asiakas != null &&
                    !asiakas.equals("Kaikki")) {

                Asiakas a = asiakasService.hae(
                        v.getAsiakasEmail()
                );

                String nimi = (a != null)
                        ? a.getNimi() + " ("+a.getSapo()+")"
                        : v.getAsiakasEmail();

                if (!nimi.equalsIgnoreCase(asiakas)) {
                    return false;
                }
            }

            return true;
        });

        // Päivitetään yhteenvedot aina suodatuksen jälkeen
        updateSummary();
    }

    // -------------------------------------------------
    // YHTEENVETO (raportin laskelmat)
    // -------------------------------------------------

    private void updateSummary() {

        int yhteensa = filteredVaraukset.size();

        int aktiiviset = 0;
        int perutut = 0;

        double summa = 0;

        for (Varaus v : filteredVaraukset) {

            if ("aktiivinen".equalsIgnoreCase(v.getTila()) ||
                    "maksettu".equalsIgnoreCase(v.getTila())) {

                aktiiviset++;

                summa += v.getKokonaissumma();
            }

            if ("peruutettu".equalsIgnoreCase(v.getTila())) {
                perutut++;
            }
        }

        // Näytetään raportin tunnusluvut käyttöliittymässä
        yhteensaLabel.setText(String.valueOf(yhteensa));

        varatutLabel.setText(String.valueOf(aktiiviset));

        perututLabel.setText(String.valueOf(perutut));

        kokonaissummaLabel.setText(
                String.format("%.2f €", summa)
        );
    }

    // -------------------------------------------------
    // TIETOJEN LATAUS (varaukset ja listat UI:lle)
    // -------------------------------------------------

    private void refreshTable() {

        if (varausService == null) return;

        varaukset.setAll(varausService.getAllVaraukset());

        loadAsiakkaat();

        updateSummary();
    }

    private void loadMokit() {

        mokkiComboBox.getItems().clear();

        mokkiComboBox.getItems().add("Kaikki");

        if (mokkiService == null) return;

        for (Mokki m : mokkiService.haeKaikki()) {

            if (!mokkiComboBox.getItems().contains(m.getNimi())) {
                mokkiComboBox.getItems().add(m.getNimi());
            }
        }

        mokkiComboBox.setValue("Kaikki");
    }

    private void loadAsiakkaat() {

        asiakasComboBox.getItems().clear();

        asiakasComboBox.getItems().add("Kaikki");

        for (Varaus v : varaukset) {

            Asiakas a = asiakasService.hae(
                    v.getAsiakasEmail()
            );

            String nimi = (a != null)
                    ? a.getNimi() + " ("+a.getSapo()+")"
                    : v.getAsiakasEmail();

            if (!asiakasComboBox.getItems().contains(nimi)) {
                asiakasComboBox.getItems().add(nimi);
            }
        }

        asiakasComboBox.setValue("Kaikki");
    }

    // -------------------------------------------------
    // AVUSTAJAT (apumetodit UI-esityksiin)
    // -------------------------------------------------

    private String formatTila(String tila) {

        if (tila == null) return "-";

        switch (tila.toLowerCase()) {

            case "aktiivinen":
                return "Aktiivinen";

            case "maksettu":
                return "Maksettu";

            case "peruutettu":
                return "Peruutettu";

            default:
                return tila;
        }
    }

    // -------------------------------------------------
    // Paluu napista raporttinäkymään
    // -------------------------------------------------

    @FXML
    private void goBack() {
        MainController.getInstance().showRaportit();
    }
}