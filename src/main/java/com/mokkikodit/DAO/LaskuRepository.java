package com.mokkikodit.DAO;

import com.mokkikodit.mallit.Lasku;
import com.mokkikodit.mallit.Varaus;

import java.sql.*;
import java.util.*;

public class LaskuRepository {

    public List<Lasku> haeKaikki() {
        List<Lasku> lista = new ArrayList<>();

        String sql = "SELECT * FROM laskut";

        try (Connection yhteys = Tietokanta.getYhteys();
             Statement stmt = yhteys.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                // ✔ create minimal Varaus object (only id for now)
                Varaus v = new Varaus();
                v.setId(rs.getInt("reservation_id"));

                Lasku l = new Lasku(
                        rs.getInt("id"),
                        v,
                        rs.getDouble("total")
                );

                l.setMaksettu(rs.getBoolean("paid"));

                lista.add(l);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}