package com.mokkikodit.tietokanta;

import com.mokkikodit.mallit.Lasku;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LaskuRepository {

    public List<Lasku> findAll() {

        List<Lasku> lista = new ArrayList<>();

        String sql = "SELECT * FROM lasku";

        try (Connection yhteys = Tietokanta.getYhteys();
             Statement stmt = yhteys.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                Lasku l = new Lasku(
                        rs.getInt("lasku_ID"),
                        rs.getInt("varaus_ID"),
                        null,
                        rs.getString("tila"),
                        rs.getTimestamp("aikaleima").toLocalDateTime(),
                        rs.getDate("erapaiva").toLocalDate(),
                        rs.getDouble("summa")
                );

                lista.add(l);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Laskujen haku epäonnistui", e);
        }

        return lista;
    }

    public Lasku findById(int id) {

        String sql = "SELECT * FROM lasku WHERE lasku_ID = ?";

        try (Connection yhteys = Tietokanta.getYhteys();
             PreparedStatement ps = yhteys.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new Lasku(
                            rs.getInt("lasku_ID"),
                            rs.getInt("varaus_ID"),
                            null,
                            rs.getString("tila"),
                            rs.getTimestamp("aikaleima").toLocalDateTime(),
                            rs.getDate("erapaiva").toLocalDate(),
                            rs.getDouble("summa")
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Laskun haku epäonnistui", e);
        }

        return null;
    }

    public void save(Lasku l) {

        String sql =
                "INSERT INTO lasku " +
                        "(tila, erapaiva, summa, varaus_ID) " +
                        "VALUES (?, ?, ?, ?)";

        try (Connection yhteys = Tietokanta.getYhteys();
             PreparedStatement ps = yhteys.prepareStatement(sql)) {

            ps.setString(1, l.getTila());
            ps.setString(2, l.getErapaiva().toString());
            ps.setDouble(3, l.getSumma());
            ps.setInt(4, l.getVarausId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Laskun tallennus epäonnistui", e);
        }
    }

    public void update(Lasku l) {

        String sql =
                "UPDATE lasku SET " +
                        "tila = ?, " +
                        "erapaiva = ?, " +
                        "summa = ?, " +
                        "varaus_ID = ? " +
                        "WHERE lasku_ID = ?";

        try (Connection yhteys = Tietokanta.getYhteys();
             PreparedStatement ps = yhteys.prepareStatement(sql)) {

            ps.setString(1, l.getTila());
            ps.setString(2, l.getErapaiva().toString());
            ps.setDouble(3, l.getSumma());
            ps.setInt(4, l.getVarausId());
            ps.setInt(5, l.getLaskuId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Laskun päivitys epäonnistui", e);
        }
    }

    public void delete(int id) {

        String sql = "DELETE FROM lasku WHERE lasku_ID = ?";

        try (Connection yhteys = Tietokanta.getYhteys();
             PreparedStatement ps = yhteys.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Laskun poisto epäonnistui", e);
        }
    }
}