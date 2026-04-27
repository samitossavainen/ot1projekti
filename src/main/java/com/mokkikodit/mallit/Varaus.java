package com.mokkikodit.mallit;

import java.time.LocalDate;

public class Varaus {
    private int id;
    private Asiakas asiakas;
    private Mokki mokki;
    private LocalDate alkuPvm;
    private LocalDate loppuPvm;

    public Varaus(int id, Asiakas asiakas, Mokki mokki,
                  LocalDate alkuPvm, LocalDate loppuPvm) {
        this.id = id;
        this.asiakas = asiakas;
        this.mokki = mokki;
        this.alkuPvm = alkuPvm;
        this.loppuPvm = loppuPvm;
    }

    public int getId() { return id; }
    public Asiakas getAsiakas() { return asiakas; }
    public Mokki getMokki() { return mokki; }
    public LocalDate getAlkuPvm() { return alkuPvm; }
    public LocalDate getLoppuPvm() { return loppuPvm; }

    public long getPaivienMaara() {
        return java.time.temporal.ChronoUnit.DAYS.between(alkuPvm, loppuPvm);
    }
}
