package ba.unsa.etf.rpr.zadaca3;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class VlasnikController {

    private VozilaDAO vozilaDAO;
    private Vlasnik vlasnik;
    public Button cancelButton;

    public VlasnikController(VozilaDAO vozilaDAO, Vlasnik vlasnik) {
        this.vozilaDAO = vozilaDAO;
        this.vlasnik = vlasnik;
    }

    public void cancelEventHandler(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void okEventHandler(ActionEvent actionEvent) {

    }
}
