package ba.unsa.etf.rpr.zadaca3;

import javafx.fxml.FXML;

public class VoziloController {

    private VozilaDAO vozilaDAO;
    private Vozilo vozilo;

    public VoziloController(VozilaDAO vozilaDAO, Vozilo vozilo) {
        this.vozilaDAO = vozilaDAO;
        this.vozilo = vozilo;
    }

    @FXML
    public void initialize() {

    }
}
