package com.mokkikodit.logiikka;
import com.mokkikodit.mallit.Asiakas;
import java.util.ArrayList;
import java.util.List;

public class AsiakasService {
    private List<Asiakas> asiakkaat = new ArrayList<>();
    private int nextId = 1;

    public Asiakas lisaaAsiakas(String nimi, String email, String puhelin) {
        Asiakas a = new Asiakas(nextId++, nimi, email, puhelin);
        asiakkaat.add(a);
        return a;
    }

    public List<Asiakas> haeKaikki() {
        return asiakkaat;
    }

    public Asiakas haeIdlla(int id) {
        return asiakkaat.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void poista(int id) {
        asiakkaat.removeIf(a -> a.getId() == id);
    }
}