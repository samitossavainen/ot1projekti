package com.mokkikodit.logiikka;

import com.mokkikodit.mallit.Asiakas;
import com.mokkikodit.mallit.Mokki;
import com.mokkikodit.mallit.Varaus;

import java.util.ArrayList;
import java.util.List;

public class VarausService {
    private List<Varaus> varaukset = new ArrayList<>();
    private int nextId = 1;

    public Varaus teeVaraus(Asiakas asiakas, Mokki mokki,
                            java.time.LocalDate alku,
                            java.time.LocalDate loppu) {

        if (!onkoVapaa(mokki, alku, loppu)) {
            throw new IllegalArgumentException("Mökki ei ole vapaa!");
        }

        Varaus v = new Varaus(nextId++, asiakas, mokki, alku, loppu);
        varaukset.add(v);
        return v;
    }

    public boolean onkoVapaa(Mokki mokki,
                             java.time.LocalDate alku,
                             java.time.LocalDate loppu) {

        return varaukset.stream().noneMatch(v ->
                v.getMokki().getId() == mokki.getId() &&
                        !(loppu.isBefore(v.getAlkuPvm()) || alku.isAfter(v.getLoppuPvm()))
        );
    }

    public List<Varaus> haeKaikki() {
        return varaukset;
    }
}
