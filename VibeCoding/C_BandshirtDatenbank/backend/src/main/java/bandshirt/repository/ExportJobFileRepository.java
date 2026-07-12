package bandshirt.repository;

import bandshirt.model.ExportJob;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse speichert Export-Aufträge in einer Datei.
 *
 * Warum gibt es diese Klasse?
 *
 * Das Backend soll Export-Aufträge dauerhaft speichern können.
 * Gleichzeitig soll ein zweiter Prozess (der Export-Service)
 * diese Aufträge später wieder lesen können.
 *
 * Beide Prozesse laufen unabhängig voneinander.
 *
 * Deshalb benötigen sie einen gemeinsamen Speicherort.
 *
 * In diesem Prototyp übernimmt eine einfache CSV-Datei
 * diese Aufgabe.
 *
 * Die Datei befindet sich unter:
 *
 * shared/export-jobs.csv
 *
 * Das Backend schreibt neue Export-Aufträge hinein.
 * Der Export-Service liest dieselbe Datei später wieder aus.
 *
 * Dadurch entsteht eine einfache Kommunikation
 * zwischen zwei getrennten Programmen.
 *
 * In einer echten Anwendung würde hier vermutlich
 * kein Dateisystem verwendet werden,
 * sondern beispielsweise:
 *
 * - RabbitMQ
 * - Apache Kafka
 * - ActiveMQ
 * - eine Datenbank
 *
 * Für das Verständnis einer verteilten Architektur
 * reicht eine gemeinsame Datei jedoch vollkommen aus.
 *
 * Zusammenarbeit:
 *
 * Backend
 * ↓
 * ExportRequestService
 * ↓
 * ExportJobFileRepository
 * ↓
 * export-jobs.csv
 * ↓
 * Export-Service
 *
 * Das Repository kennt nur die Datei.
 * Es weiß nichts über den Export-Service.
 */
