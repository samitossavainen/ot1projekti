package com.mokkikodit.tietokanta;

import com.mokkikodit.mallit.Varaus;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.mokkikodit.util.DateUtil;

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

                v.setVarausId(rs.getInt("varaus_ID"));
                v.setAsiakasEmail(rs.getString("sapo"));
                v.setMokkiId(rs.getInt("mokki_ID"));
                v.setTila(rs.getString("varauksen_tila"));
                v.setKokonaissumma(rs.getDouble("kokonaissumma"));

                String alkuStr = rs.getString("alkamispvm");
                String loppuStr = rs.getString("loppumispvm");
                String luontiStr = rs.getString("luontipvm");

                if (alkuStr != null) {
                    v.setAlkuPvm(DateUtil.parseDate(rs.getString("alkamispvm")));
                }

                if (loppuStr != null) {
                    v.setLoppuPvm(DateUtil.parseDate(rs.getString("loppumispvm")));
                }

                if (luontiStr != null) {
                    v.setLuontiPvm(DateUtil.parseDateTime(rs.getString("luontipvm")));

                }

                lista.add(v);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Varauksien haku epäonnistui", e);
        }

        return lista;
    }

    // =========================
    // INSERT
    // =========================
    public void tallenna(Varaus v) {

        String sql =
                "INSERT INTO varaus (sapo, alkamispvm, loppumispvm, mokki_ID, kokonaissumma) " +
                        "VALUES (?, ?, ?, ?, ?)";

        try (Connection yhteys = Tietokanta.getYhteys();
             PreparedStatement ps = yhteys.prepareStatement(sql)) {

            ps.setString(1, v.getAsiakasEmail());

            ps.setString(2, v.getAlkuPvm().toString());
            ps.setString(3, v.getLoppuPvm().toString());

            ps.setInt(4, v.getMokkiId());
            ps.setDouble(5, v.getKokonaissumma());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Varauksen tallennus epäonnistui", e);
        }
    }

    // =========================
    // UPDATE
    // =========================
    public void paivita(Varaus v) {

        String sql =
                "UPDATE varaus SET sapo = ?, mokki_ID = ?, varauksen_tila = ?, " +
                        "alkamispvm = ?, loppumispvm = ? WHERE varaus_ID = ?";

        try (Connection yhteys = Tietokanta.getYhteys();
             PreparedStatement ps = yhteys.prepareStatement(sql)) {

            ps.setString(1, v.getAsiakasEmail());
            ps.setInt(2, v.getMokkiId());
            ps.setString(3, v.getTila());

            ps.setString(2, v.getAlkuPvm().toString());
            ps.setString(3, v.getLoppuPvm().toString());

            ps.setInt(6, v.getVarausId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Varauksen päivitys epäonnistui", e);
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
            throw new RuntimeException("Varauksen poisto epäonnistui", e);
        }
    }
}