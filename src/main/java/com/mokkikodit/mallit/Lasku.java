package com.mokkikodit.mallit;

public class Lasku {

    private int id;
    private Varaus varaus;
    private double summa;
    private boolean maksettu;

    public Lasku(int id, Varaus varaus, double summa) {
        this.id = id;
        this.varaus = varaus;
        this.summa = summa;
        this.maksettu = false;
    }

    public int getId() { return id; }

    public Varaus getVaraus() { return varaus; }

    public double getSumma() { return summa; }

    public void setSumma(double summa) { this.summa = summa; }

    public boolean isMaksettu() { return maksettu; }

    public void setMaksettu(boolean maksettu) {
        this.maksettu = maksettu;
    }
}