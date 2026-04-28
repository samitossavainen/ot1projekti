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
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

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

        // Muokattavat kentät (ei harmaaksi!)
        firstNameField.setMouseTransparent(!editable);
        lastNameField.setMouseTransparent(!editable);
        phoneField.setMouseTransparent(!editable);
        addressArea.setMouseTransparent(!editable);

        // Sähköposti EI ole koskaan muokattava
        emailField.setMouseTransparent(true);

        // Estää tab-fokuksen view-tilassa
        firstNameField.setFocusTraversable(editable);
        lastNameField.setFocusTraversable(editable);
        phoneField.setFocusTraversable(editable);
        addressArea.setFocusTraversable(editable);
        emailField.setFocusTraversable(false);

        // Tallenna-nappi
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

        tableAsiakkaat.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {

                    if (editMode && newSelection == null) {
                        cancelEdit();
                    }
                });
    }

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

    @FXML
    private void deleteCustomer() {

        Stage stage = (Stage) tableAsiakkaat.getScene().getWindow();

        boolean confirmed = DialogUtil.confirm(
                stage,
                "Vahvista poisto",
                "Haluatko varmasti poistaa asiakkaan?",
                "Asiakkaan " + emailField.getText() + " tiedot poistetaan."
        );

        if (confirmed) {

            showSavedStatus("Asiakas poistettu");
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
