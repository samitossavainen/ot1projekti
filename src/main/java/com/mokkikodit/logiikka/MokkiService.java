package com.mokkikodit.logiikka;

import com.mokkikodit.mallit.Mokki;
import com.mokkikodit.tietokanta.MokkiRepository;

import java.sql.SQLException;
import java.util.List;

public class MokkiService {

    private final MokkiRepository repository;

    public MokkiService(MokkiRepository repository) {
        this.repository = repository;
    }

    /**
     * Adds a new cottage to the database.
     */
    public void lisaaMokki(String nimi,
                           String osoite,
                           int kapasiteetti,
                           double hinta,
                           String lisatiedot,
                           int vessat,
                           int huoneet)
            throws SQLException {

        if (hinta < 0) {
            throw new IllegalArgumentException(
                    "Hinta ei voi olla negatiivinen."
            );
        }
    }

    /**
     * Returns all cottages.
     */
    public List<Mokki> haeKaikki() {

        return repository.findAll();
    }

    /**
     * Finds cottage by ID.
     */
    public Mokki haeIdlla(int mokkiId) {

        return repository.findById(mokkiId);
    }

    /**
     * Soft delete:
     * tila = 0
     */
    public void deaktivoiMokki(int mokkiId) {

        Mokki mokki = repository.findById(mokkiId);

        if (mokki != null) {

            mokki.setTila(0);

            repository.update(mokki);
        }
    }

    /**
     * Reactivates cottage.
     * tila = 1
     */
    public void aktivoiMokki(int mokkiId) {

        Mokki mokki = repository.findById(mokkiId);

        if (mokki != null) {

            mokki.setTila(1);

            repository.update(mokki);
        }
    }
}