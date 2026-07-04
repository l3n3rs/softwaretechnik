# Architektur

Dieses Projekt ist als einfache verteilte Anwendung aufgebaut. Das bedeutet:
Die Anwendung besteht nicht aus einer einzigen grossen Datei, sondern aus
mehreren getrennten Bereichen mit klaren Aufgaben.

## Grundidee der Architektur

Die Bandshirt-Datenbank ist in drei technische Hauptbereiche aufgeteilt:

- Frontend
- Backend
- Datenbank

Diese Trennung ist wichtig, weil jeder Bereich eine andere Aufgabe hat. Das
Frontend ist fuer die Anzeige und Bedienung da. Das Backend enthaelt die
Programmlogik. Die Datenbank beschreibt, wie die Informationen dauerhaft
gespeichert werden sollen.

## Architekturuebersicht

```text
Benutzer
   |
   v
Frontend
HTML, CSS, JavaScript
   |
   | spaeter: REST-Aufrufe
   v
Backend
Java: Controller, Service, Repository, Model
   |
   | spaeter: SQL-Zugriff
   v
Datenbank
Tabellen fuer Bands, Namen, Quellen und Aktivitaetslog
```

Im aktuellen Stand ist das Frontend noch nicht technisch mit dem Backend
verbunden. Das ist fuer dieses Studienprojekt bewusst in Ordnung. Ziel ist
zuerst, die Struktur und die Verantwortlichkeiten zu verstehen.

## Frontend

Das Frontend liegt im Ordner `frontend/`.

Es besteht aus:

- `index.html`: Struktur der Oberflaeche
- `css/`: Gestaltung der Oberflaeche
- `js/app.js`: Logik im Browser

Das Frontend kann im aktuellen Prototyp bereits Bands anzeigen, anlegen,
suchen, filtern, sortieren, bearbeiten, loeschen und exportieren. Die Daten
werden dabei noch im Browser gespeichert. Spaeter wuerde das Frontend die Daten
ueber REST-Schnittstellen vom Backend laden.

## Backend

Das Backend liegt im Ordner `backend/`.

Es ist in mehrere Schichten aufgeteilt:

- Controller
- Service
- Repository
- Model

Diese Schichten machen den Code uebersichtlich. Jede Schicht hat eine eigene
Aufgabe und muss nicht alles ueber die anderen Schichten wissen.

## Datenbank

Das Datenmodell liegt im Ordner `database/` in der Datei `schema.sql`.

Darin wird beschrieben, welche Tabellen spaeter gebraucht werden:

- `bands`
- `sekundaere_bandnamen`
- `quellen`
- `activity_log`

Im aktuellen Java-Code werden die Daten noch nicht in einer echten Datenbank
gespeichert. Stattdessen gibt es einfache In-Memory-Repositories. Diese
speichern Daten nur zur Laufzeit im Arbeitsspeicher. Das macht den ersten Code
leichter verstaendlich.

## Warum diese Architektur sinnvoll ist

Die Trennung in Frontend, Backend und Datenbank hilft beim Verstehen und
Erweitern der Anwendung.

Das Frontend kann geaendert werden, ohne die Java-Logik direkt anzufassen. Das
Backend kann Regeln pruefen, ohne vom Layout der Webseite abzuhaengen. Die
Datenbank kann spaeter angebunden oder ausgetauscht werden, ohne die gesamte
Oberflaeche neu zu schreiben.

Fuer ein Studienprojekt ist diese Struktur besonders sinnvoll, weil man daran
gut erklaeren kann, wie verteilte Anwendungen aufgebaut sind.

## Geplanter Datenfluss

Ein spaeterer Ablauf beim Anlegen einer Band koennte so aussehen:

1. Der Benutzer fuellt im Frontend das Formular aus.
2. Das Frontend sendet die Daten per REST an das Backend.
3. Der Controller nimmt die Anfrage entgegen.
4. Der Service prueft die Daten und erstellt eine Band.
5. Das Repository speichert die Band.
6. Die Datenbank haelt die Band dauerhaft fest.
7. Das Backend meldet dem Frontend das Ergebnis zurueck.

Aktuell ist dieser Ablauf teilweise vorbereitet, aber noch nicht vollstaendig
technisch verbunden.
