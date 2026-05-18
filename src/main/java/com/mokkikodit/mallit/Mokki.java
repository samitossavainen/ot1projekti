package com.mokkikodit.mallit;

public class Mokki {

    // Mökin yksilöllinen tunniste (primary key)
    private int mokkiId;

    // Mökin tila:
    // 0 = ei aktiivinen
    // 1 = aktiivinen
    private int tila = 1;

    // Mökin nimi
    private String nimi;

    // Mökin osoite
    private String osoite;

    // Mökin maksimikapasiteetti (henkilömäärä)
    private int kapasiteetti;

    // Mökin hinta per aikayksikkö (esim. yö)
    private double hinta;

    // Lisätiedot mökistä
    private String lisatiedot;

    // Vessojen määrä
    private int vessat;

    // Huoneiden määrä
    private int huoneet;

    // ---------- CONSTRUCTORS ----------

    /**
     * Tyhjä konstruktori esimerkiksi tietokantamappaukseen.
     */
    public Mokki() {
    }

    /**
     * Luo mökin kaikilla tiedoilla.
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

        // Alustetaan mökin tiedot
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

    /**
     * Palauttaa mökin ID:n.
     */
    public int getMokkiId() {
        return mokkiId;
    }

    /**
     * Palauttaa mökin tilan.
     */
    public int getTila() {
        return tila;
    }

    /**
     * Palauttaa mökin nimen.
     */
    public String getNimi() {
        return nimi;
    }

    /**
     * Palauttaa mökin osoitteen.
     */
    public String getOsoite() {
        return osoite;
    }

    /**
     * Palauttaa mökin kapasiteetin.
     */
    public int getKapasiteetti() {
        return kapasiteetti;
    }

    /**
     * Palauttaa mökin hinnan.
     */
    public double getHinta() {
        return hinta;
    }

    /**
     * Palauttaa mökin lisätiedot.
     */
    public String getLisatiedot() {
        return lisatiedot;
    }

    /**
     * Palauttaa vessojen määrän.
     */
    public int getVessat() {
        return vessat;
    }

    /**
     * Palauttaa huoneiden määrän.
     */
    public int getHuoneet() {
        return huoneet;
    }

    // ---------- SETTERS ----------

    /**
     * Asettaa mökin ID:n.
     */
    public void setMokkiId(int mokkiId) {
        this.mokkiId = mokkiId;
    }

    /**
     * Asettaa mökin tilan.
     * Sallittuja arvoja: 0 ja 1
     */
    public void setTila(int tila) {

        // Vain 0 tai 1 sallitaan
        if (tila != 0 && tila != 1) {
            throw new IllegalArgumentException(
                    "Tila voi olla vain 0 tai 1."
            );
        }

        this.tila = tila;
    }

    /**
     * Asettaa mökin nimen.
     */
    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    /**
     * Asettaa mökin osoitteen.
     */
    public void setOsoite(String osoite) {
        this.osoite = osoite;
    }

    /**
     * Asettaa mökin kapasiteetin.
     */
    public void setKapasiteetti(int kapasiteetti) {
        this.kapasiteetti = kapasiteetti;
    }

    /**
     * Asettaa mökin hinnan.
     */
    public void setHinta(double hinta) {

        // Hinta ei saa olla negatiivinen
        if (hinta < 0) {
            throw new IllegalArgumentException(
                    "Hinta ei voi olla negatiivinen."
            );
        }

        this.hinta = hinta;
    }

    /**
     * Asettaa mökin lisätiedot.
     */
    public void setLisatiedot(String lisatiedot) {
        this.lisatiedot = lisatiedot;
    }

    /**
     * Asettaa vessojen määrän.
     */
    public void setVessat(int vessat) {
        this.vessat = vessat;
    }

    /**
     * Asettaa huoneiden määrän.
     */
    public void setHuoneet(int huoneet) {
        this.huoneet = huoneet;
    }

    // ---------- HELPER METHODS ----------

    /**
     * Tarkistaa onko mökki aktiivinen.
     */
    public boolean isAktiivinen() {
        return tila == 1;
    }

    /**
     * Deaktivoi mökin.
     */
    public void deaktivoi() {
        this.tila = 0;
    }

    /**
     * Aktivoi mökin.
     */
    public void aktivoi() {
        this.tila = 1;
    }

    /**
     * Palauttaa mökin tekstimuotoisena.
     */
    @Override
    public String toString() {
        return nimi + " - " + osoite;
    }
}