package com.mokkikodit.logiikka;

import com.mokkikodit.mallit.Lasku;
import com.mokkikodit.mallit.Varaus;

import java.util.ArrayList;
import java.util.List;

public class LaskuService {
    private List<Lasku> laskut = new ArrayList<>();
    private int nextId = 1;

    public Lasku luoLasku(Varaus varaus) {
        double summa = varaus.getPaivienMaara() *
                varaus.getMokki().getHintaPerYo();

        Lasku l = new Lasku(nextId++, varaus, summa);
        laskut.add(l);
        return l;
    }

    public void merkitseMaksetuksi(int laskuId) {
        laskut.stream()
                .filter(l -> l.getId() == laskuId)
                .findFirst()
                .ifPresent(l -> l.setMaksettu(true));
    }

    public List<Lasku> haeKaikki() {
        return laskut;
    }
}
