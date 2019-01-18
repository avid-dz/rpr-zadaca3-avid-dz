package ba.unsa.etf.rpr.zadaca3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class VozilaDAOBaza implements VozilaDAO {

    private Connection connection;
    private int maxNedozvoljeniIdVlasnika;
    private int maxNedozvoljeniIdVozila;
    private int maxNedozvoljeniIdMjesta;
    private int maxNedozvoljeniIdProizvodjaca;
    private PreparedStatement psDajVlasnike;
    private PreparedStatement psDajVozila;
    private PreparedStatement psDajMjesta;
    private PreparedStatement psDajProizvodjace;
    private PreparedStatement psNadjiMjesto;
    private PreparedStatement psNadjiVozilo;
    private PreparedStatement psNadjiVlasnika;
    private PreparedStatement psNadjiProizvodjaca;

    public VozilaDAOBaza() {
        maxNedozvoljeniIdVlasnika = 0;
        maxNedozvoljeniIdVozila = 0;
        maxNedozvoljeniIdMjesta = 0;
        maxNedozvoljeniIdProizvodjaca = 0;
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String url = "jdbc:sqlite:vozila.db";
        try {
            connection = DriverManager.getConnection(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            psDajVlasnike = connection.prepareStatement
                    ("SELECT * FROM vlasnik ORDER BY id");
            psDajVozila = connection.prepareStatement
                    ("SELECT * FROM vozilo ORDER BY id");
            psDajMjesta = connection.prepareStatement
                    ("SELECT * FROM mjesto ORDER BY naziv");
            psDajProizvodjace = connection.prepareStatement
                    ("SELECT * FROM proizvodjac ORDER BY naziv");
            psNadjiMjesto = connection.prepareStatement
                    ("SELECT * FROM mjesto WHERE id=?");
            psNadjiVlasnika = connection.prepareStatement
                    ("SELECT * FROM vlasnik WHERE id=?");
            psNadjiVozilo = connection.prepareStatement
                    ("SELECT * FROM vozilo WHERE id=?");
            psNadjiProizvodjaca = connection.prepareStatement
                    ("SELECT * FROM proizvodjac WHERE id=?");
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Vlasnik> getVlasnici() {
        ObservableList<Vlasnik> vlasnici = FXCollections.observableArrayList();
        try {
            ResultSet rezultat1 = psDajVlasnike.executeQuery();
            while (rezultat1.next()) {
                int idVlasnika = rezultat1.getInt(1);
                String imeVlasnika = rezultat1.getString(2);
                String prezimeVlasnika = rezultat1.getString(3);
                String imeRoditeljaVlasnika = rezultat1.getString(4);
                LocalDate datumRodjenjaVlasnika = rezultat1.getDate(5).toLocalDate();
                int idMjestaRodjenjaVlasnika = rezultat1.getInt(6);
                String adresaPrebivalistaVlasnika = rezultat1.getString(7);
                int idMjestaPrebivalistaVlasnika = rezultat1.getInt(8);
                String jmbgVlasnika = rezultat1.getString(9);
                Vlasnik trazeni = new Vlasnik(idVlasnika, imeVlasnika, prezimeVlasnika, imeRoditeljaVlasnika,
                        datumRodjenjaVlasnika, nadjiMjestoPoIdju(idMjestaRodjenjaVlasnika), adresaPrebivalistaVlasnika,
                        nadjiMjestoPoIdju(idMjestaPrebivalistaVlasnika), jmbgVlasnika);
                vlasnici.add(trazeni);
            }
            rezultat1.close();
        } catch (Exception e) {

        }
        return vlasnici;
    }

    public ObservableList<Mjesto> getMjesta() {
        ObservableList<Mjesto> mjesta = FXCollections.observableArrayList();
        return mjesta;
    }

    public ObservableList<Proizvodjac> getProizvodjaci() {
        ObservableList<Proizvodjac> proizvodjaci = FXCollections.observableArrayList();
        return proizvodjaci;
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

    public void dodajVlasnika(Vlasnik vlasnik) {

    }

    public void promijeniVlasnika(Vlasnik vlasnik) {

    }

    public void obrisiVlasnika(Vlasnik vlasnik) {

    }

    public void dodajVozilo(Vozilo vozilo) {

    }

    public void promijeniVozilo(Vozilo vozilo) {

    }

    public void obrisiVozilo(Vozilo vozilo) {

    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        connection = null;
    }

    private Mjesto nadjiMjestoPoIdju(int id) {
        String naziv = "";
        String postanskiBroj = "";
        try {
            psNadjiMjesto.setInt(1, id);
            ResultSet rezultat1 = psNadjiMjesto.executeQuery();
            if (!rezultat1.next()) return null;
            naziv = rezultat1.getString(2);
            postanskiBroj = rezultat1.getString(3);
            rezultat1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Mjesto(id, naziv, postanskiBroj);
    }

    private Proizvodjac nadjiProizvodjacaPoIdju(int id) {
        String naziv = "";
        try {
            psNadjiProizvodjaca.setInt(1, id);
            ResultSet rezultat1 = psNadjiProizvodjaca.executeQuery();
            if (!rezultat1.next()) return null;
            naziv = rezultat1.getString(2);
            rezultat1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Proizvodjac(id, naziv);
    }

    private Vlasnik nadjiVlasnikaPoIdju(int id) {
        String imeVlasnika = "";
        String prezimeVlasnika = "";
        String imeRoditeljaVlasnika = "";
        LocalDate datumRodjenjaVlasnika = null;
        int idMjestaRodjenjaVlasnika = 0;
        String adresaPrebivalistaVlasnika = "";
        int idMjestaPrebivalistaVlasnika = 0;
        String jmbgVlasnika = "";
        try {
            psNadjiVlasnika.setInt(1, id);
            ResultSet rezultat1 = psNadjiVlasnika.executeQuery();
            if (!rezultat1.next()) return null;
            imeVlasnika = rezultat1.getString(2);
            prezimeVlasnika = rezultat1.getString(3);
            imeRoditeljaVlasnika = rezultat1.getString(4);
            datumRodjenjaVlasnika = rezultat1.getDate(5).toLocalDate();
            idMjestaRodjenjaVlasnika = rezultat1.getInt(6);
            adresaPrebivalistaVlasnika = rezultat1.getString(7);
            idMjestaPrebivalistaVlasnika = rezultat1.getInt(8);
            jmbgVlasnika = rezultat1.getString(9);
            rezultat1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Vlasnik(id, imeVlasnika, prezimeVlasnika, imeRoditeljaVlasnika,
                datumRodjenjaVlasnika, nadjiMjestoPoIdju(idMjestaRodjenjaVlasnika), adresaPrebivalistaVlasnika,
                nadjiMjestoPoIdju(idMjestaPrebivalistaVlasnika), jmbgVlasnika);
    }

    private Vozilo nadjiVoziloPoIdju(int id) {
        int idProizvodjaca = 0;
        String model = "";
        String brojSasije = "";
        String brojTablica = "";
        int idVlasnika = 0;
        try {
            psNadjiVozilo.setInt(1, id);
            ResultSet rezultat1 = psNadjiVozilo.executeQuery();
            if (!rezultat1.next()) return null;
            idProizvodjaca = rezultat1.getInt(2);
            model = rezultat1.getString(3);
            brojSasije = rezultat1.getString(4);
            brojTablica = rezultat1.getString(5);
            idVlasnika = rezultat1.getInt(6);
            rezultat1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Vozilo(id, nadjiProizvodjacaPoIdju(idProizvodjaca), model, brojSasije, brojTablica,
                nadjiVlasnikaPoIdju(idVlasnika));
    }
}
