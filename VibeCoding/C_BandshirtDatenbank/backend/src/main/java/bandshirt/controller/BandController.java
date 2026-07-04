package bandshirt.controller;

import bandshirt.model.ActivityLogEntry;
import bandshirt.model.Band;
import bandshirt.model.BandSortierung;
import bandshirt.model.ResearchStatus;
import bandshirt.service.BandService;

import java.util.List;

/**
 * Warum gibt es diese Klasse?
 * Ein Controller ist die Eingangstuer zum Backend. In einer echten Webanwendung
 * wuerde er HTTP-Anfragen vom Frontend entgegennehmen.
 *
 * Aufgabe im System:
 * Diese erste Version leitet Methodenaufrufe an den BandService weiter. Damit
 * sieht man schon die spaetere REST-Struktur, ohne sofort ein Framework zu
 * brauchen.
 *
 * Zusammenarbeit:
 * Das Frontend wuerde spaeter den Controller ueber REST-Endpunkte ansprechen.
 * Der Controller selbst arbeitet mit dem BandService zusammen.
 */
public class BandController {
    private final BandService bandService;

    public BandController(BandService bandService) {
        this.bandService = bandService;
    }

    public Band createBand(String primaererName, String erstelltVon) {
        return bandService.bandAnlegen(primaererName, erstelltVon);
    }

    public List<Band> getBands() {
        return bandService.alleBandsAnzeigen();
    }

    public List<Band> findBands(String suchtext, ResearchStatus status, BandSortierung sortierung) {
        return bandService.bandsFinden(suchtext, status, sortierung);
    }

    public Band getBand(Long id) {
        return bandService.bandAnzeigen(id);
    }

    public Band updateBand(
            Long id,
            String neuerName,
            List<String> neueSekundaereNamen,
            ResearchStatus neuerStatus,
            List<String> neueQuellen,
            String neuerKommentar,
            String bearbeiter
    ) {
        return bandService.bandBearbeiten(
                id,
                neuerName,
                neueSekundaereNamen,
                neuerStatus,
                neueQuellen,
                neuerKommentar,
                bearbeiter
        );
    }

    public void deleteBand(Long id, String benutzer) {
        bandService.bandLoeschen(id, benutzer);
    }

    public List<ActivityLogEntry> getActivityLog() {
        return bandService.aktivitaetslogAnzeigen();
    }
}
