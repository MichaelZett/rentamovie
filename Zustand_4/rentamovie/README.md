# Zustand 4 — Erste Tests

Die Anwendung selbst bleibt gegenüber Zustand 3 unverändert — neu ist der
Testbaum unter `src/test/java`: JUnit Jupiter, AssertJ und Mockito
(mit `@ExtendWith(MockitoExtension.class)`, `@Mock` und `@InjectMocks`).

## Was die Anwendung kann

Wie Zustand 3:

- Filme anlegen und ändern, Kunden anlegen.
- Ausleihen anlegen, zurücknehmen und offene Ausleihen abfragen.
- Services als Singletons, Gleichheit über die ID aus `AbstractIdCarrier`.

Neu abgesichert durch Tests:

- `RentServiceImplTest` — Ausleihe anlegen, zurückgeben, offene Ausleihen
  filtern (mit gemockten Nachbar-Services).
- `CustomerServiceImplTest` und `CustomerTest` — Anlegen und Ändern.
- `AbstractIdCarrierTest` — ID-basierte Gleichheit.

## Starten

```bash
cd Zustand_4/rentamovie
mvn exec:java -Dexec.mainClass=de.zettsystems.rentamovie.main.App
```

Der Demo-Lauf entspricht Zustand 3: Film und Kunde anlegen, ausleihen,
zurückgeben, offene Ausleihen zählen.

## Tests ausführen

```bash
# Aus dem Repository-Wurzelverzeichnis
mvn -pl Zustand_4/rentamovie -am test

# Einzelner Test / einzelne Testmethode
mvn -pl Zustand_4/rentamovie test -Dtest=RentServiceImplTest
mvn -pl Zustand_4/rentamovie test -Dtest=RentServiceImplTest#shouldCreateRent
```
