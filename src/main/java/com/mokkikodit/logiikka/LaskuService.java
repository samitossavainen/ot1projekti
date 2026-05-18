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
     * Luo laskun varauksesta ja tallentaa sen tietokantaan.
     */
    public Lasku createLasku(Varaus varaus,
                             double hintaPerYo) {

        // Lasketaan varauksen kesto päivinä
        long paivat = ChronoUnit.DAYS.between(
                varaus.getAlkuPvm(),
                varaus.getLoppuPvm()
        );

        // Lasketaan laskun loppusumma: päivät * hinta per yö
        double summa = paivat * hintaPerYo;

        // Luodaan uusi lasku-olio
        Lasku lasku = new Lasku(
                varaus.getVarausId(),
                LocalDate.now().plusDays(14), // eräpäivä 14 päivän päähän
                summa
        );

        // Tallennetaan lasku tietokantaan
        repository.save(lasku);

        return lasku;
    }

    /**
     * Palauttaa kaikki laskut tietokannasta.
     */
    public List<Lasku> getAllLaskut() {
        // Haetaan kaikki laskut repositorysta
        return repository.findAll();
    }

    /**
     * Merkitsee laskun maksetuksi (tietokannan päivitys).
     */
    public void markAsPaid(int laskuId) {

        // Haetaan lasku tietokannasta ID:n perusteella
        Lasku lasku = repository.findById(laskuId);

        // Jos laskua ei löydy, heitetään poikkeus
        if (lasku == null) {
            throw new IllegalArgumentException(
                    "Laskua ei löytynyt ID:llä " + laskuId
            );
        }

        // Päivitetään lasku maksetuksi domain-oliossa
        lasku.merkitseMaksetuksi();

        // Päivitetään lasku tietokantaan
        repository.update(lasku);

        // Varmistetaan erillinen päivitys maksutilaan repositoryssa (redundanssi / synkronointi)
        repository.merkitseMaksetuksi(
                lasku.getLaskuId()
        );

        // Haetaan varaus, joka liittyy laskuun
        Varaus varaus = varausService.getAllVaraukset().stream()
                .filter(v -> v.getVarausId() == lasku.getVarausId())
                .findFirst()
                .orElse(null);

        // Jos varaus löytyy, päivitetään sen tila maksetuksi
        if (varaus != null) {
            varaus.setTila("maksettu");
            varausService.updateVaraus(varaus);
        }
    }

    /**
     * Poistaa laskun tietokannasta.
     */
    public void deleteLasku(int id) {
        // Poistetaan lasku tietokannasta ID:n perusteella
        repository.delete(id);
    }
}