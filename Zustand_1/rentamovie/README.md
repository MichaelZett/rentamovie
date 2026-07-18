# Zustand 1 — Reine Domänenklassen

Erster Schnappschuss der Videothek: nur Domänenklassen (`Customer`, `Movie`,
`Copy`, `Rent`, `Rate`), keine Services, keine Persistenz. `App.main` baut
alle Objekte von Hand zusammen. Geloggt wird mit dem JDK-eigenen
`System.Logger` — bewusst ohne externe Bibliothek.

## Was die Anwendung kann

- Einen Film mit Erscheinungsjahr und Titel anlegen (`Movie`).
- Eine Kopie zu einem Film anlegen (`Copy`).
- Einen Kunden mit Name und Geburtsdatum anlegen (`Customer`).
- Eine Ausleihe von Kunde und Kopie mit Startdatum anlegen (`Rent`).
- Eine Ausleihe per `endRent(...)` beenden; `isOpen()` / `isFinished()`
  zeigen den Zustand der Ausleihe.

Alles passiert im Speicher und direkt in `App.main` — es gibt noch keine
Services und keine Speicherung. Nach dem Programmende sind die Daten weg.

## Starten

```bash
cd Zustand_1/rentamovie
mvn exec:java -Dexec.mainClass=de.zettsystems.rentamovie.main.App
```

## Was der Demo-Lauf zeigt

1. Der Film „A new hope" (1977) wird angelegt und geloggt.
2. Eine Kopie und der Kunde Luke Skywalker entstehen.
3. Eine Ausleihe wird angelegt — das Log zeigt, dass die Kopie verliehen ist.
4. Die Ausleihe wird beendet — das Log zeigt, dass die Kopie wieder frei ist.

Zum Experimentieren einfach `App.main` anpassen: weitere Filme, Kopien,
Kunden und Ausleihen anlegen und die Log-Ausgaben beobachten.
