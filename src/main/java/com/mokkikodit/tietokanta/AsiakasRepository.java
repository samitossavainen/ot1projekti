package com.mokkikodit.tietokanta;

import com.mokkikodit.mallit.Asiakas;

import java.sql.*;
import java.util.*;

public class AsiakasRepository {

    public List<Asiakas> findAll() {
        List<Asiakas> lista = new ArrayList<>();

        String sql = "SELECT * FROM asiakas";

        try (Connection yhteys = Tietokanta.getYhteys();
             Statement stmt = yhteys.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Asiakas a = new Asiakas();
                a.setNimi(rs.getString("name"));
                a.setSapo(rs.getString("email")); // ✔ FIXED
                a.setPuhelinnumero(rs.getString("puhelinnumero"));
                a.setOsoite(rs.getString("osoite"));
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

            ps.setString(3, a.getNimi());
            ps.setString(1, a.getSapo()); // ✔ FIXED
            ps.setString(2, a.getPuhelinnumero());
            ps.setString(4, a.getOsoite());
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
                a.setNimi(rs.getString("name"));
                a.setSapo(rs.getString("email")); // ✔ FIXED
                a.setPuhelinnumero(rs.getString("puhelinnumero"));
                a.setOsoite(rs.getString("osoite"));
                return a;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}