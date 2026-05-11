package com.mokkikodit.controller;

import com.mokkikodit.logiikka.MokkiService;
import com.mokkikodit.mallit.Mokki;
import javafx.animation.PauseTransition;
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
import javafx.util.Duration;
import com.mokkikodit.util.DialogUtil;

public class MokkiController {

    @FXML private TableView<Mokki> tableCabins;

    @FXML private TextField nimiField;
    @FXML private TextField capacityField;
    @FXML private TextField roomsField;
    @FXML private TextField vessatField;
    @FXML private TextField pricePerNightField;
    @FXML private ComboBox<Integer> tilaComboBox;
    @FXML private TextArea addressArea;
    @FXML private TextArea lisatiedotArea;
    @FXML private TextField searchField;
    @FXML private DatePicker alkuDatePicker;
    @FXML private DatePicker loppuDatePicker;

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
    @FXML private TableColumn<Mokki, Integer> capasityCol;
    @FXML private TableColumn<Mokki, Double> priceCol;
    @FXML private TableColumn<Mokki, Integer> roomsCol;
    @FXML private TableColumn<Mokki, Integer> bathroomsCol;
    @FXML private TableColumn<Mokki, Integer> statusCol;

    @FXML private Label statusLabel;

    private boolean editMode = false;

    private MokkiService service;

    private Mokki selectedMokki;
    private Mokki muokattavaMokki;
    private Mokki viimeksiLisattyMokki;

    private final ObservableList<Mokki> mokit =
            FXCollections.observableArrayList();

    private FilteredList<Mokki> filteredMokit;

    public void setMokkiService(MokkiService service){
        this.service = service;
        refreshTable();
    }

    @FXML
    public void initialize() {

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

        capasityCol.setCellValueFactory(data ->
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

        statusCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(
                        data.getValue().getTila()
                ).asObject());

        statusLabel.setVisible(false);
        statusLabel.setManaged(false);
        editButton.setText("Muokkaa");

        setEditMode(false);

        tableCabins.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        tableCabins.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {

                    selectedMokki = newSelection;

                    if (newSelection != null) {
                        populateFields(newSelection);
                    }
                });

