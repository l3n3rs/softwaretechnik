package bandshirt.exportservice;

/*
 * Diese importierten Klassen stammen aus der Java-Standardbibliothek.
 *
 * Sie werden benötigt, damit das Programm:
 *
 * - Dateien lesen und schreiben kann,
 * - mit Dateipfaden arbeiten kann,
 * - Texte in UTF-8 speichert,
 * - den aktuellen Zeitpunkt ermittelt,
 * - und Listen von Textzeilen verwalten kann.
 */

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse stellt den Export-Service dar.
 *
 * Warum gibt es diese Klasse?
 *
 * Die Anwendung soll verteilt aufgebaut sein. Das bedeutet:
 * Nicht ein einziges Programm erledigt alle Aufgaben selbst.
 *
 * Stattdessen gibt es mindestens zwei getrennte Prozesse:
 *
 * 1. Das Backend
 * - verwaltet die Banddaten,
 * - nimmt Anfragen entgegen,
 * - und erstellt Export-Aufträge.
 *
 * 2. Dieser Export-Service
 * - liest die Export-Aufträge,
 * - verarbeitet sie,
 * - erstellt die CSV-Dateien,
 * - und markiert die Aufträge anschließend als erledigt.
 *
 * Der Export-Service kann unabhängig vom Backend gestartet werden.
 * Er ist damit ein eigener Prozess mit einer klar abgegrenzten Aufgabe.
 *
 * Wie kommunizieren Backend und Export-Service miteinander?
 *
 * Beide verwenden dieselbe Datei:
 *
 * shared/export-jobs.csv
 *
 * Das Backend schreibt dort neue Export-Aufträge hinein.
 * Der Export-Service liest diese Aufträge später aus der Datei.
 *
 * Diese gemeinsame Datei übernimmt in diesem einfachen Beispiel
 * die Rolle einer Nachrichtenwarteschlange beziehungsweise einer Job-Liste.
 *
 * In einer größeren echten Anwendung könnte dafür zum Beispiel
 * ein Nachrichtensystem oder eine Datenbank verwendet werden.
 */
public class ExportServiceApplication {

    /*
     * JOB_FILE enthält den Pfad zur gemeinsamen Job-Datei.
     *
     * Path.of("shared", "export-jobs.csv") bedeutet:
     *
     * - gehe in den Ordner "shared"
     * - und verwende dort die Datei "export-jobs.csv"
     *
     * Das Ergebnis entspricht ungefähr:
     *
     * shared/export-jobs.csv
     *
     * Da der Pfad als Konstante gespeichert wird,
     * muss er nicht an mehreren Stellen erneut geschrieben werden.
     */
    private static final Path JOB_FILE = Path.of("shared", "export-jobs.csv");

    /**
     * main() ist der Startpunkt des Programms.
     *
     * Wenn der Export-Service gestartet wird,
     * beginnt Java mit dieser Methode.
     *
     * args enthält mögliche Startparameter.
     * In diesem Programm werden diese Parameter nicht verwendet.
     */
    public static void main(String[] args) {

        /*
         * Hier wird ein neues Objekt der Klasse
         * ExportServiceApplication erzeugt.
         *
         * Dieses Objekt brauchen wir,
         * um die nicht-statische Methode verarbeiteOffeneJobs()
         * aufrufen zu können.
         */
        ExportServiceApplication application = new ExportServiceApplication();

        /*
         * Jetzt startet die eigentliche Arbeit des Export-Services.
         *
         * Die Methode sucht nach offenen Export-Aufträgen
         * und verarbeitet sie.
         */
        application.verarbeiteOffeneJobs();
    }

