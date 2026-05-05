package com.mokkikodit.logiikka;

import com.mokkikodit.DAO.VarausRepository;
import com.mokkikodit.mallit.Varaus;

import java.util.List;

public class VarausService {

    private final VarausRepository repo = new VarausRepository();

    public List<Varaus> getAllVaraukset() {
        return repo.haeKaikki();
    }

    public void addVaraus(Varaus uusi) {
        validate(uusi);
        repo.tallenna(uusi);
    }

    public void updateVaraus(Varaus updated) {
        validate(updated);
        repo.paivita(updated);
    }

    public void deleteVaraus(int id) {
        repo.poista(id);
    }

    // basic validation (safe for DB version)
    private void validate(Varaus v) {

        if (v.getAlkuPvm() == null || v.getLoppuPvm() == null) {
            throw new IllegalArgumentException("Päivämäärät puuttuvat");
        }

        if (!v.getLoppuPvm().isAfter(v.getAlkuPvm())) {
            throw new IllegalArgumentException("Loppupäivän pitää olla jälkeen alkupäivän");
        }
    }
}