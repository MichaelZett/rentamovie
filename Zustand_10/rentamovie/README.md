# Zustand 10 — Convenience-UI

Die JavaFX-Oberfläche aus Zustand 9 bekommt Komfort: Suche, Statusfilter,
robuste Sortierung, ein Paging-Beispiel, einen Gebührenvorschlag im
Zahlungsdialog und einen Demo-Daten-Reset mit Seed-Daten.

## Starten

```bash
cd Zustand_10/rentamovie
mvn javafx:run
```

Alternativ in IntelliJ über die Run-Konfiguration für `App`.

## Bedienung

Navigation oben: **Customers**, **Movies**, **Rent** — neu dazu
**Reset demo data**: leert alle Repositories und legt die Seed-Daten neu an
(`DemoDataService`). Praktisch, um nach dem Ausprobieren wieder auf einen
definierten Stand zu kommen.

### Customers und Movies

Wie in Zustand 9 (anlegen, ändern, Status pflegen, Kopien einbuchen) —
neu ist jeweils ein **Suchfeld** über der Tabelle: Tippen filtert die
Liste sofort. Ebenfalls neu: Das Geburtsdatum wird über einen
**DatePicker** (Kalender-Auswahl) erfasst statt als Text — die
Referenzlösung zur Aufgabe aus Zustand 9.

### Rent — mit Filter, Sortierung und Paging

- **Statusfilter** (ComboBox rechts oben): `All`, `Open rents`, `Overdue`,
  `Open payments`, `Paid` — die Tabelle zeigt nur die passenden Ausleihen.
- **Sortierung**: Klick auf einen Spaltenkopf sortiert robust über den
  gesamten gefilterten Bestand (per `SortedList`), nicht nur über die
  sichtbare Seite.
- **Paging**: `Prev` / `Next` blättern durch die Ausleihen, die Anzeige
  dazwischen zeigt die aktuelle Seite (z. B. „1 / 3").
- **New**, **Return** wie in Zustand 9.
- **Pay**: Der Zahlungsdialog zeigt jetzt den **erwarteten Betrag** an
  (berechnet über den `RateService` aus Tarif und Mietdauer) und trägt ihn
  als Vorgabe ein — eine passende Zahlung markiert die Ausleihe als bezahlt.

## Persistenz

Alle Änderungen landen in den `.db`-Dateien im Modulverzeichnis. Zum
Zurücksetzen entweder den Button **Reset demo data** benutzen oder
`git restore Zustand_10/rentamovie/*.db`.

## Tests ausführen

```bash
mvn -pl Zustand_10/rentamovie -am test
```

Neu dabei: `RentOverviewControllerTest` für Filter- und Paging-Logik.