    /**
     * Diese Methode verarbeitet alle Aufträge aus der Job-Datei.
     *
     * Der Ablauf ist:
     *
     * 1. Job-Datei einlesen
     * 2. jede Zeile einzeln prüfen
     * 3. offene Jobs erkennen
     * 4. Export-Datei erstellen
     * 5. Job als erledigt markieren
     * 6. aktualisierte Job-Datei zurückschreiben
     */
    public void verarbeiteOffeneJobs() {

        /*
         * Die komplette Job-Datei wird eingelesen.
         *
         * Jede Zeile der Datei wird als einzelner String
         * in der Liste lines gespeichert.
         *
         * Beispiel:
         *
         * lines.get(0) enthält die erste Zeile,
         * lines.get(1) enthält die zweite Zeile usw.
         */
        List<String> lines = liesJobDatei();

        /*
         * In dieser neuen Liste werden später
         * die verarbeiteten und aktualisierten Zeilen gesammelt.
         *
         * Warum wird nicht direkt die ursprüngliche Liste verändert?
         *
         * So bleibt der Ablauf übersichtlicher:
         * Wir lesen alte Zeilen ein und bauen daraus
         * eine neue aktualisierte Liste auf.
         */
        List<String> aktualisierteLines = new ArrayList<>();

        /*
         * Diese Schleife geht jede Zeile der Job-Datei einzeln durch.
         *
         * Bei jedem Durchlauf enthält die Variable line
         * genau eine Zeile aus der Datei.
         */
        for (String line : lines) {

            /*
             * isBlank() prüft, ob eine Zeile leer ist
             * oder nur aus Leerzeichen besteht.
             *
             * Solche Zeilen enthalten keinen gültigen Auftrag
             * und werden deshalb übersprungen.
             */
            if (line.isBlank()) {
                continue;
            }

            /*
             * Die Textzeile wird in ein Java-Objekt umgewandelt.
             *
             * Vorher liegt der Auftrag nur als Text vor.
             * Danach können wir bequem auf Eigenschaften zugreifen,
             * zum Beispiel:
             *
             * job.status
             * job.zielDatei
             * job.bandSnapshot
             *
             * Diese Umwandlung übernimmt fromCsvLine().
             */
            ExportJobLine job = ExportJobLine.fromCsvLine(line);

            /*
             * Nur offene Jobs sollen verarbeitet werden.
             *
             * Ein Job mit Status "ERLEDIGT"
             * darf nicht noch einmal exportiert werden.
             */
            if (job.istOffen()) {

                /*
                 * Für den offenen Job wird eine CSV-Datei erzeugt.
                 *
                 * Diese Datei enthält später:
                 *
                 * - Bandname
                 * - Status
                 */
                erstelleExportDatei(job);

                /*
                 * Wenn die Export-Datei erfolgreich erstellt wurde,
                 * wird der Job als erledigt markiert.
                 *
                 * Dabei wird:
                 *
                 * - der Status auf "ERLEDIGT" gesetzt
                 * - der Zeitpunkt der Erledigung gespeichert
                 */
                job.markiereAlsErledigt();
            }

            /*
             * Das Job-Objekt wird wieder in eine CSV-Zeile umgewandelt.
             *
             * Diese aktualisierte Zeile wird in die neue Liste aufgenommen.
             *
             * Auch bereits erledigte Jobs bleiben dadurch
             * weiterhin in der Job-Datei enthalten.
             */
            aktualisierteLines.add(job.toCsvLine());
        }

        /*
         * Zum Schluss wird die gesamte Job-Datei neu geschrieben.
         *
         * Dabei werden die alten Zeilen durch die aktualisierten Zeilen ersetzt.
         */
        schreibeJobDatei(aktualisierteLines);
    }

    /**
     * Liest die gemeinsame Job-Datei vollständig ein.
     *
     * Rückgabewert:
     * Eine Liste von Textzeilen.
     *
     * Jede Zeile entspricht einem Export-Auftrag.
     */
    private List<String> liesJobDatei() {

        try {
            /*
             * Zuerst wird geprüft, ob die Datei überhaupt existiert.
             *
             * Beim ersten Start kann es sein,
             * dass das Backend noch keinen Auftrag erstellt hat.
             */
            if (!Files.exists(JOB_FILE)) {

                /*
                 * Falls die Datei nicht existiert,
                 * wird einfach eine leere Liste zurückgegeben.
                 *
                 * Das Programm bricht dadurch nicht ab.
                 * Es gibt dann nur keine Jobs zu verarbeiten.
                 */
                return new ArrayList<>();
            }

            /*
             * Files.readAllLines() liest die komplette Datei ein.
             *
             * Jede Zeile wird als String in einer Liste gespeichert.
             *
             * StandardCharsets.UTF_8 sorgt dafür,
             * dass auch deutsche Sonderzeichen wie ä, ö, ü und ß
             * korrekt verarbeitet werden.
             */
            return Files.readAllLines(
                    JOB_FILE,
                    StandardCharsets.UTF_8);

        } catch (IOException exception) {

            /*
             * Beim Lesen einer Datei kann ein Fehler auftreten.
             *
             * Beispiele:
             *
             * - die Datei ist gesperrt,
             * - die Zugriffsrechte fehlen,
             * - der Datenträger ist nicht erreichbar.
             *
             * IOException ist die allgemeine Java-Ausnahme
             * für Fehler beim Lesen oder Schreiben von Dateien.
             *
             * Hier wird daraus eine IllegalStateException gemacht.
             * Damit wird deutlich:
             * Das Programm befindet sich in einem Zustand,
             * in dem es seine Aufgabe nicht fortsetzen kann.
             */
            throw new IllegalStateException(
                    "Job-Datei konnte nicht gelesen werden.",
                    exception);
        }
    }

