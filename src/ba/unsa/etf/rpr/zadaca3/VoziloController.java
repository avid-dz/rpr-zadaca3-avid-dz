package ba.unsa.etf.rpr.zadaca3;

public class VoziloController {

    private VozilaDAO vozilaDAO;
    private Vozilo vozilo;

    public VoziloController(VozilaDAO vozilaDAO, Vozilo vozilo) {
        this.vozilaDAO = vozilaDAO;
        this.vozilo = vozilo;
    }
}
