package ba.unsa.etf.rpr.zadaca3;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class VoziloController {

    private VozilaDAO vozilaDAO;
    private Vozilo vozilo;
    public Button cancelButton;
    public Button okButton;
    public ComboBox vlasnikCombo;
    public ComboBox proizvodjacCombo;
    public TextField modelField;
    public TextField brojSasijeField;
    public TextField brojTablicaField;
    private boolean validanModelVozila;
    private boolean validanBrojSasijeVozila;
    private boolean validanBrojTablicaVozila;
    private boolean validanProizvodjacVozila;

    public VoziloController(VozilaDAO vozilaDAO, Vozilo vozilo) {
        this.vozilaDAO = vozilaDAO;
        this.vozilo = vozilo;
    }

    @FXML
    public void initialize() {
        vlasnikCombo.setItems(vozilaDAO.getVlasnici());
        FXCollections.sort(vlasnikCombo.getItems());
        proizvodjacCombo.setItems(vozilaDAO.getProizvodjaci());
    }

    public void cancelEventHandler(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void okEventHandler(ActionEvent actionEvent) {
        validacijaModela(modelField.getText());
        validacijaBrojaSasije(brojSasijeField.getText());
        validacijaBrojaTablica(brojTablicaField.getText());
        validacijaProizvodjaca(proizvodjacCombo);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (validnaForma()) {
            Proizvodjac proizvodjac = new Proizvodjac(0, proizvodjacCombo.getValue().toString());
            if (vozilo == null) {
                vozilaDAO.dodajVozilo(new Vozilo(0, proizvodjac, modelField.getText(), brojSasijeField.getText(),
                        brojTablicaField.getText(), (Vlasnik) vlasnikCombo.getValue()));
            }
            else {
                vozilo.setVlasnik((Vlasnik) vlasnikCombo.getValue());
                vozilo.setModel(modelField.getText());
                vozilo.setBrojSasije(brojSasijeField.getText());
                vozilo.setBrojTablica(brojTablicaField.getText());
                vozilo.setProizvodjac(proizvodjac);
                vozilaDAO.promijeniVozilo(vozilo);
            }
            Stage stage = (Stage) okButton.getScene().getWindow();
            Platform.runLater(() -> stage.close());
        }
    }

    private boolean validanModel(String n) {
        return !n.trim().isEmpty();
    }

    private boolean validanBrojSasije(String n) {
        return !n.trim().isEmpty();
    }

    private boolean validanBrojTablica(String n) {
        if (n.trim().isEmpty()) return false;
        if (n.length() != 9) return false;
        String dozvoljeni = "AEJKMOT";
        if (!Character.isLetter(n.charAt(0))) return false;
        if (!dozvoljeni.contains(String.valueOf(n.charAt(0)))) return false;
        if (!Character.isDigit(n.charAt(1))) return false;
        if (!Character.isDigit(n.charAt(2))) return false;
        if (n.charAt(3) != '-') return false;
        if (!Character.isLetter(n.charAt(4))) return false;
        if (!dozvoljeni.contains(String.valueOf(n.charAt(4)))) return false;
        if (n.charAt(5) != '-') return false;
        if (!Character.isDigit(n.charAt(6))) return false;
        if (!Character.isDigit(n.charAt(7))) return false;
        if (!Character.isDigit(n.charAt(8))) return false;
        return true;
    }

    private boolean validanProizvodjac(ComboBox proizvodjacCombo) {
        if (proizvodjacCombo.getEditor().getText().trim().isEmpty()) return false;
        return true;
    }

    private void validacijaModela(String n) {
        if (validanModel(n)) {
            modelField.getStyleClass().removeAll("invalidField");
            modelField.getStyleClass().add("validField");
            validanModelVozila = true;
        } else {
            modelField.getStyleClass().removeAll("validField");
            modelField.getStyleClass().add("invalidField");
            validanModelVozila = false;
        }
    }

    private void validacijaBrojaSasije(String n) {
        if (validanBrojSasije(n)) {
            brojSasijeField.getStyleClass().removeAll("invalidField");
            brojSasijeField.getStyleClass().add("validField");
            validanBrojSasijeVozila = true;
        } else {
            brojSasijeField.getStyleClass().removeAll("validField");
            brojSasijeField.getStyleClass().add("invalidField");
            validanBrojSasijeVozila = false;
        }
    }

    private void validacijaBrojaTablica(String n) {
        if (validanBrojTablica(n)) {
            brojTablicaField.getStyleClass().removeAll("invalidField");
            brojTablicaField.getStyleClass().add("validField");
            validanBrojTablicaVozila = true;
        } else {
            brojTablicaField.getStyleClass().removeAll("validField");
            brojTablicaField.getStyleClass().add("invalidField");
            validanBrojTablicaVozila = false;
        }
    }

    private void validacijaProizvodjaca(ComboBox proizvodjacCombo) {
        if (validanProizvodjac(proizvodjacCombo)) {
            proizvodjacCombo.getStyleClass().removeAll("invalidField");
            proizvodjacCombo.getStyleClass().add("validField");
            validanProizvodjacVozila = true;
        } else {
            proizvodjacCombo.getStyleClass().removeAll("validField");
            proizvodjacCombo.getStyleClass().add("invalidField");
            validanProizvodjacVozila = false;
        }
    }

    private boolean validnaForma() {
        return validanModelVozila && validanBrojSasijeVozila && validanBrojTablicaVozila && validanProizvodjacVozila;
    }
}
