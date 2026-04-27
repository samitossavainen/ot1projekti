package com.mokkikodit.mallit;

public class Mokki {
    private int id;
    private String nimi;
    private int kapasiteetti;
    private double hintaPerYo;

    public Mokki(int id, String nimi, int kapasiteetti, double hintaPerYo) {
        this.id = id;
        this.nimi = nimi;
        this.kapasiteetti = kapasiteetti;
        this.hintaPerYo = hintaPerYo;
    }

    public int getId() { return id; }
    public String getNimi() { return nimi; }
    public int getKapasiteetti() { return kapasiteetti; }
    public double getHintaPerYo() { return hintaPerYo; }
}
