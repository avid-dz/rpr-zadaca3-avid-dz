package ba.unsa.etf.rpr.zadaca3;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class VlasnikController {

    private VozilaDAO vozilaDAO;
    private Vlasnik vlasnik;
    public Button cancelButton;
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
    private boolean validnoMjestoRodjenjaVlasnika;
    private boolean validnoMjestoPrebivalistaVlasnika;
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
        validacijaImena(imeField.getText());
        validacijaPrezimena(imeField.getText());
        validacijaImenaRoditelja(imeRoditeljaField.getText());
        validacijaAdresePrebivalista(adresaField.getText());
        validacijaJmbg(jmbgField.getText());
        validacijaDatumaRodjenja(datumField.getValue());

        datumField.setConverter(new StringConverter<LocalDate>() { // Ovo regulise format datuma u DatePickeru
            @Override
            public String toString(LocalDate datumZaPretvaranje) {
                if (datumZaPretvaranje != null) {
                    try {
                        return DateTimeFormatter.ofPattern("M/d/yyyy").format(datumZaPretvaranje);
                    } catch (DateTimeException dte) { }
                }
                datumField.getStyleClass().removeAll("validField");
                datumField.getStyleClass().add("invalidField");
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
                datumField.getStyleClass().removeAll("validField");
                datumField.getStyleClass().add("invalidField");
                validanDatumRodjenjaVlasnika = false;
                return null;
            }
        });

        imeField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String o, String n) {
                validacijaImena(n);
            }
        });
        prezimeField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String o, String n) {
                validacijaPrezimena(n);
            }
        });
        imeRoditeljaField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String o, String n) {
                validacijaImenaRoditelja(n);
            }
        });
        adresaField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String o, String n) {
                validacijaAdresePrebivalista(n);
            }
        });
        jmbgField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String o, String n) {
                validacijaJmbg(n);
                validacijaDatumaRodjenja(datumField.getValue());
            }
        });
        jmbgField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String o, String n) {
                validacijaJmbg(n);
                validacijaDatumaRodjenja(datumField.getValue());
            }
        });
        datumField.valueProperty().addListener((old, o, n) -> {
            validacijaDatumaRodjenja(n);
            validacijaJmbg(jmbgField.getText());
        });
    }

    public void cancelEventHandler(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void okEventHandler(ActionEvent actionEvent) {

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

    private boolean validnoMjestoRodjenja() {

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

    public boolean validnaForma() {
        return validnoImeVlasnika && validnoPrezimeVlasnika && validnoImeRoditeljaVlasnika
                && validnaAdresaPrebivalistaVlasnika && validanJmbgVlasnika && validanDatumRodjenjaVlasnika
                && validnoMjestoRodjenjaVlasnika && validnoMjestoPrebivalistaVlasnika
                && validanPostanskiBrojVlasnika;
    }
}
