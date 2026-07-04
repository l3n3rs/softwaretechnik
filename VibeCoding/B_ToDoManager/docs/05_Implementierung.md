# Implementierung

Die erste Version der ToDo-App nutzt keine externen Bibliotheken.

## HTML

Die Datei `index.html` enthält:

- ein Formular zum Hinzufügen neuer Aufgaben
- Auswahlfelder für Priorität und Fälligkeitsdatum
- eine Aufgabenliste
- einen Zähler für offene Aufgaben
- Auswahlfelder für Sortierung und Prioritätsfilter
- eine Schaltfläche zum Löschen erledigter Aufgaben

## CSS

Die Datei `css/style.css` enthält:

- die Nutzung der Theme-Variablen aus `css/theme.css`
- ein zentriertes App-Layout
- Formular- und Button-Styling
- Styling für Sortierung, Filter und Prioritätsmarkierungen
- visuelle Markierung erledigter Aufgaben
- einfache Anpassungen für kleine Bildschirme

## JavaScript

Die Datei `js/script.js` enthält:

- Laden der Aufgaben aus dem LocalStorage
- Speichern der Aufgaben im LocalStorage
- Hinzufügen neuer Aufgaben mit Priorität und Fälligkeitsdatum
- Umschalten des Erledigt-Status
- Löschen einzelner Aufgaben
- Löschen aller erledigten Aufgaben
- Sortieren nach Fälligkeitsdatum
- Sortieren nach Priorität
- Filtern nach Priorität
