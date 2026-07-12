package bandshirt.controller;

/*
 * Die folgenden import-Anweisungen machen andere Klassen
 * in dieser Datei verfügbar.
 *
 * Ohne diese Imports könnte der Controller zum Beispiel
 * die Klasse Band oder den BandService nicht verwenden.
 */

import bandshirt.model.ActivityLogEntry;
import bandshirt.model.Band;
import bandshirt.model.BandSortierung;
import bandshirt.model.ExportJob;
import bandshirt.model.ResearchStatus;
import bandshirt.service.BandService;
import bandshirt.service.ExportRequestService;

import java.util.List;

/**
 * Diese Klasse ist der Controller für die Bandverwaltung.
 *
 * Was ist ein Controller?
 *
 * Ein Controller ist vereinfacht gesagt die Eingangstür zum Backend.
 *
 * In einer späteren echten Webanwendung würde das Frontend
 * Anfragen an das Backend senden, zum Beispiel:
 *
 * - neue Band anlegen
 * - alle Bands laden
 * - nach Bands suchen
 * - eine Band bearbeiten
 * - eine Band löschen
 * - einen Export anfordern
 *
 * Der Controller nimmt solche Anfragen entgegen
 * und leitet sie an die passende Service-Klasse weiter.
 *
 * Wichtig:
 *
 * Der Controller soll möglichst wenig eigene Fachlogik enthalten.
 * Er entscheidet also nicht selbst:
 *
 * - ob eine Band angelegt werden darf,
 * - wie eine Band gesucht wird,
 * - wie ein Export erstellt wird,
 * - oder welche Regeln beim Bearbeiten gelten.
 *
 * Diese Aufgaben gehören in die Service-Klassen.
 *
 * Der Controller verbindet daher hauptsächlich:
 *
 * Frontend
 * ↓
 * Controller
 * ↓
 * Service
 *
 * In dieser vereinfachten Version gibt es noch keine echten HTTP-Endpunkte.
 * Die Methoden zeigen aber bereits,
 * wie eine spätere REST-Schnittstelle aufgebaut sein könnte.
 *
 * Zusammenarbeit mit anderen Klassen:
 *
 * - BandService:
 * kümmert sich um Bands, Suche, Bearbeitung, Löschen
 * und das Aktivitätsprotokoll.
 *
 * - ExportRequestService:
 * kümmert sich um Export-Aufträge.
 *
 * - Band:
 * stellt eine Band mit ihren Daten dar.
 *
 * - ResearchStatus:
 * beschreibt den Recherche-Status einer Band.
 *
 * - BandSortierung:
 * legt fest, wie eine Bandliste sortiert werden soll.
 *
 * - ActivityLogEntry:
 * stellt einen Eintrag im Aktivitätsprotokoll dar.
 *
 * - ExportJob:
 * beschreibt einen angeforderten Export-Auftrag.
 */
public class BandController {

    /*
     * Der BandService enthält die fachliche Logik
     * für alle normalen Bandfunktionen.
     *
     * final bedeutet:
     * Nachdem der Wert im Konstruktor gesetzt wurde,
     * kann später kein anderer BandService zugewiesen werden.
     *
     * Das macht die Abhängigkeit stabiler und nachvollziehbarer.
     */
    private final BandService bandService;

    /*
     * Dieser Service ist ausschließlich für Export-Aufträge zuständig.
     *
     * Die Trennung ist sinnvoll,
     * weil Bandverwaltung und Export unterschiedliche Aufgaben sind.
     */
    private final ExportRequestService exportRequestService;

    /**
     * Konstruktor des Controllers.
     *
     * Ein Konstruktor wird aufgerufen,
     * wenn ein neues Objekt dieser Klasse erzeugt wird.
     *
     * Beispiel:
     *
     * new BandController(bandService, exportRequestService)
     *
     * Die beiden benötigten Service-Objekte werden von außen übergeben.
     *
     * Das nennt man Abhängigkeitsübergabe.
     * Häufig wird dafür auch der Begriff Dependency Injection verwendet.
     *
     * Vereinfacht bedeutet das:
     *
     * Der Controller erzeugt seine Services nicht selbst,
     * sondern bekommt sie bereits fertig zur Verfügung gestellt.
     *
     * Vorteil:
     *
     * - Die Klassen bleiben klar voneinander getrennt.
     * - Die Services können leichter ausgetauscht werden.
     * - Der Controller lässt sich einfacher testen.
     */
    public BandController(
            BandService bandService,
            ExportRequestService exportRequestService) {
        /*
         * Der übergebene BandService wird
         * im Feld dieser Klasse gespeichert.
         *
         * this.bandService bedeutet:
         * das Feld bandService dieses Controller-Objekts.
         */
        this.bandService = bandService;

        /*
         * Dasselbe passiert mit dem ExportRequestService.
         */
        this.exportRequestService = exportRequestService;
    }

