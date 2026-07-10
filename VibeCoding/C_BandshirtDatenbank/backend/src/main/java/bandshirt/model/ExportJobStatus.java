package bandshirt.model;

/**
 * Warum gibt es diese Klasse?
 * Ein Export-Auftrag kann verschiedene Zustaende haben. Diese Enum-Klasse
 * sammelt die erlaubten Zustaende an einer Stelle.
 *
 * Aufgabe im System:
 * Der Status zeigt, ob ein Export noch wartet, gerade verarbeitet wird oder
 * bereits erledigt ist.
 *
 * Zusammenarbeit:
 * ExportJob verwendet diesen Status. Das Backend schreibt neue Jobs mit
 * OFFEN. Der getrennte Export-Service setzt den Status spaeter auf ERLEDIGT.
 */
public enum ExportJobStatus {
    OFFEN,
    IN_ARBEIT,
    ERLEDIGT,
    FEHLER
}
