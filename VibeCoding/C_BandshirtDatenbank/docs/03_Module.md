# Module (erstellt durch Codex)

Dieses Dokument beschreibt die wichtigsten Module der Bandshirt-Datenbank. Ein
Modul ist ein klar abgegrenzter Teil der Anwendung mit einer bestimmten
Aufgabe.

Die Anwendung besteht nicht nur aus Frontend, Backend und
Datenbankbeschreibung. Es gibt zusätzlich einen getrennten
Export-Service. Dadurch wird die Anwendung als verteilte Anwendung erkennbar.

## Frontend-Modul

Pfad: `frontend/`

Das Frontend ist die Benutzeroberfläche. Hier arbeitet der Benutzer direkt mit
der Anwendung.

### Dateien

- `index.html`
- `css/globals.css`
- `css/theme.css`
- `css/fonts.css`
- `js/app.js`

### Aufgaben

Das Frontend kann im aktuellen Prototyp:

- Bands anlegen
- Bands anzeigen
- Bands in der Detailansicht bearbeiten
- Bands löschen
- nach primärem Bandnamen suchen
- nach sekundären Bandnamen suchen
- nach Recherche-Status filtern
- nach Bandname, letzter Änderung oder Status sortieren
- die komplette Bandliste als CSV oder XLS exportieren
- einen einfachen angemeldeten Nutzer simulieren

Das Frontend ist weiterhin bewusst einfach. Später koennte es Export-Anfragen
an das Backend senden, statt den Export direkt im Browser zu erzeugen.

## Backend/API-Modul

Pfad: `backend/src/main/java/bandshirt/`

Das Backend/API-Modul ist Prozess 1 der verteilten Anwendung. Es verwaltet die
Bands und nimmt Export-Anfragen entgegen.

Das Backend ist in mehrere Bereiche aufgeteilt:

- Controller
- Service
- Repository
- Model

### Startklasse

- `BackendApiApplication`

Diese Klasse zeigt den Backend/API-Prozess als eigenes startbares Programm. In
einer echten Anwendung würde sie einen Webserver starten. Im einfachen
Prototyp legt sie Beispieldaten an und schreibt einen Export-Auftrag in die
gemeinsame Job-Datei.

## Controller

Pfad: `backend/src/main/java/bandshirt/controller/`

Der Controller ist die Eingangsschicht des Backends. In einer echten
Webanwendung würde er HTTP-Anfragen vom Frontend entgegennehmen.

### Klasse

- `BandController`

### Aufgabe

Der `BandController` stellt Methoden bereit, die später REST-Endpunkten
entsprechen könnten.

Beispiele:

- Band anlegen
- Band anzeigen
- Band bearbeiten
- Band löschen
- Bands suchen, filtern und sortieren
- Aktivitätslog anzeigen
- CSV-Export anfordern
- Export-Jobs anzeigen

Beim Export erstellt der Controller nicht selbst die CSV-Datei. Er leitet die
Anfrage an den `ExportRequestService` weiter.

## Service

Pfad: `backend/src/main/java/bandshirt/service/`

Services enthalten die fachliche Logik der Anwendung.

### Klassen

- `BandService`
- `ExportRequestService`

### Aufgabe von `BandService`

Der `BandService` entscheidet, was bei Aktionen mit Bands passieren soll.

Beispiele:

- Pflichtfelder pruefen
- Band anlegen
- Band bearbeiten
- Band löschen
- Suche, Filter und Sortierung ausführen
- Logeinträge vorbereiten

### Aufgabe von `ExportRequestService`

Der `ExportRequestService` ist für Export-Anfragen im Backend zuständig.

Er macht drei Dinge:

1. Er liest die aktuelle Bandliste über den `BandService`.
2. Er erstellt daraus einen einfachen Snapshot mit Bandname und Status.
3. Er schreibt einen Export-Auftrag in die gemeinsame Job-Datei.

Der Service erzeugt also nur einen Auftrag. Die eigentliche CSV-Datei erzeugt
später der getrennte Export-Service.

## Repository

Pfad: `backend/src/main/java/bandshirt/repository/`

Repositories sind für das Speichern und Laden von Daten zuständig.

### Klassen und Interfaces

- `BandRepository`
- `InMemoryBandRepository`
- `ActivityLogRepository`
- `InMemoryActivityLogRepository`
- `ExportJobRepository`
- `ExportJobFileRepository`

### Aufgabe

Die Band- und ActivityLog-Repositories speichern Daten im aktuellen Prototyp im
Arbeitsspeicher.

Das `ExportJobFileRepository` speichert Export-Aufträge dagegen in einer
Datei:

```text
shared/export-jobs.csv
```

Diese Datei ist die einfache Kommunikationsschnittstelle zwischen Backend und
Export-Service.

## Model

Pfad: `backend/src/main/java/bandshirt/model/`

Model-Klassen beschreiben die Datenobjekte der Anwendung.

### Klassen

- `Band`
- `ActivityLogEntry`
- `ResearchStatus`
- `BandSortierung`
- `ExportJob`
- `ExportJobStatus`

### Aufgabe

`Band` beschreibt eine Band mit Namen, Status, Quellen, Kommentar und
Änderungsinformationen.

`ActivityLogEntry` beschreibt einen Eintrag im Aktivitätslog.

`ResearchStatus` enthält die erlaubten Recherche-Status:

- GRUEN
- GELB
- ROT
- UNRECHERCHIERT

`BandSortierung` enthält die erlaubten Sortierarten:

- BANDNAME
- LETZTE_AENDERUNG
- STATUS

`ExportJob` beschreibt einen Export-Auftrag.

`ExportJobStatus` enthält die moeglichen Zustände eines Export-Auftrags:

- OFFEN
- IN_ARBEIT
- ERLEDIGT
- FEHLER

## Export-Service-Modul

Pfad: `export-service/`

Der Export-Service ist Prozess 2 der verteilten Anwendung. Er läuft getrennt
vom Backend/API-Prozess.

### Klasse

- `ExportServiceApplication`

### Aufgabe

Der Export-Service liest offene Aufträge aus:

```text
shared/export-jobs.csv
```

Wenn ein Auftrag den Status `OFFEN` hat, erstellt der Export-Service eine CSV
mit diesen Spalten:

- Bandname
- Status

Danach schreibt er die CSV-Datei nach:

```text
shared/exports/
```

Anschliessend markiert er den Auftrag in der Job-Datei als `ERLEDIGT`.

## Shared-Modul

Pfad: `shared/`

Dieser Ordner ist kein eigener Prozess, sondern ein gemeinsamer
Austauschbereich.

### Dateien und Ordner

- `export-jobs.csv`
- `exports/`

### Aufgabe

Das Backend schreibt Export-Jobs in `export-jobs.csv`. Der Export-Service liest
diese Datei und schreibt fertige Export-Dateien in `exports/`.

Damit ist `shared/` die einfache Kommunikationsstelle zwischen den beiden
Prozessen.

## Datenbank-Modul

Pfad: `database/`

Das Datenbank-Modul enthält die Datei `schema.sql`.

Diese Datei beschreibt, welche Tabellen später in einer relationalen Datenbank
angelegt werden koennten.

## Dokumentations-Modul

Pfad: `docs/`

Die Dokumentation erklärt das Projekt Schritt fuer Schritt.

Wichtige Dokumente sind:

- Idee
- Architektur
- Module
- Datenmodell
- Fazit

Die Dokumentation ist fuer dieses Studienprojekt besonders wichtig, weil der
Code nicht nur funktionieren, sondern auch verstanden und erklärt werden soll.
