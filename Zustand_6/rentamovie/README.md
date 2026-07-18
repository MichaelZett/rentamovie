# Zustand 6 — Erste Repositories (In-Memory)

Die Services verwalten ihre Daten nicht mehr in eigenen Listen, sondern
über Repositories: `CustomerRepository`, `MovieRepository` und
`RentRepository` (jeweils Interface + In-Memory-Implementierung als
Singleton hinter `getRepository()`).

## Was die Anwendung kann

Wie bisher:

- Filme anlegen und ändern, Kunden anlegen.
- Ausleihen anlegen, zurücknehmen, offene Ausleihen abfragen.
- Tarif ermitteln und Preis berechnen.

Neu:

- Bestände gesammelt lesen: `readAllMovies`, `readAllCustomers`,
  `readAllRents` — die Services reichen die Lese-Use-Cases an die
  Repositories durch.

Die Repositories speichern weiterhin nur im Speicher — nach dem
Programmende sind die Daten weg (Datei-Persistenz folgt in Zustand 7).

## Starten

```bash
cd Zustand_6/rentamovie
mvn exec:java -Dexec.mainClass=de.zettsystems.rentamovie.main.App
```

## Was der Demo-Lauf zeigt

1. Film und Kunde werden angelegt, eine Ausleihe entsteht und wird
   zurückgegeben; der Preis wird über den Tarif berechnet.
2. Zum Schluss zeigt das Log den Repository-Bestand: Anzahl Filme,
   Kunden und Ausleihen.

## Tests ausführen

```bash
mvn -pl Zustand_6/rentamovie -am test
```

Neu dazugekommen sind Tests für die drei Repositories
(`CustomerRepositoryImplTest`, `MovieRepositoryImplTest`,
`RentRepositoryImplTest`) und `RateTest`.
