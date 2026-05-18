package com.mokkikodit.controller;

import com.mokkikodit.logiikka.LaskuService;
import com.mokkikodit.logiikka.MokkiService;
import com.mokkikodit.mallit.Lasku;
import com.mokkikodit.mallit.Mokki;
import com.mokkikodit.util.DialogUtil;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDate;

public class LaskutController {

    @FXML private TableView<Lasku> tableLaskut;
    @FXML private Label laskuIdLabel;
    @FXML private Label tilaLabelHeader;
    @FXML private Label summaryLabel;
    @FXML private ComboBox<String> statusFilterComboBox;
    @FXML private TextField searchField;

    @FXML private Label varausIdLabel;
    @FXML private Label asiakasLabel;
    @FXML private Label laskuLuotuLabel;
    @FXML private Label eraLabel;
    @FXML private Label tilaLabelDetail;
    @FXML private Label summaLabel;
    @FXML private Button markAsPaidButton;

    @FXML private Label maksupaivaLabel;
    @FXML private Label maksettuSummaLabel;

    @FXML private TableColumn<Lasku, Integer> laskuCol;
    @FXML private TableColumn<Lasku, Integer> varausCol;
    @FXML private TableColumn<Lasku, String> asiakasnmiCol;
    @FXML private TableColumn<Lasku, String> asiakasCol;
    @FXML private TableColumn<Lasku, String> erapvmCol;
    @FXML private TableColumn<Lasku, Double> summaCol;
    @FXML private TableColumn<Lasku, String> tilaCol;

    @FXML private Label statusLabel;

    // Palvelu laskujen käsittelyyn (haut, päivitykset)
    private LaskuService service;

    // Tällä hetkellä valittu lasku
    private Lasku selectedLasku;

    // Kaikki laskut muistissa
    private final ObservableList<Lasku> laskut =
            FXCollections.observableArrayList();

    // Suodatettu lista (haku + status)
    private FilteredList<Lasku> filteredLasku;

    // Asetetaan palvelu ja ladataan data
    public void setLaskuService(LaskuService service){
        this.service = service;
        refreshTable();
    }

