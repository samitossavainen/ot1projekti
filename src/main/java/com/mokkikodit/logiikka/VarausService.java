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
        this.repo = repo;
        this.mokkiRepo = new MokkiRepository();
    }

    public List<Varaus> getAllVaraukset() {
        return repo.haeKaikki();
    }

    public void addVaraus(Varaus v) {
        validate(v);

        tarkistaPaallekaisyys(v);

        double kokonaissumma = laskeKokonaissumma(v);
        v.setKokonaissumma(kokonaissumma);

        repo.tallenna(v);
    }

    public void updateVaraus(Varaus v) {
        validate(v);

        tarkistaPaallekaisyys(v);

        double kokonaissumma = laskeKokonaissumma(v);
        v.setKokonaissumma(kokonaissumma);

        repo.paivita(v);
    }

    public void peruutaVaraus(int id) {
        repo.peruuta(id);
    }

    // business logic

    private double laskeKokonaissumma(Varaus v){
        Mokki mokki = mokkiRepo.findById(v.getMokkiId());

        if (mokki == null){
            throw new IllegalArgumentException(
                    "Mökkiä ei löydy (ID: " + v.getMokkiId() + " )"
            );
        }

        long kestoPaivina = ChronoUnit.DAYS.between(
                v.getAlkuPvm(),
                v.getLoppuPvm()
        );

        if (kestoPaivina <= 0){
            throw new IllegalArgumentException(
                    "Varauksen kesto on virheellinen."
            );
        }
        return kestoPaivina * mokki.getHinta();
    }


    // =========================
    // VALIDATION (DB aligned)
    // =========================
    private void validate(Varaus v) {

        if (v == null) {
            throw new IllegalArgumentException("Varaus ei voi olla null.");
        }

        if (v.getAlkuPvm() == null || v.getLoppuPvm() == null) {
            throw new IllegalArgumentException("Päivämäärät puuttuvat.");
        }

        if (!v.getLoppuPvm().isAfter(v.getAlkuPvm())) {
            throw new IllegalArgumentException("Loppupäivän pitää olla alkupäivän jälkeen.");
        }

        if (v.getMokkiId() <= 0) {
            throw new IllegalArgumentException("Mökki_ID puuttuu tai on virheellinen.");
        }

        if (v.getAsiakasEmail() == null || v.getAsiakasEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Asiakas puuttuu.");
        }

    }

    // estetään tuplavaraukset

    private void tarkistaPaallekaisyys(Varaus uusi) {

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

            // päällekkäin, jos uuden varauksen alku < olemassa olevan loppu tai uuden loppu > olemassa olevan alku

            boolean paallekkainen =
                    uusi.getAlkuPvm().isBefore(v.getLoppuPvm()) && uusi.getLoppuPvm().isAfter(v.getAlkuPvm());

            if (paallekkainen) {
                throw new IllegalArgumentException(
                        "Valitulla mökillä on jo varaus tällä ajanjaksolla."
                );
            }
        }
    }
}