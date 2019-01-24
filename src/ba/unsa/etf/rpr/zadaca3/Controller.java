package ba.unsa.etf.rpr.zadaca3;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class Controller {

    public TableColumn kolonaImeIPrezimeVlasnika;
    private VozilaDAO vozilaDAO;
    private Vozilo trenutnoOdabranoVozilo;
    private Vlasnik trenutnoOdabraniVlasnik;
    public TableView tabelaVlasnici;
    public TableView tabelaVozilo;
    public RadioMenuItem menuDb;
    public RadioMenuItem menuXml;
    public TabPane tabPane;

    public Controller() {
        vozilaDAO = new VozilaDAOBaza();
        trenutnoOdabraniVlasnik = null;
        trenutnoOdabranoVozilo = null;
    }

    @FXML
    public void initialize() {
        tabelaVlasnici.setEditable(true);
        tabelaVozilo.setEditable(true);
        menuDb.setSelected(true);
        menuXml.setSelected(false);
        tabelaVlasnici.setItems(vozilaDAO.getVlasnici());
        tabelaVozilo.setItems(vozilaDAO.getVozila());

        // Postavljanje trenutno odabranog vozila klikom na vozilo u tabeli
        tabelaVozilo.getSelectionModel().selectedItemProperty().addListener((ChangeListener<Vozilo>)
                (observableValue, o, n) -> trenutnoOdabranoVozilo = n);
        // Postavljanje trenutno odabranog vlasnika klikom na vlasnika u tabeli
        tabelaVlasnici.getSelectionModel().selectedItemProperty().addListener((ChangeListener<Vlasnik>)
                (observableValue, o, n) -> trenutnoOdabraniVlasnik = n);
    }

    public void menuDbEventHandler(ActionEvent actionEvent) {
        menuDb.setSelected(true);
        menuXml.setSelected(false);
        vozilaDAO = new VozilaDAOBaza();
        tabelaVlasnici.getSelectionModel().clearSelection();
        tabelaVozilo.getSelectionModel().clearSelection();
        tabelaVlasnici.setItems(vozilaDAO.getVlasnici());
        tabelaVozilo.setItems(vozilaDAO.getVozila());
    }

    public void menuXmlEventHandler(ActionEvent actionEvent) {
        menuXml.setSelected(true);
        menuDb.setSelected(false);
        vozilaDAO = new VozilaDAOXML();
        tabelaVlasnici.getSelectionModel().clearSelection();
        tabelaVozilo.getSelectionModel().clearSelection();
        tabelaVlasnici.setItems(vozilaDAO.getVlasnici());
        tabelaVozilo.setItems(vozilaDAO.getVozila());
    }


    public void removeVlasnikEventHandler(ActionEvent actionEvent) {
        if (trenutnoOdabraniVlasnik == null) return;
        Alert potvrda = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK, ButtonType.CANCEL);
        potvrda.setTitle("Brisanje vlasnika");
        potvrda.setHeaderText("Da li ste sigurni da želite obrisati vlasnika?");
        potvrda.showAndWait().ifPresent(izborKorisnika -> {
            if (izborKorisnika == ButtonType.OK) {
                try {
                    vozilaDAO.obrisiVlasnika(trenutnoOdabraniVlasnik);
                } catch (IllegalArgumentException e) {
                    prikazProzoraZaGreskuBrisanjeVlasnika();
                    return;
                }
                tabelaVlasnici.setItems(vozilaDAO.getVlasnici());
                tabelaVlasnici.getSelectionModel().clearSelection();
            }
            else if (izborKorisnika == ButtonType.CANCEL) {
                potvrda.close();
            }
        });
    }

    public void removeVoziloEventHandler(ActionEvent actionEvent) {
        if (trenutnoOdabranoVozilo == null) return;
        Alert potvrda = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK, ButtonType.CANCEL);
        potvrda.setTitle("Brisanje vozila");
        potvrda.setHeaderText("Da li ste sigurni da želite obrisati vozilo?");
        potvrda.showAndWait().ifPresent(izborKorisnika -> {
            if (izborKorisnika == ButtonType.OK) {
                vozilaDAO.obrisiVozilo(trenutnoOdabranoVozilo);
                tabelaVozilo.setItems(vozilaDAO.getVozila());
                tabelaVozilo.getSelectionModel().clearSelection();
            }
            else if (izborKorisnika == ButtonType.CANCEL) {
                potvrda.close();
            }
        });
    }

    private void prikazProzoraZaGreskuBrisanjeVlasnika() {  // Pomocna metoda za brisanje vlasnika
        Alert greska = new Alert(Alert.AlertType.ERROR);
        greska.setTitle("Greška");
        greska.setHeaderText("Ne možete obrisati ovog vlasnika.");
        greska.setContentText("Naime, vlasnik posjeduje bar jedno vozilo.");
        greska.show();
    }

    public void addVlasnikEventHandler(ActionEvent actionEvent) {
        Stage noviStage = null;
        FXMLLoader loader = null;
        VlasnikController vlasnikController = null;
        try {
            loader = new FXMLLoader(getClass().getResource("/fxml/vlasnik.fxml"));
            vlasnikController = new VlasnikController(vozilaDAO, null);
            loader.setController(vlasnikController);
            Parent root = loader.load();
            noviStage = new Stage();
            noviStage.setResizable(false);
            noviStage.setTitle("Dodavanje novog vlasnika");
            noviStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            noviStage.show();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        noviStage.setOnHiding(event -> {
            Platform.runLater(() -> tabelaVlasnici.setItems(vozilaDAO.getVlasnici()));
        });
    }

    public void addVoziloEventHandler(ActionEvent actionEvent) {
        Stage noviStage = null;
        FXMLLoader loader = null;
        VoziloController voziloController = null;
        try {
            loader = new FXMLLoader(getClass().getResource("/fxml/vozilo.fxml"));
            voziloController = new VoziloController(vozilaDAO, null);
            loader.setController(voziloController);
            Parent root = loader.load();
            noviStage = new Stage();
            noviStage.setResizable(false);
            noviStage.setTitle("Dodavanje novog vozila");
            noviStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            noviStage.show();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        noviStage.setOnHiding(event -> {
            Platform.runLater(() -> tabelaVozilo.setItems(vozilaDAO.getVozila()));
        });
    }
}
