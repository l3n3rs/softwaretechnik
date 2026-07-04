-- Einfaches Datenmodell fuer die Bandshirt-Datenbank.
--
-- Diese Datei ist noch keine angebundene Datenbank, sondern beschreibt das
-- geplante relationale Datenmodell. Das passt zum Lernziel: Man kann sehen,
-- welche Daten das Backend spaeter dauerhaft speichern soll.
--
-- Der aktuelle Java-Prototyp nutzt zunaechst In-Memory-Repositories. Das
-- bedeutet: Die Daten liegen nur im Arbeitsspeicher. Spaeter koennte man diese
-- Repositories durch echte Datenbank-Repositories ersetzen, die diese Tabellen
-- verwenden.

-- Haupttabelle fuer Bands.
-- Hier stehen die Informationen, die genau einmal pro Band vorkommen:
-- primaerer Name, Status, Kommentar und Informationen zur letzten Aenderung.
CREATE TABLE bands (
    id BIGINT PRIMARY KEY,
    primaerer_name VARCHAR(255) NOT NULL,
    recherche_status VARCHAR(30) NOT NULL,
    kommentar TEXT,
    erstellt_von VARCHAR(100) NOT NULL,
    letzte_aenderung_am TIMESTAMP NOT NULL,
    letzte_aenderung_von VARCHAR(100) NOT NULL
);

-- Eine Band kann mehrere sekundaere Namen haben.
-- Deshalb werden diese Namen in einer eigenen Tabelle gespeichert.
-- band_id verbindet jeden sekundaeren Namen mit genau einer Band.
CREATE TABLE sekundaere_bandnamen (
    id BIGINT PRIMARY KEY,
    band_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    FOREIGN KEY (band_id) REFERENCES bands(id)
);

-- Eine Band kann mehrere Quellen oder Links haben.
-- Auch diese Daten stehen in einer eigenen Tabelle, weil es pro Band mehrere
-- Links geben kann.
CREATE TABLE quellen (
    id BIGINT PRIMARY KEY,
    band_id BIGINT NOT NULL,
    url TEXT NOT NULL,
    FOREIGN KEY (band_id) REFERENCES bands(id)
);

-- Das Aktivitaetslog speichert wichtige Aenderungen.
-- Dadurch kann man spaeter nachvollziehen, wer wann welchen Wert geaendert hat.
-- band_id darf hier theoretisch leer sein, falls irgendwann ein Logeintrag
-- nicht direkt zu einer einzelnen Band gehoert.
CREATE TABLE activity_log (
    id BIGINT PRIMARY KEY,
    band_id BIGINT,
    benutzer VARCHAR(100) NOT NULL,
    zeitpunkt TIMESTAMP NOT NULL,
    art_der_aenderung VARCHAR(255) NOT NULL,
    alter_wert TEXT,
    neuer_wert TEXT,
    FOREIGN KEY (band_id) REFERENCES bands(id)
);
