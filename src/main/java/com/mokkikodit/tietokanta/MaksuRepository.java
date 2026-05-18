package com.mokkikodit.tietokanta;

import com.mokkikodit.mallit.Maksu;
import com.mokkikodit.util.DateUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaksuRepository {

    // Hakee kaikki maksut tietokannasta
    public List<Maksu> findAll() {

        List<Maksu> lista = new ArrayList<>();

        String sql = "SELECT * FROM maksut";

        try (Connection c = Tietokanta.getYhteys();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            // Käydään kaikki maksurivit läpi ja mapataan olioiksi
            while (rs.next()) {
                lista.add(map(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Tallentaa uuden maksun tietokantaan
    public void tallenna(Maksu m) {

        String sql = "INSERT INTO maksut (lasku_ID, maksettu_summa) VALUES (?, ?)";

        try (Connection yhteys = Tietokanta.getYhteys();
             PreparedStatement ps = yhteys.prepareStatement(sql)) {

            ps.setInt(1, m.getLaskuId());
            ps.setDouble(2, m.getMaksettuSumma());

            // maksupäivä tulee tietokannan DEFAULT-arvona (esim. datetime('now','localtime'))
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Muuntaa ResultSet-rivin Maksu-olioksi (mapper-metodi)
    private Maksu map(ResultSet rs) throws SQLException {

        Maksu m = new Maksu();

        m.setMaksuId(rs.getInt("maksu_ID"));
        m.setLaskuId(rs.getInt("lasku_ID"));
        m.setMaksettuSumma(rs.getDouble("maksettu_summa"));

        // SQLite tallentaa päivämäärän muodossa "YYYY-MM-DD HH:MM:SS"
        // Java-mallissa käytetään LocalDatea → otetaan vain päivämääräosa
        String maksuPaivaStr = rs.getString("maksupäivä");

        if (maksuPaivaStr != null) {
            m.setMaksuPaiva(
                    DateUtil.parseDate(maksuPaivaStr.substring(0, 10))
            );
        }

        return m;
    }
}