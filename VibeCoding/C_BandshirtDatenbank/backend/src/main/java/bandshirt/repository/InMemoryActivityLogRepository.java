package bandshirt.repository;

import bandshirt.model.ActivityLogEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Warum gibt es diese Klasse?
 * Wie bei den Bands wird auch das Aktivitaetslog zuerst nur im Arbeitsspeicher
 * gespeichert, damit der Code leicht verstaendlich bleibt.
 *
 * Aufgabe im System:
 * Die Klasse sammelt Logeintraege in einer Liste.
 *
 * Zusammenarbeit:
 * Der BandService ruft save auf, wenn eine wichtige Aktion passiert.
 */
public class InMemoryActivityLogRepository implements ActivityLogRepository {
    private final List<ActivityLogEntry> entries = new ArrayList<>();

    @Override
    public ActivityLogEntry save(ActivityLogEntry entry) {
        entries.add(entry);
        return entry;
    }

    @Override
    public List<ActivityLogEntry> findAll() {
        return new ArrayList<>(entries);
    }
}
