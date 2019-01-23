package ba.unsa.etf.rpr.zadaca3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class VoziloController {

    private VozilaDAO vozilaDAO;
    private Vozilo vozilo;
    public Button cancelButton;
    public Button okButton;

    public VoziloController(VozilaDAO vozilaDAO, Vozilo vozilo) {
        this.vozilaDAO = vozilaDAO;
        this.vozilo = vozilo;
    }

    @FXML
    public void initialize() {

    }

    public void cancelEventHandler(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void okEventHandler(ActionEvent actionEvent) {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }
}
