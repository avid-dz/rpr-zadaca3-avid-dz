package ba.unsa.etf.rpr.zadaca3;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class VlasnikController {

    private VozilaDAO vozilaDAO;
    private Vlasnik vlasnik;
    public Button cancelButton;
    public Button okButton;
    public TextField imeField;
    public TextField prezimeField;
    public TextField imeRoditeljaField;
    public TextField adresaField;
    public TextField jmbgField;
    public DatePicker datumField;
    public ComboBox mjestoRodjenja;
    public ComboBox adresaMjesto;
    public TextField postanskiBrojField;
    private boolean validnoImeVlasnika;
    private boolean validnoPrezimeVlasnika;
    private boolean validnoImeRoditeljaVlasnika;
    private boolean validnaAdresaPrebivalistaVlasnika;
    private boolean validanJmbgVlasnika;
    private boolean validanDatumRodjenjaVlasnika;
    private boolean validnoMjestoRodjenjaVlasnika = true;
    private boolean validnoMjestoPrebivalistaVlasnika = true;
    private boolean validanPostanskiBrojVlasnika;
    private int danIzJMBG;
    private int mjesecIzJMBG;
    private int godinaIzJMBG;

    public VlasnikController(VozilaDAO vozilaDAO, Vlasnik vlasnik) {
        this.vozilaDAO = vozilaDAO;
        this.vlasnik = vlasnik;
    }

    @FXML
    public void initialize() {
        mjestoRodjenja.setItems(vozilaDAO.getMjesta());
        adresaMjesto.setItems(vozilaDAO.getMjesta());

        adresaMjesto.valueProperty().addListener((old, o, n) -> {
            boolean unijetoNovoMjesto = true;
            int indeks = 0;
            for (int i = 0; i < adresaMjesto.getItems().size(); i++) {
                if (adresaMjesto.getItems().get(i).toString().equals(adresaMjesto.getValue())) {
                    unijetoNovoMjesto = false;
                    indeks = i;
                    break;
                }
            }
            String postanski = vozilaDAO.getMjesta().get(indeks).getPostanskiBroj();
            if (!unijetoNovoMjesto) {
                Platform.runLater(() -> postanskiBrojField.setText(postanski));
            }
        });

        datumField.setConverter(new StringConverter<LocalDate>() { // Ovo regulise format datuma u DatePickeru
            @Override
            public String toString(LocalDate datumZaPretvaranje) {
                if (datumZaPretvaranje != null) {
                    try {
                        return DateTimeFormatter.ofPattern("M/d/yyyy").format(datumZaPretvaranje);
                    } catch (DateTimeException dte) { }
                }
                validanDatumRodjenjaVlasnika = false;
                return "";
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    try {
                        return LocalDate.parse(string, DateTimeFormatter.ofPattern("M/d/yyyy"));
                    } catch (DateTimeParseException dtpe) { }
                }
                validanDatumRodjenjaVlasnika = false;
                return null;
            }
        });
    }

    public void cancelEventHandler(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void okEventHandler(ActionEvent actionEvent) {
        validacijaImena(imeField.getText());
        validacijaPrezimena(imeField.getText());
        validacijaImenaRoditelja(imeRoditeljaField.getText());
        validacijaAdresePrebivalista(adresaField.getText());
        validacijaJmbg(jmbgField.getText());
        validacijaDatumaRodjenja(datumField.getValue());
        validacijaMjestaRodjenja(mjestoRodjenja);
        validacijaMjestaPrebivalista(adresaMjesto);
        new Thread(() -> validacijaPostanskogBroja(postanskiBrojField.getText())).start();
        if (validnaForma()) {
            Mjesto mjestoPrebivalista = new Mjesto
                    (0, adresaMjesto.getValue().toString(), postanskiBrojField.getText());
            Mjesto mjestoRodjenjaVlasnika = new Mjesto
                    (0, mjestoRodjenja.getValue().toString(), "");
            if (vlasnik == null) {
                vozilaDAO.dodajVlasnika(new Vlasnik(0, imeField.getText(), prezimeField.getText(),
                        imeRoditeljaField.getText(), datumField.getValue(), mjestoRodjenja.getValue().toString(),
                        adresaMjesto.getValue().toString(), jmbgField.getText());
            }
            else {
                vlasnik.setVlasnik((Vlasnik) vlasnikCombo.getValue());
                vlasnik.setModel(modelField.getText());
                vlasnik.setBrojSasije(brojSasijeField.getText());
                vlasnik.setBrojTablica(brojTablicaField.getText());
                vlasnik.setProizvodjac(proizvodjac);
                vozilaDAO.promijeniVlasnika(vlasnik);
            }
            Stage stage = (Stage) okButton.getScene().getWindow();
            stage.close();
        }
    }

    private boolean validnoIme(String n) {
        return !n.trim().equals("");
    }

    private boolean validnoPrezime(String n) {
        return !n.trim().equals("");
    }

    private boolean validnoImeRoditelja(String n) {
        return !n.trim().equals("");
    }

    private boolean validnaAdresaPrebivalista(String n) {
        return !n.trim().equals("");
    }

    private boolean validanJmbg(String n) {
        if (n.length() != 13) return false;
        for (int i = 0; i < n.length(); i++) {
            if (!Character.isDigit(n.charAt(i))) return false;
        }
        danIzJMBG = Integer.parseInt(n.substring(0, 2));
        mjesecIzJMBG = Integer.parseInt(n.substring(2, 4));
        godinaIzJMBG = Integer.parseInt(n.substring(4, 7));
        int a = Integer.parseInt(n.substring(0, 1));
        int b = Integer.parseInt(n.substring(1, 2));
        int v = Integer.parseInt(n.substring(2, 3));
        int g = Integer.parseInt(n.substring(3, 4));
        int d = Integer.parseInt(n.substring(4, 5));
        int dj = Integer.parseInt(n.substring(5, 6));
        int e = Integer.parseInt(n.substring(6, 7));
        int zh = Integer.parseInt(n.substring(7, 8));
        int z = Integer.parseInt(n.substring(8, 9));
        int i = Integer.parseInt(n.substring(9, 10));
        int j = Integer.parseInt(n.substring(10, 11));
        int k = Integer.parseInt(n.substring(11, 12));
        int l = Integer.parseInt(n.substring(12, 13));
        if (l != 11 - ((7 * (a + e) + 6 * (b + zh) + 5 * (v + z) + 4 * (g + i) + 3 * (d + j) + 2 * (dj + k)) % 11)
                && l != 0)
            return false;
        return true;
    }

    private boolean validanDatumRodjenja(LocalDate n) {
        if (n == null) return false;
        if (n.isAfter(LocalDate.now())) return false;
        if (n.getDayOfMonth() != danIzJMBG) return false;
        if (n.getMonthValue() != mjesecIzJMBG) return false;
        if ((n.getYear() % 1000) != godinaIzJMBG) return false;
        return true;
    }

    private boolean validnoMjestoRodjenja(ComboBox mjestoRodjenja) {
        if (mjestoRodjenja.getEditor().getText().trim().isEmpty()) return false;
        return true;
    }

    private boolean validnoMjestoPrebivalista(ComboBox mjestoPrebivalista) {
        if (mjestoPrebivalista.getEditor().getText().trim().isEmpty()) return false;
        return true;
    }

    private void validacijaImena(String n) {
        if (validnoIme(n)) {
            imeField.getStyleClass().removeAll("invalidField");
            imeField.getStyleClass().add("validField");
            validnoImeVlasnika = true;
        } else {
            imeField.getStyleClass().removeAll("validField");
            imeField.getStyleClass().add("invalidField");
            validnoImeVlasnika = false;
        }
    }

    private void validacijaPrezimena(String n) {
        if (validnoPrezime(n)) {
            prezimeField.getStyleClass().removeAll("invalidField");
            prezimeField.getStyleClass().add("validField");
            validnoPrezimeVlasnika = true;
        } else {
            prezimeField.getStyleClass().removeAll("validField");
            prezimeField.getStyleClass().add("invalidField");
            validnoPrezimeVlasnika = false;
        }
    }

    private void validacijaImenaRoditelja(String n) {
        if (validnoImeRoditelja(n)) {
            imeRoditeljaField.getStyleClass().removeAll("invalidField");
            imeRoditeljaField.getStyleClass().add("validField");
            validnoImeRoditeljaVlasnika = true;
        } else {
            imeRoditeljaField.getStyleClass().removeAll("validField");
            imeRoditeljaField.getStyleClass().add("invalidField");
            validnoImeRoditeljaVlasnika = false;
        }
    }

    private void validacijaAdresePrebivalista(String n) {
        if (validnaAdresaPrebivalista(n)) {
            adresaField.getStyleClass().removeAll("invalidField");
            adresaField.getStyleClass().add("validField");
            validnaAdresaPrebivalistaVlasnika = true;
        } else {
            adresaField.getStyleClass().removeAll("validField");
            adresaField.getStyleClass().add("invalidField");
            validnaAdresaPrebivalistaVlasnika = false;
        }
    }

    private void validacijaJmbg(String n) {
        if (validanJmbg(n)) {
            jmbgField.getStyleClass().removeAll("invalidField");
            jmbgField.getStyleClass().add("validField");
            validanJmbgVlasnika = true;
        } else {
            jmbgField.getStyleClass().removeAll("validField");
            jmbgField.getStyleClass().add("invalidField");
            validanJmbgVlasnika = false;
        }
    }

    private void validacijaDatumaRodjenja(LocalDate n) {
        if (validanDatumRodjenja(n)) {
            datumField.getStyleClass().removeAll("invalidField");
            datumField.getStyleClass().add("validField");
            validanDatumRodjenjaVlasnika = true;
        } else {
            datumField.getStyleClass().removeAll("validField");
            datumField.getStyleClass().add("invalidField");
            validanDatumRodjenjaVlasnika = false;
        }
    }

    private void validacijaPostanskogBroja(String n) {
        if (validnoMjestoPrebivalistaVlasnika) {
            boolean unijetoNovoMjesto = true;
            for (int i = 0; i < adresaMjesto.getItems().size(); i++) {
                if (adresaMjesto.getItems().get(i).toString().equals(adresaMjesto.getValue())) {
                    unijetoNovoMjesto = false;
                    break;
                }
            }
            if (unijetoNovoMjesto && postanskiBrojField.getText().trim().isEmpty()) {
                Platform.runLater(() -> {
                    postanskiBrojField.getStyleClass().removeAll("validField");
                    postanskiBrojField.getStyleClass().add("invalidField");
                    validanPostanskiBrojVlasnika = false;
                });
                return;
            }
        }
        try {
            BufferedReader ulaz = new BufferedReader
                    (new InputStreamReader(new URL("http://c9.etf.unsa.ba/proba/postanskiBroj.php?postanskiBroj="
                            + postanskiBrojField.getText()).openStream(), StandardCharsets.UTF_8));
            String procitano = "", line = null;
            while ((line = ulaz.readLine()) != null) procitano = procitano + line;
            if (procitano.contains("NOT")) {
                Platform.runLater(() -> {
                    postanskiBrojField.getStyleClass().removeAll("validField");
                    postanskiBrojField.getStyleClass().add("invalidField");
                    validanPostanskiBrojVlasnika = false;
                });
                Thread.sleep(180);
            }
            else {
                Platform.runLater(() -> {
                    postanskiBrojField.getStyleClass().removeAll("invalidField");
                    postanskiBrojField.getStyleClass().add("validField");
                    validanPostanskiBrojVlasnika = true;
                });
                Thread.sleep(180);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void validacijaMjestaRodjenja(ComboBox mjestoRodjenja) {
        if (validnoMjestoRodjenja(mjestoRodjenja)) {
            mjestoRodjenja.getStyleClass().removeAll("invalidField");
            mjestoRodjenja.getStyleClass().add("validField");
            validnoMjestoRodjenjaVlasnika = true;
        } else {
            mjestoRodjenja.getStyleClass().removeAll("validField");
            mjestoRodjenja.getStyleClass().add("invalidField");
            validnoMjestoRodjenjaVlasnika = false;
        }
    }

    private void validacijaMjestaPrebivalista(ComboBox mjestoPrebivalista) {
        if (validnoMjestoPrebivalista(mjestoPrebivalista)) {
            mjestoPrebivalista.getStyleClass().removeAll("invalidField");
            mjestoPrebivalista.getStyleClass().add("validField");
            validnoMjestoPrebivalistaVlasnika = true;
        } else {
            mjestoPrebivalista.getStyleClass().removeAll("validField");
            mjestoPrebivalista.getStyleClass().add("invalidField");
            validnoMjestoPrebivalistaVlasnika = false;
        }
    }

    public boolean validnaForma() {
        return validnoImeVlasnika && validnoPrezimeVlasnika && validnoImeRoditeljaVlasnika
                && validnaAdresaPrebivalistaVlasnika && validanJmbgVlasnika && validanDatumRodjenjaVlasnika
                && validnoMjestoRodjenjaVlasnika && validnoMjestoPrebivalistaVlasnika
                && validanPostanskiBrojVlasnika;
    }
}
