package com.mokkikodit.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VarausController {

    @FXML
    private TableView<?> tableVaraukset;

    @FXML
    private Label varausIdLabel;

    @FXML
    private TextField asiakasField;

    @FXML
    private TextField mokkiField;

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
    private TextField emailField;

    @FXML
    private TextField phoneField;

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
        editButton.setText("Peruuta");
    }

    private void cancelEdit() {
        editMode = false;

        setEditMode(false);
        editButton.setText("Muokkaa");
    }

    @FXML
    private void saveChanges() {

        // Poistutaan muokkaustilasta
        editMode = false;

        // Lukitaan kentät
        setEditMode(false);

        // Palautetaan Muokkaa‑napin teksti
        editButton.setText("Muokkaa");

        // TULEVAISUUS Tässä kohtaa kutsutaan backendia
        // esim. reservationService.save(...)
    }

    private void setEditMode(boolean editable) {

        // TextFieldit
        asiakasField.setEditable(editable);
        mokkiField.setEditable(editable);
        emailField.setEditable(editable);
        phoneField.setEditable(editable);

        // DatePickerit
        alkuDatePicker.setDisable(!editable);
        loppuDatePicker.setDisable(!editable);

        // ComboBox
        tilaComboBox.setDisable(!editable);

    }

    @FXML
    public void initialize() {
        editMode = false;
        editButton.setText("Muokkaa");
        setEditMode(false);

        tableVaraukset.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {

                    if (newSelection == null) {
                        return;
                    }

                    // jos muokkaus oli kesken, se peruutetaan
                    if (editMode) {
                        cancelEdit();
                    }
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
}