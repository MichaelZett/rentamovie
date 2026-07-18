# Zustand 8 — Validierung und Konsistenzregeln

Auf der Datei-Persistenz aus Zustand 7 setzen jetzt fachliche Regeln auf:
Der `RentService` verhindert Doppelverleih, doppelte Rückgabe und falsche
Zahlungen; die Datei-Repositories weisen Texte mit Trennzeichen oder
Zeilenumbrüchen ab (`requireStorableText`).

## Was die Anwendung kann

Wie Zustand 7: Filme mit Kopien und Kunden anlegen, ausleihen,
zurücknehmen, Preis berechnen, bezahlen — alles dateipersistent.

Neu sind die Konsistenzregeln:

- Eine bereits verliehene Kopie kann nicht ein zweites Mal verliehen
  werden, solange die Ausleihe offen ist (`findAllFreeCopies`).
- Eine beendete Ausleihe kann nicht noch einmal zurückgegeben werden;
  das Rückgabedatum darf nicht vor dem Startdatum liegen
  (`returnRent(rent, endDate)`).
- Eine Zahlung muss zum berechneten Preis passen — `payRent(rent, amount)`
  prüft den Betrag über den `RateService`.
- Die Datei-Repositories verweigern Texte, die das Speicherformat
  beschädigen würden.

## Starten

```bash
cd Zustand_8/rentamovie
mvn exec:java -Dexec.mainClass=de.zettsystems.rentamovie.main.App
```

## Was der Demo-Lauf zeigt

1. Bestand wird von der Platte geladen; Film, Kopie und Kunde entstehen,
   eine Ausleihe wird angelegt.
2. Der Versuch, dieselbe Kopie erneut zu verleihen, schlägt fehl — das Log
   zeigt die Validierungsmeldung.
3. Die Ausleihe wird zurückgegeben; eine zweite Rückgabe wird abgewiesen.
4. Eine Zahlung über 0 wird abgelehnt; die Zahlung über den berechneten
   Preis wird angenommen (offene Zahlungen: 0).
5. Der neue Bestand steht auf der Platte.

Hinweis: Wie in Zustand 7 verändert jeder Demo-Lauf die versionierten
`.db`-Dateien im Modulverzeichnis — mit
`git restore Zustand_8/rentamovie/*.db` lässt sich der Ausgangsstand
wiederherstellen.

## Tests ausführen

```bash
mvn -pl Zustand_8/rentamovie -am test
```

Die Tests decken jetzt auch die neuen Regeln ab: Doppelverleih, doppelte
Rückgabe, ungültige Rückgabedaten und Zahlungsprüfung.
