package bandshirt.model;

import java.time.LocalDateTime;

/**
 * Warum gibt es diese Klasse?
 * Aenderungen sollen spaeter nachvollziehbar sein. Ein ActivityLogEntry ist ein
 * einzelner Eintrag in diesem Aktivitaetslog.
 *
 * Aufgabe im System:
 * Die Klasse speichert, wer wann welche Aenderung gemacht hat und welcher Wert
 * vorher beziehungsweise nachher gespeichert war.
 *
 * Zusammenarbeit:
 * Der BandService erstellt Logeintraege bei wichtigen Aktionen. Das
 * ActivityLogRepository speichert diese Eintraege.
 */
public class ActivityLogEntry {
    private Long id;
    private Long bandId;
    private String benutzer;
    private LocalDateTime zeitpunkt;
    private String artDerAenderung;
    private String alterWert;
    private String neuerWert;

    public ActivityLogEntry(Long id, Long bandId, String benutzer, String artDerAenderung, String alterWert, String neuerWert) {
        this.id = id;
        this.bandId = bandId;
        this.benutzer = benutzer;
        this.artDerAenderung = artDerAenderung;
        this.alterWert = alterWert;
        this.neuerWert = neuerWert;
        this.zeitpunkt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getBandId() {
        return bandId;
    }

    public String getBenutzer() {
        return benutzer;
    }

    public LocalDateTime getZeitpunkt() {
        return zeitpunkt;
    }

    public String getArtDerAenderung() {
        return artDerAenderung;
    }

    public String getAlterWert() {
        return alterWert;
    }

    public String getNeuerWert() {
        return neuerWert;
    }
}
