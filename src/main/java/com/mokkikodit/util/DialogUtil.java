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
     * Näyttää informaatioikkunan käyttäjälle.
     */
    public static void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Info");
        alert.setHeaderText(null); // Ei käytetä erillistä otsikkoa, pidetään UI yksinkertaisena
        alert.setContentText(msg);

        alert.showAndWait();
    }

    /**
     * Näyttää virheilmoitusikkunan käyttäjälle.
     */
    public static void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Virhe");
        alert.setHeaderText(null); // Ei otsikkoa, vain viesti
        alert.setContentText(msg);

        alert.showAndWait();
    }

    /**
     * Näyttää kyllä/ei-vahvistusikkunan.
     *
     * @param owner   pääikkuna, johon dialogi sidotaan
     * @param title   ikkunan otsikko
     * @param header  valinnainen yläotsikko
     * @param content varsinainen viesti käyttäjälle
     * @return true jos käyttäjä valitsi "Kyllä", muuten false
     */
    public static boolean confirm(Stage owner, String title, String header, String content) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        // Sidotaan dialogi pääikkunaan (modaalinen ikkuna)
        alert.initOwner(owner);
        alert.initModality(Modality.WINDOW_MODAL);

        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        // Luodaan omat painikkeet suomeksi
        ButtonType yes = new ButtonType("Kyllä", ButtonBar.ButtonData.YES);
        ButtonType no = new ButtonType("Ei", ButtonBar.ButtonData.NO);

        // Asetetaan painikkeiden järjestys
        alert.getButtonTypes().setAll(no, yes);

        // Muokataan painikkeiden käyttäytymistä kun dialogi avautuu
        alert.showingProperty().addListener((obs, wasShowing, isShowing) -> {
            if (isShowing) {
                Button yesBtn = (Button) alert.getDialogPane().lookupButton(yes);
                Button noBtn = (Button) alert.getDialogPane().lookupButton(no);

                // Ei aseteta oletuspainiketta automaattisesti
                yesBtn.setDefaultButton(false);
                noBtn.setDefaultButton(false);

                // "Ei" toimii peruutuspainikkeena (ESC)
                noBtn.setCancelButton(true);

                // Kohdistus oletuksena "Ei"-painikkeeseen
                noBtn.requestFocus();
            }
        });

        // Palautetaan true vain jos käyttäjä painoi "Kyllä"
        return alert.showAndWait()
                .filter(response -> response == yes)
                .isPresent();
    }
}