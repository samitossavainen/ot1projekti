package com.mokkikodit.logiikka;

import com.mokkikodit.mallit.Varaus;
import com.mokkikodit.tietokanta.VarausRepository;

public class VarausService {

    private final VarausRepository repo;

    public VarausService(VarausRepository repo) {
        this.repo = repo;
    }

    // =========================
    // READ
    // =========================
    public java.util.List<Varaus> getAllVaraukset() {
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
    // VALIDATION
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

        if (v.getSapo() == null || v.getSapo().trim().isEmpty()) {
            throw new IllegalArgumentException("Asiakkaan sapo puuttuu.");
        }

        if (v.getMokkiId() <= 0) {
            throw new IllegalArgumentException("Mökki_ID puuttuu tai on virheellinen.");
        }

        if (v.getKokonaissumma() < 0) {
            throw new IllegalArgumentException("Kokonaissumma ei voi olla negatiivinen.");
        }

        if (v.getVarauksenTila() == null) {
            throw new IllegalArgumentException("Varauksen tila puuttuu.");
        }
    }
}