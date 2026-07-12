package bandshirt.model;

/**
 * Diese Klasse ist ein sogenanntes Enum.
 *
 * Was ist ein Enum?
 *
 * Ein Enum (Enumeration) beschreibt eine feste Menge erlaubter Werte.
 *
 * Das bedeutet:
 * Es können nur genau die Werte verwendet werden,
 * die hier definiert wurden.
 *
 * Warum ist das sinnvoll?
 *
 * Der Recherche-Status einer Band kann nur einen der folgenden Werte besitzen:
 *
 * - GRUEN
 * - GELB
 * - ROT
 * - UNRECHERCHIERT
 *
 * Andere Werte sind nicht erlaubt.
 *
 * Würde stattdessen überall mit normalen Texten (Strings) gearbeitet,
 * könnten leicht Tippfehler entstehen.
 *
 * Beispiel:
 *
 * "Gruen"
 * "grün"
 * "green"
 * "GRÜN"
 * "grun"
 *
 * Für den Computer wären das alles unterschiedliche Werte.
 *
 * Ein Enum verhindert genau solche Fehler bereits beim Programmieren.
 *
 * Zusammenarbeit mit anderen Klassen:
 *
 * - Band speichert den Recherche-Status als ResearchStatus.
 * - BandService kann den Status ändern.
 * - BandController nimmt den Status später vom Frontend entgegen.
 *
 * Dadurch verwenden alle Klassen dieselben vier erlaubten Werte.
 */
public enum ResearchStatus {

    /*
     * Die vier erlaubten Recherche-Status.
     *
     * Links steht jeweils der technische Wert.
     * Dieser wird im Java-Code verwendet.
     *
     * In den Klammern steht der Anzeigetext,
     * der später der Benutzeroberfläche angezeigt werden kann.
     */
    GRUEN("grün"),
    GELB("gelb"),
    ROT("rot"),
    UNRECHERCHIERT("unrecherchiert");

    /*
     * Hier wird der sichtbare deutsche Name gespeichert.
     *
     * Beispiel:
     *
     * Technischer Wert:
     * GRUEN
     *
     * Angezeigter Text:
     * grün
     *
     * Dadurch können im Code gut lesbare,
     * einheitliche Bezeichner verwendet werden,
     * während dem Benutzer trotzdem schöne deutsche Texte angezeigt werden.
     */
    private final String anzeigeName;

    /**
     * Konstruktor des Enums.
     *
     * Beim Start des Programms wird jeder Enum-Wert einmal erzeugt.
     *
     * Beispiel:
     *
     * GRUEN("grün")
     *
     * Dabei wird "grün" in anzeigeName gespeichert.
     *
     * Dieser Konstruktor wird nicht selbst aufgerufen,
     * sondern automatisch beim Erzeugen der Enum-Werte.
     */
    ResearchStatus(String anzeigeName) {
        this.anzeigeName = anzeigeName;
    }

    /**
     * Gibt den sichtbaren Namen des Status zurück.
     *
     * Beispiel:
     *
     * ResearchStatus.GRUEN.getAnzeigeName()
     *
     * liefert:
     *
     * "grün"
     *
     * Diese Methode wird später beispielsweise
     * für die Anzeige im Frontend benötigt.
     */
    public String getAnzeigeName() {
        return anzeigeName;
    }
}