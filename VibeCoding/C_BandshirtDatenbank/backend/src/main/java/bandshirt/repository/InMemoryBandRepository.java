package bandshirt.repository;

import bandshirt.model.Band;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Warum gibt es diese Klasse?
 * Fuer den ersten einfachen Code soll noch keine echte Datenbank angebunden
 * werden. Diese Klasse speichert Bands deshalb nur im Arbeitsspeicher.
 *
 * Aufgabe im System:
 * Sie ist eine einfache Test- und Lernversion eines Repositorys. Beim Neustart
 * waeren die Daten wieder weg, aber die Architektur ist schon sichtbar.
 *
 * Zusammenarbeit:
 * InMemoryBandRepository implementiert BandRepository und wird vom BandService
 * benutzt. Spaeter kann sie durch ein Datenbank-Repository ersetzt werden.
 */
public class InMemoryBandRepository implements BandRepository {
    private final Map<Long, Band> bands = new LinkedHashMap<>();

    @Override
    public Band save(Band band) {
        bands.put(band.getId(), band);
        return band;
    }

    @Override
    public Optional<Band> findById(Long id) {
        return Optional.ofNullable(bands.get(id));
    }

    @Override
    public List<Band> findAll() {
        return new ArrayList<>(bands.values());
    }

    @Override
    public void deleteById(Long id) {
        bands.remove(id);
    }
}
