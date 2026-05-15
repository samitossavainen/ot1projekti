package com.mokkikodit.controller;

import com.mokkikodit.logiikka.MokkiService;
import com.mokkikodit.logiikka.LaskuService;
import com.mokkikodit.logiikka.VarausService;
import com.mokkikodit.mallit.Mokki;
import com.mokkikodit.mallit.Varaus;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class MokkiRaporttiController {

    private MokkiService service;
    private LaskuService LaskuService;
    private VarausService VarausService;

    private final ObservableList<Mokki> mokit = FXCollections.observableArrayList();
    private FilteredList<Mokki> filteredMokit;

    // ===== VARAUSMAP =====
    private Map<Integer, Long> varausMap;

    // ===== VUOROKAUSIMAP =====
    private Map<Integer, Long> vuorokausiMap;

    // ===== KAIKKI VARAUKSET =====
    private List<Varaus> allVaraukset = new ArrayList<>();

    // ===== KÄYTTÖASTEIDEN VÄLIMUISTI =====
    private Map<Integer, Double> occupancyMap = new HashMap<>();

    // ===== TAULU =====
    @FXML private TableView<Mokki> tableRaportti;

    @FXML private TableColumn<Mokki, Integer> cabinCol;
    @FXML private TableColumn<Mokki, String> nameCol;
    @FXML private TableColumn<Mokki, String> addressCol;
    @FXML private TableColumn<Mokki, Integer> capacityCol;
    @FXML private TableColumn<Mokki, Double> priceCol;
    @FXML private TableColumn<Mokki, Integer> roomsCol;
    @FXML private TableColumn<Mokki, Integer> bathroomsCol;
    @FXML private TableColumn<Mokki, String> statusCol;

    @FXML private TableColumn<Mokki, Integer> varauksetCol;
    @FXML private TableColumn<Mokki, Integer> vuorokaudetCol;
    @FXML private TableColumn<Mokki, Double> kayttoasteCol;

    // PÄIVÄMÄÄRÄSUODATIN
    @FXML private DatePicker alkuDatePicker;
    @FXML private DatePicker loppuDatePicker;

    // ===== UI =====
    @FXML private Label nimiLabel;
    @FXML private Label addressLabel;
    @FXML private Label pricePerNightLabel;
    @FXML private Label vessatLabel;
    @FXML private Label capacityLabel;
    @FXML private Label roomsLabel;
    @FXML private Label tilaLabel;
    @FXML private Label lisatiedotLabel;
    @FXML private Label summaryLabel;
    @FXML private Label statusLabel;

    @FXML
    public void initialize() {

        filteredMokit = new FilteredList<>(mokit, m -> true);
        tableRaportti.setItems(filteredMokit);

        cabinCol.setCellValueFactory(d ->
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

        // VARAUKSET
        varauksetCol.setCellValueFactory(d -> {
            int id = d.getValue().getMokkiId();
            long count = varausMap != null ? varausMap.getOrDefault(id, 0L) : 0L;
            return new SimpleIntegerProperty((int) count).asObject();
        });

        // VUOROKAUDET
        vuorokaudetCol.setCellValueFactory(d -> {
            int id = d.getValue().getMokkiId();
            long days = vuorokausiMap != null ? vuorokausiMap.getOrDefault(id, 0L) : 0L;
            return new SimpleIntegerProperty((int) days).asObject();
        });

        // KÄYTTÖASTE
        kayttoasteCol.setCellValueFactory(d -> {
            int id = d.getValue().getMokkiId();
            double value = occupancyMap.getOrDefault(id, 0.0);
            return new SimpleDoubleProperty(value).asObject();
        });

        kayttoasteCol.setCellFactory(col -> new TableCell<Mokki, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.1f %%", item));
                }
            }
        });

        tableRaportti.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> {
                    if (newVal != null) populateReport(newVal);
                });

        ChangeListener<Object> refresh = (obs, oldV, newV) -> {
            recalcOccupancy();
            tableRaportti.refresh();
        };

        if (alkuDatePicker != null) {
            alkuDatePicker.valueProperty().addListener(refresh);
        }
        if (loppuDatePicker != null) {
            loppuDatePicker.valueProperty().addListener(refresh);
        }
    }

    // ===== SERVICE-PALVELUT =====
    public void setMokkiService(MokkiService service) {
        this.service = service;
        refreshTable();
    }

    public void setLaskuService(LaskuService laskuService) {
        this.LaskuService = laskuService;
        refreshTable();
    }

    public void setVarausService(VarausService varausService) {
        this.VarausService = varausService;
        refreshTable();
    }

    private void refreshTable() {

        if (service == null) return;

        mokit.setAll(service.haeKaikki());

        if (VarausService != null) {

            allVaraukset = VarausService.getAllVaraukset().stream()
                    .map(v -> (Varaus) v)
                    .collect(Collectors.toList());

            varausMap = allVaraukset.stream()
                    .collect(Collectors.groupingBy(
                            Varaus::getMokkiId,
                            Collectors.counting()
                    ));

            vuorokausiMap = allVaraukset.stream()
                    .collect(Collectors.groupingBy(
                            Varaus::getMokkiId,
                            Collectors.summingLong(v -> {

                                if (v.getAlkuPvm() == null || v.getLoppuPvm() == null) {
                                    return 0;
                                }

                                long days = ChronoUnit.DAYS.between(
                                        v.getAlkuPvm(),
                                        v.getLoppuPvm()
                                ) + 1;

                                return Math.max(days, 0);
                            })
                    ));
        }

        recalcOccupancy();
    }

    private void recalcOccupancy() {

        if (alkuDatePicker == null || loppuDatePicker == null ||
                alkuDatePicker.getValue() == null || loppuDatePicker.getValue() == null) {
            occupancyMap.clear();
            return;
        }

        LocalDate start = alkuDatePicker.getValue();
        LocalDate end = loppuDatePicker.getValue();

        long totalDays = ChronoUnit.DAYS.between(start, end) + 1;
        if (totalDays <= 0) {
            occupancyMap.clear();
            return;
        }

        occupancyMap = mokit.stream().collect(Collectors.toMap(
                Mokki::getMokkiId,
                m -> {

                    long bookedDays = allVaraukset.stream()
                            .filter(v -> v.getMokkiId() == m.getMokkiId())
                            .mapToLong(v -> {

                                if (v.getAlkuPvm() == null || v.getLoppuPvm() == null) {
                                    return 0;
                                }

                                LocalDate s = v.getAlkuPvm().isBefore(start) ? start : v.getAlkuPvm();
                                LocalDate e = v.getLoppuPvm().isAfter(end) ? end : v.getLoppuPvm();

                                long days = ChronoUnit.DAYS.between(s, e) + 1;
                                return Math.max(days, 0);
                            })
                            .sum();

                    return (bookedDays * 100.0) / totalDays;
                }
        ));
    }

    // ===== UI =====
    private void populateReport(Mokki m) {

        nimiLabel.setText(m.getNimi());
        addressLabel.setText(m.getOsoite() != null ? m.getOsoite() : "");
        capacityLabel.setText(String.valueOf(m.getKapasiteetti()));
        roomsLabel.setText(String.valueOf(m.getHuoneet()));
        vessatLabel.setText(String.valueOf(m.getVessat()));
        pricePerNightLabel.setText(m.getHinta() + " €/yö");
        statusLabel.setText(m.getTila() == 1 ? "Käytössä" : "Poissa käytöstä");

        summaryLabel.setText(
                m.getNimi() + " · " +
                        m.getKapasiteetti() + " hlö · " +
                        m.getHinta() + " €/yö"
        );
    }

    // Paluu napista raporttinäkymään
    @FXML
    private void goBack() {
        MainController.getInstance().showRaportit();
    }
}