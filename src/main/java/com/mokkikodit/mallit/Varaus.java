package com.mokkikodit.mallit;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Varaus {

    private int varausId;

    // DB column: sapo (email)
    private String asiakasEmail;

    // DB column: mokki_ID
    private int mokkiId;

    // DB columns: alkamispvm, loppumispvm (stored as TEXT)
    private LocalDate alkuPvm;
    private LocalDate loppuPvm;

    // DB column: varauksen_tila
    private String tila;

    // DB column: luontipvm (TEXT datetime)
    private LocalDateTime luontiPvm;

    // DB column: kokonaissumma
    private double kokonaissumma;

    // ---------- CONSTRUCTORS ----------

    public Varaus() {
    }

    // constructor for INSERT (no auto fields)
    public Varaus(String asiakasEmail, int mokkiId,
                  LocalDate alkuPvm, LocalDate loppuPvm,
                  String tila, double kokonaissumma) {
        this.asiakasEmail = asiakasEmail;
        this.mokkiId = mokkiId;
        this.alkuPvm = alkuPvm;
        this.loppuPvm = loppuPvm;
        this.kokonaissumma = kokonaissumma;
    }

    // full constructor (SELECT)
    public Varaus(int varausId, String asiakasEmail, int mokkiId,
                  LocalDate alkuPvm, LocalDate loppuPvm,
                  String tila, LocalDateTime luontiPvm,
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

    public void setLuontiPvm(LocalDate localDate) {
    }

    // no setter for luontiPvm (DB handles it)
}