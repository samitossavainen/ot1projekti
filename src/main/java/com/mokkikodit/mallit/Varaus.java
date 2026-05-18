package com.mokkikodit.mallit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Varaus {

    // Varauksen yksilöllinen tunniste
    private int varausId;

    // Varaajan sähköpostiosoite
    private String asiakasEmail;

    // Viittaus mökkiin (FK)
    private int mokkiId;

    // Varauksen alkamispäivä
    private LocalDate alkuPvm;

    // Varauksen päättymispäivä
    private LocalDate loppuPvm;

    // Varauksen tila (esim. aktiivinen, peruttu, maksettu)
    private String tila;

    // Varauksen luontiaika
    private LocalDateTime luontiPvm;

    // Varauksen kokonaishinta
    private double kokonaissumma;

    // ---------- ETYHJÄ KONSTRUKTORI ----------

    /**
     * Tyhjä konstruktori esimerkiksi ORM / tietokantamappausta varten.
     */
    public Varaus() {
    }

    // ---------- KOKO KONSTUKTORI (SELECT-lauseelle) ----------

    /**
     * Konstruktori täydelliselle varaukselle (käytetään tietokannasta ladattaessa).
     */
    public Varaus(int varausId,
                  String asiakasEmail,
                  int mokkiId,
                  LocalDate alkuPvm,
                  LocalDate loppuPvm,
                  String tila,
                  LocalDateTime luontiPvm,
                  double kokonaissumma) {

        // Alustetaan kaikki varauksen kentät
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

    /**
     * Palauttaa varauksen ID:n.
     */
    public int getVarausId() {
        return varausId;
    }

    /**
     * Palauttaa asiakkaan sähköpostin.
     */
    public String getAsiakasEmail() {
        return asiakasEmail;
    }

    /**
     * Palauttaa mökin ID:n.
     */
    public int getMokkiId() {
        return mokkiId;
    }

    /**
     * Palauttaa varauksen alkamispäivän.
     */
    public LocalDate getAlkuPvm() {
        return alkuPvm;
    }

    /**
     * Palauttaa varauksen päättymispäivän.
     */
    public LocalDate getLoppuPvm() {
        return loppuPvm;
    }

    /**
     * Palauttaa varauksen tilan.
     */
    public String getTila() {
        return tila;
    }

    /**
     * Palauttaa varauksen luontiajan.
     */
    public LocalDateTime getLuontiPvm() {
        return luontiPvm;
    }

    /**
     * Palauttaa varauksen kokonaissumman.
     */
    public double getKokonaissumma() {
        return kokonaissumma;
    }

    // ---------- SETTERIT ----------

    /**
     * Asettaa varauksen ID:n.
     */
    public void setVarausId(int varausId) {
        this.varausId = varausId;
    }

    /**
     * Asettaa asiakkaan sähköpostin.
     */
    public void setAsiakasEmail(String asiakasEmail) {
        this.asiakasEmail = asiakasEmail;
    }

    /**
     * Asettaa mökin ID:n.
     */
    public void setMokkiId(int mokkiId) {
        this.mokkiId = mokkiId;
    }

    /**
     * Asettaa varauksen alkamispäivän.
     */
    public void setAlkuPvm(LocalDate alkuPvm) {
        this.alkuPvm = alkuPvm;
    }

    /**
     * Asettaa varauksen päättymispäivän.
     */
    public void setLoppuPvm(LocalDate loppuPvm) {
        this.loppuPvm = loppuPvm;
    }

    /**
     * Asettaa varauksen tilan.
     */
    public void setTila(String tila) {
        this.tila = tila;
    }

    /**
     * Asettaa varauksen kokonaissumman.
     */
    public void setKokonaissumma(double kokonaissumma) {
        this.kokonaissumma = kokonaissumma;
    }

    /**
     * Asettaa varauksen luontipäivän.
     * Huom: tietokanta voi asettaa tämän automaattisesti.
     */
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

        // Jos päivämäärät puuttuvat, palautetaan 0
        if (alkuPvm == null || loppuPvm == null) {
            return 0;
        }

        // Lasketaan päivien välinen erotus ja lisätään 1 (sisältää molemmat päivät)
        long days = ChronoUnit.DAYS.between(alkuPvm, loppuPvm) + 1;

        // Ei sallita negatiivisia arvoja
        return Math.max(days, 0);
    }
}