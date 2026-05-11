package com.mokkikodit.logiikka;

import com.mokkikodit.mallit.Mokki;
import com.mokkikodit.tietokanta.MokkiRepository;

import java.util.List;

/**
 * Mokki-liiketoimintalogiikan palvelukerros.
 * Hoitaa validoinnin ja siirtää tietokantatoiminnot repositorylle.
 */
public class MokkiService {

    private final MokkiRepository repository;

    public MokkiService(MokkiRepository repository) {
        this.repository = repository;
    }

    /**
     * Lisää uuden mökin tietokantaan validoinnin jälkeen.
     */
    public void lisaaMokki(String nimi,
                           String osoite,
                           int kapasiteetti,
                           double hinta,
                           String lisatiedot,
                           int vessat,
                           int huoneet) {

        Mokki mokki = new Mokki();

        mokki.setNimi(nimi);
        mokki.setOsoite(osoite);
        mokki.setKapasiteetti(kapasiteetti);
        mokki.setHinta(hinta);
        mokki.setLisatiedot(lisatiedot);
        mokki.setVessat(vessat);
        mokki.setHuoneet(huoneet);
        mokki.setTila(1); // oletuksena aktiivinen

        validate(mokki);

        repository.save(mokki);
    }

    /**
     * Hakee kaikki mökit.
     */
    public List<Mokki> haeKaikki() {
        return repository.findAll();
    }

    public void paivita(Mokki m) {
        validate(m);
        repository.update(m);
    }

    /**
     * Hakee mökin id:n perusteella.
     */
    public Mokki haeIdlla(int mokkiId) {
        return repository.findById(mokkiId);
    }

    /**
     * Poistaa mökin käytöstä (soft delete).
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
     * Aktivoi mökin uudelleen.
     * tila = 1
     */
    public void aktivoiMokki(int mokkiId) {
        Mokki mokki = repository.findById(mokkiId);

        if (mokki != null) {
            mokki.setTila(1);
            repository.update(mokki);
        }
    }

    // =====================================================
    // VALIDOINNIT (liiketoimintasäännöt)
    // =====================================================

    /**
     * Tarkistaa mökin tiedot ennen tallennusta.
     */
    private void validate(Mokki m) {

        if (m == null) {
            throw new IllegalArgumentException("Mökki ei voi olla null.");
        }

        if (isEmpty(m.getNimi())) {
            throw new IllegalArgumentException("Nimi puuttuu.");
        }

        if (isEmpty(m.getOsoite())) {
            throw new IllegalArgumentException("Osoite puuttuu.");
        }

        if (m.getKapasiteetti() <= 0) {
            throw new IllegalArgumentException("Kapasiteetin täytyy olla yli 0.");
        }

        if (m.getHinta() < 0) {
            throw new IllegalArgumentException("Hinta ei voi olla negatiivinen.");
        }

        if (m.getVessat() < 0 || m.getHuoneet() < 0) {
            throw new IllegalArgumentException("Vessojen ja huoneiden määrä ei voi olla negatiivinen.");
        }
    }

    /**
     * Apumetodi tyhjien merkkijonojen tarkistukseen.
     */
    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}

