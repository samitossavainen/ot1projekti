package com.mokkikodit.controller;

import com.mokkikodit.logiikka.MokkiService;
import com.mokkikodit.mallit.Mokki;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import com.mokkikodit.util.DialogUtil;

public class MokkiController {

    // UI-komponentit (FXML-sidottuja näkymiä)
    @FXML private TableView<Mokki> tableCabins;

    @FXML private TextField nimiField;
    @FXML private TextField capacityField;
    @FXML private TextField roomsField;
    @FXML private TextField vessatField;
    @FXML private TextField pricePerNightField;
    @FXML private ComboBox<String> statusFilterComboBox;
    @FXML private TextArea addressArea;
    @FXML private TextArea lisatiedotArea;
    @FXML private TextField searchField;

    @FXML private Label nimiLabel;
    @FXML private Label addressLabel;
    @FXML private Label pricePerNightLabel;
    @FXML private Label vessatLabel;
    @FXML private Label capacityLabel;
    @FXML private Label roomsLabel;
    @FXML private Label tilaLabel;
    @FXML private Label lisatiedotLabel;
    @FXML private Label summaryLabel;
    @FXML private Label addCabinStatusLabel;
    @FXML private Label cabinIdLabel;

    @FXML private Button editButton;
    @FXML private Button saveButton;
    @FXML private Button deleteButton;

    @FXML private TableColumn<Mokki, Integer> cabinCol;
    @FXML private TableColumn<Mokki, String> nameCol;
    @FXML private TableColumn<Mokki, String> addressCol;
    @FXML private TableColumn<Mokki, Integer> capacityCol;
    @FXML private TableColumn<Mokki, Double> priceCol;
    @FXML private TableColumn<Mokki, Integer> roomsCol;
    @FXML private TableColumn<Mokki, Integer> bathroomsCol;
    @FXML private TableColumn<Mokki, String> statusCol;

    @FXML private Label statusLabel;

    // Muokkaustilan tila
    private boolean editMode = false;

    // Liiketoimintalogiikan palvelu mökeille
    private MokkiService service;

    // Valittu ja muokattava mökki
    private Mokki selectedMokki;
    private Mokki muokattavaMokki;
    private Mokki viimeksiLisattyMokki;

    // Kaikki mökit muistissa UI:ta varten
    private final ObservableList<Mokki> mokit =
            FXCollections.observableArrayList();

    // Suodatettu lista hakua ja filttereitä varten
    private FilteredList<Mokki> filteredMokit;

    // Asetetaan service ulkopuolelta ja päivitetään taulukko
    public void setMokkiService(MokkiService service){
        this.service = service;
        refreshTable();
    }

