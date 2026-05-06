package com.mokkikodit.mallit;

public class Mokki {

    private int id;
    private String nimi;
    private String sijainti;
    private int henkiloMaara;
    private double hinta;

    public Mokki() {
    }

    public Mokki(int id, String nimi, String sijainti, int henkiloMaara, double hinta) {
        this.id = id;
        this.nimi = nimi;
        this.sijainti = sijainti;
        this.henkiloMaara = henkiloMaara;
        this.hinta = hinta;
    }

    // ---------- GETTERS ----------
    public int getId() {
        return id;
    }

    public String getNimi() {
        return nimi;
    }

    public String getSijainti() {
        return sijainti;
    }

    public int getHenkiloMaara() {
        return henkiloMaara;
    }

    public double getHinta() {
        return hinta;
    }

    public double getHintaPerYo() {
        return hinta;
    }

    // ---------- SETTERS ----------
    public void setId(int id) {
        this.id = id;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public void setSijainti(String sijainti) {
        this.sijainti = sijainti;
    }

    public void setHenkiloMaara(int henkiloMaara) {
        this.henkiloMaara = henkiloMaara;
    }

    public void setHinta(double hinta) {
        this.hinta = hinta;
    }
}