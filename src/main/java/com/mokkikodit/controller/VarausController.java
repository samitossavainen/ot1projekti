package com.mokkikodit.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import com.mokkikodit.util.DialogUtil;
import com.mokkikodit.logiikka.VarausService;
import com.mokkikodit.mallit.Varaus;

public class VarausController {

    @FXML private TableView<Varaus> tableVaraukset;

    @FXML private Label varausIdLabel;
    @FXML private Label asiakasLabel;
    @FXML private DatePicker alkuDatePicker;
    @FXML private DatePicker loppuDatePicker;
    @FXML private ComboBox<String> tilaComboBox;

    @FXML private Button editButton;
    @FXML private Button saveButton;
    @FXML private Label statusLabel;
    @FXML private Button cancelButton;

    private boolean editMode = false;

    // ✅ injected service (NOT new)
    private VarausService service;

    public void setVarausService(VarausService service) {
        this.service = service;
    }

    // =========================
    // INITIALIZE
    // =========================
    @FXML
    public void initialize() {

        statusLabel.setVisible(false);
        statusLabel.setManaged(false);

        editMode = false;
        editButton.setText("Muokkaa");

        setFieldsVisible(false);
        setEditMode(false);

        tableVaraukset.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {

                    if (newSelection != null) {
                        populateFields(newSelection);
                    }

                    if (editMode && newSelection == null) {
                        cancelEdit();
                    }
                });

        alkuDatePicker.setOnShowing(e -> {
            if (!editMode) alkuDatePicker.hide();
        });

        loppuDatePicker.setOnShowing(e -> {
            if (!editMode) loppuDatePicker.hide();
        });
    }

    // =========================
    // DATA
    // =========================
    private void refreshTable() {
        tableVaraukset.getItems().setAll(service.getAllVaraukset());
    }

    private void populateFields(Varaus v) {
        varausIdLabel.setText(String.valueOf(v.getId()));
        asiakasLabel.setText(String.valueOf(v.getAsiakasId()));
        alkuDatePicker.setValue(v.getAlkuPvm());
        loppuDatePicker.setValue(v.getLoppuPvm());
        tilaComboBox.setValue(v.getTila());
    }

    // =========================
    // EDIT MODE
    // =========================
    @FXML
    private void toggleEdit() {
        if (!editMode) enterEditMode();
        else cancelEdit();
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

    // =========================
    // SAVE (FIXED)
    // =========================
    @FXML
    private void saveChanges() {

        Varaus selected = tableVaraukset.getSelectionModel().getSelectedItem();
        if (selected == null || service == null) return;

        if (alkuDatePicker.getValue() == null || loppuDatePicker.getValue() == null) {
            DialogUtil.showError("Valitse päivämäärät.");
            return;
        }

        selected.setAlkuPvm(alkuDatePicker.getValue());
        selected.setLoppuPvm(loppuDatePicker.getValue());
        selected.setTila(tilaComboBox.getValue());

        try {
            service.updateVaraus(selected);
        } catch (IllegalArgumentException e) {
            DialogUtil.showError(e.getMessage());
            return;
        }

        refreshTable();

        editMode = false;
        setEditMode(false);

        editButton.setText("Muokkaa");
        editButton.setStyle("-fx-base: #7A9E2E; -fx-text-fill: white;");

        showSavedStatus("Tallennettu");
        statusLabel.setStyle("-fx-text-fill: #1e7f43;");
    }

    // =========================
    // UI HELPERS
    // =========================
    private void setFieldsVisible(boolean visible) {

        alkuDatePicker.setVisible(visible);
        loppuDatePicker.setVisible(visible);
        tilaComboBox.setVisible(visible);

        alkuDatePicker.setManaged(visible);
        loppuDatePicker.setManaged(visible);
        tilaComboBox.setManaged(visible);
    }

    private void setEditMode(boolean editable) {

        setFieldsVisible(editable);

        alkuDatePicker.setMouseTransparent(!editable);
        loppuDatePicker.setMouseTransparent(!editable);

        tilaComboBox.setMouseTransparent(!editable);
        tilaComboBox.setFocusTraversable(editable);

        saveButton.setVisible(editable);
        saveButton.setManaged(editable);
    }

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

    // =========================
    // NEW RESERVATION WINDOW (FIXED INJECTION)
    // =========================
    @FXML
    private void openNewReservationWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/uusi_varaus.fxml")
            );

            Parent root = loader.load();

            UusiVarausController controller = loader.getController();
            controller.setVarausService(service);

            Stage stage = new Stage();
            stage.setTitle("Uusi varaus");

            stage.initOwner(tableVaraukset.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));

            stage.sizeToScene();
            stage.setResizable(false);

            stage.showAndWait();

            refreshTable();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // CANCEL RESERVATION
    // =========================
    @FXML
    private void cancelReservation() {

        Varaus selected = tableVaraukset.getSelectionModel().getSelectedItem();
        if (selected == null || service == null) return;

        Stage stage = (Stage) tableVaraukset.getScene().getWindow();

        boolean confirmed = DialogUtil.confirm(
                stage,
                "Vahvista peruutus",
                "Haluatko varmasti perua varauksen?",
                "Varaus #" + selected.getId() + " perutaan."
        );

        if (confirmed) {
            service.deleteVaraus(selected.getId());
            refreshTable();

            showSavedStatus("Varaus peruttu");
            statusLabel.setStyle("-fx-text-fill: #B04A30;");
        }
    }
}