    /**
     * Schreibt alle aktualisierten Jobs zurück in die Job-Datei.
     *
     * Der Parameter lines enthält die fertigen CSV-Zeilen,
     * die gespeichert werden sollen.
     */
    private void schreibeJobDatei(List<String> lines) {

        try {
            /*
             * JOB_FILE.getParent() liefert den übergeordneten Ordner.
             *
             * Bei:
             *
             * shared/export-jobs.csv
             *
             * ist der übergeordnete Ordner:
             *
             * shared
             */
            Files.createDirectories(JOB_FILE.getParent());

            /*
             * createDirectories() erstellt den Ordner,
             * falls er noch nicht existiert.
             *
             * Existiert der Ordner bereits,
             * passiert nichts und es entsteht kein Fehler.
             */

            /*
             * Files.write() schreibt alle Zeilen in die Datei.
             *
             * Falls die Datei bereits existiert,
             * wird ihr bisheriger Inhalt ersetzt.
             *
             * Falls sie noch nicht existiert,
             * wird sie neu angelegt.
             */
            Files.write(
                    JOB_FILE,
                    lines,
                    StandardCharsets.UTF_8);

        } catch (IOException exception) {

            /*
             * Auch beim Schreiben können Fehler auftreten.
             *
             * Beispiele:
             *
             * - der Ordner ist schreibgeschützt,
             * - die Datei ist gerade geöffnet oder gesperrt,
             * - der Speicherplatz reicht nicht aus.
             */
            throw new IllegalStateException(
                    "Job-Datei konnte nicht geschrieben werden.",
                    exception);
        }
    }

    /**
     * Erstellt die eigentliche Export-Datei für einen Auftrag.
     *
     * Der Job enthält:
     *
     * - den Zielpfad der Export-Datei
     * - einen gespeicherten Schnappschuss der Banddaten
     *
     * Die erzeugte Datei enthält zwei Spalten:
     *
     * Bandname;Status
     */
    private void erstelleExportDatei(ExportJobLine job) {

        /*
         * job.zielDatei enthält den Pfad,
         * unter dem die neue Export-Datei gespeichert werden soll.
         *
         * Path.of() wandelt diesen Text in ein Path-Objekt um.
         */
        Path exportPath = Path.of(job.zielDatei);

        /*
         * In dieser Liste werden alle Zeilen
         * der späteren Export-Datei gesammelt.
         */
        List<String> exportLines = new ArrayList<>();

        /*
         * Die erste Zeile ist die Überschrift der CSV-Datei.
         *
         * Das Semikolon trennt die beiden Spalten:
         *
         * Spalte 1: Bandname
         * Spalte 2: Status
         */
        exportLines.add("Bandname;Status");

        /*
         * Es wird geprüft, ob der Job überhaupt Banddaten enthält.
         *
         * isBlank() ist true,
         * wenn der Text leer ist oder nur Leerzeichen enthält.
         */
        if (!job.bandSnapshot.isBlank()) {

            /*
             * Im bandSnapshot sind mehrere Bands
             * in einer einzigen Textzeile gespeichert.
             *
             * Die Bands sind durch das Zeichen | getrennt.
             *
             * Beispiel:
             *
             * Band A=GRUEN|Band B=ROT|Band C=GELB
             *
             * split("\\|") teilt diesen Text am senkrechten Strich.
             *
             * Das Ergebnis ist ein Array:
             *
             * bands[0] = "Band A=GRUEN"
             * bands[1] = "Band B=ROT"
             * bands[2] = "Band C=GELB"
             *
             * Das Zeichen | hat in regulären Ausdrücken
             * eine besondere Bedeutung.
             * Deshalb muss es mit \\ maskiert werden.
             */
            String[] bands = job.bandSnapshot.split("\\|");

            /*
             * Jetzt wird jede einzelne Band verarbeitet.
             */
            for (String band : bands) {

                /*
                 * Innerhalb eines Band-Eintrags
                 * sind Name und Status durch = getrennt.
                 *
                 * Beispiel:
                 *
                 * Band A=GRUEN
                 *
                 * split("=", 2) teilt höchstens einmal.
                 *
                 * Das ist sinnvoll, falls der Bandname selbst
                 * theoretisch noch ein Gleichheitszeichen enthalten sollte.
                 */
                String[] teile = band.split("=", 2);

                /*
                 * Wenn mindestens ein Teil vorhanden ist,
                 * wird dieser als Bandname verwendet.
                 *
                 * Falls etwas mit den Daten nicht stimmt,
                 * wird stattdessen ein leerer Text verwendet.
                 */
                String name = teile.length > 0 ? teile[0] : "";

                /*
                 * Wenn ein zweiter Teil vorhanden ist,
                 * wird dieser als Status verwendet.
                 *
                 * Andernfalls bleibt der Status leer.
                 */
                String status = teile.length > 1 ? teile[1] : "";

                /*
                 * Die beiden Werte werden als neue CSV-Zeile gespeichert.
                 *
                 * csv(name) und csv(status) sorgen dafür,
                 * dass die Werte korrekt in Anführungszeichen gesetzt werden.
                 *
                 * Beispiel:
                 *
                 * "Band A";"GRUEN"
                 */
                exportLines.add(
                        csv(name) + ";" + csv(status));
            }
        }

        try {
            /*
             * getParent() liefert den Ordner,
             * in dem die Export-Datei gespeichert werden soll.
             *
             * Beispiel:
             *
             * exports/bands.csv
             *
             * ergibt als Parent:
             *
             * exports
             */
            Path parent = exportPath.getParent();

            /*
             * Wenn tatsächlich ein übergeordneter Ordner vorhanden ist,
             * wird sichergestellt, dass dieser Ordner existiert.
             *
             * Bei einem Dateinamen ohne Ordner,
             * zum Beispiel "bands.csv",
             * wäre parent gleich null.
             */
            if (parent != null) {
                Files.createDirectories(parent);
            }

            /*
             * Jetzt wird die fertige Export-Datei geschrieben.
             *
             * Alle gesammelten Zeilen werden in UTF-8 gespeichert.
             */
            Files.write(
                    exportPath,
                    exportLines,
                    StandardCharsets.UTF_8);

        } catch (IOException exception) {

            /*
             * Falls die Export-Datei nicht geschrieben werden kann,
             * wird das Programm mit einer verständlichen Fehlermeldung beendet.
             */
            throw new IllegalStateException(
                    "Export-Datei konnte nicht geschrieben werden.",
                    exception);
        }
    }

