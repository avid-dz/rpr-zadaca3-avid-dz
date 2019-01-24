package ba.unsa.etf.rpr.zadaca3;

import java.io.Serializable;

public class Mjesto implements Comparable<Mjesto>, Serializable {

    private int id;
    private String naziv;
    private String postanskiBroj;

    public Mjesto() {
        id = 0;
        naziv = "";
        postanskiBroj = "";
    }

    public Mjesto(int id, String naziv, String postanskiBroj) {
        this.id = id;
        this.naziv = naziv;
        this.postanskiBroj = postanskiBroj;
    }

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getPostanskiBroj() {
        return postanskiBroj;
    }

    public void setPostanskiBroj(String postanskiBroj) {
        this.postanskiBroj = postanskiBroj;
    }

    @Override
    public String toString() {
        return naziv;
    }

    @Override
    public int compareTo(Mjesto o) {
        return getNaziv().compareTo(o.getNaziv());
    }
}
