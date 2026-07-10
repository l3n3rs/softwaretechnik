# Datenmodell (erstellt durch Codex)

Dieses Dokument beschreibt die wichtigsten Daten der Bandshirt-Datenbank. Das
Datenmodell zeigt, welche Informationen im System gespeichert werden sollen und
wie diese Informationen zusammenhaengen.

## Zentrale Entitaet: Band

Die wichtigste Entitaet ist die Band.

Eine Band besitzt folgende Informationen:

- ID
- primaerer Bandname
- mehrere sekundaere Bandnamen
- Recherche-Status
- mehrere Quellen oder Links
- Kommentar
- Ersteller
- Datum und Uhrzeit der letzten Aenderung
- Bearbeiter der letzten Aenderung

Die ID dient zur eindeutigen Erkennung einer Band. Der primaere Bandname ist
der Hauptname, unter dem die Band gefuehrt wird. Sekundaere Namen sind
alternative Namen, Schreibweisen oder fruehere Namen.

## Recherche-Status

Der Recherche-Status beschreibt, wie die Band eingeschaetzt oder bearbeitet
wurde.

Moegliche Werte:

- gruen
- gelb
- rot
- unrecherchiert

Im Java-Code wird dieser Status durch das Enum `ResearchStatus` dargestellt.
Das verhindert freie Texte und Tippfehler.

## Sekundaere Bandnamen

Eine Band kann mehrere sekundaere Namen haben. Deshalb werden diese Namen im
SQL-Modell in einer eigenen Tabelle gespeichert.

Beispiel:

```text
Band: Beispielband
Sekundaere Namen:
- Demo Name
- Testprojekt
```

Diese Struktur ist flexibler als mehrere feste Spalten wie `name2`, `name3`
oder `name4`.

## Quellen

Zu einer Band koennen mehrere Quellen oder Links gespeichert werden.

Beispiele:

- Webseite der Band
- Artikel
- Social-Media-Profil
- Recherchequelle

Auch Quellen stehen im SQL-Modell in einer eigenen Tabelle, weil eine Band
mehrere Quellen haben kann.

## Kommentare

Kommentare sind freie Notizen zur Band. Dort koennen kurze Hinweise zur
Recherche, Bewertung oder offenen Fragen stehen.

Im aktuellen Datenmodell ist der Kommentar direkt in der Band-Tabelle
gespeichert, weil es pro Band zuerst nur ein Kommentarfeld gibt.

## Ersteller und Bearbeiter

Jede Band speichert:

- wer sie erstellt hat
- wann sie zuletzt geaendert wurde
- wer sie zuletzt geaendert hat

Im Frontend-Prototyp wird der Nutzername im Kopfbereich der Anwendung
eingetragen. Dieser Name wird automatisch als Ersteller oder Bearbeiter
verwendet. Spaeter kann diese Stelle durch ein echtes Login-System ersetzt
werden.

## Aktivitaetslog

Das Aktivitaetslog soll wichtige Aenderungen nachvollziehbar machen.

Ein Logeintrag enthaelt:

- ID
- Band-ID
- Benutzer
- Datum und Uhrzeit
- Art der Aenderung
- alter Wert
- neuer Wert

Dadurch kann man spaeter sehen, wer etwas geaendert hat und was genau
geaendert wurde.

Beispiel:

```text
Benutzer: Max Muster
Art der Aenderung: Recherche-Status geaendert
Alter Wert: unrecherchiert
Neuer Wert: gruen
```

## Tabellen im SQL-Modell

Die Datei `database/schema.sql` beschreibt vier Tabellen.

## Tabelle `bands`

Diese Tabelle speichert die Hauptdaten einer Band.

Wichtige Spalten:

- `id`
- `primaerer_name`
- `recherche_status`
- `kommentar`
- `erstellt_von`
- `letzte_aenderung_am`
- `letzte_aenderung_von`

## Tabelle `sekundaere_bandnamen`

Diese Tabelle speichert alternative Namen einer Band.

Wichtige Spalten:

- `id`
- `band_id`
- `name`

`band_id` verweist auf die zugehoerige Band in der Tabelle `bands`.

## Tabelle `quellen`

Diese Tabelle speichert Links und Quellen einer Band.

Wichtige Spalten:

- `id`
- `band_id`
- `url`

Auch hier verweist `band_id` auf die zugehoerige Band.

## Tabelle `activity_log`

Diese Tabelle speichert die nachvollziehbaren Aenderungen.

Wichtige Spalten:

- `id`
- `band_id`
- `benutzer`
- `zeitpunkt`
- `art_der_aenderung`
- `alter_wert`
- `neuer_wert`

## Beziehung der Tabellen

```text
bands
  |
  | 1 zu n
  v
sekundaere_bandnamen

bands
  |
  | 1 zu n
  v
quellen

bands
  |
  | 1 zu n
  v
activity_log
```

Eine Band kann also mehrere sekundaere Namen, mehrere Quellen und mehrere
Logeintraege besitzen.

## Aktueller Stand im Code

Im aktuellen Java-Prototyp wird noch keine echte Datenbank verwendet.

Stattdessen gibt es:

- `InMemoryBandRepository`
- `InMemoryActivityLogRepository`

Diese Klassen speichern Daten nur im Arbeitsspeicher. Das ist fuer den Anfang
einfacher zu verstehen. Das SQL-Datenmodell zeigt aber bereits, wie die Daten
spaeter dauerhaft gespeichert werden koennten.
