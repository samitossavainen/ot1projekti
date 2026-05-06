package com.mokkikodit.tietokanta;

import com.mokkikodit.mallit.Asiakas;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AsiakasRepository implements CrudRepository<Asiakas, Integer> {

    @Override
    public List<Asiakas> findAll() {

        List<Asiakas> lista = new ArrayList<>();
        String sql = "SELECT * FROM asiakas";

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
    public Asiakas findById(Integer id) {

        String sql = "SELECT * FROM asiakas WHERE id=?";

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
    public void save(Asiakas a) {

        String sql =
                "INSERT INTO asiakas(name, email, puhelinnumero) " +
                        "VALUES (?, ?, ?)";

        try (Connection c = Tietokanta.getYhteys();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, a.getNimi());
            ps.setString(2, a.getEmail());
            ps.setString(3, a.getPuhelin());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Asiakas a) {

        String sql =
                "UPDATE asiakas " +
                        "SET name=?, email=?, puhelinnumero=? " +
                        "WHERE id=?";

        try (Connection c = Tietokanta.getYhteys();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, a.getNimi());
            ps.setString(2, a.getEmail());
            ps.setString(3, a.getPuhelin());
            ps.setInt(4, a.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) {

        String sql = "DELETE FROM asiakas WHERE id=?";

        try (Connection c = Tietokanta.getYhteys();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Asiakas map(ResultSet rs) throws SQLException {

        Asiakas a = new Asiakas();

        a.setId(rs.getInt("id"));
        a.setNimi(rs.getString("name"));
        a.setEmail(rs.getString("email"));
        a.setPuhelin(rs.getString("puhelinnumero"));

        return a;
    }
}