    /**
     * Legt eine neue Band an.
     *
     * Diese Methode könnte später einem REST-Endpunkt entsprechen,
     * zum Beispiel:
     *
     * POST /bands
     *
     * Parameter:
     *
     * primaererName:
     * Der Hauptname der neuen Band.
     *
     * erstelltVon:
     * Der Name oder die Kennung der Person,
     * die die Band angelegt hat.
     *
     * Rückgabewert:
     *
     * Die neu angelegte Band wird zurückgegeben.
     */
    public Band createBand(
            String primaererName,
            String erstelltVon) {
        /*
         * Der Controller legt die Band nicht selbst an.
         *
         * Stattdessen gibt er die Aufgabe
         * an den BandService weiter.
         *
         * Der BandService kann dabei zum Beispiel prüfen:
         *
         * - ob der Name leer ist,
         * - ob die Band bereits existiert,
         * - welche Standardwerte gesetzt werden,
         * - ob ein Aktivitätslog geschrieben werden muss.
         */
        return bandService.bandAnlegen(
                primaererName,
                erstelltVon);
    }

    /**
     * Gibt alle gespeicherten Bands zurück.
     *
     * Diese Methode könnte später einem REST-Endpunkt entsprechen:
     *
     * GET /bands
     *
     * Rückgabewert:
     *
     * Eine Liste mit allen Bands.
     */
    public List<Band> getBands() {

        /*
         * Auch hier übernimmt der Controller
         * nicht selbst den Datenzugriff.
         *
         * Er bittet den BandService,
         * alle Bands zu liefern.
         */
        return bandService.alleBandsAnzeigen();
    }

    /**
     * Sucht, filtert und sortiert Bands.
     *
     * Diese Methode könnte später zum Beispiel
     * durch eine Anfrage wie diese ausgelöst werden:
     *
     * GET /bands?search=metal&status=GRUEN&sort=BANDNAME
     *
     * Parameter:
     *
     * suchtext:
     * Text, nach dem gesucht werden soll.
     * Das kann zum Beispiel ein primärer oder sekundärer Bandname sein.
     *
     * status:
     * Optionaler Recherche-Status,
     * nach dem gefiltert werden soll.
     *
     * sortierung:
     * Legt fest, wie das Ergebnis sortiert werden soll.
     *
     * Rückgabewert:
     *
     * Eine Liste mit den Bands,
     * die zu den Such- und Filterkriterien passen.
     */
    public List<Band> findBands(
            String suchtext,
            ResearchStatus status,
            BandSortierung sortierung) {

        /*
         * Der Controller sammelt nur die Eingaben
         * und leitet sie vollständig an den BandService weiter.
         *
         * Die eigentliche Suche, Filterung und Sortierung
         * findet im Service statt.
         */
        return bandService.bandsFinden(
                suchtext,
                status,
                sortierung);
    }

    /**
     * Liefert eine einzelne Band anhand ihrer ID.
     *
     * Eine ID ist eine eindeutige Kennung.
     *
     * Diese Methode könnte später einem REST-Endpunkt entsprechen:
     *
     * GET /bands/{id}
     *
     * Beispiel:
     *
     * GET /bands/5
     *
     * Parameter:
     *
     * id:
     * Die eindeutige Nummer der gesuchten Band.
     *
     * Rückgabewert:
     *
     * Die gefundene Band.
     */
    public Band getBand(Long id) {

        /*
         * Die Suche nach der Band übernimmt der BandService.
         *
         * Der Service kann dort auch entscheiden,
         * was passiert, wenn keine Band mit dieser ID existiert.
         */
        return bandService.bandAnzeigen(id);
    }

