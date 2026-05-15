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

        String sql = "SELECT l.lasku_ID, l.tila, l.aikaleima, l.eräpäivä, l.summa, l.varaus_ID, v.sapo, m.maksettu_summa, m.maksupäivä FROM laskut l LEFT JOIN varaus v ON l.varaus_ID = v.varaus_ID LEFT JOIN maksut m ON l.lasku_ID = m.lasku_ID";

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

        String sql = "SELECT l.lasku_ID, l.tila, l.aikaleima, l.eräpäivä, l.summa, l.varaus_ID, v.sapo, m.maksettu_summa, m.maksupäivä FROM laskut l LEFT JOIN varaus v ON l.varaus_ID = v.varaus_ID LEFT JOIN maksut m ON l.lasku_ID = m.lasku_ID WHERE l.lasku_ID=?";

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

    public void merkitseMaksetuksi(int laskuId, double summa) {

        String updateSql = "UPDATE maksut SET maksettu_summa=?, maksupäivä=? WHERE lasku_ID=?";
        String insertSql = "INSERT INTO maksut (lasku_ID, maksettu_summa, maksupäivä) VALUES (?, ?, ?)";

        try (Connection c = Tietokanta.getYhteys()) {

            // 1. yritetään päivittää olemassa oleva
            PreparedStatement update = c.prepareStatement(updateSql);
            update.setDouble(1, summa);
            update.setString(2, LocalDate.now().toString());
            update.setInt(3, laskuId);

            int rows = update.executeUpdate();

            // 2. jos ei löytynyt → luodaan uusi
            if (rows == 0) {

                PreparedStatement insert = c.prepareStatement(insertSql);
                insert.setInt(1, laskuId);
                insert.setDouble(2, summa);
                insert.setString(3, LocalDate.now().toString());

                insert.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Lasku map(ResultSet rs) throws SQLException {

        Lasku l = new Lasku();

        l.setLaskuId(rs.getInt("lasku_ID"));
        l.setVarausId(rs.getInt("varaus_ID"));
        l.setSapo(rs.getString("sapo"));
        l.setAikaleima(DateUtil.parseDateTime(rs.getString("aikaleima")));
        l.setErapaiva(DateUtil.parseDate(rs.getString("eräpäivä")));
        l.setSumma(rs.getDouble("summa"));
        l.setTila(rs.getString("tila"));
        l.setMaksupaiva(DateUtil.parseDate(rs.getString("maksupäivä")));
        double maksettu = rs.getDouble("maksettu_summa");

        if (rs.wasNull()) {
            maksettu = 0;
        }

        l.setMaksettu(maksettu);

        return l;
    }
}