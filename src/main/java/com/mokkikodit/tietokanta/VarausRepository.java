package com.mokkikodit.tietokanta;

import com.mokkikodit.mallit.Varaus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VarausRepository {

    // =========================
    // GET ALL
    // =========================
    public List<Varaus> haeKaikki() {
        List<Varaus> lista = new ArrayList<>();

        String sql = "SELECT * FROM varaus";

        try (Connection yhteys = Tietokanta.getYhteys();
             Statement stmt = yhteys.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Varaus v = new Varaus();
                v.setId(rs.getInt("id"));
                v.setAsiakasId(rs.getInt("customer_id"));
                v.setMokkiId(rs.getInt("mokki_id"));

                Date alku = rs.getDate("start_date");
                Date loppu = rs.getDate("end_date");

                if (alku != null) v.setAlkuPvm(alku.toLocalDate());
                if (loppu != null) v.setLoppuPvm(loppu.toLocalDate());

                lista.add(v);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // =========================
    // INSERT
    // =========================
    public void tallenna(Varaus v) {
        String sql = "INSERT INTO varaus(sapo, alkamispvm, loppumispvm, mokki_ID, kokonaissumma) VALUES (?, ?, ?, ?, ?)";

        try (Connection yhteys = Tietokanta.getYhteys();
             PreparedStatement ps = yhteys.prepareStatement(sql)) {

            ps.setInt(1, v.getAsiakasId());
            ps.setInt(2, v.getMokkiId());
            ps.setDate(3, Date.valueOf(v.getAlkuPvm()));
            ps.setDate(4, Date.valueOf(v.getLoppuPvm()));

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =========================
    // UPDATE
    // =========================
    public void paivita(Varaus v) {
        String sql = "UPDATE varaus SET sapo = ?, mokki_ID = ?, alkamispvm = ?, loppumispvm = ? WHERE varaus_ID = ?";

        try (Connection yhteys = Tietokanta.getYhteys();
             PreparedStatement ps = yhteys.prepareStatement(sql)) {

            ps.setInt(1, v.getAsiakasId());
            ps.setInt(2, v.getMokkiId());
            ps.setDate(3, Date.valueOf(v.getAlkuPvm()));
            ps.setDate(4, Date.valueOf(v.getLoppuPvm()));
            ps.setInt(5, v.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =========================
    // DELETE
    // =========================
    public void poista(int id) {
        String sql = "DELETE FROM varaus WHERE varaus_ID = ?";

        try (Connection yhteys = Tietokanta.getYhteys();
             PreparedStatement ps = yhteys.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}