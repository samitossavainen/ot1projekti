package com.mokkikodit.DAO;

import com.mokkikodit.mallit.Mokki;

import java.sql.*;
import java.util.*;

public class MokkiRepository {

    public List<Mokki> haeKaikki() {
        List<Mokki> lista = new ArrayList<>();

        String sql = "SELECT * FROM mokki";

        try (Connection yhteys = Tietokanta.getYhteys();
             Statement stmt = yhteys.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Mokki m = new Mokki();
                m.setId(rs.getInt("id"));
                m.setNimi(rs.getString("name"));
                m.setSijainti(rs.getString("location"));
                lista.add(m);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}