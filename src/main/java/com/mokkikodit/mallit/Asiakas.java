package com.mokkikodit.mallit;

public class Asiakas {
    private int id;
    private String nimi;
    private String email;
    private String puhelin;

    public Asiakas(int id, String nimi, String email, String puhelin) {
        this.id = id;
        this.nimi = nimi;
        this.email = email;
        this.puhelin = puhelin;
    }

    // Getterit ja setterit
    public int getId() { return id; }
    public String getNimi() { return nimi; }
    public String getEmail() { return email; }
    public String getPuhelin() { return puhelin; }

    public void setNimi(String nimi) { this.nimi = nimi; }
    public void setEmail(String email) { this.email = email; }
    public void setPuhelin(String puhelin) { this.puhelin = puhelin; }
}