package com.mokkikodit.controller;

import com.mokkikodit.logiikka.AsiakasService;
import com.mokkikodit.mallit.Asiakas;
import com.mokkikodit.tietokanta.AsiakasRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UusiAsiakasController {

    @FXML private TextField nimiField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextArea addressArea;

    private final AsiakasService service =
            new AsiakasService(new AsiakasRepository());

    private boolean asiakasLisatty = false;

    @FXML
    private void cancel(ActionEvent event) {
        close(event);
    }

    @FXML
    private void create(ActionEvent event) {

        Asiakas a = new Asiakas();

        a.setNimi(nimiField.getText());
        a.setSapo(emailField.getText());
        a.setPuhelinnumero(phoneField.getText());
        a.setOsoite(addressArea.getText());

        try {

            service.lisaa(a);
            asiakasLisatty = true;
            close(event);

        } catch (IllegalArgumentException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Virhe");
            alert.setHeaderText("Asiakasta ei voitu lisätä");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void close(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public boolean isAsiakasLisatty() {
        return asiakasLisatty;
    }
}