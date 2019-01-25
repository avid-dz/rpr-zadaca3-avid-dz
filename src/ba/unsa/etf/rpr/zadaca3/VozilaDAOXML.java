package ba.unsa.etf.rpr.zadaca3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
        boolean pronadjenoMjestoRodjenja = false;
        int maxNedozvoljeniIdMjesta = 0;
        int indeksMjestaRodjenja = 0;
        int indeksMjestaPrebivalista = 0;
        ObservableList<Mjesto> mjesta = getMjesta();
        for (int i = 0; i < mjesta.size(); i++) {
            if (mjesta.get(i).getNaziv().equals(vlasnik.getMjestoRodjenja().getNaziv())) {
                indeksMjestaRodjenja = i;
                pronadjenoMjestoRodjenja = true;
                break;
            }
        }
        boolean pronadjenoMjestoPrebivalista = false;
        for (int i = 0; i < mjesta.size(); i++) {
            if (mjesta.get(i).getNaziv().equals(vlasnik.getMjestoPrebivalista().getNaziv())) {
                indeksMjestaPrebivalista = i;
                pronadjenoMjestoPrebivalista = true;
                break;
            }
        }
        if ((!pronadjenoMjestoRodjenja || !pronadjenoMjestoPrebivalista) && mjesta.size() != 0) {
            maxNedozvoljeniIdMjesta = mjesta.get(0).getId();
            for (int i = 1; i < mjesta.size(); i++) {
                if (mjesta.get(i).getId() > maxNedozvoljeniIdMjesta) {
                    maxNedozvoljeniIdMjesta = mjesta.get(i).getId();
                }
            }
        }
        if (!pronadjenoMjestoRodjenja) {
            maxNedozvoljeniIdMjesta++;
            vlasnik.getMjestoRodjenja().setId(maxNedozvoljeniIdMjesta);
            mjesta.add(vlasnik.getMjestoRodjenja());
        }
        else vlasnik.getMjestoRodjenja().setId(mjesta.get(indeksMjestaRodjenja).getId());
        if (!pronadjenoMjestoPrebivalista) {
            vlasnik.getMjestoPrebivalista().setId(maxNedozvoljeniIdMjesta + 1);
            mjesta.add(vlasnik.getMjestoPrebivalista());
        }
        else vlasnik.getMjestoPrebivalista().setId(mjesta.get(indeksMjestaPrebivalista).getId());
        if (!pronadjenoMjestoRodjenja || !pronadjenoMjestoPrebivalista) {
            ArrayList<Mjesto> mjestoArrayList = new ArrayList<>();
            mjestoArrayList.addAll(mjesta);
            try {
                izlaz = new XMLEncoder(new FileOutputStream("mjesta.xml"));
                izlaz.writeObject(mjestoArrayList);
                close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
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
        if (vlasnik == null) return;
        ObservableList<Vlasnik> vlasnici = getVlasnici();
        boolean pronadjenVlasnik = false;
        int indeks = 0;
        for (int i = 0; i < vlasnici.size(); i++) {
            if (vlasnici.get(i).getId() == vlasnik.getId()) {
                indeks = i;
                pronadjenVlasnik = true;
                break;
            }
        }
        if (!pronadjenVlasnik) return;
        boolean pronadjenoMjestoRodjenja = false;
        int maxNedozvoljeniIdMjesta = 0;
        int indeksMjestaRodjenja = 0;
        int indeksMjestaPrebivalista = 0;
        ObservableList<Mjesto> mjesta = getMjesta();
        for (int i = 0; i < mjesta.size(); i++) {
            if (mjesta.get(i).getNaziv().equals(vlasnik.getMjestoRodjenja().getNaziv())) {
                indeksMjestaRodjenja = i;
                pronadjenoMjestoRodjenja = true;
                break;
            }
        }
        boolean pronadjenoMjestoPrebivalista = false;
        for (int i = 0; i < mjesta.size(); i++) {
            if (mjesta.get(i).getNaziv().equals(vlasnik.getMjestoPrebivalista().getNaziv())) {
                indeksMjestaPrebivalista = i;
                pronadjenoMjestoPrebivalista = true;
                break;
            }
        }
        if ((!pronadjenoMjestoRodjenja || !pronadjenoMjestoPrebivalista) && mjesta.size() != 0) {
            maxNedozvoljeniIdMjesta = mjesta.get(0).getId();
            for (int i = 1; i < mjesta.size(); i++) {
                if (mjesta.get(i).getId() > maxNedozvoljeniIdMjesta) {
                    maxNedozvoljeniIdMjesta = mjesta.get(i).getId();
                }
            }
        }
        if (!pronadjenoMjestoRodjenja) {
            maxNedozvoljeniIdMjesta++;
            vlasnik.getMjestoRodjenja().setId(maxNedozvoljeniIdMjesta);
            mjesta.add(vlasnik.getMjestoRodjenja());
        }
        else vlasnik.getMjestoRodjenja().setId(mjesta.get(indeksMjestaRodjenja).getId());
        if (!pronadjenoMjestoPrebivalista) {
            vlasnik.getMjestoPrebivalista().setId(maxNedozvoljeniIdMjesta + 1);
            mjesta.add(vlasnik.getMjestoPrebivalista());
        }
        else vlasnik.getMjestoPrebivalista().setId(mjesta.get(indeksMjestaPrebivalista).getId());
        if (!pronadjenoMjestoRodjenja || !pronadjenoMjestoPrebivalista) {
            ArrayList<Mjesto> mjestoArrayList = new ArrayList<>();
            mjestoArrayList.addAll(mjesta);
            try {
                izlaz = new XMLEncoder(new FileOutputStream("mjesta.xml"));
                izlaz.writeObject(mjestoArrayList);
                close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        vlasnici.get(indeks).setIme(vlasnik.getIme());
        vlasnici.get(indeks).setPrezime(vlasnik.getPrezime());
        vlasnici.get(indeks).setImeRoditelja(vlasnik.getImeRoditelja());
        vlasnici.get(indeks).setAdresaPrebivalista(vlasnik.getAdresaPrebivalista());
        vlasnici.get(indeks).setDatumRodjenja(vlasnik.getDatumRodjenja());
        vlasnici.get(indeks).setMjestoRodjenja(vlasnik.getMjestoRodjenja());
        vlasnici.get(indeks).setMjestoPrebivalista(vlasnik.getMjestoPrebivalista());
        vlasnici.get(indeks).setJmbg(vlasnik.getJmbg());
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

    public void obrisiVlasnika(Vlasnik vlasnik) {
        if (vlasnik == null) return;
        ObservableList<Vozilo> vozila = getVozila();
        boolean posjedujeBarJednoVozilo = false;
        for (Vozilo v : vozila) {
            if (v.getVlasnik().getId() == vlasnik.getId()) {
                posjedujeBarJednoVozilo = true;
                break;
            }
        }
        if (posjedujeBarJednoVozilo) throw new IllegalArgumentException("Vlasnik posjeduje bar jedno vozilo!");
        ObservableList<Vlasnik> vlasnici = getVlasnici();
        boolean obrisanBarJedan = false;
        for (int i = 0; i < vlasnici.size(); i++) {
            if (vlasnici.get(i).getId() == vlasnik.getId()) {
                vlasnici.remove(i);
                obrisanBarJedan = true;
                break;
            }
        }
        if (obrisanBarJedan) {
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
        ObservableList<Proizvodjac> proizvodjaci = getProizvodjaci();
        boolean pronadjenProizvodjac = false;
        for (int i = 0; i < proizvodjaci.size(); i++) {
            if (vozilo.getProizvodjac().getNaziv().equals(proizvodjaci.get(i).getNaziv())) {
                indeks = i;
                pronadjenProizvodjac = true;
                break;
            }
        }
        if (!pronadjenProizvodjac && proizvodjaci.size() != 0) {
            maxNedozvoljeniIdProizvodjaca = proizvodjaci.get(0).getId();
            for (int i = 1; i < proizvodjaci.size(); i++) {
                if (proizvodjaci.get(i).getId() > maxNedozvoljeniIdProizvodjaca) {
                    maxNedozvoljeniIdProizvodjaca = proizvodjaci.get(i).getId();
                }
            }
        }
        if (!pronadjenProizvodjac) {
            vozilo.getProizvodjac().setId(maxNedozvoljeniIdProizvodjaca + 1);
            proizvodjaci.add(vozilo.getProizvodjac());
            ArrayList<Proizvodjac> proizvodjacArrayList = new ArrayList<>();
            proizvodjacArrayList.addAll(proizvodjaci);
            try {
                izlaz = new XMLEncoder(new FileOutputStream("proizvodjaci.xml"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            izlaz.writeObject(proizvodjacArrayList);
            close();
        }
        else vozilo.getProizvodjac().setId(proizvodjaci.get(indeks).getId());
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
        if (vozilo == null) return;
        boolean pronadjenoVozilo = false;
        int indeks = 0;
        ObservableList<Vozilo> vozila = getVozila();
        for (int i = 0; i < vozila.size(); i++) {
            if (vozila.get(i).getId() == vozilo.getId()) {
                indeks = i;
                pronadjenoVozilo = true;
                break;
            }
        }
        if (!pronadjenoVozilo) return;
        ObservableList<Vlasnik> vlasnici = getVlasnici();
        boolean pronadjenVlasnik = false;
        for (int i = 0; i < vlasnici.size(); i++) {
            if (vlasnici.get(i).getId() == vozilo.getVlasnik().getId()) {
                pronadjenVlasnik = true;
                break;
            }
        }
        if (!pronadjenVlasnik) throw new IllegalArgumentException("Vlasnik ne postoji!");
        ObservableList<Proizvodjac> proizvodjaci = getProizvodjaci();
        int indeksProizvodjaca = 0;
        boolean pronadjenProizvodjac = false;
        for (int i = 0; i < proizvodjaci.size(); i++) {
            if (proizvodjaci.get(i).getNaziv().equals(vozilo.getProizvodjac().getNaziv())) {
                indeksProizvodjaca = i;
                pronadjenProizvodjac = true;
                break;
            }
        }
        int maxNedozvoljeniIdProizvodjaca = 0;
        if (!pronadjenProizvodjac && proizvodjaci.size() != 0) {
            maxNedozvoljeniIdProizvodjaca = proizvodjaci.get(0).getId();
            for (int i = 1; i < proizvodjaci.size(); i++) {
                if (proizvodjaci.get(i).getId() > maxNedozvoljeniIdProizvodjaca) {
                    maxNedozvoljeniIdProizvodjaca = proizvodjaci.get(i).getId();
                }
            }
        }
        if (!pronadjenProizvodjac) {
            vozilo.getProizvodjac().setId(maxNedozvoljeniIdProizvodjaca + 1);
            proizvodjaci.add(vozilo.getProizvodjac());
            ArrayList<Proizvodjac> proizvodjacArrayList = new ArrayList<>();
            proizvodjacArrayList.addAll(proizvodjaci);
            try {
                izlaz = new XMLEncoder(new FileOutputStream("proizvodjaci.xml"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            izlaz.writeObject(proizvodjacArrayList);
            close();
        }
        else vozilo.getProizvodjac().setId(proizvodjaci.get(indeksProizvodjaca).getId());
        vozila.get(indeks).setProizvodjac(vozilo.getProizvodjac());
        vozila.get(indeks).setBrojTablica(vozilo.getBrojTablica());
        vozila.get(indeks).setBrojSasije(vozilo.getBrojSasije());
        vozila.get(indeks).setModel(vozilo.getModel());
        vozila.get(indeks).setVlasnik(vozilo.getVlasnik());
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

    public void obrisiVozilo(Vozilo vozilo) {
        if (vozilo == null) return;
        ObservableList<Vozilo> vozila = getVozila();
        boolean obrisanBarJedan = false;
        for (int i = 0; i < vozila.size(); i++) {
            if (vozila.get(i).getId() == vozilo.getId()) {
                vozila.remove(i);
                obrisanBarJedan = true;
                break;
            }
        }
        if (obrisanBarJedan) {
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
    }

    public void close() {
        if (ulaz != null) ulaz.close();
        if (izlaz != null) izlaz.close();
    }
}
