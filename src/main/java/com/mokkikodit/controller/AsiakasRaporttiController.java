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

    @FXML private Label customerCountLabel;
    @FXML private Label totalBookingsLabel;
    @FXML private Label confirmedLabel;
    @FXML private Label canceledLabel;

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
        applyFilters();
    }

    @FXML
    public void initialize() {

        setupTable();

        filteredAsiakkaat = new FilteredList<>(asiakkaat, a -> true);

        tableRaportti.setItems(filteredAsiakkaat);
        tableRaportti.setSelectionModel(null);
        tableRaportti.setFocusTraversable(false);

        setupFilters();
        setupComboBox();

        updateSummary();
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

        asiakasComboBox.setOnAction(e -> applyFilters());
    }

    private void applyFilters() {

        filteredAsiakkaat.setPredicate(a -> {

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

        customerCountLabel.setText(String.valueOf(filteredAsiakkaat.size()));

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
    }

    private void setupComboBox() {

        asiakasComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Asiakas item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setText(null);
                } else if (item == null) {
                    setText("Kaikki");
                } else {
                    setText(item.getNimi() + " (" + item.getSapo() + ")");
                }
            }
        });

        asiakasComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Asiakas item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText("Kaikki");
                } else {
                    setText(item.getNimi() + " (" + item.getSapo() + ")");
                }
            }
        });
    }

    private void refreshCustomers() {

        if (service == null) return;

        asiakkaat.setAll(service.haeKaikki());

        ObservableList<Asiakas> lista = FXCollections.observableArrayList();

        lista.add(null);
        lista.addAll(asiakkaat);

        asiakasComboBox.setItems(lista);

        asiakasComboBox.setValue(null); // default

        applyFilters();
    }

    // Näkymän päivitys
    @FXML
    private void refreshView() {

        asiakasComboBox.setValue(null);
        applyFilters();
    }

    @FXML
    private void goBack() {
        MainController.getInstance().showRaportit();
    }
}
