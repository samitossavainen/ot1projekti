package com.mokkikodit.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Apuluokka uudelleenkäytettävien valintaikkunoiden näyttämiseen.
 * Keskittää yleiset hälytystyypit, jotta vältetään koodin päällekkäisyydet
 * sovelluksen eri osissa.
 */
public class DialogUtil {

    /**
     * Näyttää tietoruudun, jossa on viesti.
     *
     * @param msg käyttäjälle näytettävä tekstinä
     */
    public static void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Info");
        alert.setHeaderText(null); // Tässä sovellustyylissä ei käytetä otsikkoa
        alert.setContentText(msg);

        alert.showAndWait();
    }

    /**
     * Näyttää virheilmoituksen sisältävän valintaikkunan.
     *
     * @param msg näytettävä virheilmoitus
     */
    public static void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Virhe");
        alert.setHeaderText(null); // Pidetään käyttöliittymä yksinkertaisena: ei käytetä otsikkokenttää
        alert.setContentText(msg);

        alert.showAndWait();
    }

    /**
     * Näyttää vahvistusikkunan, jossa on vaihtoehdot Kyllä/Ei.
     *
     * @param owner   pääikkuna (varmistaa, että modaalinen käyttäytyminen on sidottu pääkäyttöliittymään)
     * @param title   valintaikkunan otsikko
     * @param header  valinnainen otsikkoteksti, joka näkyy sisällön yläpuolella
     * @param content käyttäjälle näytettävä pääviesti
     * @return true, jos käyttäjä valitsee ”Kyllä”, muuten false
     */
    public static boolean confirm(Stage owner, String title, String header, String content) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.initOwner(owner);
        alert.initModality(Modality.WINDOW_MODAL);

        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        ButtonType yes = new ButtonType("Kyllä", ButtonBar.ButtonData.YES);
        ButtonType no = new ButtonType("Ei", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(no, yes);

        alert.showingProperty().addListener((obs, wasShowing, isShowing) -> {
            if (isShowing) {
                Button yesBtn = (Button) alert.getDialogPane().lookupButton(yes);
                Button noBtn = (Button) alert.getDialogPane().lookupButton(no);

                yesBtn.setDefaultButton(false);
                noBtn.setDefaultButton(false);

                noBtn.setCancelButton(true);
                noBtn.requestFocus();
            }
        });

        return alert.showAndWait()
                .filter(response -> response == yes)
                .isPresent();
    }
}