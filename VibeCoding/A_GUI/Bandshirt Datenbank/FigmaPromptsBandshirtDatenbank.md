# Aufgabe A – GUI-Design mit Figma (Banddatenbank)

## Verwendetes Tool

Für das Erstellen der Benutzeroberfläche habe ich **Figma Make** verwendet.

## Erster Prompt

```text
Erstelle das UI einer modernen webbasierten Verwaltungsanwendung für eine interne Banddatenbank eines Vereins. Orientiere dich optisch am Design von secondbandshirt.com. Verwende ein dunkles Farbschema mit roten Akzentfarben, moderne Karten, viel Weißraum und ein schlichtes, professionelles Layout.

Die Anwendung soll aus drei Ansichten bestehen:

1. Bandübersicht
2. Banddetails
3. Neue Band anlegen

Das Design soll responsiv sein und sowohl auf Desktop als auch auf Tablets gut funktionieren. Es soll bewusst einfach programmierbar sein und sich an klassischen Admin-Oberflächen orientieren.
```

## Ergebnis der ersten Version

Bereits der erste Entwurf war überraschend gut gelungen. Figma erzeugte direkt einen klickbaren Prototyp mit den drei gewünschten Ansichten. Die grundsätzliche Struktur entsprach bereits meinen Vorstellungen bzw. ging in Teilen sogar darüber hinaus.

Allerdings enthielt der Entwurf noch viele Elemente, die für meine Anwendung nicht benötigt werden oder einfach nicht passten. Deshalb habe ich den Prototyp anschließend schrittweise überarbeitet.

**Siehe Screenshot:** `v1`

---

## Zweiter Prompt

```text
Das sieht schon sehr gut aus.
Bitte passe an wie folgt.

In der Übersicht:
Filtern kann man nach grün/gelb/rot/unrecherchiert (grau).
Sortierung nach Name und Sortierung nach letzter Änderung ergänzen.
Das Aktive Bands/Pausiert/Aufgelöst kann raus.
Der Filter nach Genre kann raus.
Auf den Karten können die Details bis auf Status (grün, gelb, rot, unrecherchiert) und Name raus.

Die Detailansicht der Band bitte wie folgt überarbeiten:
- Bandinformationen, Kontakt und Diskografie raus

Ergänzen:
- Primärer Bandname
- Sekundäre Bandnamen
- Recherche-Status
- Datum der letzten Änderung
- Ersteller der Band
- Bearbeiter der letzten Änderung

Recherche:
- Kommentare
- Quelle
- Freitext für Rechercheergebnisse

Aktivitätslog:
Unterhalb der Bandinformationen befindet sich ein chronologisches Aktivitätslog.

Jeder Eintrag zeigt:
- Benutzer
- Datum und Uhrzeit
- Art der Änderung
- alter Wert
- neuer Wert

Das Aktivitätslog soll am besten als Timeline oder Tabelle dargestellt werden.

Rechts oben befinden sich Buttons zum Bearbeiten und Speichern.

Das bitte auch beim "Neue Band anlegen" berücksichtigen.
```

## Ergebnis der zweiten Version

Die zweite Version entsprach den gewünschen Inhalten viel besser.

**Siehe Screenshot:** `v2`

---

## Weitere Anpassungen

Da sich beim Durchklicken des Prototyps noch kleinere Verbesserungsmöglichkeiten ergeben haben, habe ich den Entwurf erneut angepasst/anpassen lassen.

```text
In der Übersicht:

bitte die Status so nennen

grün -> grün
gelb -> gelb
rot -> rot
grau -> unrecherchiert

In der Detailansicht bzw. dem Anlegen der Bands auch die Angabe von einem oder mehreren Links ermöglichen.

Es sollte auch möglich sein mehr als einen Kommentar abzugeben.
```

## Ergebnis der dritten Version

Die dritte Version passte nun nahezu vollständig zu meinem geplanten System.

**Siehe Screenshot:** `v3`

---

## Letzte Anpassung

Zum Schluss fehlte lediglich noch eine Exportfunktion in der Bandübersicht.

```text
Füge bitte in der Bandübersicht noch einen Button ein unter dem ich dann einmal die Daten exportieren kann (Name Band, Status).
```

## Finale Version

Die vierte Version entsprach schließlich meinen Vorstellungen und bildet die wichtigsten Funktionen der geplanten Anwendung ab.

**Siehe Screenshot:** `v4`

---

## Prototyp

Der vollständige klickbare Prototyp kann hier eingesehen werden:
https://www.figma.com/make/SVAC5EGiXtksDgbzXjs764/Bandverwaltung-UI-Design?code-node-id=0-9&p=f&t=LcHrdm9XyEx8FuGD-0&fullscreen=1

Da nicht alle Ansichten und Interaktionen auf einzelnen Screenshots sichtbar sind, empfiehlt es sich, den Prototyp direkt in Figma durchzuklicken.

---

## Persönliches Fazit

Im Vergleich zur ToDo-App war dieses GUI deutlich umfangreicher. Dadurch dauerte sowohl die erste Erstellung als auch jede Überarbeitung mehrere Minuten. Besonders bei größeren Prompts mit vielen Änderungswünschen benötigt Figma Make spürbar mehr Zeit für die Generierung.
Positiv fand ich, dass die KI bereits nach dem ersten Prompt einen sehr brauchbaren Prototyp erstellt hat. Durch mehrere kleine Korrekturen konnte der Entwurf anschließend schrittweise an die fachlichen Anforderungen meines Projekts angepasst werden. 