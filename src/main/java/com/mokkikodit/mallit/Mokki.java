package com.mokkikodit.mallit;

public class Mokki {

    private int mokkiId;

    // DB: nimi
    private String nimi;

    // DB: osoite (was "sijainti")
    private String osoite;

    // DB: kapasiteetti (was "henkiloMaara")
    private int kapasiteetti;

    // DB: hinta
    private double hintaPerYo;

    // DB: tila (0 = inactive, 1 = active)
    private int tila;

    // DB: lisatiedot
    private String lisatiedot;

    // DB: vessat
    private int vessat;

    // DB: huoneet
    private int huoneet;

    // ---------- CONSTRUCTORS ----------

    public Mokki() {
    }

    // Constructor for INSERT
    public Mokki(String nimi, String osoite, int kapasiteetti,
                 double hintaPerYo, int tila,
                 String lisatiedot, int vessat, int huoneet) {
        this.nimi = nimi;
        this.osoite = osoite;
        this.kapasiteetti = kapasiteetti;
        this.hintaPerYo = hintaPerYo;
        this.tila = tila;
        this.lisatiedot = lisatiedot;
        this.vessat = vessat;
        this.huoneet = huoneet;
    }

    // Full constructor (SELECT)
    public Mokki(int mokkiId, String nimi, String osoite, int kapasiteetti,
                 double hintaPerYo, int tila,
                 String lisatiedot, int vessat, int huoneet) {
        this.mokkiId = mokkiId;
        this.nimi = nimi;
        this.osoite = osoite;
        this.kapasiteetti = kapasiteetti;
        this.hintaPerYo = hintaPerYo;
        this.tila = tila;
        this.lisatiedot = lisatiedot;
        this.vessat = vessat;
        this.huoneet = huoneet;
    }

    // ---------- GETTERS ----------

    public int getMokkiId() {
        return mokkiId;
    }

    public String getNimi() {
        return nimi;
    }

    public String getOsoite() {
        return osoite;
    }

    public int getKapasiteetti() {
        return kapasiteetti;
    }

    public double getHintaPerYo() {
        return hintaPerYo;
    }

    public int getTila() {
        return tila;
    }

    public String getLisatiedot() {
        return lisatiedot;
    }

    public int getVessat() {
        return vessat;
    }

    public int getHuoneet() {
        return huoneet;
    }

    // ---------- SETTERS ----------

    public void setMokkiId(int mokkiId) {
        this.mokkiId = mokkiId;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public void setOsoite(String osoite) {
        this.osoite = osoite;
    }

    public void setKapasiteetti(int kapasiteetti) {
        this.kapasiteetti = kapasiteetti;
    }

    public void setHintaPerYo(double hintaPerYo) {
        this.hintaPerYo = hintaPerYo;
    }

    public void setTila(int tila) {
        this.tila = tila;
    }

    public void setLisatiedot(String lisatiedot) {
        this.lisatiedot = lisatiedot;
    }

    public void setVessat(int vessat) {
        this.vessat = vessat;
    }

    public void setHuoneet(int huoneet) {
        this.huoneet = huoneet;
    }
}