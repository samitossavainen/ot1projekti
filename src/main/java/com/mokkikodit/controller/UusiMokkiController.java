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

    @FXML private TextField nimiField;
    @FXML private TextField capacityField;
    @FXML private TextField roomsField;
    @FXML private TextField vessatField;
    @FXML private TextField pricePerNightField;
    @FXML private TextArea addressArea;
    @FXML private TextArea lisatiedotArea;

    private final MokkiService service =
            new MokkiService(new MokkiRepository());

    private boolean mokkiLisatty = false;

    @FXML
    private void cancel(ActionEvent event) {
        close(event);
    }

    @FXML
    private void create(ActionEvent event) {

        Mokki m = new Mokki();

        try {
            m.setNimi(nimiField.getText());
            m.setKapasiteetti(Integer.parseInt(capacityField.getText()));
            m.setHuoneet(Integer.parseInt(roomsField.getText()));
            m.setVessat(Integer.parseInt(vessatField.getText()));
            m.setHinta(Double.parseDouble(pricePerNightField.getText()));
            m.setOsoite(addressArea.getText());
            m.setLisatiedot(lisatiedotArea.getText());

            service.lisaa(m);
            mokkiLisatty = true;
            close(event);

        } catch (NumberFormatException e) {

            showError(
                    "Virheellinen syöte",
                    "Tarkista kapasiteetin, huoneiden, vessojen ja hinnan arvot.\n" +
                            "Kenttiin tulee syöttää vain numeroita."
            );

        } catch (IllegalArgumentException e) {

            showError(
                    "Mökkiä ei voitu lisätä",
                    e.getMessage()
            );
        }
    }

    private void close(ActionEvent event) {
        Stage stage =
                (Stage) ((Node) event.getSource())
                        .getScene()
                        .getWindow();
        stage.close();
    }

    public boolean isMokkiLisatty() {
        return mokkiLisatty;
    }

    private void showError(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Virhe");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}