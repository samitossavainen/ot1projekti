package com.mokkikodit.mallit;

public class Asiakas {

    private int id;
    private String nimi;
    private String email;
    private String puhelin;

    // ✔️ Empty constructor (REQUIRED for repository usage)
    public Asiakas() {
    }

    // ✔️ Full constructor
    public Asiakas(int id, String nimi, String email, String puhelin) {
        this.id = id;
        this.nimi = nimi;
        this.email = email;
        this.puhelin = puhelin;
    }

    // ---------- GETTERS ----------
    public int getId() {
        return id;
    }

    public String getNimi() {
        return nimi;
    }

    public String getEmail() {
        return email;
    }

    public String getPuhelin() {
        return puhelin;
    }

    // ---------- SETTERS ----------
    public void setId(int id) {
        this.id = id;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPuhelin(String puhelin) {
        this.puhelin = puhelin;
    }
}