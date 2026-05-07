package com.mokkikodit.logiikka;

import com.mokkikodit.mallit.Asiakas;
import com.mokkikodit.tietokanta.AsiakasRepository;

import java.util.List;

public class AsiakasService {

    private final AsiakasRepository repo;

    public AsiakasService(AsiakasRepository repo) {
        this.repo = repo;
    }

    public List<Asiakas> haeKaikki() {
        return repo.findAll();
    }

    public Asiakas hae(int id) {
        return repo.findById(id);
    }

    public void lisaa(Asiakas a) {
        validate(a);
        repo.save(a);
    }

    public void paivita(Asiakas a) {
        validate(a);
        repo.update(a);
    }

    public void poista(int id) {
        repo.delete(id);
    }

    // =========================
    // VALIDATION
    // =========================
    private void validate(Asiakas a) {

        if (a == null) {
            throw new IllegalArgumentException("Asiakas ei voi olla null.");
        }

        if (a.getSapo() == null || a.getSapo().trim().isEmpty()) {
            throw new IllegalArgumentException("Sähköposti puuttuu.");
        }

        if (!a.getSapo().contains("@")) {
            throw new IllegalArgumentException("Virheellinen sähköposti.");
        }

        if (a.getNimi() == null || a.getNimi().trim().isEmpty()) {
            throw new IllegalArgumentException("Nimi puuttuu.");
        }

        if (a.getPuhelinnumero() == null || a.getPuhelinnumero().trim().isEmpty()) {
            throw new IllegalArgumentException("Puhelinnumero puuttuu.");
        }

        if (a.getOsoite() == null || a.getOsoite().trim().isEmpty()) {
            throw new IllegalArgumentException("Osoite puuttuu.");
        }
    }
}