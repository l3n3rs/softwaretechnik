package bandshirt.repository;

import bandshirt.model.ExportJob;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Warum gibt es diese Klasse?
 * Diese Klasse speichert Export-Auftraege in einer gemeinsamen Datei. Dadurch
 * koennen zwei getrennte Prozesse miteinander kommunizieren, ohne dass sofort
 * ein Message-Broker oder eine echte Datenbank noetig ist.
 *
 * Aufgabe im System:
 * Das Backend haengt neue Export-Jobs an die Datei `shared/export-jobs.csv`
 * an. Der Export-Service liest spaeter dieselbe Datei.
 *
 * Zusammenarbeit:
 * ExportRequestService schreibt ueber dieses Repository. Der getrennte
 * Export-Service verwendet die gleiche Datei als Eingang.
 */
public class ExportJobFileRepository implements ExportJobRepository {
    private final Path jobFile;

    public ExportJobFileRepository(Path jobFile) {
        this.jobFile = jobFile;
        erstelleDateiFallsNoetig();
    }

    @Override
    public ExportJob save(ExportJob job) {
        String line = String.join(";",
                String.valueOf(job.getId()),
                job.getStatus().name(),
                job.getAngefordertVon(),
                job.getErstelltAm().toString(),
                "",
                job.getZielDatei(),
                job.getBandSnapshot()
        );

        try {
            Files.writeString(jobFile, line + System.lineSeparator(), StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
            return job;
        } catch (IOException exception) {
            throw new IllegalStateException("Export-Job konnte nicht gespeichert werden.", exception);
        }
    }

    @Override
    public List<String> findAllRawLines() {
        try {
            return Files.readAllLines(jobFile, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            return new ArrayList<>();
        }
    }

    private void erstelleDateiFallsNoetig() {
        try {
            Path parent = jobFile.getParent();

            if (parent != null) {
                Files.createDirectories(parent);
            }

            if (!Files.exists(jobFile)) {
                Files.createFile(jobFile);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Export-Job-Datei konnte nicht vorbereitet werden.", exception);
        }
    }
}
