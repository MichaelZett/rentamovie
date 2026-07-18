# Zustand 11 — Vertiefung: Arbeitslisten, Historie, Tagesabschluss

Referenzlösung der Hausaufgaben nach Zustand 10 — ohne neue Technik:
Arbeitslisten für den Verleihalltag, Verleihhistorie pro Kunde, Paging auch
für Kunden und Filme sowie ein Tagesabschluss über die heutigen Zahlungen.

## Starten

```bash
cd Zustand_11/rentamovie
mvn javafx:run
```

Alternativ in IntelliJ über die Run-Konfiguration für `App`.

## Bedienung

Navigation oben: **Customers**, **Movies**, **Rent**, **Reset demo data**
(Repositories leeren und Seed-Daten neu anlegen).

### Customers — jetzt mit Historie und Paging

- Wie bisher: Suchfeld, Tabelle, Detailformular mit Status-ComboBox,
  **New** / **Save**.
- Neu: **Rent history** im Detailbereich — die Verleihhistorie des
  ausgewählten Kunden.
- Neu: **Prev** / **Next** blättern durch die Kundenliste.

### Movies — jetzt mit Paging

- Wie bisher: Suchfeld, Filmdetails mit Kopienliste, Status- und
  Format-ComboBoxen, **New** / **Add copy** / **Save**.
- Neu: **Prev** / **Next** blättern durch die Filmliste.

### Rent — Arbeitslisten und Tagesabschluss

- Neu: **Worklists**-Buttons springen direkt in die passende Sicht:
  **All**, **Open** (offene Ausleihen), **Overdue** (überfällig),
  **Open pay** (offene Zahlungen), **Paid** (bezahlt). Der Statusfilter
  rechts oben steht weiterhin zur Verfügung.
- Neu: **Daily closing** zeigt alle heutigen Zahlungen als Liste mit
  Summe — der Tagesabschluss der 1-Mann-Videothek.
- Wie bisher: Sortierung über Spaltenköpfe, Paging mit **Prev** / **Next**,
  **New** (Verleihdialog mit aktiven Kunden, freien Kopien und geplanter
  Leihdauer), **Return** und **Pay** mit vorgeschlagenem erwartetem Betrag.

## Persistenz

Alle Änderungen landen in den `.db`-Dateien im Modulverzeichnis. Zum
Zurücksetzen entweder **Reset demo data** benutzen oder
`git restore Zustand_11/rentamovie/*.db`.

## Tests ausführen

```bash
mvn -pl Zustand_11/rentamovie -am test
```

Neu dabei: Tests für die UI-nahe Controller-Logik
(`CustomerOverviewControllerTest`, `RentOverviewControllerTest`,
`RentDialogTest`) — u. a. Historie, Paging, Arbeitslisten und
Tagesabschluss.
