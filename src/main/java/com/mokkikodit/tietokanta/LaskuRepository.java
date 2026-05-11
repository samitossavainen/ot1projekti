package com.mokkikodit.tietokanta;

import com.mokkikodit.mallit.Lasku;
import com.mokkikodit.mallit.Mokki;
import com.mokkikodit.mallit.Varaus;
import com.mokkikodit.util.DateUtil;
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

    public Lasku findById(Integer id) {

        String sql = "SELECT * FROM laskut WHERE lasku_ID=?";

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

    public void save(Lasku l) {

        String sql =
                "INSERT INTO laskut (tila, erapaiva, summa, varaus_ID) VALUES (?, ?, ?, ?)";

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

    public void update(Lasku m) {

        String sql =
                "UPDATE laskut " +
                        "SET tila=?, eräpäivä=?, summa=? " +
                        "WHERE lasku_ID=?";

        try (Connection c = Tietokanta.getYhteys();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, m.getTila());
            ps.setString(2, m.getErapaiva().toString());
            ps.setDouble(3, m.getSumma());
            ps.setInt(4, m.getLaskuId());


            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {

        String sql = "DELETE FROM laskut WHERE lasku_ID = ?";

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
        l.setAikaleima(DateUtil.parseDateTime(rs.getString("aikaleima")));
        l.setErapaiva(DateUtil.parseDate(rs.getString("eräpäivä")));
        l.setSumma(rs.getDouble("summa"));
        l.setTila(rs.getString("tila"));

        return l;
    }
}