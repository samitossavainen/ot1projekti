package com.mokkikodit.mallit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Varaus {

    private int varausId;
    private String asiakasEmail;
    private int mokkiId;

    private LocalDate alkuPvm;
    private LocalDate loppuPvm;

    private String tila;
    private LocalDateTime luontiPvm;

    private double kokonaissumma;

    // ---------- ETYHJÄ KONSTRUKTORI ----------
    public Varaus() {
    }

    // ---------- KOKO KONSTUKTORI (SELECT-lauseelle) ----------
    public Varaus(int varausId,
                  String asiakasEmail,
                  int mokkiId,
                  LocalDate alkuPvm,
                  LocalDate loppuPvm,
                  String tila,
                  LocalDateTime luontiPvm,
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

    // ---------- GETTERIT ----------
    public int getVarausId() {
        return varausId;
    }

    public String getAsiakasEmail() {
        return asiakasEmail;
    }

    public int getMokkiId() {
        return mokkiId;
    }

    public LocalDate getAlkuPvm() {
        return alkuPvm;
    }

    public LocalDate getLoppuPvm() {
        return loppuPvm;
    }

    public String getTila() {
        return tila;
    }

    public LocalDateTime getLuontiPvm() {
        return luontiPvm;
    }

    public double getKokonaissumma() {
        return kokonaissumma;
    }

    // ---------- SETTERIT ----------
    public void setVarausId(int varausId) {
        this.varausId = varausId;
    }

    public void setAsiakasEmail(String asiakasEmail) {
        this.asiakasEmail = asiakasEmail;
    }

    public void setMokkiId(int mokkiId) {
        this.mokkiId = mokkiId;
    }

    public void setAlkuPvm(LocalDate alkuPvm) {
        this.alkuPvm = alkuPvm;
    }

    public void setLoppuPvm(LocalDate loppuPvm) {
        this.loppuPvm = loppuPvm;
    }

    public void setTila(String tila) {
        this.tila = tila;
    }

    public void setKokonaissumma(double kokonaissumma) {
        this.kokonaissumma = kokonaissumma;
    }

    // DB hoitaa tämän, mutta setteri on säilytetty joustavuuden takaamiseksi
    public void setLuontiPvm(LocalDateTime luontiPvm) {
        this.luontiPvm = luontiPvm;
    }

    // =====================================================
    // VUOROKAUDET
    // =====================================================

    /**
     * Laskee varauksen keston päivinä.
     * Sääntö: 1 päivä = 24 tuntia tai saman päivän varaus = 1 päivä.
     */
    public long getVuorokaudet() {

        if (alkuPvm == null || loppuPvm == null) {
            return 0;
        }

        long days = ChronoUnit.DAYS.between(alkuPvm, loppuPvm) + 1;

        return Math.max(days, 0);
    }
}