# Architektur (erstellt durch Codex)

Dieses Projekt ist als einfache verteilte Anwendung aufgebaut.
Wichtig ist dabei nicht nur die Trennung in Frontend, Backend und Datenbank,
sondern vor allem die Trennung in mindestens zwei Prozesse.

Ein Prozess ist ein eigenständig laufendes Programm. Wenn zwei Prozesse
getrennt laufen und trotzdem Daten austauschen, entsteht eine verteilte
Anwendung.

## Warum ist das jetzt eine verteilte Anwendung?

Die Anwendung besteht aus zwei getrennten Java-Prozessen:

- Prozess 1: Backend/API
- Prozess 2: Export-Service

Diese Prozesse haben unterschiedliche Aufgaben und koennen getrennt gestartet
werden. Sie kommunizieren nicht durch direkte Methodenaufrufe, sondern ueber
eine gemeinsame Datei.

Das ist eine sehr einfache Form verteilter Kommunikation. In grossen Systemen
wuerde man dafuer oft eine Datenbank, eine Message Queue oder einen externen
Dienst verwenden. Fuer dieses Studienprojekt reicht eine Job-Datei, weil sie
leicht zu verstehen und zu erklären ist.

## Architekturuebersicht

```text
Benutzer
   |
   v
Frontend
HTML, CSS, JavaScript
   |
   | später: REST-Aufrufe
   v
Prozess 1: Backend/API
Bandverwaltung und Export-Anfrage
   |
   | schreibt Export-Auftrag
   v
shared/export-jobs.csv
gemeinsame Job-Datei
   |
   | liest offene Export-Aufträge
   v
Prozess 2: Export-Service
erstellt CSV-Dateien
   |
   v
shared/exports/
fertige Export-Dateien
```

## Prozess 1: Backend/API

Der Backend/API-Prozess ist für die eigentliche Bandverwaltung zuständig.

Aufgaben:

- Bands anlegen
- Bands anzeigen
- Bands bearbeiten
- Bands loeschen
- Bands suchen, filtern und sortieren
- Aktivitätslog vorbereiten
- Export-Anfragen entgegennehmen
- Export-Aufträge in die Job-Datei schreiben

Wichtig: Das Backend erstellt die Export-Datei nicht selbst. Es schreibt nur
einen Auftrag. Dadurch bleibt das Backend frei fuer seine Hauptaufgabe: die
Verwaltung der Banddaten.

## Prozess 2: Export-Service

Der Export-Service ist ein eigener Prozess im Ordner `export-service/`.

Aufgaben:

- Job-Datei lesen
- offene Export-Aufträge finden
- zu jedem offenen Auftrag eine CSV-Datei erstellen
- den Auftrag danach als erledigt markieren

Der Export-Service kann unabhängig vom Backend gestartet werden. Er arbeitet
die Jobs ab, die vorher vom Backend geschrieben wurden.

## Kommunikation zwischen den Prozessen

Die Prozesse kommunizieren über diese Datei:

```text
shared/export-jobs.csv
```

Das Backend schreibt dort neue Export-Aufträge hinein. Ein Auftrag enthält
zum Beispiel:

- Job-ID
- Status
- Benutzer
- Erstellungszeit
- Ziel-Datei
- Snapshot der Bandnamen und Statuswerte

Der Export-Service liest dieselbe Datei. Wenn er einen Auftrag mit dem Status
`OFFEN` findet, verarbeitet er ihn und setzt den Status danach auf `ERLEDIGT`.

Die erzeugten Export-Dateien liegen hier:

```text
shared/exports/
```

## Warum ist der Export-Service getrennt vom Backend?

Der Export ist eine Aufgabe, die getrennt von der normalen Bandverwaltung
ablaufen kann. Das hat mehrere Vorteile:

- Das Backend muss nicht warten, bis eine Export-Datei fertig geschrieben ist.
- Der Export kann später auch grössere Datenmengen verarbeiten.
- Der Export-Service kann bei Bedarf getrennt gestartet, gestoppt oder ersetzt werden.
- Die Architektur zeigt klar, wie zwei Prozesse zusammenarbeiten.

Fr das Studienprojekt ist diese Trennung besonders hilfreich, weil man daran
gutü erklären kann, was eine verteilte Anwendung ausmacht.

## Einfacher Ablauf eines Exports

1. Ein Benutzer fordert im Backend einen CSV-Export an.
2. Das Backend liest die aktuelle Bandliste.
3. Das Backend schreibt einen neuen Auftrag in `shared/export-jobs.csv`.
4. Der Export-Service wird getrennt gestartet.
5. Der Export-Service liest die Job-Datei.
6. Der Export-Service findet offene Jobs.
7. Der Export-Service erstellt eine CSV-Datei mit Bandname und Status.
8. Der Export-Service markiert den Job als erledigt.

## Bewusst einfache Umsetzung

Die Umsetzung ist absichtlich einfach gehalten:

- keine echte REST-Implementierung
- keine echte Datenbankanbindung
- keine Message Queue
- keine Nebenläufigkeit
- keine komplexe Fehlerbehandlung

Der Schwerpunkt liegt darauf, die verteilte Architektur zu verstehen. Deshalb
wird die Kommunikation über Dateien gelöst. Das ist technisch nicht die
modernste Lösung, aber für ein Lernprojekt sehr gut nachvollziehbar.