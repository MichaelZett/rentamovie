# rentamovie

Begleit-Code für einen Java-Grundlagenkurs an der FernUni Hagen (Standort
Hamburg). Die Domäne ist eine kleine Videothek: Kunden leihen Kopien von
Filmen aus, zu unterschiedlichen Tarifen.

Die Videothek-Domäne stammt ursprünglich aus dem Schulungsthema
`netzfilm`, das 2017 für Spring-Boot- und Spring-Cloud-Schulungen
entstand. Der Name war eine Anspielung auf Netflix und zugleich ein
anachronistischer Scherz: Wer würde 2017 noch eine Online-Videothek
gründen? Historisch passt das Thema trotzdem zu den frühen 2000ern, als
Unternehmen wie Lovefilm DVDs und Blu-rays online verliehen und per Post
verschickten.

Dieses Repository nutzt dieselbe Domäne für die dritte Veranstaltung
einer Kursreihe. Die Zielgruppe hatte vorher bereits „imperative
Programmierung mit Java" und „objektorientierte Programmierung mit Java".
Hier geht es um eine konkrete Business-Anwendung mit grafischer
Java-Oberfläche, grundlegender Architektur, Tests, Datei-Arbeit,
Repository-Grundlagen, Validierungen und UI.

Das Repository ist **keine** lauffähige Einzelanwendung, sondern eine
Sequenz aus zehn aufeinander aufbauenden Maven-Modulen
(`Zustand_1` … `Zustand_10`). Jeder Zustand ist ein eigenständiger
Schnappschuss derselben Domäne und führt **genau ein** neues Konzept ein.

## Lernziel

Die Studierenden sollen Java-Grundlagen *ohne* Framework-Magie nachvollziehen:

- **Klassische OOP**: Konstruktor, private Felder, Getter, sprechende
  Update-Methoden — keine Records, kein Lombok.
- **Hand-verdrahtete Architektur**: Singletons über statische
  `getService()` / `getRepository()`, kein Spring, kein DI-Framework.
- **In-Memory- und Datei-Persistenz** statt JPA/Hibernate.
- **JavaFX mit FXML** als bewusst klassische Desktop-UI — keine Web-
  oder JS-Schicht.
- **So wenige Bibliotheken wie möglich**: externe Abhängigkeiten werden
  nur genutzt, wenn sie dem Lernziel dienen oder im Java-Ökosystem
  üblich sind.

Die Progression `Zustand_1` → `Zustand_10` ist der eigentliche Lehrstoff:
sie zeigt, wie eine Anwendung in lesbaren Schritten von „nur Domänen-
klassen" zu „GUI mit File-Repository" wächst.

Fachlich ist die Anwendung aktuell als **1-Mann-Einzel-Videothek**
geschnitten. Daraus folgt bewusst:

- keine Mitarbeiterverwaltung,
- keine Rollen- und Rechteverwaltung,
- keine konkurrierende Bearbeitung durch mehrere Arbeitsplätze,
- keine Client-Server-Architektur.

Diese Themen sind nicht vergessen, sondern gehören in eine spätere
Ausbaustufe: Die Videothek ist erfolgreich, stellt Mitarbeiter ein und
betreibt mehrere Arbeitsplätze.

Aktuelle fachliche Use-Cases in `Zustand_10` für Filme und Kopien:

- neue Filme über die Oberfläche erfassen,
- Filmdaten wie Titel und Erscheinungsjahr über die Oberfläche ändern,
- ankommende Kopien eines Films durch zusätzliche Kopien einbuchen,
- Filme suchen.

Die Domäne kennt bereits Basisfelder für Status und Format: Kunden haben
einen `CustomerStatus`, Filme einen `MovieStatus`, Kopien einen
`CopyStatus` und ein `MediaFormat`. In `Zustand_10` sind diese Felder
im Verleihkern fachlich verdrahtet: Es wird nur an aktive Kunden verliehen,
Filme müssen aktiv sein, und Kopien müssen verfügbar sein. In der
Oberfläche sind die Felder noch nicht vollständig bearbeitbar.

