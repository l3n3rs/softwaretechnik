package bandshirt.repository;

import bandshirt.model.ActivityLogEntry;

import java.util.List;

/**
 * Dieses Interface beschreibt die grundlegenden Funktionen,
 * die jedes Repository für das Aktivitätsprotokoll bereitstellen muss.
 *
 * Was ist ein Interface?
 *
 * Ein Interface kann man sich wie einen Vertrag vorstellen.
 *
 * Es legt fest,
 * welche Funktionen vorhanden sein müssen,
 * beschreibt aber noch nicht,
 * wie diese Funktionen umgesetzt werden.
 *
 * Warum gibt es dieses Interface?
 *
 * Der BandService soll Logeinträge speichern und wieder lesen können.
 *
 * Dem BandService soll dabei aber egal sein,
 * wie die Speicherung technisch erfolgt.
 *
 * Denkbar wären zum Beispiel:
 *
 * - Speicherung im Arbeitsspeicher
 * - Speicherung in einer Datei
 * - Speicherung in einer Datenbank
 * - Speicherung in einem Cloud-Dienst
 *
 * Der BandService arbeitet deshalb nur mit diesem Interface.
 * Er muss nicht wissen, welche konkrete Klasse die Speicherung übernimmt.
 *
 * Dadurch bleibt der Code flexibel.
 *
 * Wird die Art der Speicherung später geändert,
 * muss der BandService nicht angepasst werden.
 *
 * Zusammenarbeit mit anderen Klassen:
 *
 * - BandService erstellt neue ActivityLogEntry-Objekte.
 * - Diese werden über das ActivityLogRepository gespeichert.
 * - InMemoryActivityLogRepository ist die einfache Umsetzung,
 * die alle Logeinträge im Arbeitsspeicher speichert.
 *
 * Später könnte beispielsweise auch eine Klasse
 * DatabaseActivityLogRepository oder
 * FileActivityLogRepository entstehen,
 * ohne dass der BandService geändert werden müsste.
 */
public interface ActivityLogRepository {

    /**
     * Speichert einen neuen Eintrag im Aktivitätsprotokoll.
     *
     * Parameter:
     *
     * entry
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
     * Eine spätere Datenbank könnte beim Speichern
     * beispielsweise automatisch eine ID vergeben.
     * Durch den Rückgabewert erhält der Aufrufer
     * immer den endgültigen gespeicherten Datensatz.
     */
    ActivityLogEntry save(ActivityLogEntry entry);

    /**
     * Liefert alle gespeicherten Logeinträge zurück.
     *
     * Rückgabewert:
     *
     * Eine Liste aller bisher gespeicherten ActivityLogEntry-Objekte.
     *
     * Der BandService kann diese Liste beispielsweise verwenden,
     * um das Aktivitätsprotokoll im Frontend anzuzeigen.
     */
    List<ActivityLogEntry> findAll();
}