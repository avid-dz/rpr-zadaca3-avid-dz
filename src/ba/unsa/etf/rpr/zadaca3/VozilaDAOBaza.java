package ba.unsa.etf.rpr.zadaca3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class VozilaDAOBaza implements VozilaDAO {

    public ObservableList<Vlasnik> getVlasnici() {
        ObservableList<Vlasnik> vlasnici = FXCollections.observableArrayList();
        Mjesto sarajevo = new Mjesto(0, "sarajevo", "71000");
        Mjesto zenica = new Mjesto(1, "zenica", "73000");
        Vlasnik testTestovic = new Vlasnik(0, "Test", "Testović", "Te", LocalDate.now(), sarajevo,
                "Prva ulica 1", zenica, "1234567890");
        vlasnici.add(testTestovic);
        return vlasnici;
    }

    public ObservableList<Mjesto> getMjesta() {
        return null;
    }

    public ObservableList<Vozilo> getVozila() {
        ObservableList<Vozilo> vozila = FXCollections.observableArrayList();
        Proizvodjac renault = new Proizvodjac(2, "Renault");
        Mjesto sarajevo = new Mjesto(1, "Sarajevo", "71000");
        Vlasnik vlasnik = new Vlasnik(2, "Test", "Testovic", "Te", LocalDate.now(), sarajevo,
                "Prva ulica 1", sarajevo, "1234567890");
        Vozilo vozilo = new Vozilo(0, renault, "Megane", "98765", "E12-K-987", vlasnik);
        vozila.add(vozilo);
        return vozila;
    }

    public void dodajVlasnika(Vlasnik testTestovic) {

    }

    public ObservableList<Proizvodjac> getProizvodjaci() {
        return null;
    }

    public void promijeniVlasnika(Vlasnik vlasnik) {

    }

    public void promijeniVozilo(Vozilo vozilo) {

    }

    public void dodajVozilo(Vozilo vozilo) {

    }

    public void obrisiVozilo(Vozilo vozilo) {

    }

    public void obrisiVlasnika(Vlasnik mehoMehic) {

    }

    public void close() {

    }
}
