package com.mokkikodit.controller;

import com.mokkikodit.logiikka.LaskuService;
import com.mokkikodit.mallit.Lasku;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
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

public class LaskutRaporttiController {

    // -------------------------------------------------
    // SUODATTIMET
    // -------------------------------------------------

    @FXML private DatePicker alkuDatePicker;
    @FXML private DatePicker loppuDatePicker;

    @FXML private ComboBox<String> tilaComboBox;
    @FXML private ComboBox<String> asiakasComboBox;

    // -------------------------------------------------
    // TAULU
    // -------------------------------------------------

    @FXML private TableView<Lasku> tableRaportti;

    @FXML private TableColumn<Lasku, Integer> laskuIdCol;
    @FXML private TableColumn<Lasku, String> asiakasCol;
    @FXML private TableColumn<Lasku, Integer> varausIdCol;
    @FXML private TableColumn<Lasku, String> laskuPvmCol;
    @FXML private TableColumn<Lasku, String> eraPvmCol;
    @FXML private TableColumn<Lasku, Double> summaCol;
    @FXML private TableColumn<Lasku, String> tilaCol;

    // -------------------------------------------------
    // YHTEENVETOETIKETIT (LABEL)
    // -------------------------------------------------

    @FXML private Label yhteensaLabel;
    @FXML private Label maksetutLabel;
    @FXML private Label avoimetLabel;
    @FXML private Label myohassaLabel;
    @FXML private Label avoinSummaLabel;

    // -------------------------------------------------
    // DATA
    // -------------------------------------------------

    private final ObservableList<Lasku> laskut =
            FXCollections.observableArrayList();

    private FilteredList<Lasku> filteredLaskut;

    private LaskuService laskuService;

    // -------------------------------------------------
    // ALOITA (INITIALIZE)
    // -------------------------------------------------

    @FXML
    public void initialize() {

        setupTable();

        filteredLaskut = new FilteredList<>(laskut, p -> true);

        tableRaportti.setItems(filteredLaskut);

        setupFilters();
    }

    // -------------------------------------------------
    // SERVICE (PALVELU)
    // -------------------------------------------------

    public void setLaskuService(LaskuService service) {
        this.laskuService = service;

        refreshTable();
        loadCustomers();
    }

    // -------------------------------------------------
    // TAULUN ASETUKSET
    // -------------------------------------------------

    private void setupTable() {

        laskuIdCol.setCellValueFactory(data ->
                new SimpleIntegerProperty(
                        data.getValue().getLaskuId()
                ).asObject());

        asiakasCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getSapo()
                ));

        varausIdCol.setCellValueFactory(data ->
                new SimpleIntegerProperty(
                        data.getValue().getVarausId()
                ).asObject());

        laskuPvmCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getAikaleima().toString()
                ));

        eraPvmCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getErapaiva().toString()
                ));

        summaCol.setCellValueFactory(data ->
                new SimpleDoubleProperty(
                        data.getValue().getSumma()
                ).asObject());

        tilaCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getTila()
                ));
    }

    // -------------------------------------------------
    // SUODATTIMET
    // -------------------------------------------------

    private void setupFilters() {

        tilaComboBox.getItems().addAll(
                "Kaikki",
                "Maksettu",
                "Lähetetty",
                "Myöhässä"
        );

        tilaComboBox.setValue("Kaikki");

        tilaComboBox.setOnAction(e -> applyFilters());

        asiakasComboBox.setOnAction(e -> applyFilters());

        alkuDatePicker.setOnAction(e -> applyFilters());

        loppuDatePicker.setOnAction(e -> applyFilters());
    }

    private void applyFilters() {

        filteredLaskut.setPredicate(lasku -> {

            // -------------------------
            // PÄIVÄMÄÄRÄSUODATIN
            // -------------------------

            LocalDate alku = alkuDatePicker.getValue();
            LocalDate loppu = loppuDatePicker.getValue();

            LocalDate laskuDate = lasku.getAikaleima().toLocalDate();

            if (alku != null && laskuDate.isBefore(alku)) {
                return false;
            }

            if (loppu != null && laskuDate.isAfter(loppu)) {
                return false;
            }

            // -------------------------
            // TILASUODATIN
            // -------------------------

            String tila = tilaComboBox.getValue();

            if (tila != null &&
                    !tila.equals("Kaikki") &&
                    !lasku.getTila().equalsIgnoreCase(tila)) {

                return false;
            }

            // -------------------------
            // ASIAKASSUODATIN
            // -------------------------

            String asiakas = asiakasComboBox.getValue();

            if (asiakas != null &&
                    !asiakas.equals("Kaikki") &&
                    !lasku.getSapo().equalsIgnoreCase(asiakas)) {

                return false;
            }

            return true;
        });

        updateSummary();
    }

    // -------------------------------------------------
    // YHTEENVETO
    // -------------------------------------------------

    private void updateSummary() {

        int yhteensa = filteredLaskut.size();

        int maksetut = 0;
        int avoimet = 0;
        int myohassa = 0;

        double avoinSumma = 0;

        for (Lasku lasku : filteredLaskut) {

            String tila = lasku.getTila();

            if (tila.equalsIgnoreCase("Maksettu")) {
                maksetut++;
            }

            if (tila.equalsIgnoreCase("Lähetetty")) {
                avoimet++;
                avoinSumma += lasku.getSumma();
            }

            if (tila.equalsIgnoreCase("Myöhässä")) {
                myohassa++;
                avoinSumma += lasku.getSumma();
            }
        }

        yhteensaLabel.setText(String.valueOf(yhteensa));
        maksetutLabel.setText(String.valueOf(maksetut));
        avoimetLabel.setText(String.valueOf(avoimet));
        myohassaLabel.setText(String.valueOf(myohassa));

        avoinSummaLabel.setText(String.format("%.2f €", avoinSumma));
    }

    // -------------------------------------------------
    // DATA
    // -------------------------------------------------

    private void refreshTable() {

        if (laskuService == null) return;

        laskut.setAll(laskuService.getAllLaskut());

        updateSummary();
    }

    private void loadCustomers() {

        asiakasComboBox.getItems().clear();

        asiakasComboBox.getItems().add("Kaikki");

        for (Lasku lasku : laskut) {

            String asiakas = lasku.getSapo();

            if (!asiakasComboBox.getItems().contains(asiakas)) {
                asiakasComboBox.getItems().add(asiakas);
            }
        }

        asiakasComboBox.setValue("Kaikki");
    }

    // -------------------------------------------------
    // Paluu napista raporttinäkymään
    // -------------------------------------------------

    @FXML
    private void goBack() {
        MainController.getInstance().showRaportit();
    }
}