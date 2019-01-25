package ba.unsa.etf.rpr.zadaca3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class VozilaDAOXML implements VozilaDAO {

    private XMLDecoder ulaz;
    private XMLEncoder izlaz;

    public VozilaDAOXML() {
        ulaz = null;
        izlaz = null;
    }

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

    public void dodajVlasnika(Vlasnik vlasnik) {
        if (vlasnik == null) return;
        int maxNedozvoljeniIdVlasnika = 0;
        ObservableList<Vlasnik> vlasnici = getVlasnici();
        if (vlasnici.size() != 0) {
            maxNedozvoljeniIdVlasnika = vlasnici.get(0).getId();
            for (int i = 1; i < vlasnici.size(); i++) {
                if (vlasnici.get(i).getId() > maxNedozvoljeniIdVlasnika) {
                    maxNedozvoljeniIdVlasnika = vlasnici.get(i).getId();
                }
            }
        }
        vlasnik.setId(maxNedozvoljeniIdVlasnika + 1);
        vlasnici.add(vlasnik);
        ArrayList<Vlasnik> vlasnikArrayList = new ArrayList<>();
        vlasnikArrayList.addAll(vlasnici);
        try {
            izlaz = new XMLEncoder(new FileOutputStream("vlasnici.xml"));
            izlaz.writeObject(vlasnikArrayList);
            close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void promijeniVlasnika(Vlasnik vlasnik) {

    }

    public void obrisiVlasnika(Vlasnik vlasnik) {

    }

    public void dodajVozilo(Vozilo vozilo) {
        if (vozilo == null) return;
        ObservableList<Vlasnik> vlasnici = getVlasnici();
        if (vlasnici.size() == 0) throw new IllegalArgumentException("Vlasnik ne postoji!");
        boolean pronadjenVlasnik = false;
        for (int i = 0; i < vlasnici.size(); i++) {
            if (vlasnici.get(i).getId() == vozilo.getVlasnik().getId()) {
                pronadjenVlasnik = true;
                break;
            }
        }
        if (!pronadjenVlasnik) throw new IllegalArgumentException("Vlasnik ne postoji!");
        int maxNedozvoljeniIdVozila = 0;
        ObservableList<Vozilo> vozila = getVozila();
        if (vozila.size() != 0) {
            maxNedozvoljeniIdVozila = vozila.get(0).getId();
            for (int i = 1; i < vozila.size(); i++) {
                if (vozila.get(i).getId() > maxNedozvoljeniIdVozila) {
                    maxNedozvoljeniIdVozila = vozila.get(i).getId();
                }
            }
        }
        vozilo.setId(maxNedozvoljeniIdVozila + 1);
        int maxNedozvoljeniIdProizvodjaca = 0;
        int indeks = 0;
        boolean pronadjenProizvodjac = false;
        for (int i = 0; i < vozila.size(); i++) {
            if (vozila.get(i).getProizvodjac().getNaziv().equals(vozilo.getProizvodjac().getNaziv())) {
                indeks = i;
                pronadjenProizvodjac = true;
                break;
            }
        }
        if (!pronadjenProizvodjac && vozila.size() != 0) {
            maxNedozvoljeniIdProizvodjaca = vozila.get(0).getProizvodjac().getId();
            for (int i = 1; i < vozila.size(); i++) {
                if (vozila.get(i).getProizvodjac().getId() > maxNedozvoljeniIdProizvodjaca) {
                    maxNedozvoljeniIdProizvodjaca = vozila.get(i).getProizvodjac().getId();
                }
            }
        }
        if (!pronadjenProizvodjac) vozilo.getProizvodjac().setId(maxNedozvoljeniIdProizvodjaca + 1);
        else vozilo.getProizvodjac().setId(vozila.get(indeks).getProizvodjac().getId());
        vozila.add(vozilo);
        ArrayList<Vozilo> voziloArrayList = new ArrayList<>();
        voziloArrayList.addAll(vozila);
        try {
            izlaz = new XMLEncoder(new FileOutputStream("vozila.xml"));
            izlaz.writeObject(voziloArrayList);
            close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void promijeniVozilo(Vozilo vozilo) {

    }

    public void obrisiVozilo(Vozilo vozilo) {

    }

    public void close() {
        if (ulaz != null) ulaz.close();
        if (izlaz != null) izlaz.close();
    }
}
