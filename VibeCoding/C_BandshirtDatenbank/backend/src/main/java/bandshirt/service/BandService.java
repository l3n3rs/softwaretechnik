package bandshirt.service;

import bandshirt.model.ActivityLogEntry;
import bandshirt.model.Band;
import bandshirt.model.BandSortierung;
import bandshirt.model.ResearchStatus;
import bandshirt.repository.ActivityLogRepository;
import bandshirt.repository.BandRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Diese Klasse enthält die fachliche Logik für die Bandverwaltung.
 *
 * Was bedeutet "fachliche Logik"?
 *
 * Fachliche Logik beschreibt die Regeln und Abläufe der Anwendung.
 *
 * Beispiele:
 *
 * - Wie wird eine neue Band angelegt?
 * - Welche Pflichtfelder müssen gefüllt sein?
 * - Wie werden Bands gesucht?
 * - Wie werden Bands gefiltert und sortiert?
 * - Wie wird eine Band bearbeitet?
 * - Wann wird ein Eintrag im Aktivitätslog erzeugt?
 *
 * Warum gibt es dafür eine eigene Service-Klasse?
 *
 * Der Controller soll nur Anfragen entgegennehmen
 * und an die passende Stelle weiterleiten.
 *
 * Das Repository soll nur Daten speichern und laden.
 *
 * Der Service liegt dazwischen:
 *
 * Controller
 * ↓
 * BandService
 * ↓
 * Repository
 *
 * Dadurch ist klar getrennt:
 *
 * - Controller: nimmt Anfragen entgegen
 * - Service: enthält Regeln und Abläufe
 * - Repository: speichert und liest Daten
 *
 * Zusammenarbeit:
 *
 * - BandController ruft Methoden dieser Klasse auf.
 * - BandRepository speichert und lädt Bands.
 * - ActivityLogRepository speichert Einträge im Aktivitätsprotokoll.
 */
public class BandService {

    /*
     * Dieses Repository ist für die Speicherung der Bands zuständig.
     *
     * Der Service weiß nicht,
     * ob die Bands im Arbeitsspeicher,
     * in einer Datei oder in einer Datenbank gespeichert werden.
     *
     * Er kennt nur die Schnittstelle BandRepository.
     */
    private final BandRepository bandRepository;

    /*
     * Dieses Repository speichert das Aktivitätsprotokoll.
     *
     * Dadurch können Änderungen an Bands
     * getrennt von den eigentlichen Banddaten gespeichert werden.
     */
    private final ActivityLogRepository activityLogRepository;

    /*
     * nextBandId erzeugt fortlaufende IDs für neue Bands.
     *
     * AtomicLong startet hier bei 1.
     *
     * Jeder Aufruf von getAndIncrement() liefert zuerst
     * den aktuellen Wert und erhöht ihn danach um 1.
     *
     * Beispiel:
     *
     * erster Aufruf -> 1
     * zweiter Aufruf -> 2
     * dritter Aufruf -> 3
     *
     * AtomicLong ist außerdem für mehrere parallele Zugriffe geeignet.
     * Für diesen Prototyp wäre auch ein einfaches long möglich,
     * AtomicLong ist aber etwas robuster.
     */
    private final AtomicLong nextBandId = new AtomicLong(1);

    /*
     * Dasselbe Prinzip wird für die IDs der Logeinträge verwendet.
     */
    private final AtomicLong nextLogId = new AtomicLong(1);

    /**
     * Konstruktor des BandService.
     *
     * Die beiden Repositorys werden von außen übergeben.
     *
     * Dadurch kann der Service mit unterschiedlichen
     * Speicherarten verwendet werden.
     */
    public BandService(
            BandRepository bandRepository,
            ActivityLogRepository activityLogRepository) {
        this.bandRepository = bandRepository;
        this.activityLogRepository = activityLogRepository;
    }

