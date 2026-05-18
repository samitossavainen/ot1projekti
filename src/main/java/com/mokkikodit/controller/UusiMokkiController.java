package com.mokkikodit.controller;

import com.mokkikodit.logiikka.MokkiService;
import com.mokkikodit.mallit.Mokki;
import com.mokkikodit.tietokanta.MokkiRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UusiMokkiController {

    // UI-kentät uuden mökin tietojen syöttämiseen
    @FXML private TextField nimiField;
    @FXML private TextField capacityField;
    @FXML private TextField roomsField;
    @FXML private TextField vessatField;
    @FXML private TextField pricePerNightField;
    @FXML private TextArea addressArea;
    @FXML private TextArea lisatiedotArea;

    // Mökkipalvelu (liiketoimintalogiikka)
    private final MokkiService service =
            new MokkiService(new MokkiRepository());

    // Ilmaisee lisättiinkö mökki onnistuneesti
    private boolean mokkiLisatty = false;

    // Peruuta-toiminto (sulkee ikkunan ilman tallennusta)
    @FXML
    private void cancel(ActionEvent event) {
        close(event);
    }

    // Luo uusi mökki ja tallenna se järjestelmään
    @FXML
    private void create(ActionEvent event) {

        // Luodaan uusi Mokki-olio
        Mokki m = new Mokki();

        try {
            // Asetetaan käyttäjän syöttämät arvot olioon

            m.setNimi(nimiField.getText());
            m.setKapasiteetti(Integer.parseInt(capacityField.getText()));
            m.setHuoneet(Integer.parseInt(roomsField.getText()));
            m.setVessat(Integer.parseInt(vessatField.getText()));
            m.setHinta(Double.parseDouble(pricePerNightField.getText()));
            m.setOsoite(addressArea.getText());
            m.setLisatiedot(lisatiedotArea.getText());

            // Tallennetaan mökki palvelun kautta
            service.lisaa(m);

            mokkiLisatty = true;

            // Suljetaan ikkuna onnistuneen lisäyksen jälkeen
            close(event);

        } catch (NumberFormatException e) {

            // Virhe: käyttäjä syötti väärän tyyppistä dataa (ei numeroa)
            showError(
                    "Virheellinen syöte",
                    "Tarkista kapasiteetin, huoneiden, vessojen ja hinnan arvot.\n" +
                            "Kenttiin tulee syöttää vain numeroita."
            );

        } catch (IllegalArgumentException e) {

            // Liiketoimintalogiikan validointivirhe
            showError(
                    "Mökkiä ei voitu lisätä",
                    e.getMessage()
            );
        }
    }

    // Sulkee nykyisen ikkunan
    private void close(ActionEvent event) {
        Stage stage =
                (Stage) ((Node) event.getSource())
                        .getScene()
                        .getWindow();
        stage.close();
    }

    // Palauttaa tiedon siitä lisättiinkö mökki onnistuneesti
    public boolean isMokkiLisatty() {
        return mokkiLisatty;
    }

    // Näyttää virheikkunan käyttäjälle
    private void showError(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Virhe");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}