package com.mokkikodit.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import com.mokkikodit.logiikka.VarausService;
import com.mokkikodit.mallit.Varaus;

import java.time.LocalDate;

public class VarausController {

    @FXML
    private TableView<Varaus> tableVaraukset;

    private final VarausService varausService = new VarausService();

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
    private ComboBox<String> tilaComboBox;

    @FXML
    private Button editButton;

    @FXML
    private Button saveButton;

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

        Varaus selected = tableVaraukset.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        // Päivitetään varaus (oletetaan setterit mallissa)
        selected.setAlku(alkuDatePicker.getValue());
        selected.setLoppu(loppuDatePicker.getValue());
        selected.setTila(tilaComboBox.getValue());

        // Backend päivitys
        varausService.updateVaraus(selected);

        tableVaraukset.refresh();

        cancelEdit();
    }

    private void setEditMode(boolean editable) {
        asiakasField.setEditable(false); // yleensä ei muokata suoraan
        mokkiField.setEditable(false);

        alkuDatePicker.setDisable(!editable);
        loppuDatePicker.setDisable(!editable);
        tilaComboBox.setDisable(!editable);
    }

    @FXML
    public void initialize() {

        editMode = false;
        editButton.setText("Muokkaa");
        setEditMode(false);

        // ComboBox arvot
        tilaComboBox.setItems(FXCollections.observableArrayList(
                "VARATTU", "PERUTTU", "MAKSETTU"
        ));

        tableVaraukset.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {

                    if (newSelection == null) return;

                    if (editMode) cancelEdit();

                    Varaus v = newSelection;

                    varausIdLabel.setText(String.valueOf(v.getId()));
                    asiakasField.setText(v.getAsiakas().getNimi());
                    mokkiField.setText(v.getMokki().getNimi());
                    alkuDatePicker.setValue(v.getAlku());
                    loppuDatePicker.setValue(v.getLoppu());
                    tilaComboBox.setValue(v.getTila());
                });

        tableVaraukset.setItems(
                FXCollections.observableArrayList(
                        varausService.getAllVaraukset()
                )
        );
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

            // Päivitetään taulukko
            tableVaraukset.setItems(
                    FXCollections.observableArrayList(
                            varausService.getAllVaraukset()
                    )
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}