    /**
     * Legt eine neue Band an.
     *
     * Parameter:
     *
     * primaererName:
     * Hauptname der Band.
     *
     * erstelltVon:
     * Person oder Prozess, der die Band angelegt hat.
     *
     * Rückgabewert:
     *
     * Die neu angelegte und gespeicherte Band.
     */
    public Band bandAnlegen(
            String primaererName,
            String erstelltVon) {

        /*
         * Bevor die Band angelegt wird,
         * werden die Pflichtfelder geprüft.
         *
         * Ist ein Pflichtfeld leer,
         * wird eine Fehlermeldung ausgelöst.
         */
        pruefePflichtfeld(primaererName, "primaererName");
        pruefePflichtfeld(erstelltVon, "erstelltVon");

        /*
         * Es wird ein neues Band-Objekt erstellt.
         *
         * Die ID wird automatisch erzeugt.
         */
        Band band = new Band(
                nextBandId.getAndIncrement(),
                primaererName,
                erstelltVon);

        /*
         * Die neue Band wird über das Repository gespeichert.
         */
        bandRepository.save(band);

        /*
         * Zusätzlich wird dokumentiert,
         * dass die Band angelegt wurde.
         *
         * Der alte Wert ist "-",
         * weil es vorher noch keine Band gab.
         */
        log(
                band.getId(),
                erstelltVon,
                "Band angelegt",
                "-",
                primaererName);

        /*
         * Die fertige Band wird zurückgegeben.
         */
        return band;
    }

    /**
     * Gibt alle gespeicherten Bands zurück.
     */
    public List<Band> alleBandsAnzeigen() {

        /*
         * Das Repository liefert die vollständige Bandliste.
         */
        return bandRepository.findAll();
    }

    /**
     * Sucht Bands nach primären und sekundären Namen.
     *
     * Beispiel:
     *
     * Suchtext: "metal"
     *
     * Gefunden werden könnten:
     *
     * - Metallica
     * - Metal Church
     * - eine Band mit sekundärem Namen "Old Metal Project"
     */
    public List<Band> bandsSuchen(String suchtext) {

        /*
         * Wenn kein Suchtext angegeben wurde,
         * sollen alle Bands zurückgegeben werden.
         *
         * null bedeutet:
         * Es wurde gar kein Wert übergeben.
         *
         * isBlank() bedeutet:
         * Der Text ist leer oder besteht nur aus Leerzeichen.
         */
        if (suchtext == null || suchtext.isBlank()) {
            return alleBandsAnzeigen();
        }

        /*
         * Der Suchtext wird in Kleinbuchstaben umgewandelt.
         *
         * Dadurch wird die Suche unabhängig von Groß- und Kleinschreibung.
         *
         * Beispiel:
         *
         * "Metallica"
         * "metallica"
         * "METALLICA"
         *
         * werden bei der Suche gleich behandelt.
         */
        String normalisierterSuchtext = suchtext.toLowerCase();

        /*
         * bandRepository.findAll()
         * liefert zunächst alle Bands.
         *
         * stream()
         * ermöglicht eine schrittweise Verarbeitung der Liste.
         *
         * filter()
         * behält nur Bands,
         * die zur Suchbedingung passen.
         */
        return bandRepository.findAll()
                .stream()

                /*
                 * Eine Band bleibt erhalten,
                 * wenn entweder:
                 *
                 * - der primäre Name den Suchtext enthält
                 * oder
                 * - mindestens ein sekundärer Name den Suchtext enthält
                 */
                .filter(band -> enthaeltPrimaerenNamen(
                        band,
                        normalisierterSuchtext)
                        ||
                        enthaeltSekundaerenNamen(
                                band,
                                normalisierterSuchtext))

                /*
                 * collect(Collectors.toList())
                 * sammelt alle passenden Bands wieder in einer Liste.
                 */
                .collect(Collectors.toList());
    }

    /**
     * Filtert Bands nach Recherche-Status.
     *
     * Wenn kein Status übergeben wird,
     * werden alle Bands zurückgegeben.
     */
    public List<Band> bandsNachStatusFiltern(
            ResearchStatus status) {

        /*
         * null bedeutet hier:
         * Es soll kein Statusfilter angewendet werden.
         */
        if (status == null) {
            return alleBandsAnzeigen();
        }

        /*
         * Alle Bands werden geladen.
         * Danach bleiben nur Bands mit dem gewünschten Status erhalten.
         */
        return bandRepository.findAll()
                .stream()
                .filter(band -> band.getRechercheStatus() == status)
                .collect(Collectors.toList());
    }

