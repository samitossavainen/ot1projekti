package com.mokkikodit.tietokanta;

import com.mokkikodit.mallit.Maksu;

import java.sql.*;

public class MaksuRepository {

    public void tallenna(Maksu m) {
        String sql = "INSERT INTO maksut(lasku_ID, maksettu_summa) VALUES (?, ?)";

        try (Connection yhteys = Tietokanta.getYhteys();
             PreparedStatement ps = yhteys.prepareStatement(sql)) {

            ps.setInt(1, m.getLaskuId());
            ps.setDouble(2, m.getMaksettuSumma());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}