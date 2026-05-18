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
    @FXML private TableColumn<Lasku, String> asiakasCol;
    @FXML private TableColumn<Lasku, String> erapvmCol;
    @FXML private TableColumn<Lasku, Double> summaCol;
    @FXML private TableColumn<Lasku, String> tilaCol;

    @FXML private Label statusLabel;

    private LaskuService service;

    private Lasku selectedLasku;

    private final ObservableList<Lasku> laskut =
            FXCollections.observableArrayList();

    private FilteredList<Lasku> filteredLasku;

    public void setLaskuService(LaskuService service){
        this.service = service;
        refreshTable();
    }

    @FXML
    public void initialize() {

        laskuCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        data.getValue().getLaskuId()
                ).asObject());

        varausCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        data.getValue().getVarausId()
                ).asObject());

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

        tilaCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        laskunTilanEsitys(data.getValue().getTila())
                ));

        statusLabel.setVisible(false);
        statusLabel.setManaged(false);

        markAsPaidButton.setVisible(false);
        markAsPaidButton.setManaged(false);

        tableLaskut.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        tableLaskut.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {

                    selectedLasku = newSelection;

                    if (newSelection != null) {
                        populateFields(newSelection);
                    }
                });
        filteredLasku = new FilteredList<>(laskut);

        SortedList<Lasku> sortedLasku = new SortedList<>(filteredLasku);
        sortedLasku.comparatorProperty().bind(tableLaskut.comparatorProperty());
        tableLaskut.setItems(sortedLasku);

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

        searchField.textProperty().addListener((obs, oldValue, newValue) -> {
            applyFilters();
        });

        statusFilterComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            applyFilters();
        });

        // Solun korostus värillä
        tilaCol.setCellFactory(col -> new TableCell<Lasku, String>() {

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                getStyleClass().removeAll("cell-sent", "cell-laskuPaid", "cell-late", "cell-inactive");

                if (empty || item == null) {
                    setText(null);
                    return;
                }

                setText(item);

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
    @FXML
    public void refreshView() {

        searchField.clear();
        statusFilterComboBox.setValue("Kaikki");

        int id = selectedLasku != null ? selectedLasku.getLaskuId() : -1;

        refreshTable();
        applyFilters();

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

    private void refreshTable() {
        if (service == null) return;
        laskut.setAll(service.getAllLaskut());
    }
    private void applyFilters() {

        String search = searchField.getText() == null
                ? ""
                : searchField.getText().toLowerCase().trim();

        String tilaFilter = statusFilterComboBox.getValue();

        filteredLasku.setPredicate(lasku -> {

            boolean matchesSearch =
                    search.isEmpty()
                            || String.valueOf(lasku.getLaskuId()).contains(search)
                            || (lasku.getSapo() != null &&
                            lasku.getSapo().toLowerCase().contains(search))
                            || String.valueOf(lasku.getSumma()).contains(search);

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

    @FXML
    private void merkitseMaksetuksi() {

        if (selectedLasku == null) return;

        Stage stage = (Stage) tableLaskut.getScene().getWindow();

        boolean confirmed = DialogUtil.confirm(
                stage,
                "Vahvista",
                "Haluatko merkitä laskun maksetuksi?",
                "Lasku #" + selectedLasku.getLaskuId()
                        + " (" + selectedLasku.getSumma() + " €)"
        );

        if (!confirmed) return;

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

    private void populateFields(Lasku m) {

        if (m == null) return;

        // LABELIT (lukutila)
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

        boolean voiMaksaa =
                "lähetetty".equalsIgnoreCase(m.getTila()) ||
                        "myöhässä".equalsIgnoreCase(m.getTila());

        markAsPaidButton.setVisible(voiMaksaa);
        markAsPaidButton.setManaged(voiMaksaa);
    }

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
