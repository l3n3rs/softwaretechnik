package bandshirt;

import bandshirt.controller.BandController;
import bandshirt.model.ExportJob;
import bandshirt.repository.ActivityLogRepository;
import bandshirt.repository.BandRepository;
import bandshirt.repository.ExportJobFileRepository;
import bandshirt.repository.ExportJobRepository;
import bandshirt.repository.InMemoryActivityLogRepository;
import bandshirt.repository.InMemoryBandRepository;
import bandshirt.service.BandService;
import bandshirt.service.ExportRequestService;

import java.nio.file.Path;

/**
 * Warum gibt es diese Klasse?
 * Diese Klasse stellt den ersten Prozess der verteilten Anwendung dar: das
 * Backend/API-Programm.
 *
 * Aufgabe im System:
 * In einer echten Anwendung wuerde hier ein Webserver gestartet werden. Fuer
 * den bewusst einfachen Prototyp baut die Klasse die Backend-Objekte zusammen,
 * legt Beispieldaten an und schreibt einen Export-Auftrag in die Job-Datei.
 *
 * Zusammenarbeit:
 * BackendApiApplication verwendet Controller, Services und Repositories. Der
 * erzeugte Export-Auftrag wird in `shared/export-jobs.csv` gespeichert. Der
 * getrennte Export-Service kann diesen Auftrag spaeter lesen und verarbeiten.
 */
public class BackendApiApplication {
    public static void main(String[] args) {
        BandRepository bandRepository = new InMemoryBandRepository();
        ActivityLogRepository activityLogRepository = new InMemoryActivityLogRepository();
        ExportJobRepository exportJobRepository = new ExportJobFileRepository(Path.of("shared", "export-jobs.csv"));

        BandService bandService = new BandService(bandRepository, activityLogRepository);
        ExportRequestService exportRequestService = new ExportRequestService(bandService, exportJobRepository);
        BandController bandController = new BandController(bandService, exportRequestService);

        bandController.createBand("Beispielband", "Backend-Prozess");
        bandController.createBand("Zweite Beispielband", "Backend-Prozess");

        ExportJob job = bandController.requestCsvExport("Backend-Prozess");

        System.out.println("Backend/API-Prozess hat Export-Job " + job.getId() + " erstellt.");
        System.out.println("Der Export-Service kann jetzt getrennt gestartet werden.");
    }
}
