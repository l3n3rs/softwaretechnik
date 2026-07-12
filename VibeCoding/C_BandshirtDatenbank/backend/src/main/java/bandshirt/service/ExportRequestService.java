package bandshirt.service;

import bandshirt.model.Band;
import bandshirt.model.ExportJob;
import bandshirt.repository.ExportJobRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Warum gibt es diese Klasse?
 * Export-Auftraege sind eine eigene fachliche Aufgabe. Deshalb werden sie nicht
 * direkt in BandService oder BandController zusammengebaut.
 *
 * Aufgabe im System:
 * Dieser Service nimmt die aktuelle Bandliste, erstellt daraus einen einfachen
 * Snapshot und schreibt einen Export-Job in die gemeinsame Job-Datei.
 *
 * Zusammenarbeit:
 * BandController ruft ExportRequestService auf. ExportRequestService nutzt
 * BandService zum Lesen der Bands und ExportJobRepository zum Speichern des
 * Jobs. Der Export-Service verarbeitet den Job spaeter in einem eigenen
 * Prozess.
 */
public class ExportRequestService {
    private final BandService bandService;
    private final ExportJobRepository exportJobRepository;
    private final AtomicLong nextExportJobId = new AtomicLong(1);

    public ExportRequestService(BandService bandService, ExportJobRepository exportJobRepository) {
        this.bandService = bandService;
        this.exportJobRepository = exportJobRepository;
    }

    public ExportJob exportAnfordern(String benutzer) {
        pruefeBenutzer(benutzer);

        Long jobId = nextExportJobId.getAndIncrement();
        String zielDatei = "shared/exports/band-export-" + jobId + ".csv";
        String bandSnapshot = erstelleBandSnapshot(bandService.alleBandsAnzeigen());
        ExportJob job = new ExportJob(jobId, benutzer, zielDatei, bandSnapshot);

        return exportJobRepository.save(job);
    }

    public List<String> exportJobsAnzeigen() {
        return exportJobRepository.findAllRawLines();
    }

    private String erstelleBandSnapshot(List<Band> bands) {
        return bands.stream()
                .map(band -> band.getPrimaererName() + "=" + band.getRechercheStatus().getAnzeigeName())
                .collect(Collectors.joining("|"));
    }

    private void pruefeBenutzer(String benutzer) {
        if (benutzer == null || benutzer.isBlank()) {
            throw new IllegalArgumentException("Der Benutzer für den Export darf nicht leer sein.");
        }
    }
}
