package com.mokkikodit.mallit;

public class Asiakas {

    // Asiakkaan sähköposti toimii pääavaimena (Primary Key)
    private String sapo; // PK

    // Asiakkaan nimi
    private String nimi;

    // Asiakkaan puhelinnumero
    private String puhelinnumero;

    // Asiakkaan osoitetiedot
    private String osoite;

    // Asiakkaan tila:
    // 1 = aktiivinen
    // 0 = deaktivoitu
    private int tila = 1;

    /**
     * Tyhjä konstruktori esimerkiksi serialisointia varten.
     */
    public Asiakas() {
    }

    /**
     * Luo uuden asiakasolion annetuilla tiedoilla.
     */
    public Asiakas(String sapo,
                   String nimi,
                   String puhelinnumero,
                   String osoite) {

        this.sapo = sapo;
        this.nimi = nimi;
        this.puhelinnumero = puhelinnumero;
        this.osoite = osoite;
    }

    // ---------- GETTERS ----------

    /**
     * Palauttaa asiakkaan sähköpostin.
     */
    public String getSapo() {
        return sapo;
    }

    /**
     * Palauttaa asiakkaan nimen.
     */
    public String getNimi() {
        return nimi;
    }

    /**
     * Palauttaa asiakkaan puhelinnumeron.
     */
    public String getPuhelinnumero() {
        return puhelinnumero;
    }

    /**
     * Palauttaa asiakkaan osoitteen.
     */
    public String getOsoite() {
        return osoite;
    }

    /**
     * Palauttaa asiakkaan tilan.
     */
    public int getTila() {
        return tila;
    }

    // ---------- SETTERS ----------

    /**
     * Asettaa asiakkaan sähköpostin.
     */
    public void setSapo(String sapo) {
        this.sapo = sapo;
    }

    /**
     * Asettaa asiakkaan nimen.
     */
    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    /**
     * Asettaa asiakkaan puhelinnumeron.
     */
    public void setPuhelinnumero(String puhelinnumero) {
        this.puhelinnumero = puhelinnumero;
    }

    /**
     * Asettaa asiakkaan osoitteen.
     */
    public void setOsoite(String osoite) {
        this.osoite = osoite;
    }

    /**
     * Asettaa asiakkaan tilan.
     */
    public void setTila(int tila) {
        this.tila = tila;
    }

    // ---------- VALIDATION ----------

    /**
     * Tarkistaa että sähköposti on kelvollinen.
     */
    public boolean hasValidSapo() {

        // Sähköposti ei saa olla null tai tyhjä
        // ja sen täytyy sisältää @-merkki
        return sapo != null
                && !sapo.trim().isEmpty()
                && sapo.contains("@");
    }

    /**
     * Tarkistaa että kaikki pakolliset kentät ovat täytetty.
     */
    public boolean isValid() {

        return nimi != null && !nimi.trim().isEmpty()
                && sapo != null && !sapo.trim().isEmpty()
                && puhelinnumero != null && !puhelinnumero.trim().isEmpty()
                && osoite != null && !osoite.trim().isEmpty();
    }

    /**
     * Deaktivoi asiakkaan.
     * tila = 0
     */
    public void deaktivoiAsiakas() {
        this.tila = 0;
    }

    /**
     * Aktivoi asiakkaan.
     * tila = 1
     */
    public void aktivoiAsiakas() {
        this.tila = 1;
    }

    /**
     * Palauttaa asiakkaan merkkijonoesityksen.
     */
    @Override
    public String toString() {

        // Näytetään nimi ja sähköposti
        return nimi + " (" + sapo + ")";
    }
}