package com.mokkikodit.mallit;

import java.time.LocalDate;

public class Varaus {

    private int id;

    // match DB structure (reservation table uses IDs)
    private int asiakasId;
    private int mokkiId;

    private LocalDate alkuPvm;
    private LocalDate loppuPvm;

    private String tila;

    // empty constructor
    public Varaus() {
    }

    public Varaus(int id, int asiakasId, int mokkiId,
                  LocalDate alkuPvm, LocalDate loppuPvm, String tila) {
        this.id = id;
        this.asiakasId = asiakasId;
        this.mokkiId = mokkiId;
        this.alkuPvm = alkuPvm;
        this.loppuPvm = loppuPvm;
        this.tila = tila;
    }

    // ---------- GETTERS ----------
    public int getId() {
        return id;
    }

    public int getAsiakasId() {
        return asiakasId;
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

    // ---------- SETTERS ----------
    public void setId(int id) {
        this.id = id;
    }

    public void setAsiakasId(int asiakasId) {
        this.asiakasId = asiakasId;
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
}