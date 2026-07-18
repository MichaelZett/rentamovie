# Zustand 5 — Tarife und Preisberechnung

Neu ist der `RateService`: Er liefert den passenden Tarif zum Alter des
Kunden (Junior, Regular, Senior mit unterschiedlichen Tagespreisen) und
berechnet den Preis einer Ausleihe aus Start- und Rückgabedatum.

## Was die Anwendung kann

Wie bisher:

- Filme anlegen und ändern, Kunden anlegen.
- Ausleihen anlegen, zurücknehmen und offene Ausleihen abfragen.

Neu:

- Tarif zum Kundenalter ermitteln (`retrieveRateByAge`).
- Preis einer beendeten Ausleihe berechnen (`calculatePrice` aus
  Mietdauer und Tagespreis des Tarifs).

Die Daten leben weiterhin nur im Speicher.

## Starten

```bash
cd Zustand_5/rentamovie
mvn exec:java -Dexec.mainClass=de.zettsystems.rentamovie.main.App
```

## Was der Demo-Lauf zeigt

1. Film und Kunde werden angelegt, eine Ausleihe entsteht und wird
   zurückgegeben (offene Ausleihen: 1 → 0).
2. Aus dem Geburtsdatum des Kunden wird das Alter bestimmt, daraus der
   Tarif — und das Log zeigt den berechneten Preis für die Ausleihe.

## Tests ausführen

```bash
mvn -pl Zustand_5/rentamovie -am test
```

Neu dazugekommen sind u. a. `RateServiceImplTest` (Tarifwahl und
Preisberechnung) sowie Tests für `MovieService`, `Movie`, `Copy` und `Rent`.
