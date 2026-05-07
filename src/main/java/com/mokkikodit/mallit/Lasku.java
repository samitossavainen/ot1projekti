package com.mokkikodit.mallit;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Lasku {

    // Primary key
    private int laskuId;

    // Foreign key
    private int varausId;

    // Optional object relation
    private Varaus varaus;

    // lähetetty / maksettu / myöhässä
    private String tila = "lähetetty";

    // Generated automatically by SQLite
    private LocalDateTime aikaleima;

    // Invoice due date
    private LocalDate erapaiva;

    // Invoice total
    private double summa;

    // ---------- CONSTRUCTORS ----------

    public Lasku() {
    }

    /**
     * Constructor for creating new invoices.
     * SQLite generates:
     * - lasku_ID
     * - aikaleima
     */
    public Lasku(int varausId,
                 LocalDate erapaiva,
                 double summa) {

        if (summa < 0) {
            throw new IllegalArgumentException(
                    "Summa ei voi olla negatiivinen."
            );
        }

        this.varausId = varausId;
        this.erapaiva = erapaiva;
        this.summa = summa;
    }

    /**
     * Full constructor for database loading.
     */
    public Lasku(int laskuId,
                 int varausId,
                 Varaus varaus,
                 String tila,
                 LocalDateTime aikaleima,
                 LocalDate erapaiva,
                 double summa) {

        this.laskuId = laskuId;
        this.varausId = varausId;
        this.varaus = varaus;
        this.tila = tila;
        this.aikaleima = aikaleima;
        this.erapaiva = erapaiva;
        this.summa = summa;
    }

    // ---------- GETTERS ----------

    public int getLaskuId() {
        return laskuId;
    }

    public int getVarausId() {
        return varausId;
    }

    public Varaus getVaraus() {
        return varaus;
    }

    public String getTila() {
        return tila;
    }

    public LocalDateTime getAikaleima() {
        return aikaleima;
    }

    public LocalDate getErapaiva() {
        return erapaiva;
    }

    public double getSumma() {
        return summa;
    }

    // ---------- SETTERS ----------

    public void setLaskuId(int laskuId) {
        this.laskuId = laskuId;
    }

    public void setVarausId(int varausId) {
        this.varausId = varausId;
    }

    public void setVaraus(Varaus varaus) {
        this.varaus = varaus;
    }

    public void setTila(String tila) {

        if (!tila.equals("lähetetty")
                && !tila.equals("maksettu")
                && !tila.equals("myöhässä")) {

            throw new IllegalArgumentException(
                    "Virheellinen laskun tila."
            );
        }

        this.tila = tila;
    }

    public void setAikaleima(LocalDateTime aikaleima) {
        this.aikaleima = aikaleima;
    }

    public void setErapaiva(LocalDate erapaiva) {
        this.erapaiva = erapaiva;
    }

    public void setSumma(double summa) {

        if (summa < 0) {
            throw new IllegalArgumentException(
                    "Summa ei voi olla negatiivinen."
            );
        }

        this.summa = summa;
    }

    // ---------- HELPER METHODS ----------

    public boolean isMaksettu() {
        return "maksettu".equalsIgnoreCase(tila);
    }

    public boolean isMyohassa() {
        return "myöhässä".equalsIgnoreCase(tila);
    }

    public void merkitseMaksetuksi() {
        this.tila = "maksettu";
    }

    public void merkitseMyohassa() {
        this.tila = "myöhässä";
    }

    @Override
    public String toString() {

        return "Lasku #" + laskuId
                + " | summa: " + summa
                + " | tila: " + tila;
    }
}