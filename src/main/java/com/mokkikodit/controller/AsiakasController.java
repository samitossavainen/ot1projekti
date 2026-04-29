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

public class AsiakasController {

    @FXML
    private TableView<?> tableAsiakkaat;

    @FXML
    private TextField nimiField;

    @FXML
    private Label emailLabel;

    @FXML
    private TextField phoneField;

    @FXML
    private TextArea addressArea;

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

        // 🔥 Hide customer detail fields initially
        setFieldsVisible(false);

        setEditMode(false);

        tableAsiakkaat.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {
                    if (editMode && newSelection == null) {
                        cancelEdit();
                    }
                });
    }

    // -------------------------
    // TOGGLE EDIT
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

        nimiField.setVisible(visible);
        phoneField.setVisible(visible);
        addressArea.setVisible(visible);

        nimiField.setManaged(visible);
        phoneField.setManaged(visible);
        addressArea.setManaged(visible);

        // Email is ALWAYS visible (do nothing as requested)
    }

    // -------------------------
    // EDIT MODE SETTINGS
    // -------------------------
    private void setEditMode(boolean editable) {

        // 🔥 show / hide fields
        setFieldsVisible(editable);

        // Editable fields behavior
        nimiField.setMouseTransparent(!editable);
        phoneField.setMouseTransparent(!editable);
        addressArea.setMouseTransparent(!editable);

        nimiField.setFocusTraversable(editable);
        phoneField.setFocusTraversable(editable);
        addressArea.setFocusTraversable(editable);

        // Email is always read-only
        emailLabel.setMouseTransparent(true);
        emailLabel.setFocusTraversable(false);

        // Save button
        saveButton.setVisible(editable);
        saveButton.setManaged(editable);
        saveButton.setStyle("-fx-base: #6B8E3A; -fx-text-fill: white;");
    }

    // -------------------------
    // NEW CUSTOMER
    // -------------------------
    @FXML
    private void openNewCustomerWindow() {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/uusi_asiakas.fxml")
            );
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Uusi asiakas");

            stage.initOwner(tableAsiakkaat.getScene().getWindow());
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
    // DELETE CUSTOMER
    // -------------------------
    @FXML
    private void deleteCustomer() {

        Stage stage = (Stage) tableAsiakkaat.getScene().getWindow();

        boolean confirmed = DialogUtil.confirm(
                stage,
                "Vahvista poisto",
                "Haluatko varmasti poistaa asiakkaan?",
                "Asiakkaan " + emailLabel.getText() + " tiedot poistetaan."
        );

        if (confirmed) {
            showSavedStatus("Asiakas poistettu");
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