    /**
     * Bereitet einen einzelnen Textwert für die CSV-Datei vor.
     *
     * Warum ist das nötig?
     *
     * CSV-Dateien können Sonderzeichen enthalten.
     * Besonders Anführungszeichen müssen korrekt behandelt werden.
     *
     * Beispiel:
     *
     * Eingabe:
     * Band "Beispiel"
     *
     * Ausgabe:
     * "Band ""Beispiel"""
     *
     * In CSV werden innere Anführungszeichen verdoppelt.
     */
    private String csv(String value) {

        /*
         * value.replace("\"", "\"\"")
         *
         * ersetzt jedes einzelne Anführungszeichen
         * durch zwei Anführungszeichen.
         *
         * Danach wird der gesamte Wert
         * zusätzlich in Anführungszeichen eingeschlossen.
         */
        return "\"" +
                value.replace("\"", "\"\"") +
                "\"";
    }

    /**
     * Diese innere Klasse stellt genau eine Zeile
     * aus der Job-Datei als Java-Objekt dar.
     *
     * Warum gibt es diese Klasse?
     *
     * Ohne diese Klasse müsste im restlichen Code ständig
     * mit unübersichtlichen Array-Zugriffen gearbeitet werden:
     *
     * teile[0]
     * teile[1]
     * teile[2]
     *
     * Mit der Klasse können stattdessen verständliche Namen verwendet werden:
     *
     * job.id
     * job.status
     * job.zielDatei
     *
     * Die Klasse ist private,
     * weil sie nur innerhalb des Export-Services benötigt wird.
     *
     * Sie ist static,
     * weil sie kein Objekt der äußeren Klasse benötigt,
     * um verwendet zu werden.
     */
    private static class ExportJobLine {

        /*
         * Eindeutige Kennung des Export-Auftrags.
         */
        private String id;

        /*
         * Bearbeitungsstatus des Jobs.
         *
         * Mögliche Werte sind zum Beispiel:
         *
         * OFFEN
         * ERLEDIGT
         */
        private String status;

        /*
         * Name oder Kennung der Person,
         * die den Export angefordert hat.
         */
        private String angefordertVon;

        /*
         * Zeitpunkt, zu dem der Job erstellt wurde.
         */
        private String erstelltAm;

        /*
         * Zeitpunkt, zu dem der Job abgeschlossen wurde.
         *
         * Bei einem offenen Job kann dieser Wert leer sein.
         */
        private String erledigtAm;