    @FXML
    public void initialize() {

        // Taulukon sarakkeiden sidonta Mökin tietoihin

        cabinCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        data.getValue().getMokkiId()
                ).asObject());

        nameCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getNimi()
                ));

        addressCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getOsoite()
                ));

        capacityCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        data.getValue().getKapasiteetti()
                ).asObject());

        priceCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleDoubleProperty(
                        data.getValue().getHinta()
                ).asObject());

        roomsCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        data.getValue().getHuoneet()
                ).asObject());

        bathroomsCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        data.getValue().getVessat()
                ).asObject());

        // Tilan muunnos tekstiksi UI:ta varten
        statusCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getTila() == 1
                                ? "Käytössä"
                                : "Poissa käytöstä"
                ));

        // Statuslabel aluksi piilossa
        statusLabel.setVisible(false);
        statusLabel.setManaged(false);
        editButton.setText("Muokkaa");

        setEditMode(false);

        // Yhden rivin valinta taulukossa
        tableCabins.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Kun käyttäjä valitsee rivin, päivitetään näkymä
        tableCabins.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {

                    selectedMokki = newSelection;

                    if (newSelection != null) {
                        populateFields(newSelection);
                    }
                    updateCabinStatus();
                });

        // Estetään valinta muokkaustilassa
        tableCabins.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_PRESSED, event -> {
            if (editMode) {
                event.consume();
            }
        });

        // Suodatuslista hakua varten
        filteredMokit = new FilteredList<>(mokit);

        SortedList<Mokki> sortedMokit = new SortedList<>(filteredMokit);
        sortedMokit.comparatorProperty().bind(tableCabins.comparatorProperty());
        tableCabins.setItems(sortedMokit);

        // Statusfiltteri
        statusFilterComboBox.setItems(
                FXCollections.observableArrayList(
                        "Kaikki",
                        "Käytössä",
                        "Poissa käytöstä"
                )
        );
        statusFilterComboBox.setValue("Kaikki");

        // Hakukenttä reagoi muutoksiin
        searchField.textProperty().addListener((obs, oldValue, newValue) -> {
            applyFilters();
        });

        // Filtteri reagoi muutoksiin
        statusFilterComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            applyFilters();
        });

        // Rivin korostus viimeksi lisätylle mökille
        tableCabins.setRowFactory(tv -> {
            TableRow<Mokki> row = new TableRow<>();

            PauseTransition clear = new PauseTransition(Duration.seconds(2));

            row.itemProperty().addListener((obs, oldItem, newItem) -> {

                row.getStyleClass().remove("row-highlight");

                if (newItem == null) return;

                if (viimeksiLisattyMokki != null
                        && newItem.getMokkiId() == viimeksiLisattyMokki.getMokkiId()) {

                    row.getStyleClass().add("row-highlight");

                    clear.setOnFinished(e -> {
                        row.getStyleClass().remove("row-highlight");
                        viimeksiLisattyMokki = null;
                    });

                    clear.playFromStart();
                }
            });
            return row;
        });

        // Solujen värikoodaus tilan mukaan
        statusCol.setCellFactory(col -> new TableCell<Mokki, String>() {

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                getStyleClass().removeAll("cell-active", "cell-inactive");

                if (empty || item == null) {
                    setText(null);
                    return;
                }

                setText(item);

                if ("Käytössä".equalsIgnoreCase(item)) {
                    getStyleClass().add("cell-active");
                } else if ("Poissa käytöstä".equalsIgnoreCase(item)) {
                    getStyleClass().add("cell-inactive");
                }
            }
        });

        applyFilters();
    }

    // Päivitä näkymä (jos ei muokkaustilassa)
    @FXML
    private void refreshView() {

        if (editMode) return;

        searchField.clear();
        statusFilterComboBox.setValue("Kaikki");
        refreshTable();
        applyFilters();
    }

    // Lataa mökit palvelusta
    private void refreshTable() {
        if (service == null) return;
        mokit.setAll(service.haeKaikki());
    }

    // Haku + statusfiltteri
    private void applyFilters() {

        String search = searchField.getText() == null
                ? ""
                : searchField.getText().toLowerCase().trim();

        String tilaFilter = statusFilterComboBox.getValue();

        filteredMokit.setPredicate(mokki -> {

            boolean matchesSearch =
                    search.isEmpty()
                            || (mokki.getNimi() != null &&
                            mokki.getNimi().toLowerCase().contains(search))
                            || (mokki.getOsoite() != null &&
                            mokki.getOsoite().toLowerCase().contains(search))
                            || String.valueOf(mokki.getMokkiId()).contains(search)
                            || String.valueOf(mokki.getKapasiteetti()).contains(search)
                            || String.valueOf(mokki.getHuoneet()).contains(search)
                            || String.valueOf(mokki.getVessat()).contains(search);

            boolean matchesTila = true;

            if ("Käytössä".equals(tilaFilter)) {
                matchesTila = mokki.getTila() == 1;
            }
            else if ("Poissa käytöstä".equals(tilaFilter)) {
                matchesTila = mokki.getTila() == 0;
            }

            return matchesSearch && matchesTila;
        });
    }

    // Täyttää kentät valitun mökin tiedoilla
    private void populateFields(Mokki m) {

        if (m == null) return;

        // LABEL-näkymä
        nimiLabel.setText(m.getNimi());
        capacityLabel.setText(String.valueOf(m.getKapasiteetti()));
        roomsLabel.setText(String.valueOf(m.getHuoneet()));
        vessatLabel.setText(String.valueOf(m.getVessat()));
        pricePerNightLabel.setText(m.getHinta() + " €/yö");
        addressLabel.setText(m.getOsoite() != null ? m.getOsoite() : "");
        lisatiedotLabel.setText(m.getLisatiedot() != null ? m.getLisatiedot() : "");
        tilaLabel.setText(m.getTila() == 1 ? "Käytössä" : "Poissa käytöstä");
        cabinIdLabel.setText("#" + m.getMokkiId() + "   |");

        // EDIT-kentät
        nimiField.setText(m.getNimi());
        capacityField.setText(String.valueOf(m.getKapasiteetti()));
        roomsField.setText(String.valueOf(m.getHuoneet()));
        vessatField.setText(String.valueOf(m.getVessat()));
        pricePerNightField.setText(String.valueOf(m.getHinta()));
        addressArea.setText(m.getOsoite() != null ? m.getOsoite() : "");
        lisatiedotArea.setText(m.getLisatiedot() != null ? m.getLisatiedot() : "");

        // Yhteenveto
        summaryLabel.setText(
                m.getNimi() + " · " +
                        m.getKapasiteetti() + " hlö · " +
                        m.getHinta() + " €/yö"
        );
    }

    @FXML
    private void toggleEdit() {

        if (selectedMokki == null) return;

        if (!editMode) enterEditMode();
        else cancelEdit();
    }

    // Siirtyy muokkaustilaan
    private void enterEditMode() {

        editMode = true;

        // Luodaan kopio muokkaukseen
        muokattavaMokki = new Mokki();
        muokattavaMokki.setMokkiId(selectedMokki.getMokkiId());
        muokattavaMokki.setNimi(selectedMokki.getNimi());
        muokattavaMokki.setKapasiteetti(selectedMokki.getKapasiteetti());
        muokattavaMokki.setHuoneet(selectedMokki.getHuoneet());
        muokattavaMokki.setVessat(selectedMokki.getVessat());
        muokattavaMokki.setHinta(selectedMokki.getHinta());
        muokattavaMokki.setOsoite(selectedMokki.getOsoite());
        muokattavaMokki.setLisatiedot(selectedMokki.getLisatiedot());
        muokattavaMokki.setTila(selectedMokki.getTila());

        // Asetetaan kenttiin kopion tiedot
        nimiField.setText(muokattavaMokki.getNimi());
        capacityField.setText(String.valueOf(muokattavaMokki.getKapasiteetti()));
        roomsField.setText(String.valueOf(muokattavaMokki.getHuoneet()));
        vessatField.setText(String.valueOf(muokattavaMokki.getVessat()));
        pricePerNightField.setText(String.valueOf(muokattavaMokki.getHinta()));

        addressArea.setText(muokattavaMokki.getOsoite());
        lisatiedotArea.setText(muokattavaMokki.getLisatiedot());

        setEditMode(true);

        editButton.setText("Peru muokkaus");
        editButton.setStyle("-fx-base: #8A8A8A; -fx-text-fill: white;");

        searchField.setDisable(true);
    }

    // Peru muokkaus
    private void cancelEdit() {

        editMode = false;
        muokattavaMokki = null;

        populateFields(selectedMokki);
        setEditMode(false);

        editButton.setText("Muokkaa");
        editButton.setStyle("-fx-base: #7A9E2E; -fx-text-fill: white;");

        searchField.setDisable(false);
        searchField.clear();
    }

    // Tallentaa muutokset
    @FXML
    private void saveChanges() {

        if (muokattavaMokki == null) return;

        muokattavaMokki.setNimi(nimiField.getText());
        muokattavaMokki.setKapasiteetti(Integer.parseInt(capacityField.getText()));
        muokattavaMokki.setHuoneet(Integer.parseInt(roomsField.getText()));
        muokattavaMokki.setVessat(Integer.parseInt(vessatField.getText()));
        muokattavaMokki.setHinta(Double.parseDouble(pricePerNightField.getText()));
        muokattavaMokki.setOsoite(addressArea.getText());
        muokattavaMokki.setLisatiedot(lisatiedotArea.getText());

        try {
            service.paivita(muokattavaMokki);
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

        selectedMokki = service.haeIdlla(muokattavaMokki.getMokkiId());
        populateFields(selectedMokki);
        viimeksiLisattyMokki = selectedMokki;

        muokattavaMokki = null;

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

    // Vaihda mökin tila (käytössä / pois käytöstä)
    @FXML
    private void toggleCabinStatus() {

        if (editMode) return;
        if (selectedMokki == null) return;

        Stage stage = (Stage) tableCabins.getScene().getWindow();

        boolean onKaytossa = selectedMokki.getTila() == 1;

        boolean confirmed = DialogUtil.confirm(
                stage,
                "Vahvista",
                onKaytossa
                        ? "Haluatko poistaa mökin käytöstä?"
                        : "Haluatko ottaa mökin käyttöön?",
                "Mökki \"" + selectedMokki.getNimi()
                        + "\" (#" + selectedMokki.getMokkiId() + ")"
        );

        if (!confirmed) return;

        if (onKaytossa) {
            service.deaktivoiMokki(selectedMokki.getMokkiId());
            selectedMokki.deaktivoi();
            showSavedStatus("Mökki poistettu käytöstä");
            statusLabel.setStyle("-fx-text-fill: #B04A30;");
        } else {
            service.aktivoiMokki(selectedMokki.getMokkiId());
            selectedMokki.aktivoi();
            showSavedStatus("Mökki otettu käyttöön");
            statusLabel.setStyle("-fx-text-fill: #1e7f43;");
        }

        tableCabins.refresh();
        populateFields(selectedMokki);
        updateCabinStatus();
    }

    // Vaihtaa UI:n muokkaus/luku-tilan
    private void setEditMode(boolean editable) {

        nimiLabel.setVisible(!editable);
        nimiLabel.setManaged(!editable);

        capacityLabel.setVisible(!editable);
        capacityLabel.setManaged(!editable);

        roomsLabel.setVisible(!editable);
        roomsLabel.setManaged(!editable);

        vessatLabel.setVisible(!editable);
        vessatLabel.setManaged(!editable);

        pricePerNightLabel.setVisible(!editable);
        pricePerNightLabel.setManaged(!editable);

        addressLabel.setVisible(!editable);
        addressLabel.setManaged(!editable);

        lisatiedotLabel.setVisible(!editable);
        lisatiedotLabel.setManaged(!editable);

        tilaLabel.setVisible(!editable);
        tilaLabel.setManaged(!editable);

        nimiField.setVisible(editable);
        nimiField.setManaged(editable);

        capacityField.setVisible(editable);
        capacityField.setManaged(editable);

        roomsField.setVisible(editable);
        roomsField.setManaged(editable);

        vessatField.setVisible(editable);
        vessatField.setManaged(editable);

        pricePerNightField.setVisible(editable);
        pricePerNightField.setManaged(editable);

        addressArea.setVisible(editable);
        addressArea.setManaged(editable);

        lisatiedotArea.setVisible(editable);
        lisatiedotArea.setManaged(editable);

        saveButton.setVisible(editable);
        saveButton.setManaged(editable);

        statusFilterComboBox.setDisable(editable);
    }

    // Näyttää lyhyen onnistumisviestin
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

    // Avaa uuden mökin lisäysikkunan
    @FXML
    private void openNewCabinWindow() {

        if (editMode) return;

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/uusi_mokki.fxml")
            );
            Parent root = loader.load();
            UusiMokkiController dialogController = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Uusi Mökki");
            stage.initOwner(tableCabins.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.sizeToScene();
            stage.setResizable(false);

            stage.showAndWait();

            if (dialogController.isMokkiLisatty()) {

                if (!mokit.isEmpty()) {
                    viimeksiLisattyMokki =
                            mokit.get(mokit.size() - 1);
                }
                showAddCabinStatus("Mökki lisätty");
                addCabinStatusLabel.setStyle("-fx-text-fill: #1e7f43;");
                refreshTable();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Näyttää ilmoituksen lisäyksestä
    private void showAddCabinStatus(String text) {
        addCabinStatusLabel.setText(text);
        addCabinStatusLabel.setVisible(true);
        addCabinStatusLabel.setManaged(true);

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> {
            addCabinStatusLabel.setVisible(false);
            addCabinStatusLabel.setManaged(false);
        });
        pause.play();
    }

    // Päivittää delete/activate -napin tilan
    private void updateCabinStatus() {

        if (selectedMokki == null) {
            deleteButton.setDisable(true);
            return;
        }

        deleteButton.setDisable(false);

        if (selectedMokki.getTila() == 0) {
            deleteButton.setText("Ota käyttöön");
            deleteButton.setStyle("-fx-base: #4F8F8B; -fx-text-fill: white;");
        } else {
            deleteButton.setText("Poista käytöstä");
            deleteButton.setStyle("-fx-base: #B04A30; -fx-text-fill: white;");
        }
    }
}