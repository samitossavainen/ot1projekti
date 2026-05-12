package com.mokkikodit.mallit;

import java.time.LocalDate;

public class Maksu {

    private int maksuId;
    private int laskuId;
    private double maksettuSumma;
    private LocalDate maksuPaiva;

    public Maksu() {
    }

    // Full constructor
    public Maksu(int maksuId,
                 int laskuId,
                 double maksettuSumma,
                 LocalDate maksuPaiva) {

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

    public LocalDate getMaksuPaiva() {
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

    public void setMaksuPaiva(LocalDate maksuPaiva) {
        this.maksuPaiva = maksuPaiva;
    }
}
