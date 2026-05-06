package com.mokkikodit.tietokanta;

import com.mokkikodit.mallit.Varaus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VarausRepository {

    // =========================
    // READ ALL
    // =========================
    public List<Varaus> haeKaikki() {

        List<Varaus> lista = new ArrayList<>();

        String sql = "SELECT * FROM varaus";

        try (Connection yhteys = Tietokanta.getYhteys();
             Statement stmt = yhteys.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                Varaus v = new Varaus();

                v.setId(rs.getInt("varaus_ID"));
                v.setAsiakasId(rs.getInt("asiakas_ID"));
                v.setMokkiId(rs.getInt("mokki_ID"));

                Date alku = rs.getDate("alkamispvm");
                Date loppu = rs.getDate("loppumispvm");

                if (alku != null) v.setAlkuPvm(alku.toLocalDate());
                if (loppu != null) v.setLoppuPvm(loppu.toLocalDate());

                v.setTila(rs.getString("varauksen_tila"));

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

        String sql =
                "INSERT INTO varaus(asiakas_ID, mokki_ID, alkamispvm, loppumispvm, varauksen_tila) " +
                        "VALUES (?, ?, ?, ?, ?)";

        try (Connection yhteys = Tietokanta.getYhteys();
             PreparedStatement ps = yhteys.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, v.getAsiakasId());
            ps.setInt(2, v.getMokkiId());
            ps.setDate(3, Date.valueOf(v.getAlkuPvm()));
            ps.setDate(4, Date.valueOf(v.getLoppuPvm()));
            ps.setString(5, v.getTila());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    v.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =========================
    // UPDATE
    // =========================
    public void paivita(Varaus v) {

        String sql =
                "UPDATE varaus SET asiakas_ID=?, mokki_ID=?, alkamispvm=?, loppumispvm=?, varauksen_tila=? " +
                        "WHERE varaus_ID=?";

        try (Connection yhteys = Tietokanta.getYhteys();
             PreparedStatement ps = yhteys.prepareStatement(sql)) {

            ps.setInt(1, v.getAsiakasId());
            ps.setInt(2, v.getMokkiId());
            ps.setDate(3, Date.valueOf(v.getAlkuPvm()));
            ps.setDate(4, Date.valueOf(v.getLoppuPvm()));
            ps.setString(5, v.getTila());
            ps.setInt(6, v.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =========================
    // DELETE
    // =========================
    public void poista(int id) {

        String sql = "DELETE FROM varaus WHERE varaus_ID=?";

        try (Connection yhteys = Tietokanta.getYhteys();
             PreparedStatement ps = yhteys.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}