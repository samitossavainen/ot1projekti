package com.mokkikodit.logiikka;

import com.mokkikodit.mallit.Maksu;
import com.mokkikodit.mallit.Mokki;
import com.mokkikodit.tietokanta.MaksuRepository;

import java.util.List;

public class MaksuService {

    private final MaksuRepository repo;

    public MaksuService(MaksuRepository repo) {
        this.repo = repo;
    }

    public List<Maksu> haeKaikki() {

        return repo.findAll();
    }
    /**
     * Saves payment to database.
     * DB handles:
     * - maksu_ID (auto)
     * - maksupäivä (auto timestamp)
     */
    public void tallenna(Maksu m) {

        if (m == null) {
            throw new IllegalArgumentException("Maksu ei voi olla null.");
        }

        if (m.getLaskuId() <= 0) {
            throw new IllegalArgumentException("Lasku_ID puuttuu tai on virheellinen.");
        }

        if (m.getMaksettuSumma() < 0) {
            throw new IllegalArgumentException("Maksettu summa ei voi olla negatiivinen.");
        }

        repo.tallenna(m);
    }
}