package com.mokkikodit.logiikka;

import com.mokkikodit.mallit.Asiakas;
import com.mokkikodit.tietokanta.AsiakasRepository;

import java.util.List;

public class AsiakasService {

    private final AsiakasRepository repo = new AsiakasRepository();

    public List<Asiakas> haeKaikki() {
        return repo.findAll();
    }

    public Asiakas hae(int id) {
        return repo.findById(id);
    }

    public void lisaa(Asiakas a) {
        repo.save(a);
    }

    public void paivita(Asiakas a) {
        repo.update(a);
    }

    public void poista(int id) {
        repo.delete(id);
    }
}