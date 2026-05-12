package com.mokkikodit.controller;

import com.mokkikodit.logiikka.AsiakasService;
import com.mokkikodit.mallit.Asiakas;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

public class AsiakasRaporttiController {

    @FXML private ComboBox<Asiakas> asiakasComboBox;

    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneLabel;
    @FXML private Label addressLabel;

    private AsiakasService service;

    private final ObservableList<Asiakas> asiakkaat =
            FXCollections.observableArrayList();

    public void setAsiakasService(AsiakasService service) {
        this.service = service;
        refreshCustomers();
    }

    @FXML
    public void initialize() {

        asiakasComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Asiakas item, boolean empty) {
                super.updateItem(item, empty);

                setText(empty || item == null
                        ? null
                        : item.getNimi());
            }
        });

        asiakasComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Asiakas item, boolean empty) {
                super.updateItem(item, empty);

                setText(empty || item == null
                        ? null
                        : item.getNimi());
            }
        });

        asiakasComboBox.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {

                    if (newSelection != null) {
                        populateFields(newSelection);
                    }
                });
    }

    private void refreshCustomers() {

        if (service == null) return;

        asiakkaat.setAll(service.haeKaikki());

        asiakasComboBox.setItems(asiakkaat);
    }

    private void populateFields(Asiakas a) {

        nameLabel.setText(
                a.getNimi() != null ? a.getNimi() : ""
        );

        emailLabel.setText(
                a.getSapo() != null ? a.getSapo() : ""
        );

        phoneLabel.setText(
                a.getPuhelinnumero() != null
                        ? a.getPuhelinnumero()
                        : ""
        );

        addressLabel.setText(
                a.getOsoite() != null
                        ? a.getOsoite()
                        : ""
        );
    }

    @FXML
    private void goBack() {
        MainController.getInstance().showRaportit();
    }
}