package com.mokkikodit.logiikka;

import com.mokkikodit.mallit.Varaus;
import com.mokkikodit.tietokanta.VarausRepository;

import java.util.List;

public class VarausService {

    private final VarausRepository repo;

    public VarausService(VarausRepository repo) {
        this.repo = repo;
    }

    public List<Varaus> getAllVaraukset() {
        return repo.haeKaikki();
    }

    public void addVaraus(Varaus v) {
        validate(v);
        repo.tallenna(v);
    }

    public void updateVaraus(Varaus v) {
        validate(v);
        repo.paivita(v);
    }

    public void peruutaVaraus(int id) {
        repo.peruuta(id);
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

        if (v.getKokonaissumma() < 0) {
            throw new IllegalArgumentException("Kokonaissumma ei voi olla negatiivinen.");
        }

        if (v.getAsiakasEmail() == null || v.getAsiakasEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Asiakas puuttuu.");
        }

    }
}