package ba.unsa.etf.rpr.zadaca3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.beans.XMLDecoder;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class VozilaDAOXML implements VozilaDAO {

    private XMLDecoder ulaz;

    public ObservableList<Vlasnik> getVlasnici() {
        ArrayList<Vlasnik> vlasnikArrayList = null;
        try {
            ulaz = new XMLDecoder(new FileInputStream("vlasnici.xml"));
            vlasnikArrayList = (ArrayList<Vlasnik>) ulaz.readObject();
            close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        Set<Vlasnik> skupZaSortiranje = new TreeSet<>((s1, s2) -> {
            if (s1.getId() < s2.getId()) return -1;
            else if (s1.getId() == s2.getId()) return 0;
            else return 1;
        });
        skupZaSortiranje.addAll(vlasnikArrayList);
        vlasnikArrayList.clear();
        vlasnikArrayList.addAll(skupZaSortiranje);
        ObservableList<Vlasnik> vlasnici = FXCollections.observableArrayList(vlasnikArrayList);
        return vlasnici;
    }

    public ObservableList<Mjesto> getMjesta() {
        ArrayList<Mjesto> mjestoArrayList = null;
        try {
            ulaz = new XMLDecoder(new FileInputStream("mjesta.xml"));
            mjestoArrayList = (ArrayList<Mjesto>) ulaz.readObject();
            close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        Set<Mjesto> skupZaSortiranje = new TreeSet<>();
        skupZaSortiranje.addAll(mjestoArrayList);
        mjestoArrayList.clear();
        mjestoArrayList.addAll(skupZaSortiranje);
        ObservableList<Mjesto> mjesta = FXCollections.observableArrayList(mjestoArrayList);
        return mjesta;
    }

    public ObservableList<Vozilo> getVozila() {
        ArrayList<Vozilo> voziloArrayList = null;
        try {
            ulaz = new XMLDecoder(new FileInputStream("vozila.xml"));
            voziloArrayList = (ArrayList<Vozilo>) ulaz.readObject();
            close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        Set<Vozilo> skupZaSortiranje = new TreeSet<>();
        skupZaSortiranje.addAll(voziloArrayList);
        voziloArrayList.clear();
        voziloArrayList.addAll(skupZaSortiranje);
        ObservableList<Vozilo> vozila = FXCollections.observableArrayList(voziloArrayList);
        for (Vozilo v : vozila) {
            for (Vlasnik vl : getVlasnici()) {
                if (vl.getId() == v.getVlasnik().getId()) {
                    v.getVlasnik().setDatumRodjenja(vl.getDatumRodjenja());
                    break;
                }
            }
        }
        return vozila;
    }

    public ObservableList<Proizvodjac> getProizvodjaci() {
        ArrayList<Proizvodjac> proizvodjacArrayList = null;
        try {
            ulaz = new XMLDecoder(new FileInputStream("proizvodjaci.xml"));
            proizvodjacArrayList = (ArrayList<Proizvodjac>) ulaz.readObject();
            close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        Set<Proizvodjac> skupZaSortiranje = new TreeSet<>();
        skupZaSortiranje.addAll(proizvodjacArrayList);
        proizvodjacArrayList.clear();
        proizvodjacArrayList.addAll(skupZaSortiranje);
        ObservableList<Proizvodjac> proizvodjaci= FXCollections.observableArrayList(proizvodjacArrayList);
        return proizvodjaci;
    }

    public void dodajVlasnika(Vlasnik testTestovic) {

    }

    public void promijeniVlasnika(Vlasnik vlasnik) {

    }

    public void obrisiVlasnika(Vlasnik mehoMehic) {

    }

    public void dodajVozilo(Vozilo vozilo) {

    }

    public void promijeniVozilo(Vozilo vozilo) {

    }

    public void obrisiVozilo(Vozilo vozilo) {

    }

    public void close() {
        ulaz.close();
    }
}
