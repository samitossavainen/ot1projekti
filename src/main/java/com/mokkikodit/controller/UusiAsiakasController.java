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

    // UI-kentät uuden asiakkaan tietojen syöttämiseen
    @FXML private TextField nimiField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextArea addressArea;

    // Asiakaspalvelu (liiketoimintalogiikka)
    private final AsiakasService service =
            new AsiakasService(new AsiakasRepository());

    // Ilmaisee lisättiinkö asiakas onnistuneesti
    private boolean asiakasLisatty = false;

    // Peruuta-toiminto (sulkee ikkunan ilman tallennusta)
    @FXML
    private void cancel(ActionEvent event) {
        close(event);
    }

    // Luo uusi asiakas ja tallenna se järjestelmään
    @FXML
    private void create(ActionEvent event) {

        try {
            // Luodaan uusi Asiakas-olio syötteistä
            Asiakas a = new Asiakas();

            a.setNimi(nimiField.getText());
            a.setSapo(emailField.getText());
            a.setPuhelinnumero(phoneField.getText());
            a.setOsoite(addressArea.getText());

            // Tallennetaan asiakas palvelun kautta
            service.lisaa(a);

            asiakasLisatty = true;

            // Suljetaan ikkuna onnistuneen lisäyksen jälkeen
            close(event);

        }
        catch (IllegalArgumentException e) {
            // Näytetään virhe, jos validointi epäonnistuu
            showError("Asiakasta ei voitu lisätä", e.getMessage());
        }
    }

    // Sulkee nykyisen ikkunan
    private void close(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    // Palauttaa tiedon siitä lisättiinkö asiakas
    public boolean isAsiakasLisatty() {
        return asiakasLisatty;
    }

    // Näyttää virheilmoituksen popup-ikkunassa
    private void showError(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Virhe");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}