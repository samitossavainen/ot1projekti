package com.mokkikodit.logiikka;

import com.mokkikodit.mallit.Mokki;

import java.util.ArrayList;
import java.util.List;

public class MokkiService {
    private List<Mokki> mokit = new ArrayList<>();
    private int nextId = 1;

    public Mokki lisaaMokki(String nimi, int kapasiteetti, double hinta) {
        Mokki m = new Mokki(nextId++, nimi, kapasiteetti, hinta);
        mokit.add(m);
        return m;
    }

    public List<Mokki> haeKaikki() {
        return mokit;
    }

    public Mokki haeIdlla(int id) {
        return mokit.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
