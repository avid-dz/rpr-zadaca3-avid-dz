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
    private PreparedStatement psNadjiMjestoPoNazivu;
    private PreparedStatement psNadjiVozilo;
    private PreparedStatement psNadjiVlasnika;
    private PreparedStatement psNadjiProizvodjaca;
    private PreparedStatement psNadjiProizvodjacaPoNazivu;
    private PreparedStatement psMaxIdVlasnika;
    private PreparedStatement psMaxIdMjesta;
    private PreparedStatement psMaxIdProizvodjaca;
    private PreparedStatement psMaxIdVozila;
    private PreparedStatement psDodajVlasnika;
    private PreparedStatement psDodajVozilo;
    private PreparedStatement psIzmijeniVlasnika;
    private PreparedStatement psIzmijeniVozilo;
    private PreparedStatement psObrisiVlasnika;
    private PreparedStatement psObrisiVozilo;
    private PreparedStatement psPretragaIdjevaVlasnikaIzVozila;
    private PreparedStatement psDodajMjesto;
    private PreparedStatement psDodajProizvodjaca;

    public void init() {
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
            psNadjiMjestoPoNazivu = connection.prepareStatement
                    ("SELECT * FROM mjesto WHERE naziv=?");
            psNadjiVlasnika = connection.prepareStatement
                    ("SELECT * FROM vlasnik WHERE id=?");
            psNadjiVozilo = connection.prepareStatement
                    ("SELECT * FROM vozilo WHERE id=?");
            psNadjiProizvodjaca = connection.prepareStatement
                    ("SELECT * FROM proizvodjac WHERE id=?");
            psNadjiProizvodjacaPoNazivu = connection.prepareStatement
                    ("SELECT * FROM proizvodjac WHERE naziv=?");
            psMaxIdVlasnika = connection.prepareStatement
                    ("SELECT MAX(id) FROM vlasnik");
            psMaxIdVozila = connection.prepareStatement
                    ("SELECT MAX(id) FROM vozilo");
            psMaxIdMjesta = connection.prepareStatement
                    ("SELECT MAX(id) FROM mjesto");
            psMaxIdProizvodjaca = connection.prepareStatement
                    ("SELECT MAX(id) FROM proizvodjac");
            psDodajVlasnika = connection.prepareStatement
                    ("INSERT INTO vlasnik (id, ime, prezime, ime_roditelja, datum_rodjenja, mjesto_rodjenja," +
                            "adresa_prebivalista, mjesto_prebivalista, jmbg) VALUES (?,?,?,?,?,?,?,?,?)");
            psDodajVozilo = connection.prepareStatement
                    ("INSERT INTO vozilo (id, proizvodjac, model, broj_sasije, broj_tablica, vlasnik) " +
                            "VALUES (?,?,?,?,?,?)");
            psDodajMjesto = connection.prepareStatement
                    ("INSERT INTO mjesto (id, naziv, postanski_broj) VALUES (?,?,?)");
            psDodajProizvodjaca = connection.prepareStatement
                    ("INSERT INTO proizvodjac (id, naziv) VALUES (?,?)");
            psIzmijeniVlasnika = connection.prepareStatement
                    ("UPDATE vlasnik SET ime=?, prezime=?, ime_roditelja=?, datum_rodjenja=?," +
                            " mjesto_rodjenja=?, adresa_prebivalista=?, mjesto_prebivalista=?, jmbg=? " +
                            "WHERE id=?;");
            psIzmijeniVozilo = connection.prepareStatement
                    ("UPDATE vozilo SET proizvodjac=?, model=?, broj_sasije=?, broj_tablica=?, vlasnik=? " +
                            "WHERE id=?;");
            psObrisiVlasnika = connection.prepareStatement
                    ("DELETE FROM vlasnik WHERE id=?");
            psObrisiVozilo = connection.prepareStatement
                    ("DELETE FROM vozilo WHERE id=?");
            psPretragaIdjevaVlasnikaIzVozila = connection.prepareStatement
                    ("SELECT * FROM vozilo WHERE vlasnik=?");
            ResultSet rezultat1 = psMaxIdMjesta.executeQuery();
            if (!rezultat1.next()) maxNedozvoljeniIdMjesta = 0;
            else maxNedozvoljeniIdMjesta = rezultat1.getInt(1);
            rezultat1 = psMaxIdVlasnika.executeQuery();
            if (!rezultat1.next()) maxNedozvoljeniIdVlasnika = 0;
            else maxNedozvoljeniIdVlasnika = rezultat1.getInt(1);
            rezultat1 = psMaxIdProizvodjaca.executeQuery();
            if (!rezultat1.next()) maxNedozvoljeniIdProizvodjaca = 0;
            else maxNedozvoljeniIdProizvodjaca = rezultat1.getInt(1);
            rezultat1 = psMaxIdVozila.executeQuery();
            if (!rezultat1.next()) maxNedozvoljeniIdVozila = 0;
            else maxNedozvoljeniIdVozila = rezultat1.getInt(1);
            rezultat1.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Vlasnik> getVlasnici() {
        init();
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
        close();
        return vlasnici;
    }

    public ObservableList<Mjesto> getMjesta() {
        init();
        ObservableList<Mjesto> mjesta = FXCollections.observableArrayList();
        try {
            ResultSet rezultat1 = psDajMjesta.executeQuery();
            while (rezultat1.next()) {
                int idMjesta = rezultat1.getInt(1);
                String nazivMjesta = rezultat1.getString(2);
                String postanskiBrojMjesta = rezultat1.getString(3);
                Mjesto trazeni = new Mjesto(idMjesta, nazivMjesta, postanskiBrojMjesta);
                mjesta.add(trazeni);
            }
            rezultat1.close();
        } catch (Exception e) {

        }
        close();
        return mjesta;
    }

    public ObservableList<Proizvodjac> getProizvodjaci() {
        init();
        ObservableList<Proizvodjac> proizvodjaci = FXCollections.observableArrayList();
        try {
            ResultSet rezultat1 = psDajProizvodjace.executeQuery();
            while (rezultat1.next()) {
                int idProizvodjaca = rezultat1.getInt(1);
                String nazivProizvodjaca = rezultat1.getString(2);
                Proizvodjac trazeni = new Proizvodjac(idProizvodjaca, nazivProizvodjaca);
                proizvodjaci.add(trazeni);
            }
            rezultat1.close();
        } catch (Exception e) {

        }
        close();
        return proizvodjaci;
    }

    public ObservableList<Vozilo> getVozila() {
        init();
        ObservableList<Vozilo> vozila = FXCollections.observableArrayList();
        try {
            ResultSet rezultat1 = psDajVozila.executeQuery();
            while (rezultat1.next()) {
                int idVozila = rezultat1.getInt(1);
                int idProizvodjaca = rezultat1.getInt(2);
                String model = rezultat1.getString(3);
                String brojSasije = rezultat1.getString(4);
                String brojTablica = rezultat1.getString(5);
                int idVlasnika = rezultat1.getInt(6);
                Vozilo trazeni = new Vozilo(idVozila, nadjiProizvodjacaPoIdju(idProizvodjaca), model, brojSasije,
                        brojTablica, nadjiVlasnikaPoIdju(idVlasnika));
                vozila.add(trazeni);
            }
            rezultat1.close();
        } catch (Exception e) {

        }
        close();
        return vozila;
    }

    public void dodajVlasnika(Vlasnik vlasnik) {
        if (vlasnik == null) return;
        init();
        maxNedozvoljeniIdVlasnika++;
        vlasnik.setId(maxNedozvoljeniIdVlasnika);
        try {
            if (nadjiMjestoPoNazivu(vlasnik.getMjestoRodjenja().getNaziv()) == null) {
                maxNedozvoljeniIdMjesta++;
                vlasnik.getMjestoRodjenja().setId(maxNedozvoljeniIdMjesta);
                psDodajMjesto.setInt(1, vlasnik.getMjestoRodjenja().getId());
                psDodajMjesto.setString(2, vlasnik.getMjestoRodjenja().getNaziv());
                psDodajMjesto.setString(3, vlasnik.getMjestoRodjenja().getPostanskiBroj());
                psDodajMjesto.executeUpdate();
            }
            else {
                vlasnik.getMjestoRodjenja().setId(nadjiMjestoPoNazivu(vlasnik.getMjestoRodjenja().getNaziv()).getId());
                vlasnik.getMjestoRodjenja().setPostanskiBroj
                        (nadjiMjestoPoNazivu(vlasnik.getMjestoRodjenja().getNaziv()).getPostanskiBroj());
            }
            if (nadjiMjestoPoNazivu(vlasnik.getMjestoPrebivalista().getNaziv()) == null) {
                maxNedozvoljeniIdMjesta++;
                vlasnik.getMjestoPrebivalista().setId(maxNedozvoljeniIdMjesta);
                psDodajMjesto.setInt(1, vlasnik.getMjestoPrebivalista().getId());
                psDodajMjesto.setString(2, vlasnik.getMjestoPrebivalista().getNaziv());
                psDodajMjesto.setString(3, vlasnik.getMjestoPrebivalista().getPostanskiBroj());
                psDodajMjesto.executeUpdate();
            }
            else {
                vlasnik.getMjestoPrebivalista().setId
                        (nadjiMjestoPoNazivu(vlasnik.getMjestoPrebivalista().getNaziv()).getId());
                vlasnik.getMjestoPrebivalista().setPostanskiBroj
                        (nadjiMjestoPoNazivu(vlasnik.getMjestoPrebivalista().getNaziv()).getPostanskiBroj());
            }
            psDodajVlasnika.setInt(1, vlasnik.getId());
            psDodajVlasnika.setString(2, vlasnik.getIme());
            psDodajVlasnika.setString(3, vlasnik.getPrezime());
            psDodajVlasnika.setString(4, vlasnik.getImeRoditelja());
            psDodajVlasnika.setDate(5, Date.valueOf(vlasnik.getDatumRodjenja()));
            psDodajVlasnika.setInt(6, vlasnik.getMjestoRodjenja().getId());
            psDodajVlasnika.setString(7, vlasnik.getAdresaPrebivalista());
            psDodajVlasnika.setInt(8, vlasnik.getMjestoPrebivalista().getId());
            psDodajVlasnika.setString(9, vlasnik.getJmbg());
            psDodajVlasnika.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
    }

    public void promijeniVlasnika(Vlasnik vlasnik) {
        if (vlasnik == null) return;
        init();
        try {
            if (nadjiMjestoPoNazivu(vlasnik.getMjestoRodjenja().getNaziv()) == null) {
                maxNedozvoljeniIdMjesta++;
                vlasnik.getMjestoRodjenja().setId(maxNedozvoljeniIdMjesta);
                psDodajMjesto.setInt(1, vlasnik.getMjestoRodjenja().getId());
                psDodajMjesto.setString(2, vlasnik.getMjestoRodjenja().getNaziv());
                psDodajMjesto.setString(3, vlasnik.getMjestoRodjenja().getPostanskiBroj());
                psDodajMjesto.executeUpdate();
            }
            else {
                vlasnik.getMjestoRodjenja().setId(nadjiMjestoPoNazivu(vlasnik.getMjestoRodjenja().getNaziv()).getId());
                vlasnik.getMjestoRodjenja().setPostanskiBroj
                        (nadjiMjestoPoNazivu(vlasnik.getMjestoRodjenja().getNaziv()).getPostanskiBroj());
            }
            if (nadjiMjestoPoNazivu(vlasnik.getMjestoPrebivalista().getNaziv()) == null) {
                maxNedozvoljeniIdMjesta++;
                vlasnik.getMjestoPrebivalista().setId(maxNedozvoljeniIdMjesta);
                psDodajMjesto.setInt(1, vlasnik.getMjestoPrebivalista().getId());
                psDodajMjesto.setString(2, vlasnik.getMjestoPrebivalista().getNaziv());
                psDodajMjesto.setString(3, vlasnik.getMjestoPrebivalista().getPostanskiBroj());
                psDodajMjesto.executeUpdate();
            }
            else {
                vlasnik.getMjestoPrebivalista().setId
                        (nadjiMjestoPoNazivu(vlasnik.getMjestoPrebivalista().getNaziv()).getId());
                vlasnik.getMjestoPrebivalista().setPostanskiBroj
                        (nadjiMjestoPoNazivu(vlasnik.getMjestoPrebivalista().getNaziv()).getPostanskiBroj());
            }
            psIzmijeniVlasnika.setString(1, vlasnik.getIme());
            psIzmijeniVlasnika.setString(2, vlasnik.getPrezime());
            psIzmijeniVlasnika.setString(3, vlasnik.getImeRoditelja());
            psIzmijeniVlasnika.setDate(4, Date.valueOf(vlasnik.getDatumRodjenja()));
            psIzmijeniVlasnika.setInt(5, vlasnik.getMjestoRodjenja().getId());
            psIzmijeniVlasnika.setString(6, vlasnik.getAdresaPrebivalista());
            psIzmijeniVlasnika.setInt(7, vlasnik.getMjestoPrebivalista().getId());
            psIzmijeniVlasnika.setString(8, vlasnik.getJmbg());
            psIzmijeniVlasnika.setInt(9, vlasnik.getId());
            psIzmijeniVlasnika.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
    }

    public void obrisiVlasnika(Vlasnik vlasnik) {
        if (vlasnik == null) return;
        init();
        try {
            psPretragaIdjevaVlasnikaIzVozila.setInt(1, vlasnik.getId());
            ResultSet rezultat1 = psPretragaIdjevaVlasnikaIzVozila.executeQuery();
            if (rezultat1.next()) {
                close();
                throw new IllegalArgumentException("Vlasnik posjeduje bar jedno vozilo!");
            }
            psObrisiVlasnika.setInt(1, vlasnik.getId());
            psObrisiVlasnika.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
    }

    public void dodajVozilo(Vozilo vozilo) {
        if (vozilo == null) return;
        init();
        if (nadjiVlasnikaPoIdju(vozilo.getVlasnik().getId()) == null) {
            close();
            throw new IllegalArgumentException("Ne postoji vlasnik!");
        }
        maxNedozvoljeniIdVozila++;
        vozilo.setId(maxNedozvoljeniIdVozila);
        try {
            if (nadjiProizvodjacaPoNazivu(vozilo.getProizvodjac().getNaziv()) == null) {
                maxNedozvoljeniIdProizvodjaca++;
                vozilo.getProizvodjac().setId(maxNedozvoljeniIdProizvodjaca);
                psDodajProizvodjaca.setInt(1, vozilo.getProizvodjac().getId());
                psDodajProizvodjaca.setString(2, vozilo.getProizvodjac().getNaziv());
                psDodajProizvodjaca.executeUpdate();
            }
            else {
                vozilo.getProizvodjac().setId(nadjiProizvodjacaPoNazivu(vozilo.getProizvodjac().getNaziv()).getId());
            }
            psDodajVozilo.setInt(1, vozilo.getId());
            psDodajVozilo.setInt(2, vozilo.getProizvodjac().getId());
            psDodajVozilo.setString(3, vozilo.getModel());
            psDodajVozilo.setString(4, vozilo.getBrojSasije());
            psDodajVozilo.setString(5, vozilo.getBrojTablica());
            psDodajVozilo.setInt(6, vozilo.getVlasnik().getId());
            psDodajVozilo.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
    }

    public void promijeniVozilo(Vozilo vozilo) {
        if (vozilo == null) return;
        init();
        if (nadjiVlasnikaPoIdju(vozilo.getVlasnik().getId()) == null) {
            close();
            throw new IllegalArgumentException("Ne postoji vlasnik!");
        }
        try {
            if (nadjiProizvodjacaPoNazivu(vozilo.getProizvodjac().getNaziv()) == null) {
                maxNedozvoljeniIdProizvodjaca++;
                vozilo.getProizvodjac().setId(maxNedozvoljeniIdProizvodjaca);
                psDodajProizvodjaca.setInt(1, vozilo.getProizvodjac().getId());
                psDodajProizvodjaca.setString(2, vozilo.getProizvodjac().getNaziv());
                psDodajProizvodjaca.executeUpdate();
            }
            psIzmijeniVozilo.setInt(1, vozilo.getProizvodjac().getId());
            psIzmijeniVozilo.setString(2, vozilo.getModel());
            psIzmijeniVozilo.setString(3, vozilo.getBrojSasije());
            psIzmijeniVozilo.setString(4, vozilo.getBrojTablica());
            psIzmijeniVozilo.setInt(5, vozilo.getVlasnik().getId());
            psIzmijeniVozilo.setInt(6, vozilo.getId());
            psIzmijeniVozilo.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
    }

    public void obrisiVozilo(Vozilo vozilo) {
        init();
        try {
            psObrisiVozilo.setInt(1, vozilo.getId());
            psObrisiVozilo.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
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

    private Mjesto nadjiMjestoPoNazivu(String naziv) {
        int id = 0;
        String postanskiBroj = "";
        try {
            psNadjiMjestoPoNazivu.setString(1, naziv);
            ResultSet rezultat1 = psNadjiMjestoPoNazivu.executeQuery();
            if (!rezultat1.next()) return null;
            id = rezultat1.getInt(1);
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

    private Proizvodjac nadjiProizvodjacaPoNazivu(String naziv) {
        int id = 0;
        try {
            psNadjiProizvodjacaPoNazivu.setString(1, naziv);
            ResultSet rezultat1 = psNadjiProizvodjacaPoNazivu.executeQuery();
            if (!rezultat1.next()) return null;
            id = rezultat1.getInt(1);
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
