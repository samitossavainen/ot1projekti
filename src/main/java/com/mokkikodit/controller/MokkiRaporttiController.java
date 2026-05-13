package com.mokkikodit.controller;

import com.mokkikodit.logiikka.MokkiService;
import com.mokkikodit.mallit.Mokki;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class MokkiRaporttiController {

    private MokkiService service;

    private final ObservableList<Mokki> mokit = FXCollections.observableArrayList();
    private FilteredList<Mokki> filteredMokit;

    // ===== Taulu =====
    @FXML private TableView<Mokki> tableRaportti;

    @FXML private TableColumn<Mokki, Integer> idCol;
    @FXML private TableColumn<Mokki, String> nameCol;
    @FXML private TableColumn<Mokki, String> addressCol;
    @FXML private TableColumn<Mokki, Integer> capacityCol;
    @FXML private TableColumn<Mokki, Double> priceCol;
    @FXML private TableColumn<Mokki, Integer> roomsCol;
    @FXML private TableColumn<Mokki, Integer> bathroomsCol;
    @FXML private TableColumn<Mokki, String> statusCol;

    // ===== UI =====
    @FXML private Label nameLabel;
    @FXML private Label addressLabel;
    @FXML private Label capacityLabel;
    @FXML private Label priceLabel;
    @FXML private Label roomsLabel;
    @FXML private Label bathroomsLabel;
    @FXML private Label statusLabel;
    @FXML private Label notesLabel;
    @FXML private Label summaryLabel;

    @FXML
    public void initialize() {

        filteredMokit = new FilteredList<>(mokit, m -> true);

        tableRaportti.setItems(filteredMokit);

        idCol.setCellValueFactory(d ->
                new SimpleIntegerProperty(d.getValue().getMokkiId()).asObject());

        nameCol.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getNimi()));

        addressCol.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getOsoite()));

        capacityCol.setCellValueFactory(d ->
                new SimpleIntegerProperty(d.getValue().getKapasiteetti()).asObject());

        priceCol.setCellValueFactory(d ->
                new SimpleDoubleProperty(d.getValue().getHinta()).asObject());

        roomsCol.setCellValueFactory(d ->
                new SimpleIntegerProperty(d.getValue().getHuoneet()).asObject());

        bathroomsCol.setCellValueFactory(d ->
                new SimpleIntegerProperty(d.getValue().getVessat()).asObject());

        statusCol.setCellValueFactory(d ->
                new SimpleStringProperty(
                        d.getValue().getTila() == 1 ? "Käytössä" : "Poissa käytöstä"
                ));

        tableRaportti.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        populateReport(newVal);
                    }
                });
    }

    // ===== SERVICE =====
    public void setMokkiService(MokkiService service) {
        this.service = service;
        refreshTable();
    }

    private void refreshTable() {
        if (service == null) return;
        mokit.setAll(service.haeKaikki());
    }

    // ===== VALINNAINEN ULKOINEN VALINTA =====
    public void showMokki(Mokki mokki) {
        if (mokki != null) {
            populateReport(mokki);
        }
    }

    // ===== KÄYTTÖLIITTYMÄN KÄYTTÄJÄMÄÄRÄ =====
    private void populateReport(Mokki m) {

        nameLabel.setText(m.getNimi());
        addressLabel.setText(m.getOsoite() != null ? m.getOsoite() : "");
        capacityLabel.setText(String.valueOf(m.getKapasiteetti()));
        roomsLabel.setText(String.valueOf(m.getHuoneet()));
        bathroomsLabel.setText(String.valueOf(m.getVessat()));
        priceLabel.setText(m.getHinta() + " €/yö");
        statusLabel.setText(m.getTila() == 1 ? "Käytössä" : "Poissa käytöstä");
        notesLabel.setText(m.getLisatiedot() != null ? m.getLisatiedot() : "");

        summaryLabel.setText(
                m.getNimi() + " · " +
                        m.getKapasiteetti() + " hlö · " +
                        m.getHinta() + " €/yö"
        );
    }

    // Paluu napista raportti näkymään
    @FXML
    private void goBack() {
        MainController.getInstance().showRaportit();
    }
}