package com.mokkikodit.controller;

import com.mokkikodit.logiikka.AsiakasService;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import com.mokkikodit.mallit.Asiakas;
import javafx.collections.transformation.FilteredList;

public class AsiakasController {

    @FXML private TableView<Asiakas> tableAsiakkaat;

    @FXML private TextField nimiField;
    @FXML private Label emailLabel;
    @FXML private TextField phoneField;
    @FXML private TextArea addressArea;
    @FXML private Label summaryLabel;
    @FXML private Label addCustomerStatusLabel;
    @FXML private TextField searchField;

    @FXML private Label nimiLabel;
    @FXML private Label phoneLabel;
    @FXML private Label addressLabel;

    @FXML private Button editButton;
    @FXML private Button saveButton;
    @FXML private Button deleteButton;

    @FXML private TableColumn<Asiakas, String> nimiCol;
    @FXML private TableColumn<Asiakas, String> emailCol;
    @FXML private TableColumn<Asiakas, String> phoneCol;
    @FXML private TableColumn<Asiakas, String> addressCol;

    @FXML private Label statusLabel;

    // Muokkaustilan hallinta
    private boolean editMode = false;

    // Palvelu, joka hoitaa asiakkaiden haun ja päivitykset
    private AsiakasService service;

    // Valittu asiakas taulukosta
    private Asiakas selectedAsiakas;

    // Muokkausta varten luotu kopio asiakkaasta
    private Asiakas muokattavaAsiakas;

    // Viimeksi lisätty asiakas (korostusta varten)
    private Asiakas viimeksiLisattyAsiakas;

    // Kaikki asiakkaat muistissa
    private final ObservableList<Asiakas> asiakkaat =
            FXCollections.observableArrayList();

    // Hakusuodatus
    private FilteredList<Asiakas> filteredAsiakkaat;

    // Asetetaan palvelu controllerille ja päivitetään taulukko
    public void setAsiakasService(AsiakasService service) {
        this.service = service;
        refreshTable();
    }

