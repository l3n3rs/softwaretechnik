package bandshirt.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Warum gibt es diese Klasse?
 * Band ist die zentrale Fachklasse der Anwendung. Sie beschreibt eine Band, die
 * im Verein bewertet und dokumentiert werden soll.
 *
 * Aufgabe im System:
 * Die Klasse speichert alle wichtigen Bandinformationen: Namen, Status,
 * Quellen, Kommentare und Angaben zur letzten Bearbeitung.
 *
 * Zusammenarbeit:
 * Der BandService erstellt und veraendert Band-Objekte. Das BandRepository
 * speichert sie. Der BandController gibt sie spaeter an das Frontend weiter.
 */
public class Band {
    private Long id;
    private String primaererName;
    private List<String> sekundaereNamen = new ArrayList<>();
    private ResearchStatus rechercheStatus = ResearchStatus.UNRECHERCHIERT;
    private List<String> quellen = new ArrayList<>();
    private String kommentar;
    private String erstelltVon;
    private LocalDateTime letzteAenderungAm;
    private String letzteAenderungVon;

    public Band(Long id, String primaererName, String erstelltVon) {
        this.id = id;
        this.primaererName = primaererName;
        this.erstelltVon = erstelltVon;
        this.letzteAenderungVon = erstelltVon;
        this.letzteAenderungAm = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getPrimaererName() {
        return primaererName;
    }

    public void setPrimaererName(String primaererName) {
        this.primaererName = primaererName;
    }

    public List<String> getSekundaereNamen() {
        return sekundaereNamen;
    }

    public void setSekundaereNamen(List<String> sekundaereNamen) {
        this.sekundaereNamen = sekundaereNamen;
    }

    public ResearchStatus getRechercheStatus() {
        return rechercheStatus;
    }

    public void setRechercheStatus(ResearchStatus rechercheStatus) {
        this.rechercheStatus = rechercheStatus;
    }

    public List<String> getQuellen() {
        return quellen;
    }

    public void setQuellen(List<String> quellen) {
        this.quellen = quellen;
    }

    public String getKommentar() {
        return kommentar;
    }

    public void setKommentar(String kommentar) {
        this.kommentar = kommentar;
    }

    public String getErstelltVon() {
        return erstelltVon;
    }

    public LocalDateTime getLetzteAenderungAm() {
        return letzteAenderungAm;
    }

    public String getLetzteAenderungVon() {
        return letzteAenderungVon;
    }

    public void markiereGeaendertVon(String benutzer) {
        this.letzteAenderungVon = benutzer;
        this.letzteAenderungAm = LocalDateTime.now();
    }
}
