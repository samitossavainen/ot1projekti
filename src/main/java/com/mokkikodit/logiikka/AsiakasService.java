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
        // Delegoi haun suoraan repositorylle
        return repo.findAll();
    }

    /**
     * Hakee yhden asiakkaan tunnuksen perusteella.
     *
     * @param sapo asiakastunnus
     * @return Asiakas tai null, jos asiakasta ei löydy
     */
    public Asiakas hae(String sapo) {
        // Hakee asiakkaan sähköpostin perusteella
        return repo.findBySapo(sapo);
    }

    /**
     * Lisää uuden asiakkaan tietojen tarkistuksen jälkeen.
     */
    public void lisaa(Asiakas a) {
        // Tarkistetaan että pakolliset tiedot ovat kunnossa
        validate(a);

        // Tarkistetaan löytyykö asiakas jo ennestään
        boolean valid = sanitycheck(a);

        if (valid) {
            // Uusi asiakas -> tallennetaan
            repo.save(a);
        }
        else {
            // Asiakas löytyy jo -> päivitetään tiedot (esim. aktivointi tai muutos)
            repo.update(a); // asiakas aktivoitaan, mutta uusilla tiedoilla.
        }

    }

    /**
     * Päivittää olemassa olevan asiakastiedon tarkistuksen jälkeen.
     */
    public void paivita(Asiakas a) {
        // Validointi ennen päivitystä
        validate(a);

        // Päivitetään asiakas tietokantaan
        repo.update(a);
    }

    /**
     * Deaktivoi asiakkaan (asettaa tilan ei-aktiiviseksi).
     */
    public void deaktivoiAsiakas(String sapo) {
        // Haetaan asiakas tunnisteella
        Asiakas asiakas = repo.findBySapo(sapo);

        if (asiakas != null) {
            // Muutetaan asiakastila passiiviseksi
            asiakas.deaktivoiAsiakas();     // tila = 0
            repo.update(asiakas);
        }
    }

    /**
     * Aktivoi asiakkaan (asettaa tilan aktiiviseksi).
     */
    public void aktivoiAsiakas(String sapo) {
        // Haetaan asiakas tunnisteella
        Asiakas asiakas = repo.findBySapo(sapo);

        if (asiakas != null) {
            // Muutetaan asiakastila aktiiviseksi
            asiakas.aktivoiAsiakas();       // tila = 1
            repo.update(asiakas);
        }
    }

    /**
     * Poistaa asiakkaan tunnuksen perusteella.
     */
    public void poista(String sapo) {
        // Poistetaan asiakas tietokannasta
        repo.delete(sapo);
    }


    // =====================================================
    // VALIDOINTI (liiketoimintasäännöt)
    // =====================================================
    private void validate(Asiakas a) {

        // Tarkistetaan että olio ei ole null
        if (a == null) {
            throw new IllegalArgumentException("Asiakas ei voi olla null.");
        }

        // Nimi on pakollinen
        if (isEmpty(a.getNimi())) {
            throw new IllegalArgumentException("Nimi puuttuu.");
        }

        // Sähköposti on pakollinen
        if (isEmpty(a.getSapo())) {
            throw new IllegalArgumentException("Sähköposti puuttuu.");
        }

        // Perusmuototarkistus sähköpostille
        if (!a.getSapo().contains("@")) {
            throw new IllegalArgumentException("Virheellinen sähköposti.");
        }

        // Puhelinnumero on pakollinen
        if (isEmpty(a.getPuhelinnumero())) {
            throw new IllegalArgumentException("Puhelinnumero puuttuu.");
        }

        // Osoite on pakollinen
        if (isEmpty(a.getOsoite())) {
            throw new IllegalArgumentException("Osoite puuttuu.");
        }

    }

    /**
     * Tarkistaa löytyykö asiakas jo tietokannasta sähköpostin perusteella.
     */
    private boolean sanitycheck(Asiakas a) {
        // Haetaan mahdollinen olemassa oleva asiakas
        Asiakas b = repo.findBySapo(a.getSapo());

        // True = ei löydy vielä -> uusi asiakas
        return b == null;
    }

    /**
     * Apumenetelmä, jolla vähennetään päällekkäisiä nolla- ja tyhjätarkistuksia.
     */
    private boolean isEmpty(String value) {
        // Tarkistaa nullin ja tyhjän/whitespace-merkkijonon
        return value == null || value.trim().isEmpty();
    }
}