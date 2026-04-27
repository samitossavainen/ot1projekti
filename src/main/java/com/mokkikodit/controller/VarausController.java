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

public class VarausController {

    @FXML private TableView<Varaus> tableVaraukset;

    @FXML private TableColumn<Varaus, String> colId;
    @FXML private TableColumn<Varaus, String> colMokki;
    @FXML private TableColumn<Varaus, String> colAsiakas;
    @FXML private TableColumn<Varaus, String> colAlku;
    @FXML private TableColumn<Varaus, String> colLoppu;
    @FXML private TableColumn<Varaus, String> colTila;

    @FXML private Label varausIdLabel;
    @FXML private TextField asiakasField;
    @FXML private TextField mokkiField;
    @FXML private DatePicker alkuDatePicker;
    @FXML private DatePicker loppuDatePicker;
    @FXML private ComboBox<String> tilaComboBox;
    @FXML private Button editButton;
    @FXML private Button saveButton;

    private final VarausService varausService = new VarausService();
    private boolean editMode = false;

    @FXML
    public void initialize() {

        // -------------------------
        // TABLE BINDING
        // -------------------------

        colId.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(
                        String.valueOf(d.getValue().getId())
                )
        );

        colMokki.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(
                        d.getValue().getMokki().getNimi()
                )
        );

        colAsiakas.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(
                        d.getValue().getAsiakas().getNimi()
                )
        );

        colAlku.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(
                        d.getValue().getAlku().toString()
                )
        );

        colLoppu.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(
                        d.getValue().getLoppu().toString()
                )
        );

        colTila.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(
                        d.getValue().getTila()
                )
        );

        // -------------------------
        // COMBOBOX
        // -------------------------
        tilaComboBox.setItems(FXCollections.observableArrayList(
                "VARATTU", "PERUTTU", "MAKSETTU"
        ));

        // -------------------------
        // DATA LOAD
        // -------------------------
        refreshTable();

        // -------------------------
        // ROW SELECTION
        // -------------------------
        tableVaraukset.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldV, v) -> {

                    if (v == null) return;

                    if (editMode) cancelEdit();

                    varausIdLabel.setText(String.valueOf(v.getId()));
                    asiakasField.setText(v.getAsiakas().getNimi());
                    mokkiField.setText(v.getMokki().getNimi());
                    alkuDatePicker.setValue(v.getAlku());
                    loppuDatePicker.setValue(v.getLoppu());
                    tilaComboBox.setValue(v.getTila());
                });
    }

    // -------------------------
    // EDIT MODE
    // -------------------------
    @FXML
    private void toggleEdit() {
        if (!editMode) enterEditMode();
        else cancelEdit();
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

    private void setEditMode(boolean editable) {
        asiakasField.setEditable(editable);
        mokkiField.setEditable(editable);
        alkuDatePicker.setDisable(!editable);
        loppuDatePicker.setDisable(!editable);
        tilaComboBox.setDisable(!editable);
    }

    // -------------------------
    // SAVE EDIT
    // -------------------------
    @FXML
    private void saveChanges() {

        Varaus v = tableVaraukset.getSelectionModel().getSelectedItem();
        if (v == null) return;

        v.setAlku(alkuDatePicker.getValue());
        v.setLoppu(loppuDatePicker.getValue());
        v.setTila(tilaComboBox.getValue());

        v.getAsiakas().setNimi(asiakasField.getText());
        v.getMokki().setNimi(mokkiField.getText());

        varausService.updateVaraus(v);

        refreshTable();
        cancelEdit();
    }

    // -------------------------
    // NEW RESERVATION
    // -------------------------
    @FXML
    private void openNewReservationWindow() {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/uusi_varaus.fxml")
            );

            Parent root = loader.load();

            UusiVarausController controller = loader.getController();
            controller.setVarausService(varausService);

            Stage stage = new Stage();
            stage.setTitle("Uusi varaus");

            stage.initOwner(tableVaraukset.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));

            stage.showAndWait();

            refreshTable();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -------------------------
    // REFRESH HELPER
    // -------------------------
    private void refreshTable() {
        tableVaraukset.setItems(
                FXCollections.observableArrayList(
                        varausService.getAllVaraukset()
                )
        );
    }
}