public class ExportJobFileRepository
        implements ExportJobRepository {

    /*
     * Hier wird gespeichert,
     * wo sich die gemeinsame Job-Datei befindet.
     *
     * Path ist die Java-Klasse,
     * die Dateipfade beschreibt.
     *
     * Beispiel:
     *
     * shared/export-jobs.csv
     */
    private final Path jobFile;

    /**
     * Konstruktor des Repositories.
     *
     * Beim Erzeugen wird der Pfad
     * zur gemeinsamen Job-Datei übergeben.
     *
     * Danach wird sofort geprüft,
     * ob die Datei bereits existiert.
     *
     * Falls nicht,
     * wird sie automatisch angelegt.
     */
    public ExportJobFileRepository(Path jobFile) {

        /*
         * Den übergebenen Dateipfad speichern.
         */
        this.jobFile = jobFile;

        /*
         * Sicherstellen,
         * dass die Datei bereits existiert.
         *
         * Dadurch muss später beim ersten Speichern
         * nicht mehr geprüft werden,
         * ob die Datei vorhanden ist.
         */
        erstelleDateiFallsNoetig();
    }

    /**
     * Speichert einen neuen Export-Auftrag.
     *
     * Parameter:
     *
     * job
     * Der Export-Auftrag,
     * der gespeichert werden soll.
     *
     * Rückgabewert:
     *
     * Der gespeicherte Export-Auftrag.
     */
    @Override
    public ExportJob save(ExportJob job) {

        /*
         * Die Informationen des ExportJobs
         * werden zunächst in eine einzige Textzeile umgewandelt.
         *
         * String.join(";")
         * verbindet alle Werte mit Semikolons.
         *
         * Beispiel:
         *
         * 12;OFFEN;Max;2026-07-12T15:30;;exports/bands.csv;Band A=GRUEN|Band B=ROT
         *
         * Diese Zeile wird später in die Datei geschrieben.
         */
        String line = String.join(";",
                String.valueOf(job.getId()),
                job.getStatus().name(),
                job.getAngefordertVon(),
                job.getErstelltAm().toString(),

                /*
                 * erledigtAm ist beim Erstellen
                 * eines neuen Jobs noch leer.
                 *
                 * Deshalb wird hier zunächst
                 * ein leerer Text gespeichert.
                 */
                "",

                job.getZielDatei(),

                /*
                 * bandSnapshot enthält den Zustand
                 * aller Bands zum Zeitpunkt
                 * der Export-Anforderung.
                 */
                job.getBandSnapshot());

        try {

            /*
             * Files.writeString() schreibt Text
             * in eine Datei.
             *
             * Parameter:
             *
             * jobFile
             * → Ziel-Datei
             *
             * line + System.lineSeparator()
             * → eigentliche Textzeile
             *
             * System.lineSeparator()
             * sorgt dafür,
             * dass nach jeder Zeile
             * automatisch ein Zeilenumbruch eingefügt wird.
             *
             * UTF_8
             * → Zeichencodierung.
             */

            Files.writeString(
                    jobFile,
                    line + System.lineSeparator(),
                    StandardCharsets.UTF_8,

                    /*
                     * APPEND bedeutet:
                     *
                     * Neue Daten werden
                     * an das Ende der Datei angehängt.
                     *
                     * Der bisherige Inhalt bleibt erhalten.
                     *
                     * Ohne APPEND würde die komplette Datei
                     * bei jedem Speichern überschrieben werden.
                     */
                    java.nio.file.StandardOpenOption.APPEND);

            /*
             * Nach erfolgreichem Speichern
             * wird der Job zurückgegeben.
             */
            return job;

        } catch (IOException exception) {

            /*
             * Falls das Schreiben fehlschlägt,
             * wird das Programm mit einer
             * verständlichen Fehlermeldung beendet.
             *
             * Mögliche Ursachen:
             *
             * - Datei gesperrt
             * - kein Speicherplatz
             * - keine Schreibrechte
             */
            throw new IllegalStateException(
                    "Export-Job konnte nicht gespeichert werden.",
                    exception);
        }
    }

    /**
     * Liest alle gespeicherten Export-Aufträge.
     *
     * Rückgabewert:
     *
     * Eine Liste aller Zeilen
     * aus der Job-Datei.
     */
    @Override
    public List<String> findAllRawLines() {

        try {

            /*
             * readAllLines()
             * liest die komplette Datei ein.
             *
             * Jede Zeile wird als String
             * in einer Liste gespeichert.
             */
            return Files.readAllLines(
                    jobFile,
                    StandardCharsets.UTF_8);

        } catch (IOException exception) {

            /*
             * Kann die Datei nicht gelesen werden,
             * wird einfach eine leere Liste zurückgegeben.
             *
             * Dadurch kann das Programm
             * trotzdem weiterlaufen.
             */
            return new ArrayList<>();
        }
    }

    /**
     * Erstellt die Job-Datei,
     * falls sie noch nicht existiert.
     *
     * Diese Methode wird genau einmal
     * beim Start des Repositories aufgerufen.
     */
    private void erstelleDateiFallsNoetig() {

        try {

            /*
             * getParent()
             * liefert den übergeordneten Ordner.
             *
             * Beispiel:
             *
             * shared/export-jobs.csv
             *
             * ergibt:
             *
             * shared
             */
            Path parent = jobFile.getParent();

            /*
             * Manche Dateipfade besitzen
             * gar keinen übergeordneten Ordner.
             *
             * Deshalb wird zuerst geprüft,
             * ob überhaupt einer vorhanden ist.
             */
            if (parent != null) {

                /*
                 * Falls der Ordner noch nicht existiert,
                 * wird er automatisch erstellt.
                 *
                 * Existiert er bereits,
                 * passiert nichts.
                 */
                Files.createDirectories(parent);
            }

            /*
             * Jetzt wird geprüft,
             * ob die eigentliche Datei existiert.
             */
            if (!Files.exists(jobFile)) {

                /*
                 * Falls die Datei noch nicht existiert,
                 * wird sie neu angelegt.
                 */
                Files.createFile(jobFile);
            }

        } catch (IOException exception) {

            /*
             * Fehler beim Erstellen
             * von Ordner oder Datei
             * werden hier abgefangen.
             */
            throw new IllegalStateException(
                    "Export-Job-Datei konnte nicht vorbereitet werden.",
                    exception);
        }
    }
}