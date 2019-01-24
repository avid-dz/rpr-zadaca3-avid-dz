package ba.unsa.etf.rpr.zadaca3;

import java.io.Serializable;
import java.time.LocalDate;

public class Vlasnik implements Comparable<Vlasnik>, Serializable {

    private int id;
    private String ime;
    private String prezime;
    private String imeRoditelja;
    private LocalDate datumRodjenja;
    private Mjesto mjestoRodjenja;
    private String adresaPrebivalista;
    private Mjesto mjestoPrebivalista;
    private String jmbg;

    public Vlasnik() {
        id = 0;
        ime = "";
        prezime = "";
        imeRoditelja = "";
        datumRodjenja = LocalDate.now();
        mjestoRodjenja = null;
        adresaPrebivalista = "";
        mjestoPrebivalista = null;
        jmbg = "";
    }

    public Vlasnik(int id, String ime, String prezime, String imeRoditelja, LocalDate datumRodjenja,
                   Mjesto mjestoRodjenja, String adresaPrebivalista, Mjesto mjestoPrebivalista, String jmbg) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.imeRoditelja = imeRoditelja;
        this.datumRodjenja = datumRodjenja;
        this.mjestoRodjenja = mjestoRodjenja;
        this.adresaPrebivalista = adresaPrebivalista;
        this.mjestoPrebivalista = mjestoPrebivalista;
        this.jmbg = jmbg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getImeRoditelja() {
        return imeRoditelja;
    }

    public void setImeRoditelja(String imeRoditelja) {
        this.imeRoditelja = imeRoditelja;
    }

    public LocalDate getDatumRodjenja() {
        return datumRodjenja;
    }

    public void setDatumRodjenja(LocalDate datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
    }

    public Mjesto getMjestoRodjenja() {
        return mjestoRodjenja;
    }

    public void setMjestoRodjenja(Mjesto mjestoRodjenja) {
        this.mjestoRodjenja = mjestoRodjenja;
    }

    public String getAdresaPrebivalista() {
        return adresaPrebivalista;
    }

    public void setAdresaPrebivalista(String adresaPrebivalista) {
        this.adresaPrebivalista = adresaPrebivalista;
    }

    public Mjesto getMjestoPrebivalista() {
        return mjestoPrebivalista;
    }

    public void setMjestoPrebivalista(Mjesto mjestoPrebivalista) {
        this.mjestoPrebivalista = mjestoPrebivalista;
    }

    public String getJmbg() {
        return jmbg;
    }

    public void setJmbg(String jmbg) {
        this.jmbg = jmbg;
    }

    public String getImeIPrezime() {
        return ime + " " + prezime;
    }

    public void setDatumRodjenjaDani(long datumRodjenjaDani) {

    }

    public long getDatumRodjenjaDani() {
        return 0;
    }

    @Override
    public String toString() {
        return prezime + " " + ime;
    }


    @Override
    public int compareTo(Vlasnik o) {
        return toString().compareTo(o.toString());
    }
}
