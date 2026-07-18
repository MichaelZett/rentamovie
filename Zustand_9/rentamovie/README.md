# Zustand 9 — JavaFX-Basisoberfläche

Die Videothek bekommt ihre erste grafische Oberfläche: JavaFX mit FXML,
bewusst schlicht gehalten — Tabellen, Auswahl und Aktionen, noch ohne
Komfortfunktionen. Darunter arbeiten weiterhin die Services, Regeln und
Datei-Repositories aus Zustand 8.

## Starten

```bash
cd Zustand_9/rentamovie
mvn javafx:run
```

Alternativ in IntelliJ über die Run-Konfiguration für `App`.

## Bedienung

Oben in der Navigation wird zwischen den drei Ansichten gewechselt:
**Customers**, **Movies**, **Rent**.

### Customers — Kundenverwaltung

- Tabelle aller Kunden (Vorname, Nachname, Geburtstag, Status).
- Ein Klick auf eine Zeile füllt das Detailformular rechts.
- **New** leert das Formular für einen neuen Kunden; **Save** legt an bzw.
  speichert Änderungen. Das Geburtsdatum wird im Format `2001-12-24`
  eingegeben, der Status (z. B. aktiv/inaktiv) über eine ComboBox gepflegt.
- Das Geburtsdatum ist bewusst ein einfaches Textfeld mit
  `LocalDate.parse(...)` und Fehlerdialog — *Aufgabe*: durch einen JavaFX
  `DatePicker` (Kalender-Auswahl) ersetzen; die Referenzlösung steht in
  Zustand 10.

### Movies — Film- und Kopienverwaltung

- Tabelle aller Filme (Erscheinungsjahr, Titel, Status).
- Das Detailformular zeigt zusätzlich die Kopien des Films mit ihrem
  Medienformat; Status und Format werden über ComboBoxen gepflegt.
- **New** für einen neuen Film, **Add copy** bucht eine weitere Kopie zum
  ausgewählten Film ein, **Save** speichert die Filmdaten.

### Rent — Verleih, Rückgabe und Zahlung

- Tabelle aller Ausleihen (Kunde, Kopie, Startdatum, geplantes
  Rückgabedatum, Rückgabedatum, Zahlungsstatus).
- **New** öffnet den Verleihdialog: Auswahl eines aktiven Kunden und einer
  freien Kopie eines aktiven Films (bereits verliehene Kopien tauchen nicht
  auf), dazu die geplante Leihdauer in Tagen (Vorgabe: 7). Die Kopien
  werden mit ihrer Nummer angezeigt (z. B. `#12 - A new hope (1977, DVD)`),
  damit mehrere Kopien desselben Films unterscheidbar sind — wie die
  Kopiennummer auf der Hülle in einer echten Videothek.
- **Return** nimmt die ausgewählte offene Ausleihe zurück.
- **Pay** öffnet den Zahlungsdialog für eine zurückgegebene Ausleihe; der
  Betrag muss dem berechneten Preis entsprechen (Regeln aus Zustand 8),
  sonst erscheint eine Validierungsmeldung.

## Persistenz

Alle Änderungen landen sofort in den `.db`-Dateien im Modulverzeichnis und
sind nach einem Neustart wieder da. Die Dateien sind versioniert — mit
`git restore Zustand_9/rentamovie/*.db` lässt sich der Seed-Stand
wiederherstellen.

## Tests ausführen

```bash
mvn -pl Zustand_9/rentamovie -am test
```
