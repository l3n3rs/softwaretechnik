package bandshirt.service;

import bandshirt.model.ActivityLogEntry;
import bandshirt.model.Band;
import bandshirt.model.BandSortierung;
import bandshirt.model.ResearchStatus;
import bandshirt.repository.ActivityLogRepository;
import bandshirt.repository.BandRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Warum gibt es diese Klasse?
 * Der Service enthaelt die fachliche Logik. Dadurch muss der Controller nicht
 * selbst wissen, wie eine Band angelegt, bearbeitet oder geloescht wird.
 *
 * Aufgabe im System:
 * BandService prueft einfache Regeln, veraendert Band-Objekte und schreibt
 * passende Logeintraege.
 *
 * Zusammenarbeit:
 * Der BandController ruft den BandService auf. Der Service nutzt
 * BandRepository zum Speichern der Bands und ActivityLogRepository fuer das
 * Aktivitaetslog.
 */
public class BandService {
    private final BandRepository bandRepository;
    private final ActivityLogRepository activityLogRepository;
    private final AtomicLong nextBandId = new AtomicLong(1);
    private final AtomicLong nextLogId = new AtomicLong(1);

    public BandService(BandRepository bandRepository, ActivityLogRepository activityLogRepository) {
        this.bandRepository = bandRepository;
        this.activityLogRepository = activityLogRepository;
    }

    public Band bandAnlegen(String primaererName, String erstelltVon) {
        pruefePflichtfeld(primaererName, "primaererName");
        pruefePflichtfeld(erstelltVon, "erstelltVon");

        Band band = new Band(nextBandId.getAndIncrement(), primaererName, erstelltVon);
        bandRepository.save(band);
        log(band.getId(), erstelltVon, "Band angelegt", "-", primaererName);
        return band;
    }

    public List<Band> alleBandsAnzeigen() {
        return bandRepository.findAll();
    }

    /**
     * Diese Methode sucht Bands ueber den primaeren Namen und ueber sekundaere
     * Namen. Sie ist bewusst im Service, weil Suche eine fachliche Funktion ist
     * und spaeter sowohl vom Controller als auch von Tests genutzt werden kann.
     */
    public List<Band> bandsSuchen(String suchtext) {
        if (suchtext == null || suchtext.isBlank()) {
            return alleBandsAnzeigen();
        }

        String normalisierterSuchtext = suchtext.toLowerCase();

        return bandRepository.findAll().stream()
                .filter(band -> enthaeltPrimaerenNamen(band, normalisierterSuchtext)
                        || enthaeltSekundaerenNamen(band, normalisierterSuchtext))
                .collect(Collectors.toList());
    }

    /**
     * Diese Methode filtert die Bandliste nach Recherche-Status. Wenn kein
     * Status uebergeben wird, gibt sie alle Bands zurueck.
     */
    public List<Band> bandsNachStatusFiltern(ResearchStatus status) {
        if (status == null) {
            return alleBandsAnzeigen();
        }

        return bandRepository.findAll().stream()
                .filter(band -> band.getRechercheStatus() == status)
                .collect(Collectors.toList());
    }

