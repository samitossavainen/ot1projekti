package com.mokkikodit.tietokanta;

import com.mokkikodit.mallit.Mokki;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MokkiRepository {


    public List<Mokki> findAll() {

        List<Mokki> lista = new ArrayList<>();

        String sql = "SELECT * FROM mokki";

        try (Connection c = Tietokanta.getYhteys();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(map(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }


    public Mokki findById(Integer id) {

        String sql = "SELECT * FROM mokki WHERE mokki_ID=?";

        try (Connection c = Tietokanta.getYhteys();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public void save(Mokki m) {

        String sql =
                "INSERT INTO mokki(nimi, osoite, kapasiteetti, hinta, lisatiedot, vessat, huoneet) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection c = Tietokanta.getYhteys();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, m.getNimi());
            ps.setString(2, m.getOsoite());
            ps.setInt(3, m.getKapasiteetti());
            ps.setDouble(4, m.getHinta());
            ps.setString(5,m.getLisatiedot());
            ps.setInt(6, m.getVessat());
            ps.setInt(7, m.getHuoneet());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void update(Mokki m) {

        String sql =
                "UPDATE mokki SET nimi=?, osoite=?, kapasiteetti=?, hinta=?, tila=? " +
                        "WHERE mokki_ID=?";

        try (Connection c = Tietokanta.getYhteys();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, m.getNimi());
            ps.setString(2, m.getOsoite());
            ps.setInt(3, m.getKapasiteetti());
            ps.setDouble(4, m.getHinta());
            ps.setInt(5, m.getTila());
            ps.setInt(6, m.getMokkiId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void delete(Integer id) {

        String sql = "DELETE FROM mokki WHERE mokki_ID=?";

        try (Connection c = Tietokanta.getYhteys();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Mokki map(ResultSet rs) throws SQLException {

        Mokki m = new Mokki();

        m.setMokkiId(rs.getInt("mokki_ID"));
        m.setNimi(rs.getString("nimi"));
        m.setOsoite(rs.getString("osoite"));
        m.setKapasiteetti(rs.getInt("kapasiteetti"));
        m.setHinta(rs.getDouble("hinta"));
        m.setTila(rs.getInt("tila"));
        m.setLisatiedot(rs.getString("lisatiedot"));
        m.setVessat(rs.getInt("vessat"));
        m.setHuoneet(rs.getInt("huoneet"));

        return m;
    }
}