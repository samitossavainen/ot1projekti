package com.mokkikodit.mallit;

import java.time.LocalDate;

public class Varaus {

    private int id;
    private Asiakas asiakas;
    private Mokki mokki;

    private LocalDate alku;
    private LocalDate loppu;
    private String tila;

    public Varaus(int id, Asiakas asiakas, Mokki mokki,
                  LocalDate alku, LocalDate loppu, String tila) {
        this.id = id;
        this.asiakas = asiakas;
        this.mokki = mokki;
        this.alku = alku;
        this.loppu = loppu;
        this.tila = tila;
    }

    public int getId() { return id; }

    public Asiakas getAsiakas() { return asiakas; }

    public Mokki getMokki() { return mokki; }

    public LocalDate getAlku() { return alku; }
    public void setAlku(LocalDate alku) { this.alku = alku; }

    public LocalDate getLoppu() { return loppu; }
    public void setLoppu(LocalDate loppu) { this.loppu = loppu; }

    public String getTila() { return tila; }
    public void setTila(String tila) { this.tila = tila; }
}