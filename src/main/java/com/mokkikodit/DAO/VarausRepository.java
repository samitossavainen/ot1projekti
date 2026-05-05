package com.mokkikodit.DAO;

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

        String sql = "SELECT * FROM reservation";

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
        String sql = "INSERT INTO reservation(customer_id, mokki_id, start_date, end_date) VALUES (?, ?, ?, ?)";

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
        String sql = "UPDATE reservation SET customer_id = ?, mokki_id = ?, start_date = ?, end_date = ? WHERE id = ?";

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
        String sql = "DELETE FROM reservation WHERE id = ?";

        try (Connection yhteys = Tietokanta.getYhteys();
             PreparedStatement ps = yhteys.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}