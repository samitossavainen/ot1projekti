package com.mokkikodit.controller;

import com.mokkikodit.logiikka.AsiakasService;
import com.mokkikodit.logiikka.MokkiService;
import com.mokkikodit.mallit.Asiakas;
import com.mokkikodit.mallit.Mokki;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import com.mokkikodit.util.DialogUtil;
import com.mokkikodit.logiikka.VarausService;
import com.mokkikodit.mallit.Varaus;

import java.time.LocalDate;

public class VarausController {

    @FXML private TableView<Varaus> tableVaraukset;

    @FXML private Label varausIdLabel;
    @FXML private Label asiakasLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneLabel;
    @FXML private DatePicker alkuDatePickerFilter;
    @FXML private DatePicker loppuDatePickerFilter;
    @FXML private ComboBox<String> statusFilterComboBox;
    @FXML private TextField searchField;

    @FXML private Button editButton;
    @FXML private Button saveButton;
    @FXML private Label statusLabel;
    @FXML private Button cancelButton;
    @FXML private Label summaryLabel;
    @FXML private Label upperStatusLabel;
    @FXML private Label alkuLabel;
    @FXML private Label loppuLabel;
    @FXML private DatePicker alkuDatePickerDetail;
    @FXML private DatePicker loppuDatePickerDetail;
    @FXML private Label tilaLabelHeader;
    @FXML private Label tilaLabelDetail;
    @FXML private Label luontiLabel;

    @FXML private TableColumn<Varaus, Number> idCol;
    @FXML private TableColumn<Varaus, String> asiakasCol;
    @FXML private TableColumn<Varaus, String> mokkiCol;
    @FXML private TableColumn<Varaus, String> tilaCol;
    @FXML private TableColumn<Varaus, LocalDate> alkuCol;
    @FXML private TableColumn<Varaus, LocalDate> loppuCol;
    @FXML private TableColumn<Varaus, Double> kokonaisSummaCol;


    private boolean editMode = false;

    private VarausService service;
    private AsiakasService asiakasService;
    private MokkiService mokkiService;

    private Varaus selectedVaraus;
    private Varaus muokattavaVaraus;
    private Varaus viimeksiLisattyVaraus;

    private final ObservableList<Varaus> varaukset =
            FXCollections.observableArrayList();

    private FilteredList<Varaus> filteredVaraukset;

    public void setVarausService(VarausService service) {
        this.service = service;
        refreshTable();
    }

    public void setAsiakasService(AsiakasService asiakasService) {
        this.asiakasService = asiakasService;
    }

    public void setMokkiService(MokkiService mokkiService) {
        this.mokkiService = mokkiService;
    }

