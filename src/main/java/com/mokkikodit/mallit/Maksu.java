package com.mokkikodit.mallit;

import java.time.LocalDateTime;

public class Maksu {

    private int maksuId;
    private int laskuId;
    private double maksettuSumma;
    private LocalDateTime maksuPaiva;

    public Maksu() {
    }

    // Constructor without date (DB sets it automatically)
    public Maksu(int maksuId, int laskuId, double maksettuSumma) {
        this.maksuId = maksuId;
        this.laskuId = laskuId;
        this.maksettuSumma = maksettuSumma;
    }

    // Full constructor
    public Maksu(int maksuId, int laskuId, double maksettuSumma, LocalDateTime maksuPaiva) {
        this.maksuId = maksuId;
        this.laskuId = laskuId;
        this.maksettuSumma = maksettuSumma;
        this.maksuPaiva = maksuPaiva;
    }

    // ---------- GETTERS ----------
    public int getMaksuId() {
        return maksuId;
    }

    public int getLaskuId() {
        return laskuId;
    }

    public double getMaksettuSumma() {
        return maksettuSumma;
    }

    public LocalDateTime getMaksuPaiva() {
        return maksuPaiva;
    }

    // ---------- SETTERS ----------
    public void setMaksuId(int maksuId) {
        this.maksuId = maksuId;
    }

    public void setLaskuId(int laskuId) {
        this.laskuId = laskuId;
    }

    public void setMaksettuSumma(double maksettuSumma) {
        this.maksettuSumma = maksettuSumma;
    }

    public void setMaksuPaiva(LocalDateTime maksuPaiva) {
        this.maksuPaiva = maksuPaiva;
    }
}