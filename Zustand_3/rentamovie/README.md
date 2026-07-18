# Zustand 3 — Gemeinsame Basisklasse und Singletons

Zwei Aufräumarbeiten aus den Aufgaben zu Zustand 2: Alle Domänenklassen
erben jetzt von `AbstractIdCarrier` (ID-Vergabe per `AtomicLong`,
`equals`/`hashCode` auf Basis der ID), und jeder Service existiert genau
einmal — als handverdrahtetes Singleton hinter `XService.getService()`.

## Was die Anwendung kann

Fachlich dasselbe wie Zustand 2:

- Filme anlegen und ändern, Kunden anlegen.
- Ausleihen anlegen und zurücknehmen.
- Offene Ausleihen abfragen.

Neu ist das Verhalten darunter:

- Fachliche Objekte sind gleich, wenn ihre ID gleich ist — nicht mehr nur
  bei Objektidentität.
- `App` und `RentServiceImpl` teilen sich dieselben Service-Instanzen;
  der Zustand (z. B. offene Ausleihen) ist überall konsistent.

Die Daten leben weiterhin nur im Speicher.

## Starten

```bash
cd Zustand_3/rentamovie
mvn exec:java -Dexec.mainClass=de.zettsystems.rentamovie.main.App
```

## Was der Demo-Lauf zeigt

1. Film „A new hope" und Kunde Luke Skywalker werden über die
   Service-Singletons angelegt.
2. Eine Ausleihe entsteht — offene Ausleihen: 1.
3. Die Ausleihe wird zurückgegeben — offene Ausleihen: 0.