    @FXML
    public void initialize() {

        alkuDatePickerDetail.setOnShowing(e -> {
            if (!editMode) alkuDatePickerDetail.hide();
        });

        loppuDatePickerDetail.setOnShowing(e -> {
            if (!editMode) loppuDatePickerDetail.hide();
        });

        idCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        data.getValue().getVarausId()
                )
        );

        asiakasCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getAsiakasEmail()
                )
        );

        mokkiCol.setCellValueFactory(data -> {
            Mokki m = null;

            if (mokkiService != null) {
                m = mokkiService.haeIdlla(data.getValue().getMokkiId());
            }

            String text = (m != null)
                    ? m.getNimi()
                    : "";

            return new javafx.beans.property.SimpleStringProperty(text);
        });

        tilaCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        varauksenTilanEsitys(data.getValue().getTila())
                )
        );

        alkuCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(
                        data.getValue().getAlkuPvm()
                )
        );

        loppuCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(
                        data.getValue().getLoppuPvm()
                )
        );

        kokonaisSummaCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(
                        data.getValue().getKokonaissumma()
                )
        );

        statusLabel.setVisible(false);
        statusLabel.setManaged(false);
        editButton.setText("Muokkaa");

        setEditMode(false);

        tableVaraukset.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        tableVaraukset.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {

                    selectedVaraus = newSelection;

                    if (newSelection != null) {
                        populateFields(newSelection);
                    }
                    updateReservationStatus();
                });

        tableVaraukset.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_PRESSED, event -> {
            if (editMode) {
                event.consume();
            }
        });


        filteredVaraukset = new FilteredList<>(
                varaukset,
                v -> !"peruutettu".equalsIgnoreCase(v.getTila())
        );
        tableVaraukset.setItems(filteredVaraukset);
        statusFilterComboBox.setItems(
                FXCollections.observableArrayList(
                        "Kaikki",
                        "Aktiivinen",
                        "Maksettu"
                )
        );
        statusFilterComboBox.setValue("Aktiivinen");



        searchField.textProperty().addListener((obs, oldValue, newValue) -> {
            applyFilters();
        });

        alkuDatePickerFilter.valueProperty().addListener((obs, oldValue, newValue) -> {
            applyFilters();
        });

        loppuDatePickerFilter.valueProperty().addListener((obs, oldValue, newValue) -> {
            applyFilters();
        });

        statusFilterComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            applyFilters();
        });
        applyFilters();

        // Rivin korostus värillä mökin lisäyksen ja tallennuksen jälkeen.
        tableVaraukset.setRowFactory(tv -> {
            TableRow<Varaus> row = new TableRow<>();

            PauseTransition clear = new PauseTransition(Duration.seconds(2));

            row.itemProperty().addListener((obs, oldItem, newItem) -> {

                row.getStyleClass().remove("row-highlight");

                if (newItem == null) return;

                if (viimeksiLisattyVaraus != null
                        && newItem.getVarausId() == viimeksiLisattyVaraus.getVarausId()) {

                    row.getStyleClass().add("row-highlight");

                    clear.setOnFinished(e -> {
                        row.getStyleClass().remove("row-highlight");
                        viimeksiLisattyVaraus = null;
                    });

                    clear.playFromStart();
                }
            });
            return row;
        });

        // Solun korostus värillä
        tilaCol.setCellFactory(col -> new TableCell<Varaus, String>() {

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                // nollaa tyylit
                getStyleClass().removeAll("cell-active", "cell-paid");

                if (empty || item == null) {
                    setText(null);
                    return;
                }
                setText(item);

                // lisätään CSS-luokka
                if ("Aktiivinen".equalsIgnoreCase(item)) {
                    getStyleClass().add("cell-active");
                } else if ("Maksettu".equalsIgnoreCase(item)) {
                    getStyleClass().add("cell-paid");
                }
            }
        });
        applyFilters();
    }

    // Näkymän päivitys
    @FXML
    private void refreshView() {

        if (editMode) return;

        searchField.clear();
        statusFilterComboBox.setValue("Aktiivinen");
        alkuDatePickerFilter.setValue(null);
        loppuDatePickerFilter.setValue(null);

        refreshTable();
        applyFilters();
    }

    private void refreshTable() {
        if (service == null) return;
        varaukset.setAll(service.getAllVaraukset());
    }
    // Tietojen suodatus tauluun usealla tavalla
    private void applyFilters() {

        String search = searchField.getText() == null
                ? ""
                : searchField.getText().toLowerCase().trim();

        String tilaFilter = statusFilterComboBox.getValue();

        LocalDate alkuFilter = alkuDatePickerFilter.getValue();
        LocalDate loppuFilter = loppuDatePickerFilter.getValue();

        filteredVaraukset.setPredicate(v -> {

            if ("peruutettu".equalsIgnoreCase(v.getTila())) {
                return false;
            }

            Mokki m = mokkiService != null
                    ? mokkiService.haeIdlla(v.getMokkiId())
                    : null;

            Asiakas a = asiakasService != null
                    ? asiakasService.hae(v.getAsiakasEmail())
                    : null;

            boolean matchesSearch =
                    search.isEmpty()
                            || String.valueOf(v.getVarausId()).contains(search)
                            || v.getAsiakasEmail().toLowerCase().contains(search)

                            // Suodatus asiakkaan nimellä
                            || (
                            a != null &&
                                    a.getNimi() != null &&
                                    a.getNimi().toLowerCase().contains(search)
                    )
                            // Suodatus mökin nimellä
                            || (
                            m != null &&
                                    m.getNimi() != null &&
                                    m.getNimi().toLowerCase().contains(search)
                    );

            boolean matchesTila = true;

            if ("aktiivinen".equalsIgnoreCase(tilaFilter)) {
                matchesTila = "aktiivinen".equalsIgnoreCase(v.getTila());
            } else if ("maksettu".equalsIgnoreCase(tilaFilter)) {
                matchesTila = "maksettu".equalsIgnoreCase(v.getTila());
            }

            boolean matchesDate = true;

            if (alkuFilter != null) {
                // Varaus ei saa päättyä ennen alkusuodatinta
                matchesDate = !v.getLoppuPvm().isBefore(alkuFilter);
            }

            if (matchesDate && loppuFilter != null) {
                // Varaus ei saa alkaa loppusuodattimen jälkeen
                matchesDate = !v.getAlkuPvm().isAfter(loppuFilter);
            }

            return matchesSearch && matchesTila && matchesDate;
        });
    }

    private void populateFields(Varaus v) {

        if (v == null) return;

        // LABELIT (lukutila)
        varausIdLabel.setText("#" + v.getVarausId() + "   |");

        // ASIAKKAAN TIEDOT (haetaan AsiakasServicen kautta)
        Asiakas a = null;

        if (asiakasService != null && v.getAsiakasEmail() != null) {
            a = asiakasService.hae(v.getAsiakasEmail());
        }

        if (a != null) {
            asiakasLabel.setText(a.getNimi());
            emailLabel.setText(a.getSapo());
            phoneLabel.setText(a.getPuhelinnumero());
        } else {
            asiakasLabel.setText("-");
            emailLabel.setText(v.getAsiakasEmail());
            phoneLabel.setText("-");
        }

        // MÖKIN HAKU
        Mokki m = null;

        if (mokkiService != null) {
            m = mokkiService.haeIdlla(v.getMokkiId());
        }

        updateTilaLabels(v);

        alkuLabel.setText(
                v.getAlkuPvm() != null ? v.getAlkuPvm().toString() : ""
        );

        loppuLabel.setText(
                v.getLoppuPvm() != null ? v.getLoppuPvm().toString() : ""
        );

        // KENTÄT (muokkaus)
        alkuDatePickerDetail.setValue(v.getAlkuPvm());
        loppuDatePickerDetail.setValue(v.getLoppuPvm());

        // Luontipäivä
        luontiLabel.setText(
                v.getLuontiPvm() != null
                        ? v.getLuontiPvm().toLocalDate().toString()
                        : ""
        );

        // YHTEENVETO
        if (m != null) {
            summaryLabel.setText(
                    m.getNimi()
                            + " · " + v.getAsiakasEmail()
                            + " · " + v.getKokonaissumma() + "€"
            );
        } else {
            summaryLabel.setText(
                    v.getAsiakasEmail()
                            + " · " + v.getKokonaissumma() + "€"
            );
        }
    }

    @FXML
    private void toggleEdit() {
        if (!editMode) enterEditMode();
        else cancelEdit();
    }

    private void enterEditMode() {

        if (selectedVaraus == null) return;

        editMode = true;

        // Luodaan muokattava kopio varauksesta
        muokattavaVaraus = new Varaus();
        muokattavaVaraus.setVarausId(selectedVaraus.getVarausId());
        muokattavaVaraus.setAsiakasEmail(selectedVaraus.getAsiakasEmail());
        muokattavaVaraus.setMokkiId(selectedVaraus.getMokkiId());
        muokattavaVaraus.setAlkuPvm(selectedVaraus.getAlkuPvm());
        muokattavaVaraus.setLoppuPvm(selectedVaraus.getLoppuPvm());
        muokattavaVaraus.setTila(selectedVaraus.getTila());
        muokattavaVaraus.setKokonaissumma(selectedVaraus.getKokonaissumma());

        // Näytetään kopion tiedot muokkauskentissä
        alkuDatePickerDetail.setValue(muokattavaVaraus.getAlkuPvm());
        loppuDatePickerDetail.setValue(muokattavaVaraus.getLoppuPvm());

        setEditMode(true);

        editButton.setText("Peru muokkaus");
        editButton.setStyle("-fx-base: #8A8A8A; -fx-text-fill: white;");

        searchField.setDisable(true);
    }

    private void cancelEdit() {

        editMode = false;
        muokattavaVaraus = null;

        populateFields(selectedVaraus);
        setEditMode(false);

        editButton.setText("Muokkaa");
        editButton.setStyle("-fx-base: #7A9E2E; -fx-text-fill: white;");

        searchField.setDisable(false);
    }

    @FXML
    private void saveChanges() {

        if (muokattavaVaraus == null) return;

        // Päivitetään kopio varauksesta muokkauskenttien arvoilla
        muokattavaVaraus.setAlkuPvm(alkuDatePickerDetail.getValue());
        muokattavaVaraus.setLoppuPvm(loppuDatePickerDetail.getValue());

        selectedVaraus.setKokonaissumma(muokattavaVaraus.getKokonaissumma());

        try {
            service.updateVaraus(muokattavaVaraus);

        } catch (IllegalArgumentException e) {

            statusLabel.setText(e.getMessage());
            statusLabel.setStyle("-fx-text-fill: #B04A30;");
            statusLabel.setVisible(true);
            statusLabel.setManaged(true);

            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(ev -> {
                statusLabel.setVisible(false);
                statusLabel.setManaged(false);
            });
            pause.play();

            return;
        }

        // Päivitetään näkymässä oleva valittu varaus
        selectedVaraus.setAlkuPvm(muokattavaVaraus.getAlkuPvm());
        selectedVaraus.setLoppuPvm(muokattavaVaraus.getLoppuPvm());

        selectedVaraus.setKokonaissumma(muokattavaVaraus.getKokonaissumma());

        viimeksiLisattyVaraus = selectedVaraus;
        populateFields(selectedVaraus);

        muokattavaVaraus = null;
        editMode = false;
        setEditMode(false);

        editButton.setText("Muokkaa");
        editButton.setStyle("-fx-base: #7A9E2E; -fx-text-fill: white;");

        showSavedStatus("Tallennettu");
        statusLabel.setStyle("-fx-text-fill: #1e7f43;");

        refreshTable();

        searchField.setDisable(false);
        searchField.clear();
    }

    private void setEditMode(boolean editable) {

        // LABELIT (lukutila)
        alkuLabel.setVisible(!editable);
        alkuLabel.setManaged(!editable);

        loppuLabel.setVisible(!editable);
        loppuLabel.setManaged(!editable);

        // KENTÄT (muokkaus)
        alkuDatePickerDetail.setVisible(editable);
        alkuDatePickerDetail.setManaged(editable);

        loppuDatePickerDetail.setVisible(editable);
        loppuDatePickerDetail.setManaged(editable);

        saveButton.setVisible(editable);
        saveButton.setManaged(editable);

        // Haku- ja suodatukset
        searchField.setDisable(editable);
        statusFilterComboBox.setDisable(editable);
        alkuDatePickerFilter.setDisable(editable);
        loppuDatePickerFilter.setDisable(editable);
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

    @FXML
    private void openNewReservationWindow() {

        if (editMode) return;

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/uusi_varaus.fxml")
            );
            Parent root = loader.load();

            UusiVarausController ctrl = loader.getController();
            ctrl.setVarausService(service);

            Stage stage = new Stage();
            stage.setTitle("Uusi varaus");
            stage.initOwner(tableVaraukset.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.sizeToScene();
            stage.setResizable(false);
            stage.showAndWait();

            if (ctrl.isVarausLisatty()) {

                tableVaraukset.getSortOrder().clear();
                refreshTable();

                if (!varaukset.isEmpty()) {
                    viimeksiLisattyVaraus =
                            varaukset.get(varaukset.size() - 1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void cancelReservation() {

        if (editMode) return;
        if (selectedVaraus == null) return;

        // Estä maksetun varauksen peruutus
        if ("maksettu".equalsIgnoreCase(selectedVaraus.getTila())) {
            DialogUtil.showInfo("Maksettua varausta ei voi peruuttaa.");
            return;
        }

        boolean confirmed = DialogUtil.confirm(
                (Stage) tableVaraukset.getScene().getWindow(),
                "Vahvista",
                "Haluatko varmasti perua varauksen?",
                "Varaus #" + selectedVaraus.getVarausId()
        );

        if (!confirmed) return;

        // Soft delete
        selectedVaraus.setTila("peruutettu");

        service.peruutaVaraus(selectedVaraus.getVarausId());
        varaukset.remove(selectedVaraus);

        showSavedStatus("Varaus peruttu");
        statusLabel.setStyle("-fx-text-fill: #B04A30;");
        selectedVaraus = null;
        tableVaraukset.getSelectionModel().clearSelection();
    }

    private void updateReservationStatus() {

        boolean hasSelection = selectedVaraus != null;

        cancelButton.setDisable(!hasSelection);
        cancelButton.setText("Peru varaus");
        cancelButton.setStyle("-fx-base: #B04A30; -fx-text-fill: white;");
    }

    private String varauksenTilanEsitys(String tila) {
        if (tila == null) return "-";

        switch (tila.toLowerCase()) {
            case "aktiivinen":
                return "Aktiivinen";
            case "maksettu":
                return "Maksettu";
            case "peruutettu":
                return "Peruutettu";
            default:
                return tila;
        }
    }
    private void updateTilaLabels(Varaus v) {
        String tilaText = (v == null)
                ? "-"
                : varauksenTilanEsitys(v.getTila());

        tilaLabelHeader.setText(tilaText);
        tilaLabelDetail.setText(tilaText);
    }
}