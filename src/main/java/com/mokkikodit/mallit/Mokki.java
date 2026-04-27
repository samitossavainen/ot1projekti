package com.mokkikodit.mallit;

public class Mokki {

    private int id;
    private String nimi;
    private int henkiloMaara;
    private double hinta;

    public Mokki(int id, String nimi, int henkiloMaara, double hinta) {
        this.id = id;
        this.nimi = nimi;
        this.henkiloMaara = henkiloMaara;
        this.hinta = hinta;
    }

    public int getId() {
        return id;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public int getHenkiloMaara() {
        return henkiloMaara;
    }

    public double getHintaPerYo() {
        return hinta;
    }

    public double getHinta() {
        return hinta;
    }
}