Gezieltes Löschen einzelner Filme oder Kopien ist in `Zustand_10` noch
nicht umgesetzt. Es gibt nur den Demo-Daten-Reset, der alle Repositories
leert und Seed-Daten neu anlegt.

Aktuelle fachliche Use-Cases in `Zustand_10` für Kunden und Verleih:

- Kunden über die Oberfläche erfassen und ändern,
- eine Kopie an einen bestehenden Kunden verleihen,
- offene Ausleihen zurücknehmen,
- zurückgegebene Ausleihen bezahlen.

Ein Verleih ohne Kundenanlage ist bewusst nicht vorgesehen. Das passt zum
Videothek-Szenario: Wer Medien mitnimmt, braucht ein Kundenkonto bzw. einen
Ausweis, weil beim Verleih zunächst nur die geplante Gebühr erfasst wird
und spätere Nachzahlungen möglich sind. Die Kundenanlage erfolgt aktuell
vor dem Verleih in der Kundenübersicht, nicht innerhalb des Verleihdialogs.

Gezieltes Löschen einzelner Kunden ist in `Zustand_10` noch nicht
umgesetzt. Ausleihen haben fachlich ein geplantes Rückgabedatum, standardmäßig
sieben Tage nach dem Startdatum. Der Verleihdialog erfasst diese geplante
Leihdauer aber noch nicht; er nutzt aktuell den Default. In `Zustand_10`
kann die Ausleihliste nach überfälligen Ausleihen gefiltert werden.

Gebührenberechnung nach Tagen existiert als `RateService`: Junior,
Regular und Senior haben unterschiedliche Tagespreise, und der Preis wird
aus Start- und Rückgabedatum berechnet. Die JavaFX-Zahlung nutzt diese
Berechnung aktuell aber noch nicht sichtbar im Dialog; eine positive
Zahlung markiert die zurückgegebene Ausleihe als bezahlt.

## Was zeigt welcher Zustand?

| Modul          | Neues Konzept                                                                                                                                      |
|----------------|----------------------------------------------------------------------------------------------------------------------------------------------------|
| **Zustand_1**  | Reine Domänenklassen (`Customer`, `Movie`, `Copy`, `Rent`, `Rate`). `App.main` baut sie von Hand. Logging über `System.Logger` (im JDK enthalten). |
| **Zustand_2**  | Service-Schicht: `XService` als Interface, `XServiceImpl` als Implementierung. Ab hier SLF4J + Logback.                                            |
| **Zustand_3**  | Gemeinsame Basisklasse `AbstractIdCarrier` (Vererbung, ID-Verwaltung, `equals`/`hashCode`).                                                        |
| **Zustand_4**  | Erste Tests: JUnit 5, AssertJ, Mockito. Test-Methoden mit `@Mock` / `@InjectMocks`.                                                                |
| **Zustand_5**  | `RateService` — Tarif-Logik als eigenes Modul.                                                                                                     |
| **Zustand_6**  | Erstes Repository (`CustomerRepository`) mit In-Memory-Speicherung.                                                                                |
| **Zustand_7**  | Generisches `CommonRepository<T>` und `IdRepository`; Repositories speichern jetzt persistent in Dateien.                                          |
| **Zustand_8**  | Validierung und Konsistenzregeln für Verleih, Rückgabe und Zahlung.                                                                                |
| **Zustand_9**  | JavaFX-Basisoberfläche: Übersichten, Verleihdialog, Rückgabe und Zahlung.                                                                          |
| **Zustand_10** | Convenience-UI: Suche, Statusfilter und Demo-Daten-Reset.                                                                                          |

Die Zustände bleiben technisch klar geschnitten: Jeder Zustand führt ein
zentrales Java-Konzept ein. Fachliche Erweiterungen dürfen dazukommen,
wenn sie dieses Konzept greifbarer machen, zum Beispiel Rückgabe beim
Service-Konzept, Preisberechnung beim `RateService` oder Filter in der
UI. Aufgaben aus einem Zustand werden im nächsten Zustand gelöst; Tests
wachsen ab `Zustand_4` fortlaufend mit.

Zielbild für die fachliche Progression:

| Modul          | Fachlicher Schwerpunkt                                                                                  |
|----------------|---------------------------------------------------------------------------------------------------------|
| **Zustand_1**  | Grundmodell: Kunde, Film, Kopie, Ausleihe, Tarif; Vorbereitung für offene und beendete Ausleihen.       |
| **Zustand_2**  | Services orchestrieren Verleih, Rückgabe und Listen offener Ausleihen.                                  |
| **Zustand_3**  | Identität und Gleichheit bleiben im Fokus; die Service-Aufgaben aus `Zustand_2` werden fachlich gelöst. |
| **Zustand_4**  | Erste Tests sichern Verleih, Rückgabe, Verfügbarkeit und ID-Gleichheit ab.                              |
| **Zustand_5**  | Preisberechnung: Mietdauer, Tagespreis, optional Überziehung und Zahlungsfälle.                         |
| **Zustand_6**  | Repository-Lese-Use-Cases: Kunden, Filme, Ausleihen, offene und überfällige Ausleihen.                  |
| **Zustand_7**  | Datei-Persistenz für offene und abgeschlossene Ausleihen sowie Zahlungsstatus.                          |
| **Zustand_8**  | Validierung und Konsistenzregeln, z. B. keine doppelte Verleihung derselben Kopie.                      |
| **Zustand_9**  | Basic UI für Kunde, Film, Kopie, Verleih, Rückgabe und Zahlung.                                         |
| **Zustand_10** | Convenience UI mit Suche, Statusfilter und Demo-Daten für Kursübungen.                                  |

Erweiterungsplan für fachlich realistischere 1-Mann-Videothek:

| Modul            | Sinnvolle Ergänzung                                                                                                        | Warum hier?                                                                                  |
|------------------|----------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------|
| **Zustand_1**    | Domänenfelder vorbereiten: Kundenstatus, Filmstatus, Kopienstatus und Medienformat als einfache Attribute.                 | Reine OOP-Modellierung ohne Services, Tests oder Persistenz.                                 |
| **Zustand_2**    | Service-Regeln ergänzen: nur aktive Kunden, aktive Filme und verfügbare Kopien.                                            | Services sind neu und kapseln fachliche Abläufe.                                             |
| **Zustand_3**    | Statusfelder in die ID-/Gleichheitsstruktur einpassen, ohne `equals`/`hashCode` fachlich zu verwässern.                    | Identität bleibt stabil, fachlicher Status ändert sie nicht.                                 |
| **Zustand_4**    | Tests für Status- und Validierungsregeln ergänzen.                                                                         | Erste Teststufe: Regeln werden hier sichtbar abgesichert.                                    |
| **Zustand_5**    | Gebühren sauberer nutzen: Preis aus Tagespreis und Mietdauer, spätere Überziehungsgebühren als Aufgabe vorbereiten.        | `RateService` ist der richtige Ort für Preislogik.                                           |
| **Zustand_6**    | Repository-Lese-Use-Cases ergänzen: aktive Kunden, aktive Filme und überfällige Ausleihen.                                 | Repositorys liefern jetzt fachliche Listen.                                                  |
| **Zustand_7**    | Neue Felder persistieren: Status, Medienformat, geplantes Rückgabedatum und Zahlungsinformationen in Dateien speichern.    | Datei-Persistenz ist hier das neue technische Konzept.                                       |
| **Zustand_8**    | Konsistenzregeln schärfen: verfügbare Kopien filtern, keine Ausleihe an gesperrte Kunden oder verliehene Kopien.           | Validierung und Konsistenzregeln stehen hier im Zentrum.                                     |
| **Zustand_9**    | UI-Bedienung ergänzen: Status anzeigen/ändern, Medienformat pflegen, geplantes Rückgabedatum anzeigen, offene Forderungen. | JavaFX-Basis-UI ist vorhanden; neue Felder und Aktionen sind „mehr vom gleichen".            |
| **Zustand_10**   | Komfort ergänzen: Filter für aktive/inaktive Daten, überfällige Ausleihen, offene Forderungen, robuste Sortierung.         | Z10 ist die Convenience-Stufe; Suche und Statusfilter werden fachlich erweitert.             |
| **Zustand_11ff** | Optional: Soft-Delete konsequent ausbauen, Archivmodell, Paging, Großhandel, Bestellsystem und Mehrplatz-Ausblick.         | Größere fachliche oder architektonische Schritte gehören nach der Grundlagenprogression hin. |

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

