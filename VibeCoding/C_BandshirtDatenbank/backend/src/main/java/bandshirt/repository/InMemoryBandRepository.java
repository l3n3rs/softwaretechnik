package bandshirt.repository;

import bandshirt.model.Band;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Diese Klasse ist eine einfache Implementierung des BandRepository.
 *
 * Warum gibt es diese Klasse?
 *
 * Damit eine Anwendung Daten speichern kann, benötigt sie einen Speicherort.
 * In einer echten Anwendung wäre dies häufig eine Datenbank.
 *
 * Für diesen Prototyp soll die Anwendung jedoch möglichst einfach bleiben.
 * Deshalb werden die Bands zunächst nur im Arbeitsspeicher gespeichert.
 *
 * Diese Klasse übernimmt genau diese Aufgabe.
 *
 * Warum heißt die Klasse "InMemoryBandRepository"?
 *
 * "InMemory" bedeutet:
 * Die Daten werden ausschließlich im Arbeitsspeicher (RAM) des Computers
 * gespeichert.
 *
 * Vorteile:
 *
 * - sehr einfach umzusetzen
 * - keine Datenbank notwendig
 * - gut zum Lernen und Testen
 * - schnell
 *
 * Nachteil:
 *
 * Sobald das Programm beendet wird,
 * gehen alle gespeicherten Bands verloren.
 *
 * Deshalb eignet sich diese Lösung nur für einen Prototypen
 * oder zum Testen.
 *
 * Was ist ein Repository?
 *
 * Ein Repository bildet die Schnittstelle zwischen der Fachlogik
 * (BandService) und der Datenspeicherung.
 *
 * Der BandService muss dadurch nicht wissen,
 * wie die Daten gespeichert werden.
 *
 * Er arbeitet nur mit dem Interface BandRepository.
 *
 * Dadurch könnte diese Klasse später problemlos
 * durch eine Datenbank ersetzt werden.
 *
 * Zusammenarbeit mit anderen Klassen:
 *
 * - BandService speichert und liest Bands über dieses Repository.
 * - Diese Klasse implementiert das Interface BandRepository.
 * - Später könnte beispielsweise eine Klasse
 * DatabaseBandRepository dieselben Methoden bereitstellen.
 */
public class InMemoryBandRepository implements BandRepository {

    /*
     * Hier werden alle Bands gespeichert.
     *
     * Es wird eine LinkedHashMap verwendet.
     *
     * Was ist eine Map?
     *
     * Eine Map speichert Daten immer als Schlüssel-Wert-Paare.
     *
     * In diesem Fall:
     *
     * Schlüssel (Key):
     * Die eindeutige ID der Band
     *
     * Wert (Value):
     * Das komplette Band-Objekt
     *
     * Beispiel:
     *
     * 1 -> Metallica
     * 2 -> Iron Maiden
     * 3 -> Blind Guardian
     *
     * Warum keine normale Liste?
     *
     * In einer Liste müsste jede Band nacheinander gesucht werden.
     *
     * Mit einer Map kann direkt über die ID auf eine Band zugegriffen werden.
     *
     * Warum LinkedHashMap?
     *
     * LinkedHashMap merkt sich zusätzlich
     * die Reihenfolge, in der die Bands eingefügt wurden.
     *
     * Werden später alle Bands angezeigt,
     * erscheinen sie in derselben Reihenfolge,
     * in der sie gespeichert wurden.
     */
    private final Map<Long, Band> bands = new LinkedHashMap<>();

    /**
     * Speichert eine Band.
     *
     * Parameter:
     *
     * band
     * Die Band, die gespeichert werden soll.
     *
     * Rückgabewert:
     *
     * Die gespeicherte Band.
     */
    @Override
    public Band save(Band band) {

        /*
         * put() speichert einen Eintrag in der Map.
         *
         * Der Schlüssel ist die ID der Band.
         *
         * Der Wert ist das komplette Band-Objekt.
         *
         * Existiert unter dieser ID noch keine Band,
         * wird ein neuer Eintrag angelegt.
         *
         * Existiert bereits eine Band mit derselben ID,
         * wird diese automatisch überschrieben.
         *
         * Dadurch kann dieselbe Methode sowohl
         * zum Anlegen als auch zum Aktualisieren verwendet werden.
         */
        bands.put(band.getId(), band);

        /*
         * Anschließend wird die gespeicherte Band zurückgegeben.
         *
         * Das ist ein typisches Verhalten vieler Repositorys.
         */
        return band;
    }

    /**
     * Sucht eine Band anhand ihrer ID.
     *
     * Parameter:
     *
     * id
     * Die eindeutige ID der gesuchten Band.
     *
     * Rückgabewert:
     *
     * Optional<Band>
     *
     * Warum Optional?
     *
     * Es kann sein,
     * dass es zu dieser ID überhaupt keine Band gibt.
     *
     * Statt null zurückzugeben,
     * verwendet Java häufig Optional.
     *
     * Dadurch wird deutlich:
     *
     * "Es kann sein, dass kein Ergebnis vorhanden ist."
     */
    @Override
    public Optional<Band> findById(Long id) {

        /*
         * bands.get(id)
         *
         * sucht nach der Band mit dieser ID.
         *
         * Gibt es keine passende Band,
         * liefert get() null zurück.
         *
         * Optional.ofNullable()
         * verpackt dieses Ergebnis in ein Optional.
         *
         * Dadurch kann der aufrufende Code später prüfen:
         *
         * if (band.isPresent())
         *
         * statt direkt mit null arbeiten zu müssen.
         *
         * Das macht den Code sicherer.
         */
        return Optional.ofNullable(bands.get(id));
    }

    /**
     * Liefert alle gespeicherten Bands zurück.
     *
     * Rückgabewert:
     *
     * Eine Liste aller gespeicherten Bands.
     */
    @Override
    public List<Band> findAll() {

        /*
         * bands.values()
         *
         * liefert alle gespeicherten Band-Objekte
         * der Map zurück.
         *
         * Das Ergebnis ist jedoch keine normale ArrayList.
         *
         * Deshalb wird daraus eine neue ArrayList erzeugt.
         *
         * Warum?
         *
         * Würden wir direkt die interne Sammlung zurückgeben,
         * könnten andere Klassen diese verändern.
         *
         * Zum Beispiel:
         *
         * - Bands löschen
         * - neue Bands hinzufügen
         * - Reihenfolge verändern
         *
         * Dadurch hätte das Repository
         * keine Kontrolle mehr über seine eigenen Daten.
         *
         * Deshalb wird eine Kopie erzeugt.
         *
         * Änderungen an dieser neuen Liste
         * verändern die ursprüngliche Map nicht.
         *
         * Dieses Vorgehen nennt man Kapselung.
         */
        return new ArrayList<>(bands.values());
    }

    /**
     * Löscht eine Band anhand ihrer ID.
     *
     * Parameter:
     *
     * id
     * Die ID der Band,
     * die gelöscht werden soll.
     *
     * Rückgabewert:
     *
     * void bedeutet,
     * dass die Methode keinen Wert zurückgibt.
     */
    @Override
    public void deleteById(Long id) {

        /*
         * remove()
         * entfernt den Eintrag mit dieser ID aus der Map.
         *
         * Existiert keine Band mit dieser ID,
         * passiert einfach nichts.
         *
         * Es entsteht dadurch kein Fehler.
         */
        bands.remove(id);
    }
}