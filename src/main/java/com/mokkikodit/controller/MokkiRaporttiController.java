package com.mokkikodit.controller;

import com.mokkikodit.logiikka.MokkiService;
import com.mokkikodit.logiikka.LaskuService;
import com.mokkikodit.logiikka.VarausService;
import com.mokkikodit.mallit.Mokki;
import com.mokkikodit.mallit.Varaus;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class MokkiRaporttiController {

    // SERVICES
    private MokkiService service;
    private LaskuService LaskuService;
    private VarausService VarausService;

    // DATA
    private final ObservableList<Mokki> mokit = FXCollections.observableArrayList();
    private FilteredList<Mokki> filteredMokit;

    private Map<Integer, Long> varausMap;
    private Map<Integer, Long> vuorokausiMap;

    private List<Varaus> allVaraukset = new ArrayList<>();

    // FILTERS
    @FXML private DatePicker alkuDatePicker;
    @FXML private DatePicker loppuDatePicker;

    @FXML private ComboBox<String> tilaComboBox;
    @FXML private ComboBox<String> mokkiComboBox;

    // TABLE
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

    // SUMMARY
    @FXML private Label mokkejaYhteensaLabel;
    @FXML private Label varauksiaYhteensaLabel;
    @FXML private Label vuorokausiaYhteensaLabel;
    @FXML private Label kokonaistulotLabel;

    // DETAIL
    @FXML private Label nimiLabel;
    @FXML private Label addressLabel;
    @FXML private Label pricePerNightLabel;
    @FXML private Label vessatLabel;
    @FXML private Label capacityLabel;
    @FXML private Label roomsLabel;
    @FXML private Label tilaLabel;
    @FXML private Label summaryLabel;
    @FXML private Label statusLabel;

    // INIT
    @FXML
    public void initialize() {

        filteredMokit = new FilteredList<>(mokit, m -> true);
        tableRaportti.setItems(filteredMokit);
        tableRaportti.setSelectionModel(null);
        tableRaportti.setFocusTraversable(false);

        setupTable();
        setupFilters();

        ChangeListener<Object> refresh = (obs, oldV, newV) -> {
            applyFilters();
            updateYhteenveto();
        };

        if (alkuDatePicker != null) {
            alkuDatePicker.valueProperty().addListener(refresh);
        }

        if (loppuDatePicker != null) {
            loppuDatePicker.valueProperty().addListener(refresh);
        }
    }

    // SERVICES
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

    // TABLE SETUP
    private void setupTable() {

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

        varauksetCol.setCellValueFactory(d -> {
            int id = d.getValue().getMokkiId();
            long count = varausMap != null ? varausMap.getOrDefault(id, 0L) : 0L;
            return new SimpleIntegerProperty((int) count).asObject();
        });

        vuorokaudetCol.setCellValueFactory(d -> {
            int id = d.getValue().getMokkiId();
            long days = vuorokausiMap != null ? vuorokausiMap.getOrDefault(id, 0L) : 0L;
            return new SimpleIntegerProperty((int) days).asObject();
        });
    }

    // FILTERS SETUP (FIXED)
    private void setupFilters() {

        tilaComboBox.getItems().setAll(
                "Kaikki",
                "Käytössä",
                "Poissa käytöstä"
        );
        tilaComboBox.setValue("Kaikki");

        mokkiComboBox.getItems().add("Kaikki");
        mokkiComboBox.setValue("Kaikki");

        tilaComboBox.setOnAction(e -> applyFilters());
        mokkiComboBox.setOnAction(e -> applyFilters());
    }

    // FILTER LOGIC (FIXED)
    private void applyFilters() {

        filteredMokit.setPredicate(m -> {

            // TILA
            String tila = tilaComboBox.getValue();
            if (tila != null && !tila.equals("Kaikki")) {

                String mokkiTila = (m.getTila() == 1)
                        ? "Käytössä"
                        : "Poissa käytöstä";

                if (!mokkiTila.equalsIgnoreCase(tila)) {
                    return false;
                }
            }

            // MÖKKI
            String mokki = mokkiComboBox.getValue();
            if (mokki != null && !mokki.equals("Kaikki")) {
                if (!m.getNimi().equalsIgnoreCase(mokki)) {
                    return false;
                }
            }

            return true;
        });

        tableRaportti.refresh();
        updateYhteenveto();
    }

    // DATA LOAD
    private void refreshTable() {

        if (service == null) return;

        mokit.setAll(service.haeKaikki());

        if (VarausService != null) {

            allVaraukset = VarausService.getAllVaraukset();

            varausMap = allVaraukset.stream()
                    .collect(Collectors.groupingBy(
                            Varaus::getMokkiId,
                            Collectors.counting()
                    ));

            vuorokausiMap = allVaraukset.stream()
                    .collect(Collectors.groupingBy(
                            Varaus::getMokkiId,
                            Collectors.summingLong(v -> {
                                if (v.getAlkuPvm() == null || v.getLoppuPvm() == null) return 0;
                                return ChronoUnit.DAYS.between(v.getAlkuPvm(), v.getLoppuPvm()) + 1;
                            })
                    ));
        }

        loadMokkiComboBox();
        updateYhteenveto();
    }

    private void loadMokkiComboBox() {

        mokkiComboBox.getItems().clear();
        mokkiComboBox.getItems().add("Kaikki");

        for (Mokki m : mokit) {
            mokkiComboBox.getItems().add(m.getNimi());
        }

        mokkiComboBox.setValue("Kaikki");
    }

    // Yhteenveto
    private void updateYhteenveto() {

        String selectedMokki = mokkiComboBox.getValue();

        // rajattu lista: joko kaikki tai yksi mökki
        List<Mokki> targetMokit;

        if (selectedMokki != null && !selectedMokki.equals("Kaikki")) {
            targetMokit = mokit.stream()
                    .filter(m -> m.getNimi().equalsIgnoreCase(selectedMokki))
                    .collect(Collectors.toList());
        } else {
            targetMokit = mokit;
        }

        int mokkeja = filteredMokit.size();

        long varauksia = varausMap != null
                ? targetMokit.stream()
                .mapToLong(m -> varausMap.getOrDefault(m.getMokkiId(), 0L))
                .sum()
                : 0;

        long vuorokausia = vuorokausiMap != null
                ? targetMokit.stream()
                .mapToLong(m -> vuorokausiMap.getOrDefault(m.getMokkiId(), 0L))
                .sum()
                : 0;

        double tulot = targetMokit.stream()
                .mapToDouble(m ->
                        vuorokausiMap != null
                                ? vuorokausiMap.getOrDefault(m.getMokkiId(), 0L) * m.getHinta()
                                : 0
                )
                .sum();

        mokkejaYhteensaLabel.setText(String.valueOf(mokkeja));
        varauksiaYhteensaLabel.setText(String.valueOf(varauksia));
        vuorokausiaYhteensaLabel.setText(String.valueOf(vuorokausia));
        kokonaistulotLabel.setText(String.format("%.2f €", tulot));
    }

    // Paluu napista raporttinäkymään
    @FXML
    private void goBack() {
        MainController.getInstance().showRaportit();
    }
}