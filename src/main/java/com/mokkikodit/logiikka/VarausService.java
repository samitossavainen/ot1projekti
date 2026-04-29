package com.mokkikodit.logiikka;

import com.mokkikodit.mallit.Varaus;

import java.util.ArrayList;
import java.util.List;

public class VarausService {

    private final List<Varaus> varaukset = new ArrayList<>();

    public List<Varaus> getAllVaraukset() {
        return new ArrayList<>(varaukset);
    }

    public void addVaraus(Varaus varaus) {
        varaukset.add(varaus);
    }

    public void updateVaraus(Varaus updated) {

        for (int i = 0; i < varaukset.size(); i++) {
            if (varaukset.get(i).getId() == updated.getId()) {
                varaukset.set(i, updated);
                return;
            }
        }

        throw new IllegalArgumentException("Varausta ei löytynyt ID:llä " + updated.getId());
    }

    public void deleteVaraus(int id) {
        varaukset.removeIf(v -> v.getId() == id);
    }
}