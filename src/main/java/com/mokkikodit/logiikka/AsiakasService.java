package com.mokkikodit.logiikka;

import com.mokkikodit.mallit.Asiakas;
import com.mokkikodit.tietokanta.AsiakasRepository;

import java.util.List;

/**
 * Asiakas-liiketoimintalogiikan palvelukerros.
 * Hoitaa validoinnin ja siirtää tallennustoiminnot tietovarastoon.
 */
public class AsiakasService {

    private final AsiakasRepository repo;

    /**
     * Palvelu vaatii tietovarastoriippuvuuden (riippuvuuksien syöttö).
     *
     * @param repo: Asiakkaan tietojen käsittelykerros
     */
    public AsiakasService(AsiakasRepository repo) {
        this.repo = repo;
    }

    /**
     * Hakee kaikki asiakkaat.
     */
    public List<Asiakas> haeKaikki() {
        return repo.findAll();
    }

    /**
     * Hakee yhden asiakkaan tunnuksen perusteella.
     *
     * @param id asiakastunnus
     * @return Asiakas tai null, jos asiakasta ei löydy
     */
    public Asiakas hae(int id) {
        return repo.findById(id);
    }

    /**
     * Lisää uuden asiakkaan tietojen tarkistuksen jälkeen.
     */
    public void lisaa(Asiakas a) {
        validate(a);
        repo.save(a);
    }

    /**
     * Päivittää olemassa olevan asiakastiedon tarkistuksen jälkeen.
     */
    public void paivita(Asiakas a) {
        validate(a);
        repo.update(a);
    }

    /**
     * Deaktivoi asiakkaan sähköpostin perusteella.
     */

    public void deaktivoi(String sapo) {
        repo.deaktivoi(sapo);
    }

    // =====================================================
    // VALIDOINTI (liiketoimintasäännöt)
    // =====================================================
    private void validate(Asiakas a) {

        if (a == null) {
            throw new IllegalArgumentException("Asiakas ei voi olla null.");
        }

        if (isEmpty(a.getNimi())) {
            throw new IllegalArgumentException("Nimi puuttuu.");
        }

        if (isEmpty(a.getSapo())) {
            throw new IllegalArgumentException("Sähköposti puuttuu.");
        }

        if (!a.getSapo().contains("@")) {
            throw new IllegalArgumentException("Virheellinen sähköposti.");
        }

        if (isEmpty(a.getPuhelinnumero())) {
            throw new IllegalArgumentException("Puhelinnumero puuttuu.");
        }

        if (isEmpty(a.getOsoite())) {
            throw new IllegalArgumentException("Osoite puuttuu.");
        }
    }

    /**
     * Apumenetelmä, jolla vähennetään päällekkäisiä nolla- ja tyhjätarkistuksia.
     */
    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}