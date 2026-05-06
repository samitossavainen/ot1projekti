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
import com.mokkikodit.tietokanta.AsiakasRepository;
import com.mokkikodit.mallit.Asiakas;

public class AsiakasController {

    @FXML private TableView<Asiakas> tableAsiakkaat;

    @FXML private TextField nimiField;
    @FXML private Label emailLabel;
    @FXML private TextField phoneField;
    @FXML private TextArea addressArea;

    @FXML private Button editButton;
    @FXML private Button saveButton;
    @FXML private Button deleteButton;

    @FXML private Label statusLabel;

    private boolean editMode = false;

    private final AsiakasRepository repo = new AsiakasRepository();

    @FXML
    public void initialize() {

        statusLabel.setVisible(false);
        statusLabel.setManaged(false);

        editMode = false;
        editButton.setText("Muokkaa");

        setFieldsVisible(false);
        setEditMode(false);

        refreshTable();

        tableAsiakkaat.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {

                    if (newSelection != null) {
                        populateFields(newSelection);
                    }

                    if (editMode && newSelection == null) {
                        cancelEdit();
                    }
                });
    }

    // FIXED: findAll() instead of haeKaikki()
    private void refreshTable() {
        tableAsiakkaat.getItems().setAll(repo.findAll());
    }

    private void populateFields(Asiakas a) {
        nimiField.setText(a.getNimi());
        emailLabel.setText(a.getEmail());
        phoneField.setText(a.getPuhelin() != null ? a.getPuhelin() : "");
    }

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

    @FXML
    private void saveChanges() {

        Asiakas selected = tableAsiakkaat.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        selected.setNimi(nimiField.getText());
        selected.setPuhelin(phoneField.getText());

        // repo.update(selected); // enable when implemented

        refreshTable();

        editMode = false;
        setEditMode(false);

        editButton.setText("Muokkaa");
        editButton.setStyle("-fx-base: #7A9E2E; -fx-text-fill: white;");

        showSavedStatus("Tallennettu");
        statusLabel.setStyle("-fx-text-fill: #1e7f43;");
    }

    @FXML
    private void deleteCustomer() {

        Asiakas selected = tableAsiakkaat.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Stage stage = (Stage) tableAsiakkaat.getScene().getWindow();

        boolean confirmed = DialogUtil.confirm(
                stage,
                "Vahvista poisto",
                "Haluatko varmasti poistaa asiakkaan?",
                "Asiakas " + selected.getEmail() + " poistetaan."
        );

        if (confirmed) {

            // repo.delete(selected.getId()); // enable when implemented

            tableAsiakkaat.getItems().remove(selected);

            showSavedStatus("Asiakas poistettu");
            statusLabel.setStyle("-fx-text-fill: #B04A30;");
        }
    }

    private void setFieldsVisible(boolean visible) {

        nimiField.setVisible(visible);
        phoneField.setVisible(visible);
        addressArea.setVisible(visible);

        nimiField.setManaged(visible);
        phoneField.setManaged(visible);
        addressArea.setManaged(visible);
    }

    private void setEditMode(boolean editable) {

        setFieldsVisible(editable);

        nimiField.setMouseTransparent(!editable);
        phoneField.setMouseTransparent(!editable);
        addressArea.setMouseTransparent(!editable);

        nimiField.setFocusTraversable(editable);
        phoneField.setFocusTraversable(editable);
        addressArea.setFocusTraversable(editable);

        emailLabel.setMouseTransparent(true);

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

            refreshTable();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}