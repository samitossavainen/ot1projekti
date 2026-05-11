package com.mokkikodit.tietokanta;

import com.mokkikodit.mallit.Maksu;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class MaksuRepository {

    public List<Maksu> findAll() {

        List<Maksu> lista = new ArrayList<>();

        String sql = "SELECT * FROM maksut";

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

    public void tallenna(Maksu m) {
        String sql = "INSERT INTO maksut(lasku_ID, maksettu_summa) VALUES (?, ?)";

        try (Connection yhteys = Tietokanta.getYhteys();
             PreparedStatement ps = yhteys.prepareStatement(sql)) {

            ps.setInt(1, m.getLaskuId());
            ps.setDouble(2, m.getMaksettuSumma());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Maksu map(ResultSet rs) throws SQLException {

        Maksu m = new Maksu();

        m.setMaksuId(rs.getInt("Maksu_ID"));
        m.setLaskuId(rs.getInt("Lasku_ID"));
        m.setMaksuPaiva(rs.getTimestamp("Maksupäivä"));
        m.setMaksettuSumma(rs.getDouble("Summa"));

        return m;
    }
}