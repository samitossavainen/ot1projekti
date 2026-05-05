package com.mokkikodit.mallit;

public class Maksu {

    private int id;
    private int laskuId;
    private double summa;

    public Maksu() {
    }

    public Maksu(int id, int laskuId, double summa) {
        this.id = id;
        this.laskuId = laskuId;
        this.summa = summa;
    }

    // ---------- GETTERS ----------
    public int getId() {
        return id;
    }

    public int getLaskuId() {
        return laskuId;
    }

    public double getSumma() {
        return summa;
    }

    // ---------- SETTERS ----------
    public void setId(int id) {
        this.id = id;
    }

    public void setLaskuId(int laskuId) {
        this.laskuId = laskuId;
    }

    public void setSumma(double summa) {
        this.summa = summa;
    }
}