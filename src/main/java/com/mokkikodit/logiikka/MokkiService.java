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
     * Hakee kaikki mökit.
     */
    public List<Mokki> haeKaikki() {
        return repository.findAll();
    }

    public void lisaa(Mokki m){
        validate(m);
        repository.save(m);
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
            mokki.deaktivoi();
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
            mokki.aktivoi();
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

        if (m.getVessat() <= 0 || m.getHuoneet() <= 0) {
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

