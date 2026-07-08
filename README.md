# rentamovie

Begleit-Code für einen Java-Grundlagenkurs an der FernUni Hagen (Standort
Hamburg). Die Domäne ist eine kleine Videothek: Kunden leihen Kopien von
Filmen aus, zu unterschiedlichen Tarifen.

Das Repository ist **keine** lauffähige Einzelanwendung, sondern eine
Sequenz aus neun aufeinander aufbauenden Maven-Modulen
(`Zustand_1` … `Zustand_9`). Jeder Zustand ist ein eigenständiger
Schnappschuss derselben Domäne und führt **genau ein** neues Konzept ein.

## Lernziel

Die Studierenden sollen Java-Grundlagen *ohne* Framework-Magie nachvollziehen:

- **Klassische OOP**: Konstruktor, private Felder, Getter, sprechende
  Update-Methoden — keine Records, kein Lombok.
- **Hand-verdrahtete Architektur**: Singletons über statische
  `getService()` / `getRepository()`, kein Spring, kein DI-Framework.
- **In-Memory- und Datei-Persistenz** statt JPA/Hibernate.
- **JavaFX mit FXML** als UI — keine Web- oder JS-Schicht.

Die Progression `Zustand_1` → `Zustand_9` ist der eigentliche Lehrstoff:
sie zeigt, wie eine Anwendung in lesbaren Schritten von „nur Domänen-
klassen" zu „GUI mit File-Repository" wächst.

## Was zeigt welcher Zustand?

| Modul         | Neues Konzept                                                                                                                                      |
|---------------|----------------------------------------------------------------------------------------------------------------------------------------------------|
| **Zustand_1** | Reine Domänenklassen (`Customer`, `Movie`, `Copy`, `Rent`, `Rate`). `App.main` baut sie von Hand. Logging über `System.Logger` (im JDK enthalten). |
| **Zustand_2** | Service-Schicht: `XService` als Interface, `XServiceImpl` als Implementierung. Ab hier SLF4J + Logback.                                            |
| **Zustand_3** | Gemeinsame Basisklasse `AbstractIdCarrier` (Vererbung, ID-Verwaltung, `equals`/`hashCode`).                                                        |
| **Zustand_4** | Erste Tests: JUnit 5, AssertJ, Mockito. Test-Methoden mit `@Mock` / `@InjectMocks`.                                                                |
| **Zustand_5** | `RateService` — Tarif-Logik als eigenes Modul.                                                                                                     |
| **Zustand_6** | Erstes Repository (`CustomerRepository`) mit In-Memory-Speicherung.                                                                                |
| **Zustand_7** | Generisches `CommonRepository<T>` und `IdRepository`; Repositories speichern jetzt persistent in Dateien.                                          |
| **Zustand_8** | Schliff: Sichtbarkeiten, Fundroutinen, Repository-Konsistenz.                                                                                      |
| **Zustand_9** | JavaFX-Oberfläche: Übersichten für Customer, Movie, Rent + ein Dialog.                                                                             |

Die Zustände bleiben technisch klar geschnitten: Jeder Zustand führt ein
zentrales Java-Konzept ein. Fachliche Erweiterungen dürfen dazukommen,
wenn sie dieses Konzept greifbarer machen, zum Beispiel Rückgabe beim
Service-Konzept, Preisberechnung beim `RateService` oder Filter in der
UI. Aufgaben aus einem Zustand werden im nächsten Zustand gelöst; Tests
wachsen ab `Zustand_4` fortlaufend mit.

Zielbild für die fachliche Progression:

| Modul         | Fachlicher Schwerpunkt                                                                                  |
|---------------|---------------------------------------------------------------------------------------------------------|
| **Zustand_1** | Grundmodell: Kunde, Film, Kopie, Ausleihe, Tarif; Vorbereitung für offene und beendete Ausleihen.       |
| **Zustand_2** | Services orchestrieren Verleih, Rückgabe und Listen offener Ausleihen.                                  |
| **Zustand_3** | Identität und Gleichheit bleiben im Fokus; die Service-Aufgaben aus `Zustand_2` werden fachlich gelöst. |
| **Zustand_4** | Erste Tests sichern Verleih, Rückgabe, Verfügbarkeit und ID-Gleichheit ab.                              |
| **Zustand_5** | Preisberechnung: Mietdauer, Tagespreis, optional Überziehung und Zahlungsfälle.                         |
| **Zustand_6** | Repository-Lese-Use-Cases: Kunden, Filme, Ausleihen, offene Ausleihen.                                  |
| **Zustand_7** | Datei-Persistenz für offene und abgeschlossene Ausleihen sowie Zahlungsstatus.                          |
| **Zustand_8** | Validierung und Konsistenzregeln, z. B. keine doppelte Verleihung derselben Kopie.                      |
| **Zustand_9** | Basic UI für Kunde, Film, Kopie, Verleih, Rückgabe und Zahlung.                                         |

Aufgaben nach `Zustand_1`:

- Erstelle Ausleihen nicht mehr direkt in `App.main`, sondern über einen
  `RentService`.
- Ergänze im Service eine Rückgabe-Operation für eine offene Ausleihe.
- Biete eine einfache Abfrage für offene Ausleihen an, damit die
  Verfügbarkeit einer Kopie fachlich sichtbar wird.

Aufgaben nach `Zustand_2`:

- Vergleiche fachliche Objekte nicht mehr über Objektidentität, sondern
  über eine stabile ID.
