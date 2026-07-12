package bandshirt.repository;

import bandshirt.model.ExportJob;

import java.util.List;

/**
 * Dieses Interface beschreibt die grundlegenden Funktionen,
 * die jedes Repository für Export-Aufträge bereitstellen muss.
 *
 * Was ist ein Interface?
 *
 * Ein Interface kann man sich wie einen Vertrag vorstellen.
 *
 * Es legt fest:
 *
 * "Jede Klasse, die dieses Interface verwendet,
 * muss diese Methoden bereitstellen."
 *
 * Das Interface beschreibt also:
 *
 * - welche Funktionen vorhanden sein müssen,
 * - aber noch nicht, wie diese Funktionen umgesetzt werden.
 *
 * Warum gibt es dieses Interface?
 *
 * Der ExportRequestService soll Export-Aufträge speichern können.
 *
 * Dem Service soll dabei aber egal sein,
 * wo diese Aufträge tatsächlich gespeichert werden.
 *
 * Beispielsweise könnten Export-Aufträge später gespeichert werden in:
 *
 * - einer Datei,
 * - einer Datenbank,
 * - einer Message Queue,
 * - oder einem Cloud-Dienst.
 *
 * Der Service arbeitet deshalb immer nur mit diesem Interface.
 *
 * Dadurch bleibt der Service unabhängig von der eigentlichen Speicherung.
 *
 * Dieses Prinzip nennt man lose Kopplung.
 *
 * Vorteil:
 *
 * Später kann die Speicherart geändert werden,
 * ohne dass der ExportRequestService angepasst werden muss.
 *
 * Zusammenarbeit mit anderen Klassen:
 *
 * - ExportRequestService verwendet dieses Interface.
 * - ExportJobFileRepository ist eine konkrete Umsetzung,
 * die Export-Aufträge in einer Datei speichert.
 *
 * Später könnte beispielsweise auch eine Klasse
 * ExportJobDatabaseRepository entstehen,
 * ohne dass der ExportRequestService geändert werden müsste.
 */
public interface ExportJobRepository {

    /**
     * Speichert einen Export-Auftrag.
     *
     * Parameter:
     *
     * job:
     * Der Export-Auftrag, der gespeichert werden soll.
     *
     * Rückgabewert:
     *
     * Der gespeicherte Export-Auftrag.
     *
     * Die konkrete Speicherung übernimmt später
     * die jeweilige Implementierung des Interfaces.
     */
    ExportJob save(ExportJob job);

    /**
     * Liefert alle gespeicherten Export-Aufträge zurück.
     *
     * Rückgabewert:
     *
     * Eine Liste aller gespeicherten Zeilen
     * aus der Job-Datei.
     *
     * Warum wird hier List<String> verwendet?
     *
     * Die konkrete Implementierung speichert die Export-Aufträge
     * in einer CSV-Datei.
     *
     * Deshalb werden die einzelnen Zeilen zunächst
     * als einfache Texte zurückgegeben.
     *
     * Der Export-Service kann diese Zeilen anschließend
     * wieder in ExportJob-Objekte umwandeln.
     */
    List<String> findAllRawLines();
}