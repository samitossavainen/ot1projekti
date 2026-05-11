package com.mokkikodit.mallit;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Varaus {

    private int varausId;
    private String asiakasEmail;
    private int mokkiId;

    private Date alkuPvm;
    private Date loppuPvm;

    private String tila;
    private Date luontiPvm;

    private double kokonaissumma;

    // ---------- EMPTY CONSTRUCTOR ----------
    public Varaus() {
    }

    // ---------- FULL CONSTRUCTOR (for SELECT) ----------
    public Varaus(int varausId,
                  String asiakasEmail,
                  int mokkiId,
                  Date alkuPvm,
                  Date loppuPvm,
                  String tila,
                  Date luontiPvm,
                  double kokonaissumma) {

        this.varausId = varausId;
        this.asiakasEmail = asiakasEmail;
        this.mokkiId = mokkiId;
        this.alkuPvm = alkuPvm;
        this.loppuPvm = loppuPvm;
        this.tila = tila;
        this.luontiPvm = luontiPvm;
        this.kokonaissumma = kokonaissumma;
    }

    // ---------- GETTERS ----------
    public int getVarausId() {
        return varausId;
    }

    public String getAsiakasEmail() {
        return asiakasEmail;
    }

    public int getMokkiId() {
        return mokkiId;
    }

    public Date getAlkuPvm() {
        return alkuPvm;
    }

    public Date getLoppuPvm() {
        return loppuPvm;
    }

    public String getTila() {
        return tila;
    }

    public Date getLuontiPvm() {
        return luontiPvm;
    }

    public double getKokonaissumma() {
        return kokonaissumma;
    }

    // ---------- SETTERS ----------
    public void setVarausId(int varausId) {
        this.varausId = varausId;
    }

    public void setAsiakasEmail(String asiakasEmail) {
        this.asiakasEmail = asiakasEmail;
    }

    public void setMokkiId(int mokkiId) {
        this.mokkiId = mokkiId;
    }

    public void setAlkuPvm(Date alkuPvm) {
        this.alkuPvm = alkuPvm;
    }

    public void setLoppuPvm(Date loppuPvm) {
        this.loppuPvm = loppuPvm;
    }

    public void setTila(String tila) {
        this.tila = tila;
    }

    public void setKokonaissumma(double kokonaissumma) {
        this.kokonaissumma = kokonaissumma;
    }

    // DB handles this, but setter kept for mapping flexibility
    public void setLuontiPvm(Date luontiPvm) {
        this.luontiPvm = luontiPvm;
    }
}