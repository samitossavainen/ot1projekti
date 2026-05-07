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

                v.setVarausId(rs.getInt("varaus_ID"));
                v.setAsiakasEmail(rs.getString("sapo"));
                v.setMokkiId(rs.getInt("mokki_ID"));
                v.setTila(rs.getString("varauksen_tila"));
                v.setKokonaissumma(rs.getDouble("kokonaissumma"));

                Date alku = rs.getDate("alkamispvm");
                Date loppu = rs.getDate("loppumispvm");
                Timestamp luonti = rs.getTimestamp("luontipvm");

                if (alku != null) v.setAlkuPvm(alku.toLocalDate());
                if (loppu != null) v.setLoppuPvm(loppu.toLocalDate());
                if (luonti != null) v.setLuontiPvm(luonti.toLocalDateTime());

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
            ps.setDate(2, Date.valueOf(v.getAlkuPvm()));
            ps.setDate(3, Date.valueOf(v.getLoppuPvm()));
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
            ps.setDate(4, Date.valueOf(v.getAlkuPvm()));
            ps.setDate(5, Date.valueOf(v.getLoppuPvm()));
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