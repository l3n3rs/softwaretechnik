package bandshirt.backend;

import bandshirt.controller.BandController;
import bandshirt.model.ExportJob;
import bandshirt.repository.ActivityLogRepository;
import bandshirt.repository.BandRepository;
import bandshirt.repository.ExportJobRepository;
import bandshirt.repository.memory.InMemoryActivityLogRepository;
import bandshirt.repository.memory.InMemoryBandRepository;
import bandshirt.repository.file.ExportJobFileRepository;
import bandshirt.service.BandService;
import bandshirt.service.ExportRequestService;

import java.nio.file.Path;

/**
 * Diese Klasse stellt den Startpunkt des Backend-Prozesses dar.
 *
 * Warum gibt es diese Klasse?
 *
 * In Java besitzt jedes eigenständig ausführbare Programm
 * genau einen Einstiegspunkt.
 *
 * Dieser Einstiegspunkt ist immer eine main()-Methode.
 *
 * Diese Anwendung stellt das Backend der Bandshirt-Datenbank dar.
 *
 * Sie übernimmt unter anderem folgende Aufgaben:
 *
 * - Bands verwalten
 * - Bands bearbeiten
 * - Export-Aufträge erzeugen
 *
 * Wichtig:
 *
 * Diese Klasse erstellt zunächst alle benötigten Objekte
 * und verbindet sie miteinander.
 *
 * Erst danach kann die eigentliche Anwendung arbeiten.
 *
 * In einer späteren echten Spring-Boot-Anwendung würde vieles davon
 * automatisch durch das Framework erledigt werden.
 *
 * Hier erfolgt dieser Aufbau bewusst von Hand,
 * damit nachvollziehbar ist,
 * welche Klassen miteinander zusammenarbeiten.
 */
public class BackendApiApplication {

    /**
     * main() ist der Startpunkt des gesamten Backend-Prozesses.
     *
     * Sobald dieses Programm gestartet wird,
     * beginnt Java genau hier.
     */
    public static void main(String[] args) {

        /*
         * -------------------------------------------------------------
         * Repositorys erstellen
         * -------------------------------------------------------------
         *
         * Repositorys sind für die Speicherung der Daten zuständig.
         *
         * Man kann sich ein Repository vereinfacht wie eine Schnittstelle
         * zur Datenhaltung vorstellen.
         */

        /*
         * Dieses Repository speichert alle Bands.
         *
         * InMemory bedeutet:
         * Die Daten werden nur im Arbeitsspeicher gespeichert.
         *
         * Sobald das Programm beendet wird,
         * gehen diese Daten wieder verloren.
         *
         * Für einen einfachen Prototyp reicht das aus.
         */
        BandRepository bandRepository = new InMemoryBandRepository();

        /*
         * Dieses Repository speichert alle Einträge
         * des Aktivitätsprotokolls.
         *
         * Auch dieses Repository arbeitet nur im Arbeitsspeicher.
         */
        ActivityLogRepository activityLogRepository = new InMemoryActivityLogRepository();

        /*
         * Dieses Repository speichert Export-Aufträge.
         *
         * Anders als die beiden vorherigen Repositorys
         * werden diese Daten in einer Datei gespeichert.
         *
         * Dadurch kann der getrennte Export-Service
         * später dieselbe Datei lesen.
         *
         * Genau diese gemeinsame Datei bildet die Kommunikation
         * zwischen den beiden Prozessen.
         */
        ExportJobRepository exportJobRepository = new ExportJobFileRepository(
                Path.of("shared", "export-jobs.csv"));

        /*
         * -------------------------------------------------------------
         * Service-Klassen erstellen
         * -------------------------------------------------------------
         *
         * Services enthalten die eigentliche Fachlogik.
         *
         * Sie entscheiden zum Beispiel:
         *
         * - wie Bands angelegt werden,
         * - wie gesucht wird,
         * - welche Änderungen protokolliert werden,
         * - wie Export-Aufträge erzeugt werden.
         */

        /*
         * Der BandService benötigt Zugriff auf:
         *
         * - das BandRepository
         * - das ActivityLogRepository
         *
         * Deshalb werden beide Objekte beim Erzeugen übergeben.
         */
        BandService bandService = new BandService(
                bandRepository,
                activityLogRepository);

        /*
         * Dieser Service erzeugt Export-Aufträge.
         *
         * Dafür benötigt er:
         *
         * - den BandService
         * (um die aktuellen Banddaten zu kennen)
         *
         * - das ExportJobRepository
         * (um den Auftrag speichern zu können)
         */
        ExportRequestService exportRequestService = new ExportRequestService(
                bandService,
                exportJobRepository);

        /*
         * -------------------------------------------------------------
         * Controller erstellen
         * -------------------------------------------------------------
         *
         * Der Controller bildet die Eingangsstelle des Backends.
         *
         * Später würde das Frontend
         * den Controller über REST-Endpunkte ansprechen.
         *
         * In diesem kleinen Beispiel
         * wird der Controller direkt aus main() verwendet.
         */
        BandController bandController = new BandController(
                bandService,
                exportRequestService);

        /*
         * -------------------------------------------------------------
         * Beispiel-Daten anlegen
         * -------------------------------------------------------------
         *
         * Damit der Export später überhaupt etwas exportieren kann,
         * werden zunächst zwei Beispiel-Bands angelegt.
         *
         * Der zweite Parameter gibt an,
         * wer diese Bands angelegt hat.
         */
        bandController.createBand(
                "Beispielband",
                "Backend-Prozess");

        bandController.createBand(
                "Zweite Beispielband",
                "Backend-Prozess");

        /*
         * -------------------------------------------------------------
         * Export anfordern
         * -------------------------------------------------------------
         *
         * Jetzt wird KEINE CSV-Datei erstellt.
         *
         * Stattdessen passiert Folgendes:
         *
         * Der Controller erstellt lediglich einen Export-Auftrag.
         *
         * Dieser Auftrag wird in der gemeinsamen Datei
         *
         * shared/export-jobs.csv
         *
         * gespeichert.
         *
         * Der eigentliche Export erfolgt später
         * durch den zweiten Prozess (Export-Service).
         *
         * Genau dadurch entsteht die verteilte Architektur.
         */
        ExportJob job = bandController.requestCsvExport(
                "Backend-Prozess");

        /*
         * Ausgabe einiger Informationen auf der Konsole.
         *
         * System.out.println() schreibt Text
         * in das Konsolenfenster.
         */

        /*
         * Ausgabe der eindeutigen ID
         * des neu erzeugten Export-Auftrags.
         */
        System.out.println(
                "Backend/API-Prozess hat Export-Job "
                        + job.getId()
                        + " erstellt.");

        /*
         * Hinweis für den Benutzer,
         * dass jetzt der zweite Prozess gestartet werden kann.
         *
         * Erst dieser zweite Prozess liest den Auftrag,
         * erstellt die CSV-Datei
         * und markiert den Auftrag anschließend als erledigt.
         */
        System.out.println(
                "Der Export-Service kann jetzt getrennt gestartet werden.");
    }
}