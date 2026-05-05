package com.mokkikodit.logiikka;

import com.mokkikodit.tietokanta.MaksuRepository;
import com.mokkikodit.mallit.Maksu;

public class MaksuService {

    private final MaksuRepository repo = new MaksuRepository();

    // ✔ matches controller call
    public void tallenna(Maksu m) {
        repo.tallenna(m);
    }
}