package com.mokkikodit.tietokanta;

import com.mokkikodit.mallit.Mokki;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MokkiRepository {

    // Hakee kaikki mökit tietokannasta
    public List<Mokki> findAll() {

        List<Mokki> lista = new ArrayList<>();

        String sql = "SELECT * FROM mokki";

        try (Connection c = Tietokanta.getYhteys();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            // Käydään kaikki rivit läpi ja mapataan Mökit-olioiksi
            while (rs.next()) {
                lista.add(map(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Hakee vain vapaat mökit (tila == 1)
    public List<Mokki> findAllAvailable() {

        List<Mokki> lista = new ArrayList<>();

        String sql = "SELECT * FROM mokki";

        try (Connection c = Tietokanta.getYhteys();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            // Suodatetaan vain vapaat mökit
            while (rs.next()) {
                if (map(rs).getTila() == 1) {
                    lista.add(map(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Hakee yksittäisen mökin ID:n perusteella
    public Mokki findById(Integer id) {

        String sql = "SELECT * FROM mokki WHERE mokki_ID=?";

        try (Connection c = Tietokanta.getYhteys();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                // Jos löytyy, palautetaan mapattu Mökk i-olio
                if (rs.next()) {
                    return map(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Tallentaa uuden mökin tietokantaan
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
            ps.setString(5, m.getLisatiedot());
            ps.setInt(6, m.getVessat());
            ps.setInt(7, m.getHuoneet());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Päivittää mökin tiedot tietokantaan
    public void update(Mokki m) {

        String sql =
                "UPDATE mokki " +
                        "SET nimi=?, osoite=?, kapasiteetti=?, hinta=?, lisatiedot=?, vessat=?, huoneet=?, tila=? " +
                        "WHERE mokki_ID=?";

        try (Connection c = Tietokanta.getYhteys();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, m.getNimi());
            ps.setString(2, m.getOsoite());
            ps.setInt(3, m.getKapasiteetti());
            ps.setDouble(4, m.getHinta());
            ps.setString(5, m.getLisatiedot());
            ps.setInt(6, m.getVessat());
            ps.setInt(7, m.getHuoneet());
            ps.setInt(8, m.getTila());
            ps.setInt(9, m.getMokkiId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Muuntaa ResultSet-rivin Mokki-olioksi (mapper-metodi)
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