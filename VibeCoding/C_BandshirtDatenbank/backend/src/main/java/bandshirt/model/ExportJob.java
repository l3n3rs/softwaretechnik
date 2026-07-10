package bandshirt.model;

import java.time.LocalDateTime;

/**
 * Warum gibt es diese Klasse?
 * Ein Export soll nicht direkt im Backend erzeugt werden. Stattdessen erstellt
 * das Backend einen Auftrag, den ein zweiter Prozess spaeter abarbeitet.
 *
 * Aufgabe im System:
 * ExportJob beschreibt genau einen Export-Auftrag mit ID, Status, Benutzer,
 * Zeitpunkt, Ziel-Datei und einem einfachen Snapshot der Banddaten.
 *
 * Zusammenarbeit:
 * ExportRequestService erzeugt ExportJob-Objekte. ExportJobFileRepository
 * speichert sie in einer gemeinsamen Datei. Der Export-Service liest dieselbe
 * Datei und verarbeitet offene Jobs.
 */
public class ExportJob {
    private Long id;
    private ExportJobStatus status;
    private String angefordertVon;
    private LocalDateTime erstelltAm;
    private LocalDateTime erledigtAm;
    private String zielDatei;
    private String bandSnapshot;

    public ExportJob(Long id, String angefordertVon, String zielDatei, String bandSnapshot) {
        this.id = id;
        this.status = ExportJobStatus.OFFEN;
        this.angefordertVon = angefordertVon;
        this.erstelltAm = LocalDateTime.now();
        this.zielDatei = zielDatei;
        this.bandSnapshot = bandSnapshot;
    }

    public Long getId() {
        return id;
    }

    public ExportJobStatus getStatus() {
        return status;
    }

    public String getAngefordertVon() {
        return angefordertVon;
    }

    public LocalDateTime getErstelltAm() {
        return erstelltAm;
    }

    public LocalDateTime getErledigtAm() {
        return erledigtAm;
    }

    public String getZielDatei() {
        return zielDatei;
    }

    public String getBandSnapshot() {
        return bandSnapshot;
    }
}
