package com.mokkikodit.logiikka;

import com.mokkikodit.mallit.Varaus;
import com.mokkikodit.tietokanta.VarausRepository;

import java.util.List;

public class VarausService {

    private final VarausRepository repo = new VarausRepository();

    // =========================
    // READ
    // =========================
    public List<Varaus> getAllVaraukset() {
        return repo.haeKaikki();
    }

    // =========================
    // CREATE
    // =========================
    public void addVaraus(Varaus v) {
        validate(v);
        repo.tallenna(v);
    }

    // =========================
    // UPDATE
    // =========================
    public void updateVaraus(Varaus v) {
        validate(v);
        repo.paivita(v);
    }

    // =========================
    // DELETE
    // =========================
    public void deleteVaraus(int id) {
        repo.poista(id);
    }

    // =========================
    // VALIDATION (single source of truth)
    // =========================
    private void validate(Varaus v) {

        if (v.getAlkuPvm() == null || v.getLoppuPvm() == null) {
            throw new IllegalArgumentException("Päivämäärät puuttuvat");
        }

        if (!v.getLoppuPvm().isAfter(v.getAlkuPvm())) {
            throw new IllegalArgumentException("Loppupäivän pitää olla alkupäivän jälkeen");
        }
    }
}