# Folien-Outline: rentamovie — Java-Anwendungsentwicklung Schritt für Schritt

**Zielgruppe**: Dritter Kurs der Java-Reihe (nach imperativem und
objektorientiertem Java). Teilnehmer kennen Klassen, Vererbung, Collections.
**Dauer**: mehrtägig, entlang der Zustand-Progression Z1–Z11.
**Form**: Ein Gesamtdeck (Quarto + reveal.js, `rentamovie.qmd`), ein Kapitel
pro Zustand. Kapitel als Includes: `chapters/_zNN.qmd`.

**Schema jedes Kapitels** (Vorgabe):
1. Neue Technik — Vorstellung mit Inhalten/Verweisen aus offizieller Doku
2. Neue Fachlichkeit — was die Videothek jetzt kann
3. Ab in den Code — nur Stichworte als Wegweiser, dann Live-Demo im Modul
4. Aufgaben-Folie (Quelle: „Tasks after ZN")

**Stilregeln**: wenig Text, viele Mermaid-Diagramme (Klassen-, Ablauf-,
Schichtdiagramme), Code-Folien mit `code-line-numbers`-Stepping, deutsche
Folien, Code/Fachbegriffe englisch.

## Kapitel 0 — Intro

- Die Domäne: eine Videothek als bewusster Anachronismus (Netflix-Anspielung
  „netzfilm", 1990er-Desktop als Lehr-Constraint)
- Kursziel: vom Domänenmodell zur kleinen Business-Anwendung — Architektur,
  Tests, Datei-Persistenz, Validierung, Desktop-UI
- Werkzeuge: Java 25 (Temurin), Maven Multi-Module, IntelliJ
- Diagramm: Zustand-Progression Z1→Z11 als Timeline (Mermaid)
- Repo-Tour: elf eigenständige Module `Zustand_N/rentamovie`
- Doku-Links: dev.java, maven.apache.org, adoptium.net

## Kapitel 1 — Z1: Das Domänenmodell (Domain only)

- Technik: Pakete & Feature-Schnitt (`<feature>.<layer>`), POJOs mit
  Konstruktor/Gettern, `System.Logger` (was das JDK selbst mitbringt;
  Doku: dev.java / Javadoc `java.lang.System.Logger`)
- Fachlichkeit: Kunde, Film, Kopie, Verleih (offen vs. beendet), Tarif
- Diagramme: Klassendiagramm Customer/Movie/Copy/Rent/Rate;
  Objektgraph „Luke leiht ,A New Hope‘"
- Code-Stichworte: `App.main` verdrahtet von Hand; `Rent.isOpen()` =
  `endDate == null`; `endRent(...)` als sprechende Methode
- Aufgaben: Verleih-Erzeugung raus aus `App.main` → `RentService`;
  Rückgabe-Operation; Abfrage offener Verleihe

## Kapitel 2 — Z2: Die Service-Schicht

- Technik: Interface + Impl je Feature; Logging-Standard SLF4J + Logback
  (warum Fassade statt JDK-Logger; Doku: slf4j.org, logback.qos.ch)
- Fachlichkeit: Verleih anlegen, zurücknehmen, offene Verleihe abfragen —
  orchestriert durch Services
- Diagramme: Schichtdiagramm App → Services → Domain;
  Sequenzdiagramm createRent
- Code-Stichworte: `RentService`/`RentServiceImpl`; Logger pro Klasse;
  Service hält Zustand (Liste der Rents)
- Aufgaben: Vergleich über stabile ID statt Objektidentität; gemeinsame
  ID-Basisklasse; doppelte Methoden abbauen; Problem „zwei Service-Instanzen
  durch `new`" → gemeinsame Instanz hinter `getService()`

## Kapitel 3 — Z3: Identität & gemeinsame Basisklasse (common)

- Technik: `equals`/`hashCode`-Kontrakt (Doku: Javadoc `Object`),
  abstrakte Basisklasse, `AtomicLong` für IDs, hand-verdrahtete Singletons
  (`INSTANCE` + statisches `getService()`)
- Fachlichkeit: unverändert — technischer Fokus (Musterlösung der Z2-Aufgaben)
- Diagramme: Vererbung `AbstractIdCarrier` ← Customer/Movie/Copy/Rent;
  Ablauf equals (id-basiert, getClass-Vergleich)
- Code-Stichworte: `IdCarrier`-Interface; warum Subklassen equals nicht
  überschreiben; Singleton ohne Framework
- Aufgaben: erste Unit-Tests für `RentService`; Test der ID-Gleichheit;
  Mockito, wo ein Service einen Service braucht

## Kapitel 4 — Z4: Erste Tests

- Technik: JUnit Jupiter (Lifecycle, Assertions; Doku: docs.junit.org),
  AssertJ fluent Assertions (assertj.github.io), Mockito + `@ExtendWith` /
  `@Mock` / `@InjectMocks` (site.mockito.org)
- Fachlichkeit: abgesichert werden Verleih anlegen, Rückgabe, Verfügbarkeit,
  ID-Gleichheit
- Diagramme: Test-Pyramide (hier: Unit-Ebene); Mock-Schaubild
  RentServiceImplTest mit gemocktem MovieService
- Code-Stichworte: `src/test/java`-Konvention; AAA-Muster;
  `@InjectMocks` überschreibt Singleton-Felder per Reflection
- Aufgaben: Tests für MovieService/CustomerService und Domain-Klassen;
  Testfälle für Tarif/Preis vorbereiten

## Kapitel 5 — Z5: Preisberechnung (RateService)

- Technik: `BigDecimal` für Geld (Doku: Javadoc; Warum kein double),
  `java.time` für Zeiträume (`Period`/`ChronoUnit`)
- Fachlichkeit: Tarife (nach Alter), Preis je Verleihdauer, mindestens 1 Tag
- Diagramme: Entscheidungsbaum Tarifwahl; Ablauf calculatePrice
- Code-Stichworte: `RateService.retrieveRateByAge`; Guard gegen offene
  Rents in `calculatePrice`
- Aufgaben: Repository-Lesemethoden als Collections; Rent-Liste im Service
  schrittweise durch Repository ersetzen; Tests für neue Lese-Use-Cases

## Kapitel 6 — Z6: Das Repository-Muster (in-memory)

- Technik: Repository als Muster (Speicher-Abstraktion hinter Interface),
  `ConcurrentHashMap` als Store, `@Nullable`-Verträge (read kann null liefern)
- Fachlichkeit: Lese-Use-Cases — Kunden, Filme, Verleihe, offene Verleihe
- Diagramme: Schichtdiagramm Service → Repository → Map;
  „Interface vorne, Impl austauschbar hinten"
- Code-Stichworte: `CustomerRepository.getRepository()`;
  read/readAll/save; Services delegieren statt Listen zu halten
- Aufgaben: drei Repositories zu `CommonRepository<T>` generalisieren;
  Datei-Persistenz vorbereiten; Zahlungsfälle vorbereiten; ID-Erzeugung aus
  dem Domain-Modell ins Repository verschieben

## Kapitel 7 — Z7: Generisches Repository + Datei-Persistenz

- Technik: Generics an Klassen (`CommonRepositoryImpl<T extends
  AbstractIdCarrier>`), Template-Methoden (`fromText`/`toText`), Datei-I/O
  mit `java.nio.file.Files` (Doku: Javadoc `Files`), Ladezeitpunkt:
  Konstruktor setzt Felder, dann `final load()`
- Fachlichkeit: Videothek überlebt Neustart — offene/beendete Verleihe und
  Zahlungsstatus liegen in `.db`-Textdateien
- Diagramme: Vererbungsdiagramm CommonRepositoryImpl ← 4 Impls;
  Ablauf Laden (Zeile → split → fromText → Map); Dateiformat-Beispiel
- Code-Stichworte: `IdRepository.getNextId()` (synchronisiert, persistiert);
  Zeilen-robustes Laden (kaputte Zeile → Log + skip); `System.getProperty`
  für Testpfade
- Aufgaben: Konsistenzregeln für Verleih/Rückgabe/Zahlung; Doppelverleih
  verhindern; Rückgabe beendeter Rents ablehnen; Rückgabedatum und
  Zahlungsbetrag validieren

## Kapitel 8 — Z8: Validierung & Konsistenzregeln

- Technik: Fail-fast mit `IllegalArgumentException`/`IllegalStateException`,
  Guards an Service-Grenzen, Eingabe-Härtung der Datei-Schicht
  (`requireStorableText` gegen Delimiter-/Zeilenumbruch-Injection)
- Fachlichkeit: keine Kopie doppelt verleihen (`findAllFreeCopies`),
  keine Doppel-Rückgabe, Enddatum ≥ Startdatum, Zahlbetrag = berechneter
  Preis (`payRent` fragt RateService)
- Diagramme: Zustandsdiagramm Rent (offen → zurückgegeben → bezahlt);
  Ablauf payRent mit Prüfungen
- Code-Stichworte: neue RateService-Abhängigkeit in RentServiceImpl;
  Demo in App.main fängt drei Validierungsfehler
- Aufgaben: Flows in eine einfache JavaFX-UI bringen; UI bewusst basal
  halten; Komfort-Aufgaben (Suche/Filter/Sortierung) formulieren

## Kapitel 9 — Z9: JavaFX-Grundlagen

- Technik: JavaFX-Anwendungsmodell (`Application`, `Stage`, `Scene`;
  Doku: openjfx.io), FXML + `fx:controller` + `@FXML`-Injection,
  TableView/PropertyValueFactory, Events/Handler
- Fachlichkeit: Kunden/Filme/Kopien pflegen, Verleih, Rückgabe, Zahlung —
  als Desktop-Workflows mit Status-ComboBox und geplantem Rückgabedatum
- Diagramme: MVC-artiges Schaubild FXML ↔ Controller ↔ Services;
  Fensteraufbau RootLayout + Overviews (BorderPane-Skizze)
- Code-Stichworte: `javafx-maven-plugin` / `mvn javafx:run`; FXML liegt
  neben dem Controller; Validierungsfehler → Alert
- Aufgaben: Suchfelder für Kunden/Filme; Verleih-Filter, robuste
  Sortierung, Paging, erwarteter Zahlbetrag im Dialog; Demo-Daten-Reset
  und mehr Seed-Daten

## Kapitel 10 — Z10: Komfort-UI

- Technik: `FilteredList`/`SortedList` (Comparator an Tabelle gebunden),
  Listener auf Properties, Paging von Hand
- Fachlichkeit: Suche, Verleih-Statusfilter, stabile Sortierung, Seiten,
  erwarteter Zahlbetrag im Zahlungsdialog, Demo-Daten-Reset
- Diagramme: Datenfluss rents → filtered → sorted → page → TableView
- Code-Stichworte: finale Listen-Felder; `DemoDataService`; Seed-Daten
- Aufgaben: Arbeitslisten (offene Verleihe/Zahlungen, überfällig);
  Kunden-Verleihhistorie; Paging für Kunden/Filme; Tagesabschluss;
  Tests für Controller-Logik

## Kapitel 11 — Z11: Arbeitslisten, Historie, Tagesabschluss

- Technik: testbare Controller (DI-Konstruktor), Stream-Verarbeitung für
  Auswertungen, statische Helfer für Paging-Mathematik
- Fachlichkeit: Worklist-Buttons, Kunden-Historie, Kunden-/Film-Paging,
  Tagesabschluss (Zahlungen eines Tages mit Summe)
- Diagramme: Ablauf Tagesabschluss; Controller-Test-Schaubild (Services
  gemockt, ohne FXML)
- Code-Stichworte: `customerHistoryText`; `pageCount`/`clampPageIndex`
  als reine Funktionen; `paymentDate`/`paidAmount` am Rent
- Abschluss-Folie: erreichter Stand; Ausblick (BACKLOG: Mitarbeiter,
  Mehrplatz, Großhandel)

## Querschnitt (im Intro oder als Anhang)

- Build-Gate: `mvn verify` mit Tests, ErrorProne, NullAway, SpotBugs —
  kurz erklären, warum der Build „meckert" (errorprone.info,
  github.com/uber/NullAway, spotbugs.github.io, jspecify.org)
- Konventionen: Singletons über `getService()`, id-basiertes equals,
  Tests mit Mockito-Injection
