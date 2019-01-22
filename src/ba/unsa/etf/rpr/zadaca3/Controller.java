package ba.unsa.etf.rpr.zadaca3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class Controller {

    public TableColumn kolonaImeIPrezimeVlasnika;
    private VozilaDAO vozilaDAO;
    public TableView tabelaVlasnici;
    public TableView tabelaVozilo;
    public RadioMenuItem menuDb;
    public RadioMenuItem menuXml;

    public Controller() {
        vozilaDAO = new VozilaDAOBaza();
    }

    @FXML
    public void initialize() {
        tabelaVlasnici.setEditable(true);
        tabelaVozilo.setEditable(true);
        menuDb.setSelected(true);
        menuXml.setSelected(false);
        tabelaVlasnici.setItems(vozilaDAO.getVlasnici());
        tabelaVozilo.setItems(vozilaDAO.getVozila());
    }

    public void menuDbEventHandler(ActionEvent actionEvent) {
        menuXml.setSelected(false);
        vozilaDAO = new VozilaDAOBaza();
        tabelaVlasnici.setItems(vozilaDAO.getVlasnici());
        tabelaVozilo.setItems(vozilaDAO.getVozila());
    }

    public void menuXmlEventHandler(ActionEvent actionEvent) {
        menuDb.setSelected(false);
        vozilaDAO = new VozilaDAOXML();
        tabelaVlasnici.setItems(vozilaDAO.getVlasnici());
        tabelaVozilo.setItems(vozilaDAO.getVozila());
    }
}
