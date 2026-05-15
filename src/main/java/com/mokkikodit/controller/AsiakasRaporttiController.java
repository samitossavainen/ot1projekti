package com.mokkikodit.controller;

import com.mokkikodit.logiikka.AsiakasService;
import com.mokkikodit.logiikka.VarausService;
import com.mokkikodit.mallit.Asiakas;
import com.mokkikodit.mallit.Varaus;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;

public class AsiakasRaporttiController {

    @FXML private TableView<Asiakas> tableRaportti;

    @FXML private ComboBox<Asiakas> asiakasComboBox;

    @FXML private Label totalBookingsLabel;
    @FXML private Label confirmedLabel;
    @FXML private Label canceledLabel;
    @FXML private Label firstBookingLabel;
    @FXML private Label latestBookingLabel;
    @FXML private DatePicker alkuDatePicker;
    @FXML private DatePicker loppuDatePicker;

    @FXML private TableColumn<Asiakas, String> emailCol;
    @FXML private TableColumn<Asiakas, String> nimiCol;
    @FXML private TableColumn<Asiakas, String> phoneCol;
    @FXML private TableColumn<Asiakas, String> addressCol;
    @FXML private TableColumn<Asiakas, String> varaustenMaaraCol;

    private AsiakasService service;
    private VarausService varausService;

    private final ObservableList<Asiakas> asiakkaat =
            FXCollections.observableArrayList();

    private FilteredList<Asiakas> filteredAsiakkaat;

    public void setAsiakasService(AsiakasService service) {
        this.service = service;
        refreshCustomers();
    }

    public void setVarausService(VarausService varausService) {
        this.varausService = varausService;
    }

    @FXML
    public void initialize() {

        setupTable();

        filteredAsiakkaat = new FilteredList<>(asiakkaat, a -> true);

        tableRaportti.setItems(filteredAsiakkaat);

        setupFilters();

        setupComboBox();
    }

    private void setupTable() {

        nimiCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNimi()));

        emailCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getSapo()));

        phoneCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getPuhelinnumero() != null
                                ? data.getValue().getPuhelinnumero()
                                : "-"
                ));

        addressCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getOsoite() != null
                                ? data.getValue().getOsoite()
                                : "-"
                ));

        varaustenMaaraCol.setCellValueFactory(data -> {

            if (varausService == null) return new SimpleStringProperty("0");

            int count = varausService
                    .haeAsiakkaanVaraukset(data.getValue().getSapo())
                    .size();

            return new SimpleStringProperty(String.valueOf(count));
        });
    }

    private void setupFilters() {

        alkuDatePicker.setOnAction(e -> applyFilters());
        loppuDatePicker.setOnAction(e -> applyFilters());
        asiakasComboBox.setOnAction(e -> applyFilters());
    }

    private void applyFilters() {

        filteredAsiakkaat.setPredicate(a -> {

            if (varausService == null) return true;

            List<Varaus> varaukset =
                    varausService.haeAsiakkaanVaraukset(a.getSapo());

            LocalDate alku = alkuDatePicker.getValue();
            LocalDate loppu = loppuDatePicker.getValue();

            boolean löytyy = false;

            for (Varaus v : varaukset) {

                if (alku != null && v.getLoppuPvm().isBefore(alku))
                    continue;

                if (loppu != null && v.getAlkuPvm().isAfter(loppu))
                    continue;

                löytyy = true;
                break;
            }

            if ((alku != null || loppu != null) && !löytyy) {
                return false;
            }

            Asiakas selected = asiakasComboBox.getValue();

            if (selected != null && !selected.equals(a)) {
                return false;
            }

            return true;
        });

        updateSummary();
    }



    private void updateSummary() {

        if (varausService == null) return;

        int total = 0;
        int confirmed = 0;
        int canceled = 0;

        LocalDate first = null;
        LocalDate last = null;

        for (Asiakas a : filteredAsiakkaat) {

            List<Varaus> varaukset =
                    varausService.haeAsiakkaanVaraukset(a.getSapo());

            for (Varaus v : varaukset) {

                total++;

                if ("aktiivinen".equalsIgnoreCase(v.getTila())
                        || "maksettu".equalsIgnoreCase(v.getTila())) {
                    confirmed++;
                }

                if ("peruutettu".equalsIgnoreCase(v.getTila())) {
                    canceled++;
                }

                LocalDate alku = v.getAlkuPvm();

                if (first == null || alku.isBefore(first)) {
                    first = alku;
                }

                if (last == null || alku.isAfter(last)) {
                    last = alku;
                }
            }
        }

        totalBookingsLabel.setText(String.valueOf(total));
        confirmedLabel.setText(String.valueOf(confirmed));
        canceledLabel.setText(String.valueOf(canceled));

        firstBookingLabel.setText(first != null ? first.toString() : "-");
        latestBookingLabel.setText(last != null ? last.toString() : "-");
    }


    private void setupComboBox() {

        asiakasComboBox.getSelectionModel().clearSelection();

        asiakasComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Asiakas item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNimi());
            }
        });

        asiakasComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Asiakas item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNimi());
            }
        });
    }

    private void refreshCustomers() {

        if (service == null) return;

        asiakkaat.setAll(service.haeKaikki());

        asiakasComboBox.setItems(asiakkaat);

        updateSummary();
    }

    @FXML
    private void goBack() {
        MainController.getInstance().showRaportit();
    }
}
