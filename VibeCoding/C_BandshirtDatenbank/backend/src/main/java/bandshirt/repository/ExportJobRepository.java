package bandshirt.repository;

import bandshirt.model.ExportJob;

import java.util.List;

/**
 * Warum gibt es diese Klasse?
 * Das Backend soll Export-Auftraege speichern koennen, ohne zu wissen, ob diese
 * in einer Datei, Datenbank oder Job-Queue liegen.
 *
 * Aufgabe im System:
 * Dieses Interface beschreibt die benoetigten Aktionen fuer Export-Jobs:
 * speichern und alle Jobs anzeigen.
 *
 * Zusammenarbeit:
 * ExportRequestService verwendet dieses Interface. ExportJobFileRepository ist
 * die einfache Datei-Umsetzung fuer den Prototyp.
 */
public interface ExportJobRepository {
    ExportJob save(ExportJob job);

    List<String> findAllRawLines();
}
