package com.mokkikodit.logiikka;

import com.mokkikodit.mallit.Lasku;
import com.mokkikodit.mallit.Varaus;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class LaskuService {

    private final List<Lasku> laskut = new ArrayList<>();
    private int nextId = 1;

    public List<Lasku> getAllLaskut() {
        return new ArrayList<>(laskut);
    }

    public Lasku createLasku(Varaus varaus) {

        long paivat = ChronoUnit.DAYS.between(
                varaus.getAlkuPvm(),
                varaus.getLoppuPvm()
        );

        // You no longer have Mokki object in Varaus → use ID or fetch later
        double mockHintaPerYo = 100.0; // temporary fallback (important fix point)

        double summa = paivat * mockHintaPerYo;

        Lasku lasku = new Lasku(nextId++, varaus, summa);
        laskut.add(lasku);

        return lasku;
    }

    public void updateLasku(Lasku updated) {
        for (int i = 0; i < laskut.size(); i++) {
            if (laskut.get(i).getId() == updated.getId()) {
                laskut.set(i, updated);
                return;
            }
        }
        throw new IllegalArgumentException("Laskua ei löytynyt ID:llä " + updated.getId());
    }

    public void markAsPaid(int laskuId) {
        for (Lasku l : laskut) {
            if (l.getId() == laskuId) {
                l.setMaksettu(true);
                return;
            }
        }
        throw new IllegalArgumentException("Laskua ei löytynyt ID:llä " + laskuId);
    }

    public void deleteLasku(int id) {
        laskut.removeIf(l -> l.getId() == id);
    }
}