    /**
     * Bearbeitet eine bestehende Band.
     *
     * Diese Methode könnte später einem REST-Endpunkt entsprechen:
     *
     * PUT /bands/{id}
     *
     * oder:
     *
     * PATCH /bands/{id}
     *
     * Parameter:
     *
     * id:
     * ID der Band, die geändert werden soll.
     *
     * neuerName:
     * Neuer primärer Bandname.
     *
     * neueSekundaereNamen:
     * Neue Liste mit alternativen Bandnamen.
     *
     * neuerStatus:
     * Neuer Recherche-Status.
     *
     * neueQuellen:
     * Neue Liste mit Quellen oder Links.
     *
     * neuerKommentar:
     * Neuer Kommentar zur Recherche.
     *
     * bearbeiter:
     * Person, die die Änderung durchgeführt hat.
     *
     * Rückgabewert:
     *
     * Die aktualisierte Band.
     */
    public Band updateBand(
            Long id,
            String neuerName,
            List<String> neueSekundaereNamen,
            ResearchStatus neuerStatus,
            List<String> neueQuellen,
            String neuerKommentar,
            String bearbeiter) {

        /*
         * Der Controller nimmt alle neuen Werte entgegen
         * und leitet sie an den BandService weiter.
         *
         * Der Service kann dann:
         *
         * - die bestehende Band laden,
         * - alte und neue Werte vergleichen,
         * - die Änderungen speichern,
         * - und einen Eintrag im Aktivitätslog erzeugen.
         */
        return bandService.bandBearbeiten(
                id,
                neuerName,
                neueSekundaereNamen,
                neuerStatus,
                neueQuellen,
                neuerKommentar,
                bearbeiter);
    }

    /**
     * Löscht eine Band.
     *
     * Diese Methode könnte später einem REST-Endpunkt entsprechen:
     *
     * DELETE /bands/{id}
     *
     * Parameter:
     *
     * id:
     * ID der zu löschenden Band.
     *
     * benutzer:
     * Person, die die Löschung ausgelöst hat.
     *
     * Rückgabewert:
     *
     * void bedeutet:
     * Die Methode gibt keinen Wert zurück.
     */
    public void deleteBand(
            Long id,
            String benutzer) {

        /*
         * Die Löschung übernimmt der BandService.
         *
         * Dort kann auch dokumentiert werden:
         *
         * - welche Band gelöscht wurde,
         * - wer sie gelöscht hat,
         * - und wann die Löschung passiert ist.
         */
        bandService.bandLoeschen(id, benutzer);
    }

    /**
     * Gibt das vollständige Aktivitätsprotokoll zurück.
     *
     * Diese Methode könnte später einem REST-Endpunkt entsprechen:
     *
     * GET /activity-log
     *
     * Rückgabewert:
     *
     * Eine Liste von ActivityLogEntry-Objekten.
     *
     * Jeder Eintrag kann zum Beispiel enthalten:
     *
     * - Benutzer
     * - Zeitpunkt
     * - Art der Änderung
     * - alten Wert
     * - neuen Wert
     */
    public List<ActivityLogEntry> getActivityLog() {

        /*
         * Das Aktivitätslog wird vom BandService verwaltet.
         *
         * Der Controller reicht die vorhandenen Einträge nur weiter.
         */
        return bandService.aktivitaetslogAnzeigen();
    }

    /**
     * Fordert einen neuen CSV-Export an.
     *
     * Diese Methode ist für die verteilte Architektur besonders wichtig.
     *
     * Sie erstellt die CSV-Datei nicht direkt selbst.
     *
     * Stattdessen wird ein Export-Auftrag erzeugt.
     * Ein anderer Prozess, der Export-Service,
     * verarbeitet diesen Auftrag später.
     *
     * Diese Methode könnte einem REST-Endpunkt entsprechen:
     *
     * POST /exports/csv
     *
     * Parameter:
     *
     * benutzer:
     * Person, die den Export angefordert hat.
     *
     * Rückgabewert:
     *
     * Der neu angelegte ExportJob.
     */
    public ExportJob requestCsvExport(String benutzer) {

        /*
         * Der ExportRequestService erstellt
         * einen neuen Export-Auftrag.
         *
         * Dieser Auftrag wird später
         * vom getrennten Export-Service verarbeitet.
         */
        return exportRequestService.exportAnfordern(benutzer);
    }

    /**
     * Gibt eine Liste aller Export-Aufträge zurück.
     *
     * Diese Methode könnte später einem REST-Endpunkt entsprechen:
     *
     * GET /exports
     *
     * Rückgabewert:
     *
     * Eine Liste von Textzeilen.
     *
     * Diese enthalten vermutlich die gespeicherten Export-Jobs,
     * zum Beispiel mit Status OFFEN oder ERLEDIGT.
     */
    public List<String> getExportJobs() {

        /*
         * Der ExportRequestService liest die vorhandenen Export-Aufträge
         * und gibt sie an den Controller zurück.
         */
        return exportRequestService.exportJobsAnzeigen();
    }
}