package ba.unsa.etf.rpr.zadaca3;

import java.io.Serializable;

public class Proizvodjac implements Comparable<Proizvodjac>, Serializable {

    private int id;
    private String naziv;

    public Proizvodjac() {
        id = 0;
        naziv = "";
    }

    public Proizvodjac(int id, String naziv) {
        this.id = id;
        this.naziv = naziv;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    @Override
    public String toString() {
        return naziv;
    }

    @Override
    public int compareTo(Proizvodjac o) {
        return getNaziv().compareTo(o.getNaziv());
    }
}
