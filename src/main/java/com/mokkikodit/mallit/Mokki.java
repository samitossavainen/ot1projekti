package com.mokkikodit.mallit;

public class Mokki {

    private int mokkiId;

    // 0 = inactive
    // 1 = active
    private int tila = 1;

    private String nimi;
    private String osoite;
    private int kapasiteetti;
    private double hinta;
    private String lisatiedot;
    private int vessat;
    private int huoneet;

    // ---------- CONSTRUCTORS ----------

    public Mokki() {
    }

    /**
     * Constructor for INSERT operations.
     * ID is generated automatically by SQLite.
     */
    public Mokki(String nimi,
                 String osoite,
                 int kapasiteetti,
                 double hinta,
                 String lisatiedot,
                 int vessat,
                 int huoneet) {

        this.nimi = nimi;
        this.osoite = osoite;
        this.kapasiteetti = kapasiteetti;
        this.hinta = hinta;
        this.lisatiedot = lisatiedot;
        this.vessat = vessat;
        this.huoneet = huoneet;
    }

    /**
     * Constructor for SELECT/database loading.
     */
    public Mokki(int mokkiId,
                 int tila,
                 String nimi,
                 String osoite,
                 int kapasiteetti,
                 double hinta,
                 String lisatiedot,
                 int vessat,
                 int huoneet) {

        this.mokkiId = mokkiId;
        this.tila = tila;
        this.nimi = nimi;
        this.osoite = osoite;
        this.kapasiteetti = kapasiteetti;
        this.hinta = hinta;
        this.lisatiedot = lisatiedot;
        this.vessat = vessat;
        this.huoneet = huoneet;
    }

    // ---------- GETTERS ----------

    public int getMokkiId() {
        return mokkiId;
    }

    public int getTila() {
        return tila;
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

    public double getHinta() {
        return hinta;
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

    public void setTila(int tila) {

        if (tila != 0 && tila != 1) {
            throw new IllegalArgumentException(
                    "Tila voi olla vain 0 tai 1."
            );
        }

        this.tila = tila;
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

    public void setHinta(double hinta) {

        if (hinta < 0) {
            throw new IllegalArgumentException(
                    "Hinta ei voi olla negatiivinen."
            );
        }

        this.hinta = hinta;
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

    // ---------- HELPER METHODS ----------

    public boolean isAktiivinen() {
        return tila == 1;
    }

    public void deaktivoi() {
        this.tila = 0;
    }

    public void aktivoi() {
        this.tila = 1;
    }

    @Override
    public String toString() {
        return nimi + " - " + osoite;
    }
}