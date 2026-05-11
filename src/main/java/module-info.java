module com.mokkikodit {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires java.sql;

    opens com.mokkikodit.controller to javafx.fxml;

    exports com.mokkikodit;
    exports com.mokkikodit.controller;
    exports com.mokkikodit.mallit;
    exports com.mokkikodit.logiikka;
    exports com.mokkikodit.tietokanta;
}