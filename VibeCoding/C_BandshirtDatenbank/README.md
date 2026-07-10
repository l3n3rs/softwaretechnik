# BandshirtDatabase

BandshirtDatabase ist ein Studienprojekt fuer eine einfache verteilte Anwendung zur Verwaltung und Bewertung von Bands fuer einen Verein.

Ziel ist nicht zuerst eine vollstaendig lauffaehige Anwendung, sondern eine gut verstaendliche Projektstruktur, an der sich die Trennung der Aufgaben erklaeren laesst.

## Idee

Die Anwendung verwaltet Bands und ordnet jeder Band einen Recherche-Status zu:

- gruen
- gelb
- rot
- unrecherchiert / grau

## Was das System koennen soll

Das System soll Bands fuer einen Verein verwalten. Eine Band kann angelegt,
angezeigt, bearbeitet und geloescht werden. Zu jeder Band sollen der primaere
Bandname, mehrere sekundaere Bandnamen, ein Recherche-Status, Quellen oder
Links, Kommentare, der Ersteller, das Datum der letzten Aenderung und der
Bearbeiter der letzten Aenderung gespeichert werden.

Der Recherche-Status hilft dem Verein dabei, Bands einzuordnen. Er kann gruen,
gelb, rot oder unrecherchiert sein. Wichtige Aenderungen sollen spaeter in einem
Aktivitaetslog nachvollziehbar gespeichert werden. Ein Logeintrag enthaelt den
Benutzer, Datum und Uhrzeit, die Art der Aenderung sowie alten und neuen Wert.

Der erste Code-Stand zeigt die Architektur absichtlich einfach: Das Frontend ist
eine statische HTML/CSS/JavaScript-Oberflaeche, das Backend besteht aus
verstaendlichen Java-Klassen und die Datenbank wird durch eine SQL-Datei
beschrieben.

## Verteilte Architektur

Das Projekt enthaelt jetzt zwei getrennte Prozesse:

- `backend`: Backend/API-Prozess fuer Bandverwaltung und Export-Anfragen
- `export-service`: separater Prozess fuer das Abarbeiten von Export-Jobs

Die Prozesse kommunizieren ueber die gemeinsame Datei `shared/export-jobs.csv`.
Das Backend schreibt dort Export-Auftraege hinein. Der Export-Service liest
offene Auftraege, erstellt CSV-Dateien in `shared/exports/` und markiert die
Auftraege danach als erledigt.

Weitere Funktionen im Frontend-Prototyp:

- Die komplette Bandliste kann als CSV-Datei oder einfache XLS-Datei exportiert werden.
- Der Ersteller und der Bearbeiter werden automatisch aus dem aktuell eingestellten Nutzer uebernommen.
- Eine Band kann nach dem Anlegen in der Detailansicht bearbeitet werden.
- Suche, Filter und Sortierung helfen beim Arbeiten mit groesseren Bandlisten.

## Projektstruktur

- `frontend/`: einfache HTML-, CSS- und JavaScript-Oberflaeche fuer Banduebersicht, Detailansicht und Formular.
- `backend/`: Java-Code mit typischer Schichtung in Controller, Service, Repository und Model.
- `database/`: SQL-Datei oder Beschreibung des Datenmodells.
- `docs/`: schrittweise Dokumentation der Idee, Architektur, Module, REST-API, Datenmodell und des Codeverstaendnisses.

## Warum diese Struktur sinnvoll ist

Die Ordner trennen die Anwendung in klar erkennbare Module. Das Frontend ist fuer die Darstellung und Eingaben zustaendig. Das Backend enthaelt die Fachlogik und stellt spaeter Schnittstellen bereit. Die Datenbank beschreibt, wie Bands und Bewertungen gespeichert werden. Die Dokumentation hilft dabei, die Architektur Schritt fuer Schritt nachzuvollziehen und spaeter im Studienkontext zu erklaeren.

Dadurch laesst sich die Anwendung als verteiltes System darstellen: Benutzeroberflaeche, Serverlogik und Datenhaltung sind voneinander getrennt und koennen getrennt entwickelt, verstanden und erweitert werden.