    @FXML
    public void initialize() {

        // Sarakkeiden arvojen sitominen Asiakas-olioon

        nimiCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getNimi()
                ));

        emailCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getSapo()
                ));

        phoneCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getPuhelinnumero()
                ));

        addressCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getOsoite()
                ));

        // Status-teksti aluksi piilossa
        statusLabel.setVisible(false);
        statusLabel.setManaged(false);

        // Oletusteksti muokkausnapille
        editButton.setText("Muokkaa");

        // Alustetaan näkymä lukutilaan
        setEditMode(false);

        // Yksi valinta kerrallaan taulukosta
        tableAsiakkaat.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Kun käyttäjä valitsee asiakkaan taulukosta
        tableAsiakkaat.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldSelection, newSelection) -> {

                    selectedAsiakas = newSelection;

                    // Täytetään kentät valitulla asiakkaalla
                    if (newSelection != null) {
                        populateFields(newSelection);
                    }

                    // Poista-nappi käytössä vain jos valinta on olemassa
                    deleteButton.setDisable(newSelection == null);
                });

        // Estetään taulukon valinta muokkaustilassa
        tableAsiakkaat.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_PRESSED, event -> {
            if (editMode) {
                event.consume();
            }
        });

        // Suodatuslista hakua varten
        filteredAsiakkaat = new FilteredList<>(asiakkaat);

        SortedList<Asiakas> sortedAsiakkaat = new SortedList<>(filteredAsiakkaat);
        sortedAsiakkaat.comparatorProperty().bind(tableAsiakkaat.comparatorProperty());
        tableAsiakkaat.setItems(sortedAsiakkaat);

        // Haku muuttuu reaaliajassa
        searchField.textProperty().addListener((obs, oldValue, newValue) -> {
            applyFilters();
        });

        applyFilters();

        // Rivin korostus viimeksi lisätylle asiakkaalle
        tableAsiakkaat.setRowFactory(tv -> {
            TableRow<Asiakas> row = new TableRow<>();
            PauseTransition clear = new PauseTransition(Duration.seconds(2));

            row.itemProperty().addListener((obs, oldItem, item) -> {

                row.getStyleClass().remove("row-highlight");

                if (item == null) return;

                // Korostetaan juuri lisätty asiakas
                if (viimeksiLisattyAsiakas != null
                        && item.getSapo().equals(viimeksiLisattyAsiakas.getSapo())) {

                    row.getStyleClass().add("row-highlight");

                    clear.setOnFinished(e -> {
                        row.getStyleClass().remove("row-highlight");
                        viimeksiLisattyAsiakas = null;
                    });

                    clear.playFromStart();
                }
            });
            return row;
        });
    }

    // Päivitetään näkymä (ei muokkaustilassa)
    @FXML
    private void refreshView() {
        if (editMode) return;

        searchField.clear();
        refreshTable();
    }

    // Ladataan asiakkaat palvelusta
    private void refreshTable() {
        if (service == null) return;

        asiakkaat.setAll(service.haeKaikki());
        applyFilters();
    }

    // Hakusuodatus asiakkaisiin
    private void applyFilters() {

        String search = searchField.getText() == null
                ? ""
                : searchField.getText().toLowerCase().trim();

        filteredAsiakkaat.setPredicate(asiakas -> {

            // Poistetut/piilotetut asiakkaat
            if (asiakas.getTila() == 0) {
                return false;
            }

            // Jos ei hakua → näytetään kaikki
            if (search.isEmpty()) {
                return true;
            }

            // Haku useista kentistä
            return (asiakas.getNimi() != null &&
                    asiakas.getNimi().toLowerCase().contains(search))
                    || (asiakas.getSapo() != null &&
                    asiakas.getSapo().toLowerCase().contains(search))
                    || (asiakas.getPuhelinnumero() != null &&
                    asiakas.getPuhelinnumero().toLowerCase().contains(search))
                    || (asiakas.getOsoite() != null &&
                    asiakas.getOsoite().toLowerCase().contains(search));
        });
    }

    // Täytetään näkymän kentät valitulla asiakkaalla
    private void populateFields(Asiakas a) {

        nimiLabel.setText(a.getNimi());
        emailLabel.setText(a.getSapo());
        phoneLabel.setText(a.getPuhelinnumero() != null ? a.getPuhelinnumero() : "");
        addressLabel.setText(a.getOsoite() != null ? a.getOsoite() : "");

        nimiField.setText(a.getNimi());
        phoneField.setText(a.getPuhelinnumero() != null ? a.getPuhelinnumero() : "");
        addressArea.setText(a.getOsoite());

        summaryLabel.setText(
                a.getNimi() + " · " +
                        a.getSapo() + " · " +
                        a.getPuhelinnumero()
        );
    }

    // Vaihdetaan muokkaustila päälle/pois
    @FXML
    private void toggleEdit() {

        if (selectedAsiakas == null) return;

        if (!editMode) enterEditMode();
        else cancelEdit();
    }

    // Siirrytään muokkaustilaan
    private void enterEditMode() {
        editMode = true;

        // Tehdään kopio muokkausta varten
        muokattavaAsiakas = new Asiakas();
        muokattavaAsiakas.setNimi(selectedAsiakas.getNimi());
        muokattavaAsiakas.setPuhelinnumero(selectedAsiakas.getPuhelinnumero());
        muokattavaAsiakas.setOsoite(selectedAsiakas.getOsoite());
        muokattavaAsiakas.setSapo(selectedAsiakas.getSapo());

        // Näytetään kopion tiedot kentissä
        nimiField.setText(muokattavaAsiakas.getNimi());
        phoneField.setText(muokattavaAsiakas.getPuhelinnumero());
        addressArea.setText(muokattavaAsiakas.getOsoite());

        setEditMode(true);

        editButton.setText("Peru muokkaus");
        editButton.setStyle("-fx-base: #8A8A8A; -fx-text-fill: white;");

        searchField.setDisable(true);
    }

    // Perutaan muokkaus
    private void cancelEdit() {
        editMode = false;
        muokattavaAsiakas = null;

        populateFields(selectedAsiakas);
        setEditMode(false);

        editButton.setText("Muokkaa");
        editButton.setStyle("-fx-base: #7A9E2E; -fx-text-fill: white;");

        searchField.setDisable(false);
        searchField.clear();
    }

    // Tallennetaan muutokset
    @FXML
    private void saveChanges() {

        if (muokattavaAsiakas == null) return;

        muokattavaAsiakas.setNimi(nimiField.getText());
        muokattavaAsiakas.setPuhelinnumero(phoneField.getText());
        muokattavaAsiakas.setOsoite(addressArea.getText());

        try {
            service.paivita(muokattavaAsiakas);
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

        // Päivitetään valittu asiakas näkymään
        selectedAsiakas.setNimi(muokattavaAsiakas.getNimi());
        selectedAsiakas.setPuhelinnumero(muokattavaAsiakas.getPuhelinnumero());
        selectedAsiakas.setOsoite(muokattavaAsiakas.getOsoite());

        muokattavaAsiakas = null;

        editMode = false;
        setEditMode(false);

        editButton.setText("Muokkaa");
        editButton.setStyle("-fx-base: #7A9E2E; -fx-text-fill: white;");

        populateFields(selectedAsiakas);

        showSavedStatus("Tallennettu");
        statusLabel.setStyle("-fx-text-fill: #1e7f43;");

        viimeksiLisattyAsiakas = selectedAsiakas;

        refreshTable();

        searchField.setDisable(false);
        searchField.clear();
    }

    // Asiakkaan tilan muutos (poisto/aktivointi)
    @FXML
    private void toggleCustomerStatus() {

        if (editMode) return;
        if (selectedAsiakas == null) return;

        Stage stage = (Stage) tableAsiakkaat.getScene().getWindow();
        boolean onKaytossa = selectedAsiakas.getTila() == 1;

        // Vahvistusdialogi ennen poistoa
        boolean confirmed = DialogUtil.confirm(
                stage,
                "Vahvista",
                "Haluatko varmasti poistaa asiakkaan?",
                "Asiakas \"" + selectedAsiakas.getNimi()
                        + "\" (" + selectedAsiakas.getSapo() + ")"
        );

        if (!confirmed) return;

        if (onKaytossa) {
            service.poista(selectedAsiakas.getSapo());
            selectedAsiakas.deaktivoiAsiakas();

            // Tyhjennetään valinta
            tableAsiakkaat.getSelectionModel().clearSelection();
            selectedAsiakas = null;

            nimiLabel.setText("");
            emailLabel.setText("");
            phoneLabel.setText("");
            addressLabel.setText("");
            summaryLabel.setText("");

            showSavedStatus("Asiakas poistettu");
            statusLabel.setStyle("-fx-text-fill: #B04A30;");
        }

        refreshTable();
    }

    // Vaihdetaan UI:n editointi-/lukutila
    private void setEditMode(boolean editable) {

        nimiLabel.setVisible(!editable);
        nimiLabel.setManaged(!editable);

        phoneLabel.setVisible(!editable);
        phoneLabel.setManaged(!editable);

        addressLabel.setVisible(!editable);
        addressLabel.setManaged(!editable);

        nimiField.setVisible(editable);
        nimiField.setManaged(editable);

        phoneField.setVisible(editable);
        phoneField.setManaged(editable);

        addressArea.setVisible(editable);
        addressArea.setManaged(editable);

        saveButton.setVisible(editable);
        saveButton.setManaged(editable);
    }

    // Näytetään käyttäjälle statusviesti
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

    // Avataan uuden asiakkaan ikkuna
    @FXML
    private void openNewCustomerWindow() {

        if (editMode) return;

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/uusi_asiakas.fxml")
            );
            Parent root = loader.load();
            UusiAsiakasController dialogController = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Uusi asiakas");
            stage.initOwner(tableAsiakkaat.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.sizeToScene();
            stage.setResizable(false);

            stage.showAndWait();

            // Jos asiakas lisättiin onnistuneesti
            if (dialogController.isAsiakasLisatty()) {

                refreshTable();

                if (!asiakkaat.isEmpty()) {
                    viimeksiLisattyAsiakas =
                            asiakkaat.get(asiakkaat.size() - 1);
                }

                showAddCustomerStatus("Asiakas lisätty");
                addCustomerStatusLabel.setStyle("-fx-text-fill: #1e7f43;");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Näytetään lisäyksen statusviesti
    private void showAddCustomerStatus(String text) {
        addCustomerStatusLabel.setText(text);
        addCustomerStatusLabel.setVisible(true);
        addCustomerStatusLabel.setManaged(true);

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> {
            addCustomerStatusLabel.setVisible(false);
            addCustomerStatusLabel.setManaged(false);
        });
        pause.play();
    }
}