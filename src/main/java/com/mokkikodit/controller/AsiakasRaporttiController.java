package com.mokkikodit.controller;

import com.mokkikodit.logiikka.AsiakasService;
import com.mokkikodit.logiikka.VarausService;
import com.mokkikodit.mallit.Asiakas;
import com.mokkikodit.mallit.Varaus;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;

public class AsiakasRaporttiController {

    @FXML private TableView<Asiakas> tableRaportti;

    @FXML private ComboBox<Asiakas> asiakasComboBox;

    @FXML private Label customerCountLabel;
    @FXML private Label totalBookingsLabel;
    @FXML private Label confirmedLabel;
    @FXML private Label canceledLabel;

    @FXML private TableColumn<Asiakas, String> emailCol;
    @FXML private TableColumn<Asiakas, String> nimiCol;
    @FXML private TableColumn<Asiakas, String> phoneCol;
    @FXML private TableColumn<Asiakas, String> addressCol;
    @FXML private TableColumn<Asiakas, String> varaustenMaaraCol;

    // Palvelut asiakkaiden ja varausten hakemiseen
    private AsiakasService service;
    private VarausService varausService;

    // Kaikki asiakkaat muistissa
    private final ObservableList<Asiakas> asiakkaat =
            FXCollections.observableArrayList();

    // Suodatettu lista raporttinäkymää varten
    private FilteredList<Asiakas> filteredAsiakkaat;

    // Asetetaan asiakaspalvelu ja päivitetään asiakkaat
    public void setAsiakasService(AsiakasService service) {
        this.service = service;
        refreshCustomers();
    }

    // Asetetaan varauspalvelu ja päivitetään suodatus
    public void setVarausService(VarausService varausService) {
        this.varausService = varausService;
        applyFilters();
    }

    @FXML
    public void initialize() {

        // Alustetaan taulukon sarakkeet ja logiikka
        setupTable();

        // Suodatettu lista (aluksi kaikki näkyy)
        filteredAsiakkaat = new FilteredList<>(asiakkaat, a -> true);

        tableRaportti.setItems(filteredAsiakkaat);

        // Ei valintaa taulukossa (raporttinäkymä)
        tableRaportti.setSelectionModel(null);
        tableRaportti.setFocusTraversable(false);

        // Suodattimien ja comboboxin asetukset
        setupFilters();
        setupComboBox();

        // Päivitetään yhteenvedot
        updateSummary();
    }

    // Taulukon sarakkeiden määrittely
    private void setupTable() {

        nimiCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNimi()));

        emailCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getSapo()));

        phoneCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getPuhelinnumero() != null
                                ? data.getValue().getPuhelinnumero()
                                : "-"
                ));

        addressCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getOsoite() != null
                                ? data.getValue().getOsoite()
                                : "-"
                ));

        // Lasketaan asiakkaan varausten määrä
        varaustenMaaraCol.setCellValueFactory(data -> {

            if (varausService == null) return new SimpleStringProperty("0");

            int count = varausService
                    .haeAsiakkaanVaraukset(data.getValue().getSapo())
                    .size();

            return new SimpleStringProperty(String.valueOf(count));
        });
    }

    // Suodattimien asetukset
    private void setupFilters() {

        asiakasComboBox.setOnAction(e -> applyFilters());
    }

    // Suodatetaan valitun asiakkaan perusteella
    private void applyFilters() {

        filteredAsiakkaat.setPredicate(a -> {

            Asiakas selected = asiakasComboBox.getValue();

            // Jos asiakas on valittu, näytetään vain se
            if (selected != null && !selected.equals(a)) {
                return false;
            }

            return true;
        });

        updateSummary();
    }

    // Yhteenvetojen laskenta (varaukset jne.)
    private void updateSummary() {

        if (varausService == null) return;

        // Näytetään asiakkaiden määrä
        customerCountLabel.setText(String.valueOf(filteredAsiakkaat.size()));

        int total = 0;
        int confirmed = 0;
        int canceled = 0;

        LocalDate first = null;
        LocalDate last = null;

        // Käydään kaikki suodatetut asiakkaat läpi
        for (Asiakas a : filteredAsiakkaat) {

            List<Varaus> varaukset =
                    varausService.haeAsiakkaanVaraukset(a.getSapo());

            for (Varaus v : varaukset) {

                total++;

                // Vahvistetut varaukset
                if ("aktiivinen".equalsIgnoreCase(v.getTila())
                        || "maksettu".equalsIgnoreCase(v.getTila())) {
                    confirmed++;
                }

                // Perutut varaukset
                if ("peruutettu".equalsIgnoreCase(v.getTila())) {
                    canceled++;
                }

                LocalDate alku = v.getAlkuPvm();

                // Haetaan aikaisin ja myöhäisin varaus
                if (first == null || alku.isBefore(first)) {
                    first = alku;
                }

                if (last == null || alku.isAfter(last)) {
                    last = alku;
                }
            }
        }

        // Päivitetään UI-yhteenvedot
        totalBookingsLabel.setText(String.valueOf(total));
        confirmedLabel.setText(String.valueOf(confirmed));
        canceledLabel.setText(String.valueOf(canceled));
    }

    // ComboBoxin ulkoasun määrittely
    private void setupComboBox() {

        asiakasComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Asiakas item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setText(null);
                } else if (item == null) {
                    setText("Kaikki");
                } else {
                    setText(item.getNimi() + " (" + item.getSapo() + ")");
                }
            }
        });

        asiakasComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Asiakas item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText("Kaikki");
                } else {
                    setText(item.getNimi() + " (" + item.getSapo() + ")");
                }
            }
        });
    }

    // Ladataan asiakkaat palvelusta ComboBoxia varten
    private void refreshCustomers() {

        if (service == null) return;

        asiakkaat.setAll(service.haeKaikki());

        ObservableList<Asiakas> lista = FXCollections.observableArrayList();

        // "Kaikki"-valinta
        lista.add(null);
        lista.addAll(asiakkaat);

        asiakasComboBox.setItems(lista);

        // Oletuksena kaikki asiakkaat
        asiakasComboBox.setValue(null);

        applyFilters();
    }

    // Päivitetään näkymä
    @FXML
    private void refreshView() {

        asiakasComboBox.setValue(null);
        applyFilters();
    }

    // Palataan raporttinäkymään
    @FXML
    private void goBack() {
        MainController.getInstance().showRaportit();
    }
}