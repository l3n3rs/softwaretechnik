# Module

Dieses Dokument beschreibt die wichtigsten Module der Bandshirt-Datenbank. Ein
Modul ist ein klar abgegrenzter Teil der Anwendung mit einer bestimmten
Aufgabe.

## Frontend-Modul

Pfad: `frontend/`

Das Frontend ist die Benutzeroberflaeche. Hier arbeitet der Benutzer direkt mit
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
- Bands loeschen
- nach primaerem Bandnamen suchen
- nach sekundaeren Bandnamen suchen
- nach Recherche-Status filtern
- nach Bandname, letzter Aenderung oder Status sortieren
- die komplette Bandliste als CSV oder XLS exportieren
- einen einfachen angemeldeten Nutzer simulieren

Der angemeldete Nutzer wird aktuell ueber ein Eingabefeld im Kopfbereich
gesetzt. Dieser Name wird automatisch als Ersteller oder Bearbeiter verwendet.
Spaeter koennte diese Stelle durch ein echtes Login ersetzt werden.

## Backend-Modul

Pfad: `backend/src/main/java/bandshirt/`

Das Backend enthaelt die Java-Klassen. Es ist in vier Unterbereiche aufgeteilt.

## Controller

Pfad: `backend/src/main/java/bandshirt/controller/`

Der Controller ist die Eingangsschicht des Backends. In einer echten
Webanwendung wuerde er HTTP-Anfragen vom Frontend entgegennehmen.

### Klasse

- `BandController`

### Aufgabe

Der `BandController` stellt Methoden bereit, die spaeter REST-Endpunkten
entsprechen koennten. Er enthaelt selbst moeglichst wenig Logik und leitet die
Arbeit an den `BandService` weiter.

Beispiele:

- Band anlegen
- Band anzeigen
- Band bearbeiten
- Band loeschen
- Bands suchen, filtern und sortieren
- Aktivitaetslog anzeigen

## Service

Pfad: `backend/src/main/java/bandshirt/service/`

Der Service enthaelt die fachliche Logik der Anwendung.

### Klasse

- `BandService`

### Aufgabe

Der `BandService` entscheidet, was bei einer Aktion passieren soll. Er prueft
Pflichtfelder, aendert Banddaten und erzeugt Logeintraege fuer wichtige
Aenderungen.

Beispiele:

- Beim Anlegen einer Band wird ein Logeintrag erstellt.
- Beim Bearbeiten werden alte und neue Werte im Log festgehalten.
- Bei Suche, Filter und Sortierung wird die passende Bandliste erzeugt.

## Repository

Pfad: `backend/src/main/java/bandshirt/repository/`

Repositories sind fuer das Speichern und Laden von Daten zustaendig.

### Klassen und Interfaces

- `BandRepository`
- `InMemoryBandRepository`
- `ActivityLogRepository`
- `InMemoryActivityLogRepository`

### Aufgabe

Die Interfaces beschreiben, welche Speicherfunktionen gebraucht werden. Die
In-Memory-Klassen setzen diese Funktionen fuer den ersten Prototyp um.

In-Memory bedeutet: Die Daten werden nur im Arbeitsspeicher gespeichert. Nach
einem Neustart waeren sie weg. Spaeter koennte man diese Klassen durch echte
Datenbank-Repositories ersetzen.

## Model

Pfad: `backend/src/main/java/bandshirt/model/`

Model-Klassen beschreiben die Datenobjekte der Anwendung.

### Klassen

- `Band`
- `ActivityLogEntry`
- `ResearchStatus`
- `BandSortierung`

### Aufgabe

`Band` beschreibt eine Band mit Namen, Status, Quellen, Kommentar und
Aenderungsinformationen.

`ActivityLogEntry` beschreibt einen Eintrag im Aktivitaetslog.

`ResearchStatus` enthaelt die erlaubten Recherche-Status:

- GRUEN
- GELB
- ROT
- UNRECHERCHIERT

`BandSortierung` enthaelt die erlaubten Sortierarten:

- BANDNAME
- LETZTE_AENDERUNG
- STATUS

## Datenbank-Modul

Pfad: `database/`

Das Datenbank-Modul enthaelt die Datei `schema.sql`.

Diese Datei beschreibt, welche Tabellen spaeter in einer relationalen Datenbank
angelegt werden koennten.

## Dokumentations-Modul

Pfad: `docs/`

Die Dokumentation erklaert das Projekt Schritt fuer Schritt.

Wichtige Dokumente sind:

- Idee
- Architektur
- Module
- Datenmodell
- Fazit

Die Dokumentation ist fuer dieses Studienprojekt besonders wichtig, weil der
Code nicht nur funktionieren, sondern auch verstanden und erklaert werden soll.
