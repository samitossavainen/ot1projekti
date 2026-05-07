package com.mokkikodit.mallit;

public class Asiakas {

    private String sapo;
    private String nimi;
    private String puhelinnumero;
    private String osoite;

    public Asiakas() {
    }

    public Asiakas(String sapo,
                   String nimi,
                   String puhelinnumero,
                   String osoite) {

        this.sapo = sapo;
        this.nimi = nimi;
        this.puhelinnumero = puhelinnumero;
        this.osoite = osoite;
    }

    public String getSapo() {
        return sapo;
    }

    public String getNimi() {
        return nimi;
    }

    public String getPuhelinnumero() {
        return puhelinnumero;
    }

    public String getOsoite() {
        return osoite;
    }

    public void setSapo(String sapo) {
        this.sapo = sapo;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public void setPuhelinnumero(String puhelinnumero) {
        this.puhelinnumero = puhelinnumero;
    }

    public void setOsoite(String osoite) {
        this.osoite = osoite;
    }

    public boolean hasValidSapo() {
        return sapo != null
                && !sapo.trim().isEmpty()
                && sapo.contains("@");
    }

    public boolean isValid() {
        return nimi != null && !nimi.trim().isEmpty()
                && sapo != null && !sapo.trim().isEmpty()
                && puhelinnumero != null && !puhelinnumero.trim().isEmpty()
                && osoite != null && !osoite.trim().isEmpty();
    }

    @Override
    public String toString() {
        return nimi + " (" + sapo + ")";
    }
}