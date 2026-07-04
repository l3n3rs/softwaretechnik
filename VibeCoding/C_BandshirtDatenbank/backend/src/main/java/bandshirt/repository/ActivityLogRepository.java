package bandshirt.repository;

import bandshirt.model.ActivityLogEntry;

import java.util.List;

/**
 * Warum gibt es diese Klasse?
 * Das Aktivitaetslog soll getrennt von den Bands gespeichert werden.
 *
 * Aufgabe im System:
 * Diese Schnittstelle beschreibt, wie Logeintraege gespeichert und gelesen
 * werden koennen.
 *
 * Zusammenarbeit:
 * Der BandService erzeugt ActivityLogEntry-Objekte und speichert sie ueber
 * dieses Repository.
 */
public interface ActivityLogRepository {
    ActivityLogEntry save(ActivityLogEntry entry);

    List<ActivityLogEntry> findAll();
}
