package com.mokkikodit.logiikka;

import com.mokkikodit.mallit.Varaus;
import com.mokkikodit.mallit.Asiakas;
import com.mokkikodit.mallit.Mokki;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VarausService {

    private final List<Varaus> varaukset = new ArrayList<>();

    public VarausService() {
        lataaMockData(); // 🔹 load sample data automatically
    }

    private void lataaMockData() {

        // 🔹 Mock asiakkaat
        Asiakas a1 = new Asiakas(1, "Matti Meikäläinen", "matti@example.com", "0401234567");
        Asiakas a2 = new Asiakas(2, "Liisa Virtanen", "liisa@example.com", "0509876543");

        // 🔹 Mock mökit
        Mokki m1 = new Mokki(1, "Rantamökki", 4, 120.0);
        Mokki m2 = new Mokki(2, "Hirsimökki", 6, 180.0);

        // 🔹 Mock varaukset
        varaukset.add(new Varaus(
                1,
                a1,
                m1,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(5),
                "VARATTU"
        ));

        varaukset.add(new Varaus(
                2,
                a2,
                m2,
                LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(7),
                "MAKSETTU"
        ));
    }

    public List<Varaus> getAllVaraukset() {
        return new ArrayList<>(varaukset);
    }

    public void addVaraus(Varaus varaus) {
        varaukset.add(varaus);
    }

    public void updateVaraus(Varaus updated) {
        for (int i = 0; i < varaukset.size(); i++) {
            if (varaukset.get(i).getId() == updated.getId()) {
                varaukset.set(i, updated);
                return;
            }
        }
        throw new IllegalArgumentException("Varausta ei löytynyt ID:llä " + updated.getId());
    }

    public void deleteVaraus(int id) {
        varaukset.removeIf(v -> v.getId() == id);
    }
}