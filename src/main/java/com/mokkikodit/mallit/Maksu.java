package com.mokkikodit.mallit;

import java.time.LocalDate;

public class Maksu {

    // Maksun yksilöllinen tunniste
    private int maksuId;

    // Viittaus laskuun, johon maksu liittyy
    private int laskuId;

    // Maksettu rahamäärä
    private double maksettuSumma;

    // Maksun suorituspäivä
    private LocalDate maksuPaiva;

    /**
     * Tyhjä konstruktori esimerkiksi serialisointia varten.
     */
    public Maksu() {
    }

    // Kokonainen konstruktori

    /**
     * Luo uuden maksuolion kaikilla tiedoilla.
     */
    public Maksu(int maksuId,
                 int laskuId,
                 double maksettuSumma,
                 LocalDate maksuPaiva) {

        // Alustetaan maksun tiedot
        this.maksuId = maksuId;
        this.laskuId = laskuId;
        this.maksettuSumma = maksettuSumma;
        this.maksuPaiva = maksuPaiva;
    }

    // ---------- GETTERS ----------

    /**
     * Palauttaa maksun ID:n.
     */
    public int getMaksuId() {
        return maksuId;
    }

    /**
     * Palauttaa laskun ID:n.
     */
    public int getLaskuId() {
        return laskuId;
    }

    /**
     * Palauttaa maksetun summan.
     */
    public double getMaksettuSumma() {
        return maksettuSumma;
    }

    /**
     * Palauttaa maksupäivän.
     */
    public LocalDate getMaksuPaiva() {
        return maksuPaiva;
    }

    // ---------- SETTERS ----------

    /**
     * Asettaa maksun ID:n.
     */
    public void setMaksuId(int maksuId) {
        this.maksuId = maksuId;
    }

    /**
     * Asettaa laskun ID:n.
     */
    public void setLaskuId(int laskuId) {
        this.laskuId = laskuId;
    }

    /**
     * Asettaa maksetun summan.
     */
    public void setMaksettuSumma(double maksettuSumma) {
        this.maksettuSumma = maksettuSumma;
    }

    /**
     * Asettaa maksupäivän.
     */
    public void setMaksuPaiva(LocalDate maksuPaiva) {
        this.maksuPaiva = maksuPaiva;
    }
}