    @FXML
    public void initialize() {

        // Taulukon sarakkeiden sitominen Lasku-olioihin

        laskuCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        data.getValue().getLaskuId()
                ).asObject());

        varausCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        data.getValue().getVarausId()
                ).asObject());

        asiakasnmiCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getAsiakasnmi()
                ));

        asiakasCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getSapo()
                ));

        erapvmCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getErapaiva().toString()
                ));

        summaCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleDoubleProperty(
                        data.getValue().getSumma()
                ).asObject());

        // Näytetään tilan käyttäjäystävällinen muoto
        tilaCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        laskunTilanEsitys(data.getValue().getTila())
                ));

        // Status-viesti aluksi piilossa
        statusLabel.setVisible(false);
        statusLabel.setManaged(false);

        // Maksunappi piilotetaan oletuksena
        markAsPaidButton.setVisible(false);
        markAsPaidButton.setManaged(false);

        // Yksi valinta kerrallaan
        tableLaskut.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Valinnan kuuntelu
        tableLaskut.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {

                    selectedLasku = newSelection;

                    // Täytetään näkymän tiedot
                    if (newSelection != null) {
                        populateFields(newSelection);
                    }
                });

        // Suodatettu lista (haku + status)
        filteredLasku = new FilteredList<>(laskut);

        SortedList<Lasku> sortedLasku = new SortedList<>(filteredLasku);
        sortedLasku.comparatorProperty().bind(tableLaskut.comparatorProperty());
        tableLaskut.setItems(sortedLasku);

        // Status-filtterin vaihtoehdot
        statusFilterComboBox.setItems(
                FXCollections.observableArrayList(
                        "Kaikki",
                        "Lähetetty",
                        "Maksettu",
                        "Myöhässä",
                        "Peruttu"
                )
        );
        statusFilterComboBox.setValue("Kaikki");

        // Haku ja status-filtteri
        searchField.textProperty().addListener((obs, oldValue, newValue) -> {
            applyFilters();
        });

        statusFilterComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            applyFilters();
        });

        // Tilasarakkeen värikorostus
        tilaCol.setCellFactory(col -> new TableCell<Lasku, String>() {

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                // Poistetaan vanhat tyylit
                getStyleClass().removeAll("cell-sent", "cell-laskuPaid", "cell-late", "cell-inactive");

                if (empty || item == null) {
                    setText(null);
                    return;
                }

                setText(item);

                // Värikoodaus tilan mukaan
                if ("Lähetetty".equalsIgnoreCase(item)) {
                    getStyleClass().add("cell-sent");

                } else if ("Maksettu".equalsIgnoreCase(item)) {
                    getStyleClass().add("cell-laskuPaid");

                } else if ("Myöhässä".equalsIgnoreCase(item)) {
                    getStyleClass().add("cell-late");

                } else if ("Peruttu".equalsIgnoreCase(item)) {
                    getStyleClass().add("cell-inactive");
                }
            }
        });

        applyFilters();
    }

    // Näkymän päivitys
    @FXML
    public void refreshView() {

        searchField.clear();
        statusFilterComboBox.setValue("Kaikki");

        int id = selectedLasku != null ? selectedLasku.getLaskuId() : -1;

        refreshTable();
        applyFilters();

        // Yritetään palauttaa valinta päivityksen jälkeen
        if (id != -1) {
            selectedLasku = laskut.stream()
                    .filter(l -> l.getLaskuId() == id)
                    .findFirst()
                    .orElse(null);

            if (selectedLasku != null) {
                populateFields(selectedLasku);
                tableLaskut.getSelectionModel().select(selectedLasku);
            }
        }
    }

    // Ladataan laskut palvelusta
    private void refreshTable() {
        if (service == null) return;
        laskut.setAll(service.getAllLaskut());
    }

    // Haku + status-suodatus
    private void applyFilters() {

        String search = searchField.getText() == null
                ? ""
                : searchField.getText().toLowerCase().trim();

        String tilaFilter = statusFilterComboBox.getValue();

        filteredLasku.setPredicate(lasku -> {

            // Haku: id, email tai summa
            boolean matchesSearch =
                    search.isEmpty()
                            || String.valueOf(lasku.getLaskuId()).contains(search)
                            || (lasku.getSapo() != null &&
                            lasku.getSapo().toLowerCase().contains(search))
                            || String.valueOf(lasku.getSumma()).contains(search);

            // Status-suodatus
            boolean matchesTila = true;

            if ("Lähetetty".equals(tilaFilter)) {
                matchesTila = "lähetetty".equalsIgnoreCase(lasku.getTila());
            }
            else if ("Maksettu".equals(tilaFilter)) {
                matchesTila = "maksettu".equalsIgnoreCase(lasku.getTila());
            }
            else if ("Myöhässä".equals(tilaFilter)) {
                matchesTila = "myöhässä".equalsIgnoreCase(lasku.getTila());
            }
            else if ("Peruttu".equals(tilaFilter)) {
                matchesTila = "peruttu".equalsIgnoreCase(lasku.getTila());
            }

            return matchesSearch && matchesTila;
        });
    }

    // Merkitään lasku maksetuksi
    @FXML
    private void merkitseMaksetuksi() {

        if (selectedLasku == null) return;

        Stage stage = (Stage) tableLaskut.getScene().getWindow();

        // Vahvistus ennen muutosta
        boolean confirmed = DialogUtil.confirm(
                stage,
                "Vahvista",
                "Haluatko merkitä laskun maksetuksi?",
                "Lasku #" + selectedLasku.getLaskuId()
                        + " (" + selectedLasku.getSumma() + " €)"
        );

        if (!confirmed) return;

        // Vain tietyissä tiloissa sallitaan maksaminen
        if ("lähetetty".equalsIgnoreCase(selectedLasku.getTila()) ||
                "myöhässä".equalsIgnoreCase(selectedLasku.getTila())) {

            try {
                service.markAsPaid(selectedLasku.getLaskuId());
            } catch (IllegalArgumentException e) {

                statusLabel.setText("Muutos epäonnistui");
                statusLabel.setStyle("-fx-text-fill: #B04A30;");
                statusLabel.setVisible(true);
                statusLabel.setManaged(true);

                return;
            }
        }

        int id = selectedLasku.getLaskuId();

        refreshTable();

        // Palautetaan valinta päivityksen jälkeen
        selectedLasku = laskut.stream()
                .filter(l -> l.getLaskuId() == id)
                .findFirst()
                .orElse(null);

        if (selectedLasku != null) {
            populateFields(selectedLasku);

            tableLaskut.getSelectionModel().select(selectedLasku);
            tableLaskut.scrollTo(selectedLasku);
        }

        showSavedStatus("Lasku merkitty maksetuksi");
        statusLabel.setStyle("-fx-text-fill: #1e7f43;");
    }

    // Status-viestin näyttäminen
    private void showSavedStatus(String text) {
        statusLabel.setText(text);
        statusLabel.setVisible(true);
        statusLabel.setManaged(true);

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> {
            statusLabel.setVisible(false);
            statusLabel.setManaged(false);
        });
        pause.play();
    }

    // Täytetään laskun tiedot UI:hin
    private void populateFields(Lasku m) {

        if (m == null) return;

        laskuIdLabel.setText("#" + m.getLaskuId() + "   |");
        varausIdLabel.setText("#" + m.getVarausId());

        asiakasLabel.setText(String.valueOf(m.getSapo()));

        laskuLuotuLabel.setText(
                m.getAikaleima() != null
                        ? m.getAikaleima().toLocalDate().toString()
                        : "-"
        );

        eraLabel.setText(String.valueOf(m.getErapaiva()));
        summaLabel.setText(m.getSumma() + " €");

        maksupaivaLabel.setText(
                m.getMaksupaiva() != null
                        ? m.getMaksupaiva().toString()
                        : "-"
        );

        maksettuSummaLabel.setText(
                (m.getTila() != null )
                        ? m.getMaksettu() + " €"
                        : "-"
        );

        tilaLabelHeader.setText(laskunTilanEsitys(m.getTila()));
        tilaLabelDetail.setText(laskunTilanEsitys(m.getTila()));

        summaryLabel.setText(
                m.getSapo() + " · " + m.getSumma() + " €"
        );

        // Näytetään maksa-nappi vain sallituissa tiloissa
        boolean voiMaksaa =
                "lähetetty".equalsIgnoreCase(m.getTila()) ||
                        "myöhässä".equalsIgnoreCase(m.getTila());

        markAsPaidButton.setVisible(voiMaksaa);
        markAsPaidButton.setManaged(voiMaksaa);
    }

    // Muunnetaan tilakoodi käyttäjäystävälliseksi tekstiksi
    private String laskunTilanEsitys(String tila) {

        if (tila == null) return "-";

        switch (tila.toLowerCase()) {
            case "lähetetty":
                return "Lähetetty";
            case "maksettu":
                return "Maksettu";
            case "myöhässä":
                return "Myöhässä";
            case "peruttu":
                return "Peruttu";
            default:
                return tila;
        }
    }
}