        tableCabins.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_PRESSED, event -> {
            if (editMode) {
                event.consume();
            }
        });

        filteredMokit = new FilteredList<>(mokit, a -> true);
        tableCabins.setItems(filteredMokit);

        //Korostetaan juuri lisätyn asiakkaan rivi
        tableCabins.setRowFactory(tv -> {
            TableRow<Mokki> row = new TableRow<>();

            PauseTransition clear = new PauseTransition(Duration.seconds(2));

            row.itemProperty().addListener((obs, oldItem, newItem) -> {
                row.setStyle("");

                if (newItem != null && newItem == viimeksiLisattyMokki) {
                    row.setStyle("-fx-background-color: rgba(46, 204, 113, 0.3);");

                    clear.setOnFinished(e -> {
                        row.setStyle("");
                        if (viimeksiLisattyMokki == newItem) {
                            viimeksiLisattyMokki = null;
                        }
                    });

                    clear.playFromStart();
                }
            });

            return row;
        });
        searchField.textProperty().addListener((obs, oldValue, newValue) -> {

            String search = newValue == null
                    ? ""
                    : newValue.toLowerCase().trim();

            filteredMokit.setPredicate(mokki -> {

                if (search.isEmpty()) {
                    return true;
                }

                return (mokki.getNimi() != null &&
                        mokki.getNimi().toLowerCase().contains(search))
                        || (mokki.getOsoite() != null &&
                        mokki.getOsoite().toLowerCase().contains(search))
                        || String.valueOf(mokki.getMokkiId()).contains(search)
                        || String.valueOf(mokki.getKapasiteetti()).contains(search)
                        || String.valueOf(mokki.getHuoneet()).contains(search)
                        || String.valueOf(mokki.getVessat()).contains(search);
            });
        });
    }

    @FXML
    private void refreshView() {

        if (editMode) return;

        searchField.clear();
        refreshTable();
    }

    private void refreshTable() {
        if (service == null) return;
        mokit.setAll(service.haeKaikki());
    }

    private void populateFields(Mokki m) {

        if (m == null) return;

        // LABELIT (lukutila)
        nimiLabel.setText(m.getNimi());
        capacityLabel.setText(String.valueOf(m.getKapasiteetti()));
        roomsLabel.setText(String.valueOf(m.getHuoneet()));
        vessatLabel.setText(String.valueOf(m.getVessat()));
        pricePerNightLabel.setText(m.getHinta() + " €/yö");
        addressLabel.setText(m.getOsoite() != null ? m.getOsoite() : "");
        lisatiedotLabel.setText(m.getLisatiedot() != null ? m.getLisatiedot() : "");
        tilaLabel.setText(m.getTila() == 1 ? "Käytössä" : "Poissa käytöstä");
        cabinIdLabel.setText(String.valueOf(m.getMokkiId()));

        // KENTÄT (muokkaus)
        nimiField.setText(m.getNimi());
        capacityField.setText(String.valueOf(m.getKapasiteetti()));
        roomsField.setText(String.valueOf(m.getHuoneet()));
        vessatField.setText(String.valueOf(m.getVessat()));
        pricePerNightField.setText(String.valueOf(m.getHinta()));
        addressArea.setText(m.getOsoite() != null ? m.getOsoite() : "");
        lisatiedotArea.setText(m.getLisatiedot() != null ? m.getLisatiedot() : "");
        tilaComboBox.setValue(m.getTila());

        // YHTEENVETO
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

    private void enterEditMode() {
        editMode = true;

        // Luodaan muokattava kopio
        muokattavaMokki = new Mokki();
        muokattavaMokki.setNimi(selectedMokki.getNimi());
        muokattavaMokki.setKapasiteetti(selectedMokki.getKapasiteetti());
        muokattavaMokki.setHuoneet(selectedMokki.getHuoneet());
        muokattavaMokki.setVessat(selectedMokki.getVessat());
        muokattavaMokki.setHinta(selectedMokki.getHinta());
        muokattavaMokki.setOsoite(selectedMokki.getOsoite());
        muokattavaMokki.setLisatiedot(selectedMokki.getLisatiedot());
        muokattavaMokki.setTila(selectedMokki.getTila());

        // Näytetään kopion tiedot kentissä
        nimiField.setText(String.valueOf(muokattavaMokki.getNimi()));
        capacityField.setText(String.valueOf(muokattavaMokki.getKapasiteetti()));
        roomsField.setText(String.valueOf(muokattavaMokki.getHuoneet()));
        vessatField.setText(String.valueOf(muokattavaMokki.getVessat()));
        pricePerNightField.setText(String.valueOf(muokattavaMokki.getHinta()));

        addressArea.setText(muokattavaMokki.getOsoite());
        lisatiedotArea.setText(muokattavaMokki.getLisatiedot());
        tilaComboBox.setValue(muokattavaMokki.getTila());

        setEditMode(true);
        editButton.setText("Peru muokkaus");
        editButton.setStyle("-fx-base: #8A8A8A; -fx-text-fill: white;");

        searchField.setDisable(true);
    }

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

    @FXML
    private void saveChanges() {

        if (muokattavaMokki == null) return;

        // Päivitetään kopio mökkioliosta
        muokattavaMokki.setKapasiteetti(
                Integer.parseInt(capacityField.getText()));
        muokattavaMokki.setHuoneet(
                Integer.parseInt(roomsField.getText()));
        muokattavaMokki.setVessat(
                Integer.parseInt(vessatField.getText()));
        muokattavaMokki.setHinta(
                Double.parseDouble(pricePerNightField.getText()));
        muokattavaMokki.setOsoite(addressArea.getText());
        muokattavaMokki.setLisatiedot(lisatiedotArea.getText());
        muokattavaMokki.setTila((Integer) tilaComboBox.getValue());

        try {
            service.paivita(muokattavaMokki);
        } catch (IllegalArgumentException e) {

            statusLabel.setText("Täytä puuttuvat tiedot");
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

        // Päivitetään TableView:ssa oleva valittu mökki
        selectedMokki.setKapasiteetti(muokattavaMokki.getKapasiteetti());
        selectedMokki.setHuoneet(muokattavaMokki.getHuoneet());
        selectedMokki.setVessat(muokattavaMokki.getVessat());
        selectedMokki.setHinta(muokattavaMokki.getHinta());
        selectedMokki.setOsoite(muokattavaMokki.getOsoite());
        selectedMokki.setLisatiedot(muokattavaMokki.getLisatiedot());
        selectedMokki.setTila(muokattavaMokki.getTila());

        muokattavaMokki = null;

        editMode = false;
        setEditMode(false);

        editButton.setText("Muokkaa");
        editButton.setStyle("-fx-base: #7A9E2E; -fx-text-fill: white;");

        populateFields(selectedMokki);
        showSavedStatus("Tallennettu");
        statusLabel.setStyle("-fx-text-fill: #1e7f43;");

        tableCabins.refresh();

        searchField.setDisable(false);
        searchField.clear();
    }
    @FXML
    private void deleteCabin() {

        if (editMode) return;

        if (selectedMokki == null) return;

        Stage stage = (Stage) tableCabins.getScene().getWindow();

        boolean confirmed = DialogUtil.confirm(
                stage,
                "Vahvista poisto",
                "Haluatko varmasti poistaa asiakkaan?",
                "Asiakas \"" + selectedMokki.getNimi() + "\" (" + selectedMokki.getMokkiId() + ") poistetaan."
        );

        if (confirmed) {

            // service.poista(selectedMokki);

            mokit.remove(selectedMokki);

            showSavedStatus("Mökki poistettu");
            statusLabel.setStyle("-fx-text-fill: #B04A30;");
        }
    }

    private void setEditMode(boolean editable) {

        // LABELIT (lukutila)
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

        // KENTÄT (muokkaus)
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

        tilaComboBox.setVisible(editable);
        tilaComboBox.setManaged(editable);

        saveButton.setVisible(editable);
        saveButton.setManaged(editable);
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

                refreshTable();

                if (!mokit.isEmpty()) {
                    viimeksiLisattyMokki =
                            mokit.get(mokit.size() - 1);
                }
                showAddCabinStatus("Mökki lisätty");
                addCabinStatusLabel.setStyle("-fx-text-fill: #1e7f43;");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //Näytetään käyttäjälle että mökki lisättiin onnistuneesti
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
}