    /**
     * Diese Methode sortiert eine beliebige Bandliste. Dadurch kann man erst
     * suchen oder filtern und danach das Ergebnis sortieren.
     */
    public List<Band> bandsSortieren(List<Band> bands, BandSortierung sortierung) {
        if (sortierung == null) {
            return bands;
        }

        Comparator<Band> comparator;

        switch (sortierung) {
            case LETZTE_AENDERUNG:
                comparator = Comparator.comparing(Band::getLetzteAenderungAm).reversed();
                break;
            case STATUS:
                comparator = Comparator.comparing(band -> band.getRechercheStatus().getAnzeigeName());
                break;
            case BANDNAME:
            default:
                comparator = Comparator.comparing(Band::getPrimaererName, String.CASE_INSENSITIVE_ORDER);
                break;
        }

        return bands.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    /**
     * Diese Methode kombiniert Suche, Filter und Sortierung. Sie passt gut zu
     * einem spaeteren REST-Endpunkt wie GET /bands?suche=...&status=...&sort=...
     */
    public List<Band> bandsFinden(String suchtext, ResearchStatus status, BandSortierung sortierung) {
        List<Band> gefundeneBands = bandsSuchen(suchtext);

        if (status != null) {
            gefundeneBands = gefundeneBands.stream()
                    .filter(band -> band.getRechercheStatus() == status)
                    .collect(Collectors.toList());
        }

        return bandsSortieren(gefundeneBands, sortierung);
    }

    public Band bandAnzeigen(Long id) {
        return bandRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Keine Band mit ID " + id + " gefunden."));
    }

    public Band bandBearbeiten(
            Long id,
            String neuerName,
            List<String> neueSekundaereNamen,
            ResearchStatus neuerStatus,
            List<String> neueQuellen,
            String neuerKommentar,
            String bearbeiter
    ) {
        pruefePflichtfeld(bearbeiter, "bearbeiter");

        Band band = bandAnzeigen(id);

        /*
         * Jede if-Abfrage steht fuer ein bearbeitbares Feld.
         * Wenn ein Wert uebergeben wurde, wird zuerst der alte Wert gemerkt,
         * dann der neue Wert gesetzt und anschliessend ein Logeintrag erzeugt.
         */
        if (neuerName != null && !neuerName.isBlank()) {
            String alterName = band.getPrimaererName();
            band.setPrimaererName(neuerName);
            log(id, bearbeiter, "Primaerer Bandname geaendert", alterName, neuerName);
        }

        if (neueSekundaereNamen != null) {
            String alterWert = String.join(", ", band.getSekundaereNamen());
            String neuerWert = String.join(", ", neueSekundaereNamen);
            band.setSekundaereNamen(new ArrayList<>(neueSekundaereNamen));
            log(id, bearbeiter, "Sekundaere Bandnamen geaendert", alterWert, neuerWert);
        }

        if (neuerStatus != null && neuerStatus != band.getRechercheStatus()) {
            String alterStatus = band.getRechercheStatus().getAnzeigeName();
            band.setRechercheStatus(neuerStatus);
            log(id, bearbeiter, "Recherche-Status geaendert", alterStatus, neuerStatus.getAnzeigeName());
        }

        if (neueQuellen != null) {
            String alterWert = String.join(", ", band.getQuellen());
            String neuerWert = String.join(", ", neueQuellen);
            band.setQuellen(new ArrayList<>(neueQuellen));
            log(id, bearbeiter, "Quellen geaendert", alterWert, neuerWert);
        }

        if (neuerKommentar != null) {
            String alterKommentar = band.getKommentar();
            band.setKommentar(neuerKommentar);
            log(id, bearbeiter, "Kommentar geaendert", alterKommentar, neuerKommentar);
        }

        band.markiereGeaendertVon(bearbeiter);
        return bandRepository.save(band);
    }

    public void bandLoeschen(Long id, String benutzer) {
        pruefePflichtfeld(benutzer, "benutzer");
        Band band = bandAnzeigen(id);
        bandRepository.deleteById(id);
        log(id, benutzer, "Band geloescht", band.getPrimaererName(), "-");
    }

    public List<ActivityLogEntry> aktivitaetslogAnzeigen() {
        return activityLogRepository.findAll();
    }

    private void log(Long bandId, String benutzer, String artDerAenderung, String alterWert, String neuerWert) {
        ActivityLogEntry entry = new ActivityLogEntry(
                nextLogId.getAndIncrement(),
                bandId,
                benutzer,
                artDerAenderung,
                alterWert,
                neuerWert
        );
        activityLogRepository.save(entry);
    }

    private void pruefePflichtfeld(String wert, String feldname) {
        if (wert == null || wert.isBlank()) {
            throw new IllegalArgumentException("Das Feld " + feldname + " darf nicht leer sein.");
        }
    }

    private boolean enthaeltPrimaerenNamen(Band band, String normalisierterSuchtext) {
        return band.getPrimaererName().toLowerCase().contains(normalisierterSuchtext);
    }

    private boolean enthaeltSekundaerenNamen(Band band, String normalisierterSuchtext) {
        return band.getSekundaereNamen().stream()
                .anyMatch(name -> name.toLowerCase().contains(normalisierterSuchtext));
    }
}
