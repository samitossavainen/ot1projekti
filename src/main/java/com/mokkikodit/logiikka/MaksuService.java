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

    /**
     * Hakee kaikki maksut tietokannasta.
     */
    public List<Maksu> haeKaikki() {

        // Delegoidaan haku suoraan repository:lle
        return repo.findAll();
    }

    /**
     * Tallentaa uuden maksun tietokantaan validoinnin jälkeen.
     */
    public void tallenna(Maksu m) {

        // Tarkistetaan, että maksu-olio ei ole null
        if (m == null) {
            throw new IllegalArgumentException("Maksu ei voi olla null.");
        }

        // Tarkistetaan, että lasku-ID on validi (positiivinen)
        if (m.getLaskuId() <= 0) {
            throw new IllegalArgumentException("Lasku_ID puuttuu tai on virheellinen.");
        }

        // Tarkistetaan, että maksettu summa ei ole negatiivinen
        if (m.getMaksettuSumma() < 0) {
            throw new IllegalArgumentException("Maksettu summa ei voi olla negatiivinen.");
        }

        // Tallennetaan maksu tietokantaan
        repo.tallenna(m);
    }
}