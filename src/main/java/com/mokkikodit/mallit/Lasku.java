package com.mokkikodit.mallit;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Lasku {

    // Primary key (PK)
    // Laskun yksilöllinen tunniste
    private int laskuId;

    // Foreign key (FK)
    // Viittaus varaukseen
    private int varausId;

    // Asiakkaan sähköposti
    private String sapo;

    // Valinnainen objektisuhde
    // Varausolio laskuun liittyen
    private Varaus varaus;

    // lähetetty / maksettu / myöhässä
    // Laskun nykyinen tila
    private String tila = "lähetetty";

    // Luotu automaattisesti SQLite-ohjelmistolla
    // Tietokannan automaattisesti luoma aikaleima
    private LocalDateTime aikaleima;

    // Laskun eräpäivä
    private LocalDate erapaiva;

    // Laskun kokonaissumma
    private double summa;

    // Päivämäärä jolloin lasku on maksettu
    private LocalDate maksupaiva;

    // Maksettu summa
    private double maksettu;

    // Asiakkaan nimi
    private String asiakasnmi;

    // ---------- KONSTRUKTORIT ----------

    /**
     * Tyhjä konstruktori esimerkiksi serialisointia varten.
     */
    public Lasku() {
    }

    /**
     * Rakentaja uusien laskujen luomiseen.
     * SQLite luo:
     * - lasku_ID
     * - aikaleima
     */
    public Lasku(int varausId,
                 LocalDate erapaiva,
                 double summa) {

        // Tarkistetaan ettei summa ole negatiivinen
        if (summa < 0) {
            throw new IllegalArgumentException(
                    "Summa ei voi olla negatiivinen."
            );
        }

        // Alustetaan laskun tiedot
        this.varausId = varausId;
        this.erapaiva = erapaiva;
        this.summa = summa;
    }

    /**
     * Kokonainen konstruktori tietokannan lataamista varten.
     */
    public Lasku(int laskuId,
                 int varausId,
                 String sapo,
                 Varaus varaus,
                 String tila,
                 LocalDateTime aikaleima,
                 LocalDate erapaiva,
                 double summa,
                 String asiakasnmi) {

        // Alustetaan kaikki laskun tiedot tietokannasta ladattaessa
        this.laskuId = laskuId;
        this.varausId = varausId;
        this.sapo = sapo;
        this.varaus = varaus;
        this.tila = tila;
        this.aikaleima = aikaleima;
        this.erapaiva = erapaiva;
        this.summa = summa;
        this.asiakasnmi = asiakasnmi;
    }

    // ---------- GETTERIT ----------

    /**
     * Palauttaa laskun ID:n.
     */
    public int getLaskuId() {
        return laskuId;
    }

    /**
     * Palauttaa varauksen ID:n.
     */
    public int getVarausId() {
        return varausId;
    }

    /**
     * Palauttaa asiakkaan sähköpostin.
     */
    public String getSapo() {
        return sapo;
    }

    /**
     * Palauttaa laskuun liittyvän varauksen.
     */
    public Varaus getVaraus() {
        return varaus;
    }

    /**
     * Palauttaa laskun tilan.
     */
    public String getTila() {
        return tila;
    }

    /**
     * Palauttaa laskun aikaleiman.
     */
    public LocalDateTime getAikaleima() {
        return aikaleima;
    }

    /**
     * Palauttaa laskun eräpäivän.
     */
    public LocalDate getErapaiva() {
        return erapaiva;
    }

    /**
     * Palauttaa laskun summan.
     */
    public double getSumma() {
        return summa;
    }

    /**
     * Palauttaa asiakkaan nimen.
     */
    public String getAsiakasnmi() {
        return asiakasnmi;
    }

    /**
     * Palauttaa maksupäivän.
     */
    public LocalDate getMaksupaiva() {
        return maksupaiva;
    }

    /**
     * Palauttaa maksetun summan.
     */
    public double getMaksettu() {
        return maksettu;
    }

    // ---------- SETTERIT ----------

    /**
     * Asettaa laskun ID:n.
     */
    public void setLaskuId(int laskuId) {
        this.laskuId = laskuId;
    }

    /**
     * Asettaa varauksen ID:n.
     */
    public void setVarausId(int varausId) {
        this.varausId = varausId;
    }

    /**
     * Asettaa asiakkaan sähköpostin.
     */
    public void setSapo(String sapo) {
        this.sapo = sapo;
    }

    /**
     * Asettaa varausolion.
     */
    public void setVaraus(Varaus varaus) {
        this.varaus = varaus;
    }

    /**
     * Asettaa laskun tilan.
     */
    public void setTila(String tila) {

        // Sallitaan vain tunnetut tilat
        if (!tila.equals("lähetetty")
                && !tila.equals("maksettu")
                && !tila.equals("myöhässä")
                && !tila.equals("peruttu")) {

            throw new IllegalArgumentException(
                    "Virheellinen laskun tila."
            );
        }

        this.tila = tila;
    }

    /**
     * Asettaa laskun aikaleiman.
     */
    public void setAikaleima(LocalDateTime aikaleima) {
        this.aikaleima = aikaleima;
    }

    /**
     * Asettaa laskun eräpäivän.
     */
    public void setErapaiva(LocalDate erapaiva) {
        this.erapaiva = erapaiva;
    }

    /**
     * Asettaa laskun summan.
     */
    public void setSumma(double summa) {

        // Summa ei saa olla negatiivinen
        if (summa < 0) {
            throw new IllegalArgumentException(
                    "Summa ei voi olla negatiivinen."
            );
        }

        this.summa = summa;
    }

    /**
     * Asettaa asiakkaan nimen.
     */
    public void setAsiakasnmi(String asiakasnmi) {
        this.asiakasnmi = asiakasnmi;
    }

    /**
     * Asettaa maksupäivän.
     */
    public void setMaksupaiva(LocalDate maksupaiva) {
        this.maksupaiva = maksupaiva;
    }

    /**
     * Asettaa maksetun summan.
     */
    public void setMaksettu(double maksettu) {
        this.maksettu = maksettu;
    }

    // ---------- APUMENETELMÄT ----------

    /**
     * Tarkistaa onko lasku maksettu.
     */
    public boolean isMaksettu() {
        return "maksettu".equalsIgnoreCase(tila);
    }

    /**
     * Tarkistaa onko lasku myöhässä.
     */
    public boolean isMyohassa() {
        return "myöhässä".equalsIgnoreCase(tila);
    }

    /**
     * Merkitsee laskun maksetuksi.
     */
    public void merkitseMaksetuksi() {
        this.tila = "maksettu";
    }

    /**
     * Merkitsee laskun myöhästyneeksi.
     */
    public void merkitseMyohassa() {
        this.tila = "myöhässä";
    }

    /**
     * Palauttaa laskun tekstimuotoisen esityksen.
     */
    @Override
    public String toString() {

        return "Lasku #" + laskuId
                + " | summa: " + summa
                + " | tila: " + tila;
    }
}