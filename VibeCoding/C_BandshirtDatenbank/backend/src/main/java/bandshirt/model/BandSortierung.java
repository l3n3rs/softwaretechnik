package bandshirt.model;

/**
 * Warum gibt es diese Klasse?
 * Diese Enum-Klasse sammelt die erlaubten Sortierungen für die Bandliste.
 *
 * Aufgabe im System:
 * Statt freie Texte wie "name", "datum" oder "status" im Service zu verteilen,
 * gibt es hier feste Werte. Das macht den Code leichter lesbar und verhindert
 * Tippfehler.
 *
 * Zusammenarbeit:
 * Der BandController kann eine BandSortierung entgegennehmen und an den
 * BandService weitergeben. Der BandService entscheidet damit, wie die Liste
 * sortiert wird.
 */
public enum BandSortierung {
    BANDNAME,
    LETZTE_AENDERUNG,
    STATUS
}
