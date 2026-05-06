package com.mokkikodit.DAO;

import com.mokkikodit.mallit.Asiakas;

import java.sql.*;
import java.util.*;

public class AsiakasRepository {

    public List<Asiakas> haeKaikki() {
        List<Asiakas> lista = new ArrayList<>();

        String sql = "SELECT * FROM asiakas";

        try (Connection yhteys = Tietokanta.getYhteys();
             Statement stmt = yhteys.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Asiakas a = new Asiakas();
                a.setId(rs.getInt("id"));
                a.setNimi(rs.getString("name"));
                a.setEmail(rs.getString("email")); // ✔ FIXED
                lista.add(a);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void tallenna(Asiakas a) {
        String sql = "INSERT INTO asiakas(sapo, puhelinnumero, nimi, osoite) VALUES (?, ?)";

        try (Connection yhteys = Tietokanta.getYhteys();
             PreparedStatement ps = yhteys.prepareStatement(sql)) {

            ps.setString(1, a.getNimi());
            ps.setString(2, a.getEmail()); // ✔ FIXED
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Asiakas haeIdlla(int id) {
        String sql = "SELECT * FROM asiakas WHERE sapo = ?";

        try (Connection yhteys = Tietokanta.getYhteys();
             PreparedStatement ps = yhteys.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Asiakas a = new Asiakas();
                a.setId(rs.getInt("id"));
                a.setNimi(rs.getString("name"));
                a.setEmail(rs.getString("email")); // ✔ FIXED
                return a;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}