package bandshirt.exportservice;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Warum gibt es diese Klasse?
 * Diese Klasse ist der zweite Prozess der Anwendung. Sie gehoert nicht zum
 * Backend/API-Prozess, sondern kann getrennt gestartet werden.
 *
 * Aufgabe im System:
 * Der Export-Service liest offene Export-Auftraege aus `shared/export-jobs.csv`,
 * erstellt daraus CSV-Dateien mit Bandname und Status und markiert die Jobs
 * danach als erledigt.
 *
 * Zusammenarbeit:
 * Das Backend schreibt Export-Auftraege in die Job-Datei. Der Export-Service
 * liest dieselbe Datei. Genau diese gemeinsame Datei ist die einfache
 * Kommunikation zwischen den beiden Prozessen.
 */
public class ExportServiceApplication {
    private static final Path JOB_FILE = Path.of("shared", "export-jobs.csv");

    public static void main(String[] args) {
        ExportServiceApplication application = new ExportServiceApplication();
        application.verarbeiteOffeneJobs();
    }

    public void verarbeiteOffeneJobs() {
        List<String> lines = liesJobDatei();
        List<String> aktualisierteLines = new ArrayList<>();

        for (String line : lines) {
            if (line.isBlank()) {
                continue;
            }

            ExportJobLine job = ExportJobLine.fromCsvLine(line);

            if (job.istOffen()) {
                erstelleExportDatei(job);
                job.markiereAlsErledigt();
            }

            aktualisierteLines.add(job.toCsvLine());
        }

        schreibeJobDatei(aktualisierteLines);
    }

    private List<String> liesJobDatei() {
        try {
            if (!Files.exists(JOB_FILE)) {
                return new ArrayList<>();
            }

            return Files.readAllLines(JOB_FILE, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new IllegalStateException("Job-Datei konnte nicht gelesen werden.", exception);
        }
    }

    private void schreibeJobDatei(List<String> lines) {
        try {
            Files.createDirectories(JOB_FILE.getParent());
            Files.write(JOB_FILE, lines, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new IllegalStateException("Job-Datei konnte nicht geschrieben werden.", exception);
        }
    }

    private void erstelleExportDatei(ExportJobLine job) {
        Path exportPath = Path.of(job.zielDatei);
        List<String> exportLines = new ArrayList<>();

        exportLines.add("Bandname;Status");

        if (!job.bandSnapshot.isBlank()) {
            String[] bands = job.bandSnapshot.split("\\|");

            for (String band : bands) {
                String[] teile = band.split("=", 2);
                String name = teile.length > 0 ? teile[0] : "";
                String status = teile.length > 1 ? teile[1] : "";

                exportLines.add(csv(name) + ";" + csv(status));
            }
        }

        try {
            Path parent = exportPath.getParent();

            if (parent != null) {
                Files.createDirectories(parent);
            }

            Files.write(exportPath, exportLines, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new IllegalStateException("Export-Datei konnte nicht geschrieben werden.", exception);
        }
    }

    private String csv(String value) {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    /**
     * Diese kleine innere Klasse stellt eine Zeile aus der Job-Datei dar.
     * Dadurch bleibt die Verarbeitung lesbarer als mit vielen Array-Zugriffen
     * wie teile[0], teile[1] und so weiter.
     */
    private static class ExportJobLine {
        private String id;
        private String status;
        private String angefordertVon;
        private String erstelltAm;
        private String erledigtAm;
        private String zielDatei;
        private String bandSnapshot;

        private static ExportJobLine fromCsvLine(String line) {
            String[] teile = line.split(";", 7);
            ExportJobLine job = new ExportJobLine();

            job.id = wertOderLeer(teile, 0);
            job.status = wertOderLeer(teile, 1);
            job.angefordertVon = wertOderLeer(teile, 2);
            job.erstelltAm = wertOderLeer(teile, 3);
            job.erledigtAm = wertOderLeer(teile, 4);
            job.zielDatei = wertOderLeer(teile, 5);
            job.bandSnapshot = wertOderLeer(teile, 6);

            return job;
        }

        private boolean istOffen() {
            return "OFFEN".equals(status);
        }

        private void markiereAlsErledigt() {
            status = "ERLEDIGT";
            erledigtAm = LocalDateTime.now().toString();
        }

        private String toCsvLine() {
            return String.join(";",
                    id,
                    status,
                    angefordertVon,
                    erstelltAm,
                    erledigtAm,
                    zielDatei,
                    bandSnapshot
            );
        }

        private static String wertOderLeer(String[] values, int index) {
            if (index >= values.length) {
                return "";
            }

            return values[index];
        }
    }
}