    /**
     * Sortiert eine beliebige Bandliste.
     *
     * Das ist praktisch,
     * weil vorher bereits gesucht oder gefiltert worden sein kann.
     *
     * Danach wird nur das Ergebnis sortiert.
     */
    public List<Band> bandsSortieren(
            List<Band> bands,
            BandSortierung sortierung) {

        /*
         * Wenn keine Sortierung angegeben wurde,
         * wird die vorhandene Liste unverändert zurückgegeben.
         */
        if (sortierung == null) {
            return bands;
        }

        /*
         * Ein Comparator beschreibt,
         * nach welchem Kriterium zwei Bands verglichen werden.
         *
         * Dieser Comparator wird abhängig von der gewünschten
         * Sortierung festgelegt.
         */
        Comparator<Band> comparator;

        /*
         * switch prüft,
         * welche Sortierart ausgewählt wurde.
         */
        switch (sortierung) {

            case LETZTE_AENDERUNG:

                /*
                 * Vergleich nach dem Zeitpunkt der letzten Änderung.
                 *
                 * reversed() dreht die Reihenfolge um,
                 * damit die zuletzt geänderten Bands zuerst erscheinen.
                 */
                comparator = Comparator
                        .comparing(Band::getLetzteAenderungAm)
                        .reversed();
                break;

            case STATUS:

                /*
                 * Vergleich nach dem sichtbaren Namen des Status.
                 *
                 * Dadurch wird alphabetisch nach:
                 *
                 * gelb
                 * grün
                 * rot
                 * unrecherchiert
                 *
                 * sortiert.
                 */
                comparator = Comparator.comparing(
                        band -> band
                                .getRechercheStatus()
                                .getAnzeigeName());
                break;

            case BANDNAME:
            default:

                /*
                 * Standardmäßig wird nach dem primären Bandnamen sortiert.
                 *
                 * CASE_INSENSITIVE_ORDER sorgt dafür,
                 * dass Groß- und Kleinschreibung ignoriert werden.
                 */
                comparator = Comparator.comparing(
                        Band::getPrimaererName,
                        String.CASE_INSENSITIVE_ORDER);
                break;
        }

        /*
         * Die Liste wird als Stream verarbeitet,
         * mit dem gewählten Comparator sortiert
         * und danach wieder in eine Liste umgewandelt.
         *
         * Die ursprüngliche Liste wird dabei nicht direkt verändert.
         */
        return bands.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    /**
     * Kombiniert Suche, Filter und Sortierung.
     *
     * Diese Methode ist besonders praktisch für eine spätere API.
     *
     * Beispiel:
     *
     * GET /bands?suche=metal&status=GRUEN&sort=BANDNAME
     */
    public List<Band> bandsFinden(
            String suchtext,
            ResearchStatus status,
            BandSortierung sortierung) {

        /*
         * Zuerst wird nach dem Suchtext gesucht.
         */
        List<Band> gefundeneBands = bandsSuchen(suchtext);

        /*
         * Falls ein Status angegeben wurde,
         * wird das Suchergebnis zusätzlich gefiltert.
         */
        if (status != null) {
            gefundeneBands = gefundeneBands
                    .stream()
                    .filter(band -> band.getRechercheStatus() == status)
                    .collect(Collectors.toList());
        }

        /*
         * Zum Schluss wird das Ergebnis sortiert.
         */
        return bandsSortieren(
                gefundeneBands,
                sortierung);
    }

    /**
     * Sucht eine einzelne Band anhand ihrer ID.
     *
     * Falls keine Band gefunden wird,
     * wird eine verständliche Ausnahme ausgelöst.
     */
    public Band bandAnzeigen(Long id) {

        /*
         * findById() liefert Optional<Band>.
         *
         * orElseThrow() bedeutet:
         *
         * - wenn eine Band vorhanden ist, gib sie zurück
         * - wenn keine vorhanden ist, löse einen Fehler aus
         */
        return bandRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        "Keine Band mit ID "
                                + id
                                + " gefunden."));
    }

    /**
     * Bearbeitet eine bestehende Band.
     *
     * Es werden nur Felder geändert,
     * für die tatsächlich neue Werte übergeben wurden.
     *
     * Jede Änderung wird im Aktivitätslog dokumentiert.
     */
    public Band bandBearbeiten(
            Long id,
            String neuerName,
            List<String> neueSekundaereNamen,
            ResearchStatus neuerStatus,
            List<String> neueQuellen,
            String neuerKommentar,
            String bearbeiter) {

        /*
         * Der Bearbeiter ist ein Pflichtfeld,
         * weil jede Änderung nachvollziehbar sein soll.
         */
        pruefePflichtfeld(bearbeiter, "bearbeiter");

        /*
         * Die bestehende Band wird geladen.
         *
         * Falls sie nicht existiert,
         * beendet bandAnzeigen() die Verarbeitung mit einer Ausnahme.
         */
        Band band = bandAnzeigen(id);

        /*
         * Jede folgende if-Abfrage behandelt ein einzelnes Feld.
         *
         * Grundprinzip:
         *
         * 1. prüfen, ob ein neuer Wert vorhanden ist
         * 2. alten Wert merken
         * 3. neuen Wert setzen
         * 4. Änderung protokollieren
         */

        /*
         * Primären Bandnamen ändern.
         */
        if (neuerName != null && !neuerName.isBlank()) {

            /*
             * Alten Namen vor der Änderung speichern.
             */
            String alterName = band.getPrimaererName();

            /*
             * Neuen Namen setzen.
             */
            band.setPrimaererName(neuerName);

            /*
             * Änderung protokollieren.
             */
            log(
                    id,
                    bearbeiter,
                    "Primaerer Bandname geaendert",
                    alterName,
                    neuerName);
        }

        /*
         * Sekundäre Bandnamen ersetzen.
         */
        if (neueSekundaereNamen != null) {

            /*
             * Die bisherige Liste wird für das Log
             * in einen lesbaren Text umgewandelt.
             */
            String alterWert = String.join(
                    ", ",
                    band.getSekundaereNamen());

            /*
             * Dasselbe passiert mit der neuen Liste.
             */
            String neuerWert = String.join(
                    ", ",
                    neueSekundaereNamen);

            /*
             * Es wird bewusst eine neue ArrayList erzeugt.
             *
             * Dadurch übernimmt die Band nicht direkt
             * dieselbe Listeninstanz, die von außen übergeben wurde.
             */
            band.setSekundaereNamen(
                    new ArrayList<>(neueSekundaereNamen));

            /*
             * Änderung protokollieren.
             */
            log(
                    id,
                    bearbeiter,
                    "Sekundaere Bandnamen geaendert",
                    alterWert,
                    neuerWert);
        }

        /*
         * Recherche-Status ändern.
         *
         * Die Änderung wird nur durchgeführt,
         * wenn:
         *
         * - ein neuer Status übergeben wurde
         * - und er sich vom bisherigen Status unterscheidet
         */
        if (neuerStatus != null
                &&
                neuerStatus != band.getRechercheStatus()) {

            /*
             * Alten Status als sichtbaren Text speichern.
             */
            String alterStatus = band.getRechercheStatus()
                    .getAnzeigeName();

            /*
             * Neuen Status setzen.
             */
            band.setRechercheStatus(neuerStatus);

            /*
             * Änderung protokollieren.
             */
            log(
                    id,
                    bearbeiter,
                    "Recherche-Status geaendert",
                    alterStatus,
                    neuerStatus.getAnzeigeName());
        }

        /*
         * Quellen ersetzen.
         */
        if (neueQuellen != null) {

            String alterWert = String.join(
                    ", ",
                    band.getQuellen());

            String neuerWert = String.join(
                    ", ",
                    neueQuellen);

            band.setQuellen(
                    new ArrayList<>(neueQuellen));

            log(
                    id,
                    bearbeiter,
                    "Quellen geaendert",
                    alterWert,
                    neuerWert);
        }

        /*
         * Kommentar ändern.
         *
         * Hier reicht die Prüfung auf null.
         *
         * Ein leerer String wäre damit ebenfalls erlaubt
         * und würde den vorhandenen Kommentar löschen.
         */
        if (neuerKommentar != null) {

            String alterKommentar = band.getKommentar();

            band.setKommentar(neuerKommentar);

            log(
                    id,
                    bearbeiter,
                    "Kommentar geaendert",
                    alterKommentar,
                    neuerKommentar);
        }

        /*
         * Zum Schluss wird gespeichert,
         * wer die Band zuletzt geändert hat
         * und wann die Änderung stattgefunden hat.
         */
        band.markiereGeaendertVon(bearbeiter);

        /*
         * Die aktualisierte Band wird gespeichert
         * und danach zurückgegeben.
         */
        return bandRepository.save(band);
    }

    /**
     * Löscht eine Band.
     *
     * Auch die Löschung wird im Aktivitätslog dokumentiert.
     */
    public void bandLoeschen(
            Long id,
            String benutzer) {

        /*
         * Der Name des Benutzers ist Pflicht,
         * damit nachvollziehbar bleibt,
         * wer die Band gelöscht hat.
         */
        pruefePflichtfeld(benutzer, "benutzer");

        /*
         * Die Band wird vor dem Löschen geladen.
         *
         * Dadurch kann ihr Name später
         * im Aktivitätslog gespeichert werden.
         */
        Band band = bandAnzeigen(id);

        /*
         * Jetzt wird die Band aus dem Repository entfernt.
         */
        bandRepository.deleteById(id);

        /*
         * Die Löschung wird protokolliert.
         *
         * Alter Wert:
         * der bisherige Bandname
         *
         * Neuer Wert:
         * "-"
         *
         * Das Minus zeigt,
         * dass nach der Löschung kein neuer Wert mehr existiert.
         */
        log(
                id,
                benutzer,
                "Band geloescht",
                band.getPrimaererName(),
                "-");
    }

    /**
     * Gibt alle Einträge des Aktivitätsprotokolls zurück.
     */
    public List<ActivityLogEntry> aktivitaetslogAnzeigen() {

        /*
         * Das Repository liefert alle gespeicherten Einträge.
         */
        return activityLogRepository.findAll();
    }

    /**
     * Hilfsmethode zum Erstellen eines Logeintrags.
     *
     * Warum eine eigene Methode?
     *
     * Ohne diese Methode müsste der gleiche Code
     * bei jeder Änderung wiederholt werden.
     *
     * Die Methode sorgt dafür,
     * dass alle Logeinträge gleich aufgebaut sind.
     */
    private void log(
            Long bandId,
            String benutzer,
            String artDerAenderung,
            String alterWert,
            String neuerWert) {

        /*
         * Ein neues ActivityLogEntry-Objekt wird erstellt.
         *
         * Die ID wird automatisch erzeugt.
         */
        ActivityLogEntry entry = new ActivityLogEntry(
                nextLogId.getAndIncrement(),
                bandId,
                benutzer,
                artDerAenderung,
                alterWert,
                neuerWert);

        /*
         * Der Eintrag wird im Log-Repository gespeichert.
         */
        activityLogRepository.save(entry);
    }

    /**
     * Prüft, ob ein Pflichtfeld ausgefüllt wurde.
     *
     * Diese Methode wird mehrfach verwendet,
     * damit die Prüfung nicht an verschiedenen Stellen
     * doppelt geschrieben werden muss.
     */
    private void pruefePflichtfeld(
            String wert,
            String feldname) {

        /*
         * Ein Wert ist ungültig,
         * wenn er null, leer oder nur aus Leerzeichen besteht.
         */
        if (wert == null || wert.isBlank()) {

            /*
             * IllegalArgumentException bedeutet:
             * Die Methode wurde mit einem ungültigen Argument aufgerufen.
             */
            throw new IllegalArgumentException(
                    "Das Feld "
                            + feldname
                            + " darf nicht leer sein.");
        }
    }

    /**
     * Prüft, ob der primäre Bandname
     * den Suchtext enthält.
     */
    private boolean enthaeltPrimaerenNamen(
            Band band,
            String normalisierterSuchtext) {

        /*
         * Der Bandname wird ebenfalls in Kleinbuchstaben umgewandelt.
         *
         * contains() prüft,
         * ob der Suchtext irgendwo im Namen vorkommt.
         */
        return band
                .getPrimaererName()
                .toLowerCase()
                .contains(normalisierterSuchtext);
    }

    /**
     * Prüft, ob mindestens ein sekundärer Bandname
     * den Suchtext enthält.
     */
    private boolean enthaeltSekundaerenNamen(
            Band band,
            String normalisierterSuchtext) {

        /*
         * stream() verarbeitet alle sekundären Namen.
         *
         * anyMatch() liefert true,
         * sobald mindestens ein Name zur Bedingung passt.
         */
        return band
                .getSekundaereNamen()
                .stream()
                .anyMatch(name -> name
                        .toLowerCase()
                        .contains(normalisierterSuchtext));
    }
}