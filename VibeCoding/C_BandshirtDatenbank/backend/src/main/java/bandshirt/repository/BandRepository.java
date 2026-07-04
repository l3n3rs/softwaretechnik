package bandshirt.repository;

import bandshirt.model.Band;

import java.util.List;
import java.util.Optional;

/**
 * Warum gibt es diese Klasse?
 * Das Repository trennt den restlichen Java-Code von der konkreten Speicherung.
 *
 * Aufgabe im System:
 * Diese Schnittstelle beschreibt, welche Speicher-Aktionen fuer Bands gebraucht
 * werden: speichern, suchen, auflisten und loeschen.
 *
 * Zusammenarbeit:
 * Der BandService verwendet BandRepository. Die konkrete Klasse
 * InMemoryBandRepository setzt diese Schnittstelle fuer den ersten Prototyp um.
 */
public interface BandRepository {
    Band save(Band band);

    Optional<Band> findById(Long id);

    List<Band> findAll();

    void deleteById(Long id);
}
