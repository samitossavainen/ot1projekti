package com.mokkikodit.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Tietokanta {

    private static final String URL = "jdbc:sqlite:./mokkivaraus.db";

    public static Connection getYhteys() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}