package com.mokkikodit.controller;

import com.mokkikodit.logiikka.LaskuService;
import com.mokkikodit.logiikka.MokkiService;
import com.mokkikodit.mallit.Lasku;
import com.mokkikodit.mallit.Mokki;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.time.LocalDate;

public class LaskutController {

    @FXML private TableView<Lasku> tableLaskut;
    @FXML private Label laskuIdLabel;
    @FXML private Label tilaLabel;
    @FXML private Label summaryLabel;

    // -------------------------
    // DETAIL FIELDS
    // -------------------------

    @FXML private Label varausIdLabel;
    @FXML private Label asiakasLabel;
    @FXML private Label laskuLuotuLabel;
    @FXML private Label eraLabel;
    @FXML private Label summaLabel;

    @FXML private ComboBox<?> tilaComboBox;

    @FXML private Label maksupaivaLabel;
    @FXML private Label maksettuSummaLabel;

    @FXML private Button editButton;
    @FXML private Button saveButton;

    @FXML private TableColumn<Lasku, Integer> laskuCol;
    @FXML private TableColumn<Lasku, Integer> varausCol;
    @FXML private TableColumn<Lasku, String> asiakasCol;
    @FXML private TableColumn<Lasku, String> erapvmCol;
    @FXML private TableColumn<Lasku, Double> summaCol;
    @FXML private TableColumn<Lasku, String> tilaCol;

    @FXML private Label statusLabel;

    private boolean editMode = false;

    private LaskuService service;

    private Lasku selectedLasku;

    private final ObservableList<Lasku> laskut =
            FXCollections.observableArrayList();

    private FilteredList<Lasku> filteredLasku;

    public void setLaskuService(LaskuService service){
        this.service = service;
        refreshTable();
    }

    @FXML
    public void initialize() {

        laskuCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        data.getValue().getLaskuId()
                ).asObject());

        varausCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        data.getValue().getVarausId()
                ).asObject());

        asiakasCol.setCellValueFactory(data ->
                new javafx.beans.property.uSimpleStringProperty("TESTI"));

        //asiakasCol.setCellValueFactory(data ->
        //        new javafx.beans.property.SimpleStringProperty(
        //                data.getValue().getVaraus().getAsiakasEmail()
        //        ));

        erapvmCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getErapaiva().toString()
                ));

        summaCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleDoubleProperty(
                        data.getValue().getSumma()
                ).asObject());

        tilaCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getTila()
                ));

        statusLabel.setVisible(false);
        statusLabel.setManaged(false);

        editMode = false;
        editButton.setText("Muokkaa");

        filteredLasku = new FilteredList<>(laskut, a -> true);
        tableLaskut.setItems(filteredLasku);

        // IMPORTANT: hide fields initially
        setFieldsVisible(false);

        setEditMode(false);

        tableLaskut.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        tableLaskut.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {

                    selectedLasku = newSelection;

                    if (newSelection != null) {
                        populateFields(newSelection);
                    }
                });

        tableLaskut.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_PRESSED, event -> {
            if (editMode) {
                event.consume();
            }
        });
    }

    private void refreshTable() {
        if (service == null) return;
        laskut.setAll(service.getAllLaskut());
    }

    // -------------------------
    // EDIT MODE TOGGLE
    // -------------------------

    @FXML
    private void toggleEdit() {
        if (!editMode) {
            enterEditMode();
        } else {
            cancelEdit();
        }
    }

    private void enterEditMode() {
        editMode = true;
        setEditMode(true);

        editButton.setText("Peru muokkaus");
        editButton.setStyle("-fx-base: #8A8A8A; -fx-text-fill: white;");
    }

    private void cancelEdit() {
        editMode = false;
        setEditMode(false);

        editButton.setText("Muokkaa");
        editButton.setStyle("-fx-base: #7A9E2E; -fx-text-fill: white;");
    }

    // -------------------------
    // SAVE
    // -------------------------

    @FXML
    private void saveChanges() {

        editMode = false;
        setEditMode(false);

        editButton.setText("Muokkaa");
        editButton.setStyle("-fx-base: #7A9E2E; -fx-text-fill: white;");

        showSavedStatus("Tallennettu");
        statusLabel.setStyle("-fx-text-fill: #1e7f43;");
    }

    // -------------------------
    // FIELD VISIBILITY CONTROL
    // -------------------------

    private void setFieldsVisible(boolean visible) {

        tilaComboBox.setVisible(visible);
        tilaComboBox.setManaged(visible);
    }

    // -------------------------
    // EDIT MODE SETTINGS
    // -------------------------

    private void setEditMode(boolean editable) {

        // show/hide fields
        setFieldsVisible(editable);

        // lock/unlock interaction
        tilaComboBox.setMouseTransparent(!editable);
        tilaComboBox.setFocusTraversable(editable);

        saveButton.setVisible(editable);
        saveButton.setManaged(editable);
        saveButton.setStyle("-fx-base: #6B8E3A; -fx-text-fill: white;");
    }

    // -------------------------
    // STATUS MESSAGE
    // -------------------------

    private void showSavedStatus(String text) {
        statusLabel.setText(text);
        statusLabel.setVisible(true);
        statusLabel.setManaged(true);

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> {
            statusLabel.setVisible(false);
            statusLabel.setManaged(false);
        });
        pause.play();
    }

    private void populateFields(Lasku m) {

        if (m == null) return;

        // LABELIT (lukutila)
        laskuIdLabel.setText(String.valueOf(m.getLaskuId()));
        varausIdLabel.setText(String.valueOf(m.getVarausId()));
        asiakasLabel.setText(String.valueOf(m.getVaraus().getAsiakasEmail()));
        laskuLuotuLabel.setText(String.valueOf(m.getAikaleima()));
        eraLabel.setText(String.valueOf(m.getErapaiva()));
        summaLabel.setText(String.valueOf(m.getSumma()));
        maksupaivaLabel.setText(String.valueOf(m.getAikaleima()));
        maksettuSummaLabel.setText(String.valueOf(m.getSumma()));
    }
}