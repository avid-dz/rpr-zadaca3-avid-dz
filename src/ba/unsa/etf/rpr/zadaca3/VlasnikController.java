package ba.unsa.etf.rpr.zadaca3;

public class VlasnikController {

    private VozilaDAO vozilaDAO;
    private Vlasnik vlasnik;

    public VlasnikController(VozilaDAO vozilaDAO, Vlasnik vlasnik) {
        this.vozilaDAO = vozilaDAO;
        this.vlasnik = vlasnik;
    }
}
