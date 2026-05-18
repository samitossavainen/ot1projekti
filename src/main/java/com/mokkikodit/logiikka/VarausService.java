package com.mokkikodit.logiikka;

import com.mokkikodit.mallit.Mokki;
import com.mokkikodit.mallit.Varaus;
import com.mokkikodit.tietokanta.MokkiRepository;
import com.mokkikodit.tietokanta.VarausRepository;

import java.time.temporal.ChronoUnit;
import java.util.List;

public class VarausService {

    private final VarausRepository repo;
    private final MokkiRepository mokkiRepo;

    public VarausService(VarausRepository repo) {

        // Varausrepository injektoidaan konstruktorin kautta
        this.repo = repo;

        // Luodaan mökkirepository varausten hintalaskentaa varten
        this.mokkiRepo = new MokkiRepository();
    }

    /**
     * Hakee kaikki varaukset tietokannasta.
     */
    public List<Varaus> getAllVaraukset() {

        // Delegoidaan haku repositorylle
        return repo.haeKaikki();
    }

    /**
     * Lisää uuden varauksen validoinnin jälkeen.
     */
    public void addVaraus(Varaus v) {

        // Tarkistetaan että varauksen tiedot ovat kunnossa
        validate(v);

        // Estetään päällekkäiset varaukset samalle mökille
        tarkistaPaallekaisyys(v);

        // Lasketaan varauksen kokonaissumma
        double kokonaissumma = laskeKokonaissumma(v);

        // Tallennetaan laskettu summa varaukselle
        v.setKokonaissumma(kokonaissumma);

        // Tallennetaan varaus tietokantaan
        repo.tallenna(v);
    }

    /**
     * Hakee asiakkaan kaikki varaukset sähköpostin perusteella.
     */
    public List<Varaus> haeAsiakkaanVaraukset(String email) {

        // Suodatetaan kaikki varaukset asiakkaan sähköpostin perusteella
        return getAllVaraukset().stream()
                .filter(v -> v.getAsiakasEmail() != null
                        && v.getAsiakasEmail().equalsIgnoreCase(email))
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Päivittää olemassa olevan varauksen tiedot.
     */
    public void updateVaraus(Varaus v) {

        // Tarkistetaan että tiedot ovat validit
        validate(v);

        // Tarkistetaan ettei uusi ajankohta aiheuta päällekkäisyyttä
        tarkistaPaallekaisyys(v);

        // Lasketaan uusi kokonaissumma
        double kokonaissumma = laskeKokonaissumma(v);

        // Päivitetään summa varaukselle
        v.setKokonaissumma(kokonaissumma);

        // Tallennetaan päivitetty varaus tietokantaan
        repo.paivita(v);
    }

    /**
     * Peruuttaa varauksen ID:n perusteella.
     */
    public void peruutaVaraus(int id) {

        // Delegoidaan peruutus repositorylle
        repo.peruuta(id);
    }

    // business logic

    /**
     * Laskee varauksen kokonaishinnan mökin hinnan ja varauksen keston perusteella.
     */
    private double laskeKokonaissumma(Varaus v){

        // Haetaan mökin tiedot hinnan laskemista varten
        Mokki mokki = mokkiRepo.findById(v.getMokkiId());

        // Jos mökkiä ei löydy, heitetään poikkeus
        if (mokki == null){
            throw new IllegalArgumentException(
                    "Mökkiä ei löydy (ID: " + v.getMokkiId() + " )"
            );
        }

        // Lasketaan varauksen kesto päivinä
        long kestoPaivina = ChronoUnit.DAYS.between(
                v.getAlkuPvm(),
                v.getLoppuPvm()
        );

        // Keston täytyy olla vähintään yksi päivä
        if (kestoPaivina <= 0){
            throw new IllegalArgumentException(
                    "Varauksen kesto on virheellinen."
            );
        }

        // Kokonaissumma = päivien määrä * mökin hinta
        return kestoPaivina * mokki.getHinta();
    }


    // =========================
    // VALIDATION (DB aligned)
    // =========================

    /**
     * Tarkistaa että varauksen tiedot ovat kelvolliset.
     */
    private void validate(Varaus v) {

        // Varausolio ei saa olla null
        if (v == null) {
            throw new IllegalArgumentException("Varaus ei voi olla null.");
        }

        // Päivämäärien täytyy olla määritelty
        if (v.getAlkuPvm() == null || v.getLoppuPvm() == null) {
            throw new IllegalArgumentException("Päivämäärät puuttuvat.");
        }

        // Loppupäivän täytyy olla alkupäivän jälkeen
        if (!v.getLoppuPvm().isAfter(v.getAlkuPvm())) {
            throw new IllegalArgumentException("Loppupäivän pitää olla alkupäivän jälkeen.");
        }

        // Mökin ID:n täytyy olla positiivinen
        if (v.getMokkiId() <= 0) {
            throw new IllegalArgumentException("Mökki_ID puuttuu tai on virheellinen.");
        }

        // Asiakkaan sähköposti on pakollinen
        if (v.getAsiakasEmail() == null || v.getAsiakasEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Asiakas puuttuu.");
        }

    }

    // estetään tuplavaraukset

    /**
     * Tarkistaa ettei mökille ole jo olemassa päällekkäistä varausta.
     */
    private void tarkistaPaallekaisyys(Varaus uusi) {

        // Haetaan kaikki saman mökin varaukset
        List<Varaus> olemassaOlevat = repo.findByMokkiId(uusi.getMokkiId());

        for (Varaus v : olemassaOlevat) {

            // ohitetaan peruutettu tilan varaukset
            if ("peruutettu".equalsIgnoreCase(v.getTila())) {
                continue;
            }

            // ohitetaan sama varaus
            if (v.getVarausId() == uusi.getVarausId()){
                continue;
            }

            // päällekkäin, jos uuden varauksen alku < olemassa olevan loppu
            // JA uuden loppu > olemassa olevan alku

            boolean paallekkainen =
                    uusi.getAlkuPvm().isBefore(v.getLoppuPvm()) &&
                            uusi.getLoppuPvm().isAfter(v.getAlkuPvm());

            // Jos löytyy päällekkäinen varaus, estetään tallennus
            if (paallekkainen) {
                throw new IllegalArgumentException(
                        "Valitulla mökillä on jo varaus tällä ajanjaksolla."
                );
            }
        }
    }
}