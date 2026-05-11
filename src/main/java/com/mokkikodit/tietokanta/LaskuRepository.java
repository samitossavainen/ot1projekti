package com.mokkikodit.tietokanta;

import com.mokkikodit.mallit.Lasku;
import com.mokkikodit.mallit.Mokki;
import com.mokkikodit.mallit.Varaus;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LaskuRepository {

    public List<Lasku> findAll() {

        List<Lasku> lista = new ArrayList<>();

        String sql = "SELECT * FROM laskut";

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
                            rs.getTimestamp("aikaleima"),
                            LocalDate.parse(rs.getString("eräpäivä")),
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
                "INSERT INTO lasku (tila, erapaiva, summa, varaus_ID) VALUES (?, ?, ?, ?)";

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
                "UPDATE lasku SET tila = ?, erapaiva = ?, summa = ?, varaus_ID = ? WHERE lasku_ID = ?";

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

    private Lasku map(ResultSet rs) throws SQLException {

        Lasku l = new Lasku();

        l.setLaskuId(rs.getInt("lasku_ID"));
        l.setVarausId(rs.getInt("varaus_ID"));
        l.setAikaleima(rs.getTimestamp("aikaleima"));
        l.setErapaiva(LocalDate.parse(rs.getString("eräpäivä")));
        l.setSumma(rs.getDouble("summa"));
        l.setTila(rs.getString("tila"));

        return l;
    }
}