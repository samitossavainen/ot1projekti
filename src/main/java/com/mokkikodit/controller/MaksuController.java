package com.mokkikodit.controller;

import com.mokkikodit.logiikka.MaksuService;
import com.mokkikodit.mallit.Maksu;
import com.mokkikodit.util.DialogUtil;

public class MaksuController {

    private final MaksuService service = new MaksuService();

    public void maksa(Maksu m) {
        try {
            service.tallenna(m);
            DialogUtil.showInfo("Maksu tallennettu!");
        } catch (Exception e) {
            DialogUtil.showError(e.getMessage());
        }
    }
}