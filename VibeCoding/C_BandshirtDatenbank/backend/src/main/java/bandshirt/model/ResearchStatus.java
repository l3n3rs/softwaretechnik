package bandshirt.model;

/**
 * Warum gibt es diese Klasse?
 * Diese Enum-Klasse sammelt alle erlaubten Recherche-Status an einer Stelle.
 *
 * Aufgabe im System:
 * Eine Band darf nur einen dieser vier Status haben. Dadurch werden Tippfehler
 * wie "Gruen", "green" oder "gurn" im Backend vermieden.
 *
 * Zusammenarbeit:
 * Die Klasse Band verwendet ResearchStatus als Feld. Der BandService kann den
 * Status setzen und der BandController kann ihn spaeter ueber eine API annehmen.
 */
public enum ResearchStatus {
    GRUEN("gruen"),
    GELB("gelb"),
    ROT("rot"),
    UNRECHERCHIERT("unrecherchiert");

    private final String anzeigeName;

    ResearchStatus(String anzeigeName) {
        this.anzeigeName = anzeigeName;
    }

    public String getAnzeigeName() {
        return anzeigeName;
    }
}
