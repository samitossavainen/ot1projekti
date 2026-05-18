package com.mokkikodit.tietokanta;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Tietokanta {

    // SQLite-tietokannan tiedostopolku (paikallinen tietokanta)
    private static final String URL = "jdbc:sqlite:./mokkivaraus.db";

    // Palauttaa uuden tietokantayhteyden aina kun metodia kutsutaan
    // Huom: yhteys tulee sulkea käytön jälkeen (try-with-resources hoitaa tämän)
    public static Connection getYhteys() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}