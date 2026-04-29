package com.mokkikodit.controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import com.mokkikodit.util.DialogUtil;

public class MokkiController {

    @FXML
    private TableView<?> tableCabins;

    @FXML
    private TextField cabinField;

    @FXML
    private TextField capacityField;

    @FXML
    private TextField roomsField;

    @FXML
    private TextField vessatField;

    @FXML
    private TextField pricePerNightField;

    @FXML
    private ComboBox<?> tilaComboBox;

    @FXML
    private TextArea addressArea;

    @FXML
    private TextArea lisatiedotArea;

    @FXML
    private Button editButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Label statusLabel;

    private boolean editMode = false;

    // -------------------------
    // INIT
    // -------------------------

    @FXML
    public void initialize() {

        statusLabel.setVisible(false);
        statusLabel.setManaged(false);

        editMode = false;
        editButton.setText("Muokkaa");

        // 🔥 IMPORTANT: hide fields initially
        setFieldsVisible(false);

        setEditMode(false);

        tableCabins.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {
                    if (editMode && newSelection == null) {
                        cancelEdit();
                    }
                });
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

        cabinField.setVisible(visible);
        capacityField.setVisible(visible);
        roomsField.setVisible(visible);
        vessatField.setVisible(visible);
        pricePerNightField.setVisible(visible);
        addressArea.setVisible(visible);
        lisatiedotArea.setVisible(visible);
        tilaComboBox.setVisible(visible);

        cabinField.setManaged(visible);
        capacityField.setManaged(visible);
        roomsField.setManaged(visible);
        vessatField.setManaged(visible);
        pricePerNightField.setManaged(visible);
        addressArea.setManaged(visible);
        lisatiedotArea.setManaged(visible);
        tilaComboBox.setManaged(visible);
    }

    // -------------------------
    // EDIT MODE SETTINGS
    // -------------------------

    private void setEditMode(boolean editable) {

        // 🔥 show/hide fields
        setFieldsVisible(editable);

        // lock/unlock interaction
        cabinField.setMouseTransparent(!editable);
        capacityField.setMouseTransparent(!editable);
        roomsField.setMouseTransparent(!editable);
        vessatField.setMouseTransparent(!editable);
        pricePerNightField.setMouseTransparent(!editable);
        addressArea.setMouseTransparent(!editable);
        lisatiedotArea.setMouseTransparent(!editable);
        tilaComboBox.setMouseTransparent(!editable);

        cabinField.setFocusTraversable(editable);
        capacityField.setFocusTraversable(editable);
        roomsField.setFocusTraversable(editable);
        vessatField.setFocusTraversable(editable);
        pricePerNightField.setFocusTraversable(editable);
        addressArea.setFocusTraversable(editable);
        lisatiedotArea.setFocusTraversable(editable);
        tilaComboBox.setFocusTraversable(editable);

        saveButton.setVisible(editable);
        saveButton.setManaged(editable);
        saveButton.setStyle("-fx-base: #6B8E3A; -fx-text-fill: white;");
    }

    // -------------------------
    // NEW CABIN WINDOW
    // -------------------------

    @FXML
    private void openNewCabinWindow() {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/uusi_mokki.fxml")
            );
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Uusi mökki");

            stage.initOwner(tableCabins.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));

            stage.sizeToScene();
            stage.setResizable(false);
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -------------------------
    // DELETE
    // -------------------------

    @FXML
    private void deleteCabin() {

        Stage stage = (Stage) tableCabins.getScene().getWindow();

        boolean confirmed = DialogUtil.confirm(
                stage,
                "Vahvista poisto",
                "Haluatko varmasti poistaa mökin?",
                "Mökin " + cabinField.getText() + " tiedot poistetaan."
        );

        if (confirmed) {
            showSavedStatus("Mökki poistettu");
            statusLabel.setStyle("-fx-text-fill: #B04A30;");
        }
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
}