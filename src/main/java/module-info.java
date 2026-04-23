module com.mokkikodit {

    requires javafx.controls;
    requires javafx.fxml;

    opens com.mokkikodit.controller to javafx.fxml;

    exports com.mokkikodit;
}