- Ziehe die gemeinsame ID-Logik aus `Customer`, `Movie`, `Copy` und
  `Rent` in eine gemeinsame Basisklasse.
- Prüfe, welche Methoden dadurch in den Domänenklassen entfallen können.

Aufgaben nach `Zustand_3`:

- Schreibe erste Unit-Tests für `RentService`: Ausleihe anlegen,
  Rückgabe durchführen, offene Ausleihen filtern.
- Schreibe Tests für die ID-basierte Gleichheit aus `AbstractIdCarrier`.
- Nutze Mockito dort, wo ein Service von einem anderen Service abhängt.

Aufgaben nach `Zustand_4`:

- Ergänze Tests für `MovieService`, `CustomerService` und die
  Domänenklassen `Movie`, `Copy` und `Rent`.
- Prüfe Grenzfälle beim Verleih: Rückgabe einer bereits beendeten
  Ausleihe und mehrere offene Ausleihen.
- Bereite Testfälle für Tarif- und Preisberechnung vor.

Aufgaben nach `Zustand_5`:

- Baue Repository-Methoden, um Kunden, Filme und Ausleihen gesammelt
  lesen zu können.
- Ersetze die service-interne Ausleih-Liste schrittweise durch ein
  Repository.
- Bereite Zahlungsfälle vor: berechneter Preis, offener Betrag und
  bezahlte Ausleihe.
- Ergänze Tests für die neuen Lese-Use-Cases.

Aufgaben nach `Zustand_6`:

- Verallgemeinere die drei In-Memory-Repositories zu einem gemeinsamen
  `CommonRepository<T>`.
- Bereite Datei-Persistenz für Kunden, Filme und Ausleihen vor.
- Prüfe, welche ID-Erzeugung noch im Domain-Modell steckt und in ein
  Repository ausgelagert werden kann.

Aufgaben nach `Zustand_7`:

- Ergänze Konsistenzregeln für Verleih, Rückgabe und Zahlung.
- Verhindere doppelte Verleihung derselben Kopie, solange eine offene
  Ausleihe existiert.
- Prüfe Rückgabedatum und Zahlungsstatus fachlich, bevor gespeichert wird.

Aufgaben nach `Zustand_8`:

- Binde Kunden-, Film-, Kopien-, Verleih-, Rückgabe- und Zahlungsfälle in
  eine einfache JavaFX-Oberfläche ein.
- Halte die erste UI bewusst schlicht: Tabellen, Auswahl und Aktionen
  statt Komfortfunktionen.
- Formuliere Convenience-Aufgaben für Suche, Filter und Sortierung.

Jeder Zustand bleibt nach Veröffentlichung „eingefroren" — er
dokumentiert den Lernschritt, nicht den letzten Stand. Wer ein Konzept
aus `Zustand_9` braucht, schaut dort; in `Zustand_3` gehört es nicht hin.

## Voraussetzungen

- **JDK 25** (Temurin/Adoptium empfohlen)
- **Maven 3.9 oder neuer**

Versionen werden zentral im Root-`pom.xml` verwaltet — die einzelnen
Modul-POMs enthalten bewusst keine Versionen.

## Bauen & Ausführen

Aus dem Repository-Wurzelverzeichnis:

```bash
# Alles bauen und alle Tests laufen lassen
mvn verify

# Nur einen Zustand bauen
mvn -pl Zustand_4/rentamovie -am test

# Einen einzelnen Test ausführen
mvn -pl Zustand_4/rentamovie test -Dtest=RentServiceImplTest
```

### Konsolen-App starten (Zustand_1 … Zustand_8)

```bash
cd Zustand_1/rentamovie
mvn exec:java -Dexec.mainClass=hh.fernuni.rentamovie.main.App
```

### JavaFX-Oberfläche starten (Zustand_9)

```bash
cd Zustand_9/rentamovie
mvn javafx:run
```

In IntelliJ IDEA lässt sich `Zustand_9` auch über die Run-Konfiguration
für `App` starten.

## Code-Qualität

[ErrorProne](https://errorprone.info/) läuft als
Annotation-Processor im Compile-Schritt und gibt Hinweise direkt in der
Maven-Ausgabe (Severity `WARN` — der Build wird dadurch nicht rot, aber
neue Findings fallen sofort auf).

## Verzeichnis-Aufbau

```
rentamovie/
├── pom.xml                 ← Parent-POM (Java-25-Toolchain, alle Versionen)
├── .mvn/jvm.config         ← JDK-internal-API-Exports für ErrorProne
├── Zustand_1/rentamovie/   ← Schritt 1: nur Domäne
├── Zustand_2/rentamovie/   ← Schritt 2: Service-Schicht
├── …
└── Zustand_9/rentamovie/   ← Schritt 9: JavaFX-UI
```

Innerhalb jedes Zustands folgt der Java-Code dem Schema
`hh.fernuni.rentamovie.<feature>.<schicht>`, mit
`feature ∈ { customer, movie, rent, rate, common, main }` und
`schicht ∈ { domain, application, adapter }`.

## Was ist *bewusst* nicht enthalten

- Spring, Spring Boot oder andere DI-Container
- Hibernate / JPA / eine Datenbank
- Lombok, Records als Domänenklassen, Builder-Pattern
- Web-Frameworks, REST-APIs, JavaScript-Frontend

All diese Themen bauen auf den hier vermittelten Grundlagen auf — sie
gehören in eine Folgeveranstaltung, nicht hierher.
