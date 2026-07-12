package bandshirt.repository;

import bandshirt.model.ActivityLogEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse ist ein sogenanntes Repository.
 *
 * Was ist ein Repository?
 *
 * Ein Repository ist vereinfacht gesagt die Schnittstelle zur Speicherung
 * von Daten.
 *
 * Andere Klassen müssen dadurch nicht wissen,
 * wo oder wie Daten gespeichert werden.
 *
 * In diesem Fall werden die Daten lediglich im Arbeitsspeicher gespeichert.
 *
 * Warum gibt es diese Klasse?
 *
 * Immer wenn im System etwas Wichtiges passiert,
 * zum Beispiel:
 *
 * - eine Band wird angelegt,
 * - eine Band wird bearbeitet,
 * - eine Band wird gelöscht,
 *
 * soll dies im Aktivitätsprotokoll festgehalten werden.
 *
 * Diese Klasse übernimmt genau diese Aufgabe.
 *
 * Warum "InMemory"?
 *
 * "InMemory" bedeutet:
 * Die Daten werden ausschließlich im Arbeitsspeicher gespeichert.
 *
 * Vorteile:
 *
 * - sehr einfach zu verstehen
 * - keine Datenbank notwendig
 * - keine Dateien notwendig
 * - gut für erste Prototypen
 *
 * Nachteil:
 *
 * Sobald das Programm beendet wird,
 * gehen alle gespeicherten Logeinträge verloren.
 *
 * In einer späteren echten Anwendung würde hier vermutlich
 * eine Datenbank oder eine Datei verwendet werden.
 *
 * Zusammenarbeit mit anderen Klassen:
 *
 * - BandService erstellt neue Logeinträge.
 * - Dieses Repository speichert sie.
 * - Der Controller kann sie später wieder anzeigen lassen.
 */
public class InMemoryActivityLogRepository
        implements ActivityLogRepository {

    /*
     * Diese Liste enthält alle Aktivitätsprotokolle.
     *
     * ArrayList ist eine dynamische Liste.
     *
     * Das bedeutet:
     * Es können beliebig viele Logeinträge gespeichert werden.
     *
     * Anfangs ist die Liste leer.
     */
    private final List<ActivityLogEntry> entries = new ArrayList<>();

    /**
     * Speichert einen neuen Eintrag im Aktivitätsprotokoll.
     *
     * Parameter:
     *
     * entry:
     * Der neue Logeintrag,
     * der gespeichert werden soll.
     *
     * Rückgabewert:
     *
     * Der gespeicherte Logeintrag.
     *
     * Warum wird der Eintrag zurückgegeben?
     *
     * Das ist ein häufiges Muster bei Repositorys.
     * Später könnte eine Datenbank beispielsweise
     * zusätzliche Informationen ergänzen,
     * etwa eine automatisch erzeugte ID.
     */
    @Override
    public ActivityLogEntry save(ActivityLogEntry entry) {

        /*
         * add() fügt den neuen Logeintrag
         * am Ende der Liste hinzu.
         *
         * Danach befindet sich der Eintrag dauerhaft
         * in der Repository-Liste,
         * solange das Programm läuft.
         */
        entries.add(entry);

        /*
         * Der gespeicherte Eintrag wird anschließend
         * wieder an den Aufrufer zurückgegeben.
         */
        return entry;
    }

    /**
     * Gibt alle gespeicherten Logeinträge zurück.
     *
     * Rückgabewert:
     *
     * Eine Liste mit sämtlichen Aktivitätsprotokollen.
     */
    @Override
    public List<ActivityLogEntry> findAll() {

        /*
         * Warum wird hier NICHT einfach
         *
         * return entries;
         *
         * verwendet?
         *
         * Dadurch würde der Aufrufer die ursprüngliche Liste erhalten.
         *
         * Er könnte anschließend zum Beispiel:
         *
         * - Einträge löschen,
         * - neue Einträge hinzufügen,
         * - die Reihenfolge verändern.
         *
         * Das Repository hätte dann keine Kontrolle mehr
         * über seine eigenen Daten.
         *
         * Deshalb wird eine Kopie der Liste erzeugt.
         *
         * new ArrayList<>(entries)
         *
         * erstellt eine neue Liste,
         * die dieselben Einträge enthält.
         *
         * Änderungen an dieser neuen Liste verändern
         * die ursprüngliche Repository-Liste nicht.
         *
         * Dieses Vorgehen schützt die gespeicherten Daten
         * vor unbeabsichtigten Änderungen.
         */
        return new ArrayList<>(entries);
    }
}