        /*
         * Dateipfad, unter dem die Export-Datei gespeichert werden soll.
         */
        private String zielDatei;

        /*
         * Gespeicherte Banddaten zum Zeitpunkt der Export-Anfrage.
         *
         * Beispiel:
         *
         * Band A=GRUEN|Band B=ROT
         *
         * Dieser Schnappschuss wird verwendet,
         * damit der Export-Service nicht direkt
         * auf die eigentliche Banddatenbank zugreifen muss.
         */
        private String bandSnapshot;

        /**
         * Wandelt eine Textzeile aus der Job-Datei
         * in ein ExportJobLine-Objekt um.
         *
         * Beispiel für eine Zeile:
         *
         * 123;OFFEN;Lena;2026-07-10T10:00:00;;exports/bands.csv;Band A=GRUEN|Band B=ROT
         */
        private static ExportJobLine fromCsvLine(String line) {

            /*
             * Die Zeile wird am Semikolon getrennt.
             *
             * Die Zahl 7 bedeutet:
             * Es sollen höchstens sieben Teile entstehen.
             *
             * Das ist wichtig,
             * weil bandSnapshot der letzte Teil sein soll.
             */
            String[] teile = line.split(";", 7);

            /*
             * Ein leeres Job-Objekt wird erstellt.
             */
            ExportJobLine job = new ExportJobLine();

            /*
             * Die einzelnen Werte der CSV-Zeile
             * werden den passenden Feldern zugeordnet.
             *
             * wertOderLeer() verhindert Fehler,
             * falls eine Zeile weniger Spalten enthält als erwartet.
             */
            job.id = wertOderLeer(teile, 0);
            job.status = wertOderLeer(teile, 1);
            job.angefordertVon = wertOderLeer(teile, 2);
            job.erstelltAm = wertOderLeer(teile, 3);
            job.erledigtAm = wertOderLeer(teile, 4);
            job.zielDatei = wertOderLeer(teile, 5);
            job.bandSnapshot = wertOderLeer(teile, 6);

            /*
             * Das vollständig gefüllte Job-Objekt
             * wird an die aufrufende Methode zurückgegeben.
             */
            return job;
        }

        /**
         * Prüft, ob der Auftrag noch offen ist.
         *
         * Der Vergleich wird bewusst so geschrieben:
         *
         * "OFFEN".equals(status)
         *
         * Das ist sicherer als:
         *
         * status.equals("OFFEN")
         *
         * Denn falls status null wäre,
         * würde die zweite Variante einen Fehler auslösen.
         */
        private boolean istOffen() {
            return "OFFEN".equals(status);
        }

        /**
         * Markiert den Job als erfolgreich abgeschlossen.
         */
        private void markiereAlsErledigt() {

            /*
             * Der Status wird geändert.
             */
            status = "ERLEDIGT";

            /*
             * Zusätzlich wird der aktuelle Zeitpunkt gespeichert.
             *
             * LocalDateTime.now() liefert Datum und Uhrzeit.
             *
             * toString() wandelt den Wert in Text um.
             */
            erledigtAm = LocalDateTime.now().toString();
        }

        /**
         * Wandelt das Job-Objekt wieder in eine CSV-Zeile um.
         *
         * Diese Methode wird benötigt,
         * damit die aktualisierten Jobs wieder
         * in die gemeinsame Datei geschrieben werden können.
         */
        private String toCsvLine() {

            /*
             * String.join() verbindet alle angegebenen Texte
             * mit einem Semikolon.
             *
             * Beispiel:
             *
             * "123", "ERLEDIGT", "Lena"
             *
             * wird zu:
             *
             * 123;ERLEDIGT;Lena
             */
            return String.join(
                    ";",
                    id,
                    status,
                    angefordertVon,
                    erstelltAm,
                    erledigtAm,
                    zielDatei,
                    bandSnapshot);
        }

        /**
         * Gibt einen Wert aus einem Array zurück.
         *
         * Falls der gewünschte Index nicht vorhanden ist,
         * wird ein leerer Text zurückgegeben.
         *
         * Dadurch wird verhindert,
         * dass eine ArrayIndexOutOfBoundsException entsteht.
         */
        private static String wertOderLeer(
                String[] values,
                int index) {

            /*
             * Wenn der Index größer oder gleich
             * der Anzahl der vorhandenen Werte ist,
             * existiert dieser Eintrag nicht.
             */
            if (index >= values.length) {
                return "";
            }

            /*
             * Andernfalls wird der vorhandene Wert zurückgegeben.
             */
            return values[index];
        }
    }
}