# Zustand 7 — Generisches Repository und Datei-Persistenz

Die Repositories werden zu einem gemeinsamen `CommonRepository<T>`
verallgemeinert und speichern jetzt in Dateien. Die ID-Vergabe wandert aus
`AbstractIdCarrier` in ein eigenes `IdRepository`. Damit überleben die
Daten erstmals einen Neustart.

## Was die Anwendung kann

Wie bisher:

- Filme und Kunden anlegen, Ausleihen anlegen und zurücknehmen,
  offene Ausleihen abfragen, Preis über den Tarif berechnen.

Neu:

- Kopien zu einem Film anlegen (`createCopies`).
- Eine Ausleihe bezahlen (`payRent`) und Ausleihen mit offener Zahlung
  abfragen (`findRentsWithOpenPayment`).
- **Persistenz**: Filme, Kopien, Kunden, Ausleihen und der ID-Zähler werden
  in Dateien im Modulverzeichnis gespeichert (`movie.db`, `copy.db`,
  `customer.db`, `rent.db`, `id.db`) und beim nächsten Start wieder geladen.

## Starten

```bash
cd Zustand_7/rentamovie
mvn exec:java -Dexec.mainClass=de.zettsystems.rentamovie.main.App
```

## Was der Demo-Lauf zeigt

1. Beim Start meldet das Log, wie viele Filme, Kunden und Ausleihen von der
   Platte geladen wurden.
2. Ein Film mit Kopie und ein Kunde werden angelegt, eine Ausleihe entsteht,
   wird zurückgegeben und bezahlt; der Preis kommt aus dem Tarif.
3. Zum Schluss steht der neue Bestand auf der Platte — **die App erneut
   starten**, um zu sehen, dass die Daten wieder geladen werden und der
   Bestand mit jedem Lauf wächst.

Hinweis: Die `.db`-Dateien im Modulverzeichnis sind versioniert
(Seed-Daten). Jeder Demo-Lauf verändert sie — mit
`git restore Zustand_7/rentamovie/*.db` lässt sich der Ausgangsstand
wiederherstellen. Maven-Tests schreiben stattdessen nach `target/` und
lassen die versionierten Dateien unangetastet.

## Tests ausführen

```bash
mvn -pl Zustand_7/rentamovie -am test
```
