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

public class VarausController {

    @FXML
    private TableView<?> tableVaraukset;

    @FXML
    private Label varausIdLabel;

    @FXML
    private Label asiakasLabel;

    @FXML
    private DatePicker alkuDatePicker;

    @FXML
    private DatePicker loppuDatePicker;

    @FXML
    private ComboBox tilaComboBox;

    @FXML
    private Button editButton;

    @FXML
    private Button saveButton;

    @FXML
    private Label statusLabel;

    @FXML
    private Button cancelButton;

    // Varauksen muokkaustila
    private boolean editMode = false;

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

    @FXML
    private void saveChanges() {

        // Poistutaan muokkaustilasta
        editMode = false;

        // Lukitaan kentät
        setEditMode(false);

        // Palautetaan Muokkaa‑napin teksti
        editButton.setText("Muokkaa");
        editButton.setStyle("-fx-base: #7A9E2E; -fx-text-fill: white;");

        // Näyttää "Tallennettu" kuittauksen
        showSavedStatus("Tallennettu");
        statusLabel.setStyle("-fx-text-fill: #1e7f43;");

        // TULEVAISUUS Tässä kohtaa kutsutaan backendia
        // esim. reservationService.save(...)
    }

    private void setEditMode(boolean editable) {

        // DatePickerit
        alkuDatePicker.setMouseTransparent(!editable);
        loppuDatePicker.setMouseTransparent(!editable);

        // ComboBox
        tilaComboBox.setMouseTransparent(!editable);

        // Estää tab-focus
        alkuDatePicker.setFocusTraversable(editable);
        loppuDatePicker.setFocusTraversable(editable);
        tilaComboBox.setFocusTraversable(editable);

        // Tallenna nappi
        saveButton.setVisible(editable);
        saveButton.setManaged(editable);
        saveButton.setStyle("-fx-base: #6B8E3A; -fx-text-fill: white;");

    }

    @FXML
    public void initialize() {

        statusLabel.setVisible(false);
        statusLabel.setManaged(false);

        editMode = false;
        editButton.setText("Muokkaa");

        setEditMode(false);

        tableVaraukset.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {

                    // jos muokkaus oli kesken → perutaan
                    if (editMode && newSelection == null) {
                        cancelEdit();
                    }
                });

        // DatePickerit: estää popupin kun ei edit-tilassa
        alkuDatePicker.setOnShowing(e -> {
            if (!editMode) alkuDatePicker.hide();
        });

        loppuDatePicker.setOnShowing(e -> {
            if (!editMode) loppuDatePicker.hide();
        });
    }
    @FXML
    private void openNewReservationWindow() {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/uusi_varaus.fxml")
            );
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Uusi varaus");

            stage.initOwner(tableVaraukset.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));

            stage.sizeToScene();
            stage.setResizable(false);

            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void cancelReservation() {

        Stage stage = (Stage) tableVaraukset.getScene().getWindow();

        boolean confirmed = DialogUtil.confirm(
                stage,
                "Vahvista peruutus",
                "Haluatko varmasti perua varauksen?",
                "Varaus #" + varausIdLabel.getText() + " perutaan."
        );

        if (confirmed) {

            showSavedStatus("Varaus peruttu");
            statusLabel.setStyle("-fx-text-fill: #B04A30;");
        }
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

}