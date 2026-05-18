package com.mokkikodit.tietokanta;

import com.mokkikodit.mallit.Asiakas;
import com.mokkikodit.mallit.Mokki;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AsiakasRepository {

    // Hakee kaikki asiakkaat tietokannasta
    public List<Asiakas> findAll() {

        List<Asiakas> lista = new ArrayList<>();

        String sql = "SELECT * FROM asiakas";

        try (Connection yhteys = Tietokanta.getYhteys();
             Statement stmt = yhteys.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Käydään kaikki rivit läpi ja muodostetaan Asiakas-oliot
            while (rs.next()) {
                Asiakas a = new Asiakas();
                a.setNimi(rs.getString("nimi"));
                a.setSapo(rs.getString("sapo"));
                a.setPuhelinnumero(rs.getString("puhelinnumero"));
                a.setOsoite(rs.getString("osoite"));
                a.setTila(rs.getInt("tila"));

                lista.add(a);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Asiakkaiden haku epäonnistui", e);
        }

        return lista;
    }

    // Hakee vain käytettävissä olevat asiakkaat (tila = 1)
    public List<Asiakas> findAllAvailable() {

        List<Asiakas> lista = new ArrayList<>();

        String sql = "SELECT * FROM asiakas";

        try (Connection yhteys = Tietokanta.getYhteys();
             Statement stmt = yhteys.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Suodatetaan asiakkaat, joilla tila == 1
            while (rs.next()) {
                if (rs.getInt("tila") == 1)  {
                    Asiakas a = new Asiakas();
                    a.setNimi(rs.getString("nimi"));
                    a.setSapo(rs.getString("sapo"));
                    a.setPuhelinnumero(rs.getString("puhelinnumero"));
                    a.setOsoite(rs.getString("osoite"));
                    a.setTila(rs.getInt("tila"));

                    lista.add(a);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Asiakkaiden haku epäonnistui", e);
        }

        return lista;
    }

    // Hakee asiakkaan sähköpostin perusteella (sapo = primary key)
    public Asiakas findBySapo(String sapo) {

        String sql = "SELECT * FROM asiakas WHERE sapo = ?";

        try (Connection yhteys = Tietokanta.getYhteys();
             PreparedStatement ps = yhteys.prepareStatement(sql)) {

            ps.setString(1, sapo);

            try (ResultSet rs = ps.executeQuery()) {

                // Jos asiakas löytyy, luodaan ja palautetaan olio
                if (rs.next()) {
                    Asiakas a = new Asiakas();
                    a.setSapo(rs.getString("sapo"));
                    a.setNimi(rs.getString("nimi"));
                    a.setPuhelinnumero(rs.getString("puhelinnumero"));
                    a.setOsoite(rs.getString("osoite"));
                    a.setTila(rs.getInt("tila"));
                    return a;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Asiakkaan haku epäonnistui", e);
        }

        // Palautetaan null jos asiakasta ei löydy
        return null;
    }

    // Tallentaa uuden asiakkaan tietokantaan
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

    // Päivittää olemassa olevan asiakkaan tiedot (sapo ei muutu)
    public void update(Asiakas a) {

        // asiakkaan tietoja päivittäessä sapo ei voi muuttua koska se on PK
        String sql = "UPDATE asiakas SET nimi=?, puhelinnumero=?, osoite=?, tila=? WHERE sapo=?";

        try (Connection yhteys = Tietokanta.getYhteys();
             PreparedStatement ps = yhteys.prepareStatement(sql)) {

            ps.setString(1, a.getNimi());
            ps.setString(2, a.getPuhelinnumero());
            ps.setString(3, a.getOsoite());
            ps.setInt(4, a.getTila());
            ps.setString(5, a.getSapo());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Asiakkaan päivitys epäonnistui", e);
        }
    }

    // Poistaa asiakkaan tietokannasta sähköpostin perusteella
    public void delete(String sapo) {

        String sql = "DELETE FROM asiakas WHERE sapo = ?";

        try (Connection yhteys = Tietokanta.getYhteys();
             PreparedStatement ps = yhteys.prepareStatement(sql)) {

            ps.setString(1, sapo);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Asiakkaan poisto epäonnistui", e);
        }
    }
}