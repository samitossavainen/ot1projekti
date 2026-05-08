package com.mokkikodit.tietokanta;

import com.mokkikodit.mallit.Asiakas;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AsiakasRepository {

    public List<Asiakas> findAll() {

        List<Asiakas> lista = new ArrayList<>();

        String sql = "SELECT * FROM asiakas";

        try (Connection yhteys = Tietokanta.getYhteys();
             Statement stmt = yhteys.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                Asiakas a = new Asiakas();
                a.setNimi(rs.getString("nimi"));
                a.setSapo(rs.getString("sapo"));
                a.setPuhelinnumero(rs.getString("puhelinnumero"));
                a.setOsoite(rs.getString("osoite"));

                lista.add(a);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Asiakkaiden haku epäonnistui", e);
        }

        return lista;
    }

    public Asiakas findById(int id) {

        String sql = "SELECT * FROM asiakas WHERE id = ?";

        try (Connection yhteys = Tietokanta.getYhteys();
             PreparedStatement ps = yhteys.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    Asiakas a = new Asiakas();
                    a.setNimi(rs.getString("nimi"));
                    a.setSapo(rs.getString("sapo"));
                    a.setPuhelinnumero(rs.getString("puhelinnumero"));
                    a.setOsoite(rs.getString("osoite"));
                    return a;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Asiakkaan haku epäonnistui", e);
        }

        return null;
    }

    public void save(Asiakas a) {

        String sql = "INSERT INTO asiakas (sapo, puhelinnumero, nimi, osoite) VALUES (?, ?, ?, ?)";

        try (Connection yhteys = Tietokanta.getYhteys();
             PreparedStatement ps = yhteys.prepareStatement(sql)) {

            ps.setString(1, a.getSapo());
            ps.setString(2, a.getPuhelinnumero());
            ps.setString(3, a.getNimi());
            ps.setString(4, a.getOsoite());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Asiakkaan tallennus epäonnistui", e);
        }
    }

    public void update(Asiakas a) {

        String sql = "UPDATE asiakas SET sapo=?, puhelinnumero=?, nimi=?, osoite=? WHERE id=?";

        try (Connection yhteys = Tietokanta.getYhteys();
             PreparedStatement ps = yhteys.prepareStatement(sql)) {

            ps.setString(1, a.getSapo());
            ps.setString(2, a.getPuhelinnumero());
            ps.setString(3, a.getNimi());
            ps.setString(4, a.getOsoite());
            ps.setInt(5, a.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Asiakkaan päivitys epäonnistui", e);
        }
    }

    public void delete(int id) {

        String sql = "DELETE FROM asiakas WHERE id = ?";

        try (Connection yhteys = Tietokanta.getYhteys();
             PreparedStatement ps = yhteys.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Asiakkaan poisto epäonnistui", e);
        }
    }
}