Aufgaben nach `Zustand_9`:

- Ergänze Suche, Filter und optional robuste Sortierung für Kunden,
  Filme und Ausleihen.
- Erweitere die vorhandenen Filter zu klareren Arbeitslisten für offene
  Ausleihen, offene Zahlungen und überfällige Ausleihen.
- Ergänze Demo-Daten-Reset und mehr Seed-Daten für Kursübungen.

Mögliche Hausaufgaben nach `Zustand_10` sollen die Anwendung fachlich
realistischer machen, ohne die Studierenden mit viel neuer Technik zu
überfordern. Gute Kandidaten sind „mehr vom gleichen": zusätzliche
Felder, Validierungen, Repository-Methoden, Services, JavaFX-Dialoge und
Tests. Dazu gehören zum Beispiel UI-Erfassung der geplanten Leihdauer,
klarere Arbeitslisten, UI-Filter für Status/Format sowie das
fachlich saubere Entfernen von Kunden, Filmen und Kopien. Robuste
Sortierung und Paging sind ebenfalls denkbare Erweiterungen, aber nicht
zwingend für die 1-Mann-Videothek.

Gerade Löschen ist didaktisch interessant: Wegen der Verleihhistorie ist
hartes Entfernen oft falsch. Realistisch sind eher Soft-Delete-Varianten
wie „inaktiv", „gesperrt" oder „ausgemustert". Alternativ könnte eine
Ausleihe vor dem Löschen beteiligter Objekte textuell archiviert werden,
damit historische Belege erhalten bleiben, ohne weiter auf aktive
Domain-Objekte zu zeigen. Solche Themen eignen sich gut für
`Zustand_11ff`: erst als Aufgabe formulieren, im nächsten Zustand eine
Referenzlösung zeigen.

Jeder Zustand bleibt nach Veröffentlichung „eingefroren" — er
dokumentiert den Lernschritt, nicht den letzten Stand. Wer ein Konzept
aus `Zustand_10` braucht, schaut dort; in `Zustand_3` gehört es nicht hin.

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

### JavaFX-Oberfläche starten (Zustand_9 / Zustand_10)

```bash
cd Zustand_9/rentamovie
mvn javafx:run

cd Zustand_10/rentamovie
mvn javafx:run
```

In IntelliJ IDEA lassen sich `Zustand_9` und `Zustand_10` auch über die
Run-Konfiguration für `App` starten.

## Code-Qualität

[ErrorProne](https://errorprone.info/) läuft als
Annotation-Processor im Compile-Schritt und gibt Hinweise direkt in der
Maven-Ausgabe (Severity `WARN` — der Build wird dadurch nicht rot, aber
neue Findings fallen sofort auf).

### Test-Isolation für ID-Dateien

Ab `Zustand_7` schreibt die Anwendung die nächste ID in `id.db`. Im
Produktiv- und UI-Lauf bleibt das bewusst eine Datei im jeweiligen
Zustand, damit Datei-Persistenz sichtbar bleibt. Maven-Tests setzen
dagegen `rentamovie.id.db` auf `target/id.db`; Testläufe verändern
dadurch nicht die versionierten `id.db`-Dateien.

## Verzeichnis-Aufbau

```
rentamovie/
├── pom.xml                 ← Parent-POM (Java-25-Toolchain, alle Versionen)
├── .mvn/jvm.config         ← JDK-internal-API-Exports für ErrorProne
├── Zustand_1/rentamovie/   ← Schritt 1: nur Domäne
├── Zustand_2/rentamovie/   ← Schritt 2: Service-Schicht
├── …
├── Zustand_9/rentamovie/   ← Schritt 9: JavaFX-Basis-UI
└── Zustand_10/rentamovie/  ← Schritt 10: JavaFX-Convenience-UI
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
