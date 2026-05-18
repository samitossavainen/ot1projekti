package com.mokkikodit.controller;

import com.mokkikodit.logiikka.MaksuService;
import com.mokkikodit.mallit.Maksu;
import com.mokkikodit.util.DialogUtil;

public class MaksuController {

    // Maksuihin liittyvä liiketoimintalogiikka (service)
    private final MaksuService service;

    // Konstruktorissa injektoidaan MaksuService riippuvuus
    public MaksuController(MaksuService service) {
        this.service = service;
    }

    /**
     * Suorittaa maksun tallennuksen ja näyttää käyttäjälle viestin onnistumisesta tai virheestä.
     */
    public void maksa(Maksu m) {
        try {
            // Tallennetaan maksu palvelun kautta
            service.tallenna(m);

            // Näytetään onnistumisilmoitus
            DialogUtil.showInfo("Maksu tallennettu!");
        } catch (Exception e) {
            // Näytetään virheilmoitus käyttäjälle
            DialogUtil.showError(e.getMessage());
        }
    }
}