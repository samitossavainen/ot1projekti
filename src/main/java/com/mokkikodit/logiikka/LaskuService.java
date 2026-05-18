package com.mokkikodit.logiikka;

import com.mokkikodit.mallit.Lasku;
import com.mokkikodit.mallit.Varaus;
import com.mokkikodit.tietokanta.LaskuRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class LaskuService {

    private final LaskuRepository repository;
    private final VarausService varausService;

    public LaskuService(LaskuRepository repository,
                        VarausService varausService) {
        this.repository = repository;
        this.varausService = varausService;
    }

    /**
     * Creates invoice from reservation and saves to DB.
     */
    public Lasku createLasku(Varaus varaus,
                             double hintaPerYo) {

        long paivat = ChronoUnit.DAYS.between(
                varaus.getAlkuPvm(),
                varaus.getLoppuPvm()
        );

        double summa = paivat * hintaPerYo;

        Lasku lasku = new Lasku(
                varaus.getVarausId(),
                LocalDate.now().plusDays(14), // example due date
                summa
        );

        repository.save(lasku);

        return lasku;
    }

    /**
     * Returns all invoices from database.
     */
    public List<Lasku> getAllLaskut() {
        return repository.findAll();
    }

    /**
     * Marks invoice as paid (DB update).
     */
    public void markAsPaid(int laskuId) {

        Lasku lasku = repository.findById(laskuId);

        if (lasku == null) {
            throw new IllegalArgumentException(
                    "Laskua ei löytynyt ID:llä " + laskuId
            );
        }

        lasku.merkitseMaksetuksi();
        repository.update(lasku);

        repository.merkitseMaksetuksi(
                lasku.getLaskuId()
        );

        Varaus varaus = varausService.getAllVaraukset().stream()
                .filter(v -> v.getVarausId() == lasku.getVarausId())
                .findFirst()
                .orElse(null);

        if (varaus != null) {
            varaus.setTila("maksettu");
            varausService.updateVaraus(varaus);
        }
    }

    /**
     * Deletes invoice from database.
     */
    public void deleteLasku(int id) {
        repository.delete(id);
    }
}