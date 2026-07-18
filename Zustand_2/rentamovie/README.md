# Zustand 2 — Service-Schicht

Die Abläufe wandern aus `App.main` in Services: `CustomerService`,
`MovieService` und `RentService` (jeweils Interface + Implementierung)
kapseln Anlegen, Ändern, Verleihen und Zurücknehmen. Ab diesem Zustand wird
mit SLF4J + Logback geloggt.

## Was die Anwendung kann

- Filme über den `MovieService` anlegen und ändern
  (`createMovie`, `updateMovie`).
- Kunden über den `CustomerService` anlegen (`createCustomer`).
- Eine Ausleihe über den `RentService` anlegen (`createRent`) — der Service
  kümmert sich intern um die Kopie.
- Eine Ausleihe zurücknehmen (`returnRent`).
- Offene Ausleihen abfragen (`findOpenRents`), damit die Verfügbarkeit
  sichtbar wird.

Die Daten leben weiterhin nur im Speicher; jeder Service verwaltet seine
eigene Liste. Nach dem Programmende sind die Daten weg.

## Starten

```bash
cd Zustand_2/rentamovie
mvn exec:java -Dexec.mainClass=de.zettsystems.rentamovie.main.App
```

## Was der Demo-Lauf zeigt

1. Der Film „A new hope" wird angelegt und anschließend in
   „A good hope" umbenannt.
2. Der Kunde Luke Skywalker wird angelegt.
3. Eine Ausleihe entsteht — das Log zeigt die Zahl der offenen Ausleihen (1).
4. Die Ausleihe wird zurückgegeben — die Zahl der offenen Ausleihen sinkt
   auf 0.

Bekannte Schwäche (Absicht, wird in Zustand 3 gelöst): `App` und
`RentServiceImpl` erzeugen sich ihre Services jeweils selbst per `new` —
dadurch existieren mehrere Instanzen mit getrenntem Zustand.
