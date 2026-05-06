package com.mokkikodit.tietokanta;

import com.mokkikodit.mallit.Mokki;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MokkiRepository implements CrudRepository<Mokki, Integer> {

    @Override
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

    @Override
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

    @Override
    public void save(Mokki m) {

        String sql =
                "INSERT INTO mokki(nimi, osoite, kapasiteetti, hinta) " +
                        "VALUES (?, ?, ?, ?)";

        try (Connection c = Tietokanta.getYhteys();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, m.getNimi());
            ps.setString(2, m.getSijainti());
            ps.setInt(3, m.getHenkiloMaara());
            ps.setDouble(4, m.getHinta());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Mokki m) {

        String sql =
                "UPDATE mokki SET nimi=?, osoite=?, kapasiteetti=?, hinta=? " +
                        "WHERE mokki_ID=?";

        try (Connection c = Tietokanta.getYhteys();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, m.getNimi());
            ps.setString(2, m.getSijainti());
            ps.setInt(3, m.getHenkiloMaara());
            ps.setDouble(4, m.getHinta());
            ps.setInt(5, m.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
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

        m.setId(rs.getInt("mokki_ID"));
        m.setNimi(rs.getString("nimi"));
        m.setSijainti(rs.getString("osoite"));
        m.setHenkiloMaara(rs.getInt("kapasiteetti"));
        m.setHinta(rs.getDouble("hinta"));

        return m;
    }
}