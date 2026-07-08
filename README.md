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
