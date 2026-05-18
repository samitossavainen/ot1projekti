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
        // Repository injektoidaan konstruktorin kautta
        this.repository = repository;
    }


    /**
     * Hakee kaikki mökit.
     */
    public List<Mokki> haeKaikki() {
        // Delegoidaan haku repositorylle
        return repository.findAll();
    }

    /**
     * Lisää uuden mökin validoinnin jälkeen.
     */
    public void lisaa(Mokki m){

        // Tarkistetaan että mökin tiedot ovat kunnossa
        validate(m);

        // Tallennetaan mökki tietokantaan
        repository.save(m);
    }

    /**
     * Päivittää olemassa olevan mökin tiedot.
     */
    public void paivita(Mokki m) {

        // Tarkistetaan että päivitettävät tiedot ovat validit
        validate(m);

        // Päivitetään mökki tietokantaan
        repository.update(m);
    }

    /**
     * Hakee mökin id:n perusteella.
     */
    public Mokki haeIdlla(int mokkiId) {

        // Haetaan yksittäinen mökki tunnisteen perusteella
        return repository.findById(mokkiId);
    }

    /**
     * Poistaa mökin käytöstä (soft delete).
     * tila = 0
     */
    public void deaktivoiMokki(int mokkiId) {

        // Haetaan mökki tietokannasta
        Mokki mokki = repository.findById(mokkiId);

        if (mokki != null) {

            // Muutetaan mökin tila passiiviseksi
            mokki.deaktivoi();

            // Tallennetaan muutos tietokantaan
            repository.update(mokki);
        }
    }

    /**
     * Aktivoi mökin uudelleen.
     * tila = 1
     */
    public void aktivoiMokki(int mokkiId) {

        // Haetaan mökki tietokannasta
        Mokki mokki = repository.findById(mokkiId);

        if (mokki != null) {

            // Muutetaan mökin tila aktiiviseksi
            mokki.aktivoi();

            // Tallennetaan muutos tietokantaan
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

        // Tarkistetaan ettei mökkiolio ole null
        if (m == null) {
            throw new IllegalArgumentException("Mökki ei voi olla null.");
        }

        // Mökin nimi on pakollinen
        if (isEmpty(m.getNimi())) {
            throw new IllegalArgumentException("Nimi puuttuu.");
        }

        // Osoite on pakollinen
        if (isEmpty(m.getOsoite())) {
            throw new IllegalArgumentException("Osoite puuttuu.");
        }

        // Kapasiteetin tulee olla vähintään 1
        if (m.getKapasiteetti() <= 0) {
            throw new IllegalArgumentException("Kapasiteetin täytyy olla yli 0.");
        }

        // Hinta ei saa olla negatiivinen
        if (m.getHinta() < 0) {
            throw new IllegalArgumentException("Hinta ei voi olla negatiivinen.");
        }

        // Huoneiden ja vessojen määrän tulee olla positiivinen
        if (m.getVessat() <= 0 || m.getHuoneet() <= 0) {
            throw new IllegalArgumentException("Vessojen ja huoneiden määrä ei voi olla negatiivinen.");
        }
    }

    /**
     * Apumetodi tyhjien merkkijonojen tarkistukseen.
     */
    private boolean isEmpty(String value) {

        // Tarkistaa että arvo ei ole null tai pelkkää whitespacea
        return value == null || value.trim().isEmpty();
    }
}