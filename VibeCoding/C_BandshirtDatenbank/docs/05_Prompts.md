# Lege Bandshirt-Projektstruktur an

> Ich möchte eine größere, aber bewusst verständliche verteilte Anwendung als Studienprojekt entwickeln.
>
> Thema:
> Bandshirt-Datenbank für einen Verein.
>
> Die Anwendung soll Bands verwalten und bewerten. Dabei sollen Bands mit einem Recherche-Status versehen werden:
>
> - grün
> - gelb
> - rot
> - unrecherchiert (grau)
>
> Wichtig:
> Die Anwendung muss nicht vollständig lauffähig sein. Ziel ist vor allem, eine verteilte Architektur mit getrennten Modulen zu zeigen und den Code später vollständig verstehen und erklären zu können.
>
> Bitte erstelle zunächst nur eine sinnvolle Projekt- und Ordnerstruktur.
>
> Die Anwendung soll aus folgenden Teilen bestehen:
>
> 1. frontend
>    - einfache HTML/CSS/JavaScript-Oberfläche
>    - Bandübersicht
>    - Detailansicht
>    - Formular zum Anlegen einer Band
>
> 2. backend
>    - Java-Code
>    - Controller
>    - Service
>    - Repository
>    - Model-Klassen
>
> 3. database
>    - einfache SQL-Datei oder Beschreibung des Datenmodells
>
> 4. docs
>    - Markdown-Dateien zur schrittweisen Dokumentation
>
> Bitte lege folgende Struktur an:
>
> BandshirtDatabase/
> ├── frontend/
> │   ├── index.html
> │   ├── css/
> │   │   └── style.css
> │   └── js/
> │       └── app.js
> │
> ├── backend/
> │   └── src/
> │       └── main/
> │           └── java/
> │               └── bandshirt/
> │                   ├── controller/
> │                   ├── service/
> │                   ├── repository/
> │                   └── model/
> │
> ├── database/
> │   └── schema.sql
> │
> ├── docs/
> │   ├── 01_Idee.md
> │   ├── 02_Architektur.md
> │   ├── 03_Module.md
> │   ├── 04_REST_API.md
> │   ├── 05_Datenmodell.md
> │   ├── 06_Codeverstaendnis.md
> │   └── 07_Fazit.md
> │
> └── README.md
>
> Bitte erstelle zuerst nur die Ordnerstruktur, leere Platzhalterdateien und eine kurze README.md.
>
> Schreibe noch keinen vollständigen Code.
>
> Kommentiere kurz, warum diese Struktur für eine verteilte Anwendung sinnvoll ist.

<details><summary>4 previous messages</summary>

> Ich lege jetzt nur das Grundgerüst an: Ordner, leere Platzhalterdateien und eine knappe README mit der Begründung der Modultrennung. Danach prüfe ich kurz, ob die Struktur wie gewünscht existiert.
>
> Die Struktur ist angelegt. Ich fülle jetzt nur die README minimal aus, damit sie den Zweck und die verteilte Aufteilung erklärt, ohne schon Implementierung vorwegzunehmen.
>
> <details><summary>Edited a file</summary>
>
> - Wrote `./BandshirtDatabase/README.md` (+27 -0)
>
> </details>
>
> Die README ist bewusst kurz gehalten und bleibt auf Architektur und Zweck fokussiert. Ich kontrolliere noch einmal die tatsächlichen Dateien, damit nichts fehlt oder falsch benannt ist.
</details>

Erledigt. Die Projektstruktur wurde unter [BandshirtDatabase](~/OneDrive%20-%20Berliner%20Hochschule%20für%20Technik/SS26/Softwaretechnik/github/VibeCoding/C_BandshirtDatenbank/BandshirtDatabase) angelegt, inklusive leerer Platzhalterdateien und einer kurzen [README.md](~/OneDrive%20-%20Berliner%20Hochschule%20für%20Technik/SS26/Softwaretechnik/github/VibeCoding/C_BandshirtDatenbank/BandshirtDatabase/README.md).

Die README erklärt knapp, warum die Trennung in `frontend`, `backend`, `database` und `docs` für eine verteilte Anwendung sinnvoll ist: Oberfläche, Serverlogik, Datenhaltung und Dokumentation sind sauber getrennt und können später einzeln erklärt und erweitert werden.

Es wurde noch kein vollständiger Code geschrieben.

> Beachte, dass ich die Ordnerstruktur manuell ein wenig angepasst habe.
> Für den Style nutze die außerdem die jetzt neu abgelegten Dateien.
>
> Bitte entwickle die Bandshirt-Datenbank jetzt einen Schritt weiter.
>
> Wichtig:  Ich habe nur grundlegende Programmierkenntnisse. Bitte erkläre deshalb bei jeder neu erstellten Klasse kurz und leicht verständlich:
>
> - warum diese Klasse benötigt wird
> - welche Aufgabe sie im System hat
> - mit welchen anderen Klassen sie zusammenarbeitet
>
> Bitte kommentiere den Code ausführlich und verständlich.
>
> Das System soll Folgendes können:
>
> 1. Bands verwalten
>    - Band anlegen
>    - Band anzeigen
>    - Band bearbeiten
>    - Band löschen
>
> 2. Bandinformationen speichern
>    - primärer Bandname
>    - mehrere sekundäre Bandnamen
>    - Recherche-Status
>    - Quellen/Links
>    - Kommentare
>    - Ersteller der Band
>    - Datum der letzten Änderung
>    - Bearbeiter der letzten Änderung
>
> 3. Recherche-Status verwenden
>    Folgende Status sollen möglich sein:
>    - grün
>    - gelb
>    - rot
>    - unrecherchiert (grau)
>
> 4. Nachvollziehbarkeit
>    Jede wichtige Änderung soll später in einem Aktivitätslog gespeichert werden.
>
>    Ein Logeintrag soll enthalten:
>    - Benutzer
>    - Datum und Uhrzeit
>    - Art der Änderung
>    - alter Wert
>    - neuer Wert
>
> 5. Verteilte Architektur
>    Die Anwendung soll aus getrennten Bereichen bestehen:
>    - Frontend
>    - Backend
>    - Datenbank
>
> Bitte erstelle jetzt einen ersten einfachen Code.
>
> Bitte ergänze die README.md um eine kurze Beschreibung, was das System können soll.

<details><summary>7 previous messages</summary>

> Alles klar, ich schaue mir zuerst die manuell angepasste Struktur und die neu abgelegten Style-Dateien an, damit ich mich sauber daran halte. Danach ergänze ich nur einen ersten, verständlichen Code-Stand mit viel Erklärung statt zu viel Framework-Magie.
>
> Ich sehe, dass das Projekt jetzt direkt im Hauptordner liegt, nicht mehr unter einem extra `BandshirtDatabase/`-Unterordner. Außerdem gibt es mehrere CSS-Dateien im Frontend; ich lese sie kurz, damit die HTML-Oberfläche daran andockt statt einen zweiten Stil daneben zu bauen.
>
> Der erste Code-Stand wird bewusst einfach: keine Spring-Boot-Komplexität, sondern klare Java-Klassen, die die Rollen Controller, Service, Repository und Model zeigen. Ich ergänze dazu eine kleine statische Oberfläche und ein SQL-Modell, damit Frontend, Backend und Datenbank als getrennte Bereiche sichtbar werden.
>
> Git meldet wegen der Sandbox eine Ownership-Warnung; ich ändere daran nichts und arbeite direkt an den Projektdateien weiter. Jetzt kommen die eigentlichen Ergänzungen: kleine Modellklassen, Repositories, Service, Controller, Schema und ein einfaches Frontend mit den vorhandenen CSS-Grundlagen.
>
> <details><summary>Created 9 files, edited 5 files</summary>
>
> - Wrote `./README.md` (+18 -0)
> - Wrote `./backend/src/main/java/bandshirt/controller/BandController.java` (+54 -0)
> - Wrote `./backend/src/main/java/bandshirt/model/ActivityLogEntry.java` (+64 -0)
> - Wrote `./backend/src/main/java/bandshirt/model/Band.java` (+99 -0)
> - Wrote `./backend/src/main/java/bandshirt/model/ResearchStatus.java` (+30 -0)
> - Wrote `./backend/src/main/java/bandshirt/repository/ActivityLogRepository.java` (+23 -0)
> - Wrote `./backend/src/main/java/bandshirt/repository/BandRepository.java` (+28 -0)
> - Wrote `./backend/src/main/java/bandshirt/repository/InMemoryActivityLogRepository.java` (+32 -0)
> - Wrote `./backend/src/main/java/bandshirt/repository/InMemoryBandRepository.java` (+47 -0)
> - Wrote `./backend/src/main/java/bandshirt/service/BandService.java` (+106 -0)
> - Wrote `./database/schema.sql` (+39 -0)
> - Wrote `./frontend/css/globals.css` (+221 -0)
> - Wrote `./frontend/index.html` (+86 -0)
> - Wrote `./frontend/js/app.js` (+187 -0)
>
> </details>
>
> Die Dateien sind jetzt ergänzt. Ich prüfe als Nächstes, ob die Java-Klassen zumindest kompilierbar sind; das ist ein guter kleiner Sicherheitscheck, auch wenn das Projekt noch keine vollständige lauffähige Anwendung sein soll.
>
> Der Java-Check ist sauber durchgelaufen: alle neu angelegten Backend-Klassen lassen sich kompilieren. Ich mache noch eine kurze Inhaltskontrolle der wichtigsten Dateien, damit die Antwort gleich konkret sagen kann, was entstanden ist.
</details>

Erledigt. Ich habe die manuell angepasste Struktur beibehalten und den ersten einfachen Code-Stand ergänzt.

Neu im Backend sind verständlich kommentierte Java-Klassen für Model, Repository, Service und Controller, z. B. [Band.java](<./backend/src/main/java/bandshirt/model/Band.java>), [ResearchStatus.java](<./backend/src/main/java/bandshirt/model/ResearchStatus.java>), [BandService.java](<./backend/src/main/java/bandshirt/service/BandService.java>) und [BandController.java](<./backend/src/main/java/bandshirt/controller/BandController.java>). Jede neue Klasse erklärt im Kommentar, warum sie existiert, welche Aufgabe sie hat und mit welchen Klassen sie zusammenarbeitet.

Außerdem ergänzt:
- [frontend/index.html](<./frontend/index.html>) mit Bandübersicht, Detailansicht und Formular
- [frontend/js/app.js](<./frontend/js/app.js>) als einfacher Frontend-Prototyp mit Beispieldaten
- [frontend/css/globals.css](<./frontend/css/globals.css>) passend zu deinen vorhandenen Style-Dateien
- [database/schema.sql](<./database/schema.sql>) mit Tabellen für Bands, sekundäre Namen, Quellen und Activity Log
- [README.md](<./README.md>) mit kurzer Beschreibung, was das System können soll

Wichtig: Das Frontend ist noch nicht mit dem Java-Backend verbunden. Das ist absichtlich ein erster, gut erklärbarer Architekturstand. Die Java-Klassen habe ich mit `javac` geprüft; sie kompilieren erfolgreich.

> Bitte denk an eine Kommentierung von Codes und zwar in allen Dateien.
>
> Kommentiere den gesamten neu erstellten Code ausführlich.
>
> Die Anwendung soll noch folgende Funktionen zusätzlich erhalten:
>
> Suche
>
> Es soll möglich sein
>
> - nach dem primären Bandnamen zu suchen
> - nach sekundären Bandnamen zu suchen
>
> Filter
>
> Es soll möglich sein, Bands zu filtern nach Recherche-Status
>
> Sortierung
>
> Die Bandliste soll sortierbar sein nach
>
> - Bandname (alphabetisch)
> - letzter Änderung
> - Status
>
> Das "Status weiter" (<button type="button" data-action="edit-status" data-id="${band.id}">Status weiter</button>) ist unnötig.

<details><summary>13 previous messages</summary>

> Verstanden. Ich baue Suche, Filter und Sortierung in den einfachen Prototyp ein und entferne den „Status weiter“-Button. Dabei kommentiere ich die betroffenen Dateien deutlich ausführlicher, damit der Code später gut erklärbar bleibt.
>
> Ich erweitere jetzt nicht nur das Frontend, sondern auch die Backend-Schicht um passende Such-, Filter- und Sortiermethoden. So ist die Funktion nicht nur optisch vorhanden, sondern auch in der Architektur erklärbar: Frontend-Bedienung, Controller-Eingang, Service-Logik.
>
> <details><summary>Created 4 files, edited 2 files</summary>
>
> - Wrote `./backend/src/main/java/bandshirt/controller/BandController.java` (+5 -0)
> - Wrote `./backend/src/main/java/bandshirt/model/BandSortierung.java` (+21 -0)
> - Wrote `./backend/src/main/java/bandshirt/service/BandService.java` (+89 -0)
> - Wrote `./frontend/css/globals.css` (+256 -0)
> - Wrote `./frontend/index.html` (+154 -0)
> - Wrote `./frontend/js/app.js` (+291 -0)
>
> </details>
>
> Die Kernfunktionen sind jetzt eingebaut. Ich prüfe noch auf Kompilierbarkeit und auf versehentliche Nicht-ASCII-Zeichen, weil die Projektdateien sonst je nach Editor wieder unschön mit Umlauten dargestellt werden könnten.
>
> Der Java-Compile-Test ist erfolgreich. Es gab nur ein einzelnes Umlaut-Zeichen in einem Kommentar; das glätte ich noch, damit die Dateien konsistent bleiben.
>
> <details><summary>Edited a file</summary>
>
> - Wrote `./frontend/js/app.js` (+1 -1)
>
> </details>
>
> Ich mache jetzt noch eine letzte Kontrolle der geänderten Oberfläche und der Java-Erweiterungen. Danach fasse ich dir knapp zusammen, welche Funktionen wo umgesetzt wurden.
>
> Ich ergänze noch Kommentare im SQL-Schema, weil das ebenfalls Code im Projekt ist und später beim Erklären des Datenmodells helfen wird. Die von dir neu abgelegten Style-Basisdateien lasse ich inhaltlich unberührt und nutze sie nur weiter.
>
> <details><summary>Edited a file</summary>
>
> - Wrote `./database/schema.sql` (+22 -0)
>
> </details>
>
> Damit ist auch das Datenmodell kommentiert. Ich lasse den letzten Check noch einmal laufen: Java kompilieren und sicherstellen, dass der entfernte Button wirklich nicht mehr vorkommt.
>
> Der letzte Compile-Lauf ist diesmal nicht wegen Codefehlern gescheitert, sondern weil `javac` keinen Speicherplatz zum Schreiben der Klassendateien hatte. Ich probiere den Check noch einmal mit einem Ausgabeordner im Projekt selbst, damit klar ist, ob es wirklich nur am temporären Speicher lag.
>
> Auch im Projektordner scheitert `javac` am fehlenden Speicherplatz, nicht an der Syntax. Ich kontrolliere noch kurz, ob dabei unerwünschte Build-Dateien liegen geblieben sind.
>
> Der versehentlich erzeugte `build`-Ordner ist wieder entfernt. Ich frage noch kurz den freien Speicher ab, damit ich den fehlgeschlagenen Compile-Check sauber einordnen kann.
</details>

Erledigt. Ich habe Suche, Filter und Sortierung ergänzt und den unnötigen Button `Status weiter` vollständig entfernt.

Umgesetzt in:
- [frontend/index.html](<./frontend/index.html>): neue Eingabefelder für Suche, Status-Filter und Sortierung
- [frontend/js/app.js](<./frontend/js/app.js>): Suche nach primärem und sekundärem Bandnamen, Filter nach Status, Sortierung nach Bandname, letzter Änderung und Status
- [frontend/css/globals.css](<./frontend/css/globals.css>): Layout für die neuen Steuerelemente
- [BandService.java](<./backend/src/main/java/bandshirt/service/BandService.java>): passende Backend-Methoden für Suche, Filter und Sortierung
- [BandController.java](<./backend/src/main/java/bandshirt/controller/BandController.java>): Einstiegsmethode für kombinierte Suche/Filter/Sortierung
- [BandSortierung.java](<./backend/src/main/java/bandshirt/model/BandSortierung.java>): neue Enum-Klasse für erlaubte Sortierarten
- [database/schema.sql](<./database/schema.sql>): ausführlicher kommentiert

Ich habe den neu erstellten bzw. bearbeiteten Code ausführlich kommentiert, auch HTML, CSS, JavaScript und SQL. Der entfernte Button kommt im Projekt nicht mehr vor.

Hinweis zur Prüfung: Der Java-Compile-Check konnte diesmal nicht abgeschlossen werden, weil Laufwerk `C:` laut System `0` freien Speicher hat. Der Fehler kam beim Schreiben der `.class`-Dateien, nicht als Java-Syntaxfehler.

> Kleine Verbesserungen bitte;
> 1. es soll eine Export-Funktion geben, sodass man sich die die komplette Liste als xls/csv ausgeben lassen kann.
> 2. bei Ersteller soll automatisch der jeweilige Nutzername gesetzt werden (in dieses System soll man sich einloggen können).
> 3. ergänze auch noch die Detailansicht einer jeden Band, sodass man diese nach erstellen auch bearbeiten kann

<details><summary>7 previous messages</summary>

> Ich nehme die drei Verbesserungen als nächsten kleinen Ausbauschritt: Export im Frontend, ein einfacher Platzhalter für den eingeloggten Nutzernamen, und eine bearbeitbare Detailansicht. Ich lese kurz den aktuellen Stand, dann patch ich die betroffenen Dateien gezielt.
>
> Ich setze den Login-Aspekt bewusst als einfachen Prototyp um: oben gibt es einen „angemeldeten Nutzer“, und dieser Name wird automatisch als Ersteller bzw. Bearbeiter verwendet. So bleibt es verständlich und später leicht durch echte Anmeldung ersetzbar.
>
> <details><summary>Created a file, edited 4 files</summary>
>
> - Wrote `./backend/src/main/java/bandshirt/controller/BandController.java` (+18 -2)
> - Wrote `./backend/src/main/java/bandshirt/service/BandService.java` (+35 -1)
> - Wrote `./frontend/css/globals.css` (+32 -1)
> - Wrote `./frontend/index.html` (+19 -7)
> - Wrote `./frontend/js/app.js` (+539 -0)
>
> </details>
>
> Die Funktionen sind eingebaut; ich mache jetzt die üblichen Plausibilitätschecks: keine alten Button-Reste, keine kaputten Zeichen, JavaScript-Syntax. Beim Java-Compile kann der volle Datenträger weiterhin blockieren, deshalb prüfe ich das getrennt.
>
> Die Checks sehen gut aus: JavaScript-Syntax ist sauber, und die alten `Status weiter`-Reste sind weg. Ich ergänze noch kurz die README um die neuen Funktionen, damit die Dokumentation mit dem Code Schritt hält.
>
> <details><summary>Edited a file</summary>
>
> - Wrote `./README.md` (+7 -0)
>
> </details>
>
> Die README ist aktualisiert. Ich mache noch eine letzte kurze Suche nach kaputten Zeichen oder alten Funktionsnamen und prüfe dann nicht weiter in den vollen Datenträger hinein.
</details>

Erledigt. Ich habe die drei Verbesserungen eingebaut:

1. Export-Funktion:
In der Bandübersicht gibt es jetzt Buttons für `CSV exportieren` und `XLS exportieren`. Beide exportieren die komplette gespeicherte Bandliste, nicht nur die aktuell gefilterte Ansicht.

2. Automatischer Nutzername:
Der Ersteller wird nicht mehr manuell im Formular eingegeben. Oben im Header gibt es nun `Angemeldeter Nutzer`; dieser Name wird automatisch als `createdBy` und später beim Bearbeiten als `lastChangedBy` verwendet. Das ist ein einfacher Login-Platzhalter für den Prototyp.

3. Bearbeiten in der Detailansicht:
Jede ausgewählte Band kann jetzt rechts in der Detailansicht über `Band bearbeiten` geändert werden. Bearbeitbar sind Bandname, sekundäre Namen, Status, Quellen und Kommentar. Beim Speichern wird automatisch die letzte Änderung mit Zeitpunkt und Nutzer gesetzt.

Geändert wurden vor allem:
- [frontend/index.html](<./frontend/index.html>)
- [frontend/js/app.js](<./frontend/js/app.js>)
- [frontend/css/globals.css](<./frontend/css/globals.css>)
- [BandService.java](<./backend/src/main/java/bandshirt/service/BandService.java>)
- [BandController.java](<./backend/src/main/java/bandshirt/controller/BandController.java>)
- [README.md](<./README.md>)

Ich habe den neuen Code ausführlich kommentiert. `node --check frontend/js/app.js` läuft erfolgreich. Java konnte ich weiterhin nicht kompilieren, weil Laufwerk `C:` laut System `0` freien Speicher hat.

> Fülle bitte auch die Architektur.md, Module.md und Datenmodelle.md mit Inhalt

<details><summary>5 previous messages</summary>

> Ich fülle die drei Dokumentationsdateien passend zum aktuellen Stand: Architektur, Module und Datenmodell. Ich schaue kurz auf die vorhandenen Dateinamen, weil die Datei im Projekt vermutlich `05_Datenmodell.md` heißt statt `Datenmodelle.md`.
>
> Die Struktur wurde offenbar nochmal vereinfacht: Es gibt jetzt `02_Architektur.md`, `03_Module.md` und `04_Datenmodell.md`. Ich befülle genau diese drei Dateien, damit die Doku zur aktuellen Ordnerstruktur passt.
>
> Ich halte die neuen Inhalte bewusst einfach und erklaerbar: keine ueberladene Theorie, sondern genau die Architektur, die im Code schon sichtbar ist. Wegen der bisherigen Zeichencodierung schreibe ich robust mit `ae/oe/ue`, damit die Dateien nicht wieder kaputte Umlaute bekommen.
>
> <details><summary>Edited 3 files</summary>
>
> - Wrote `./docs/02_Architektur.md` (+115 -0)
> - Wrote `./docs/03_Module.md` (+174 -0)
> - Wrote `./docs/04_Datenmodell.md` (+212 -0)
>
> </details>
>
> Die drei Dokumente sind befüllt. Ich prüfe noch kurz, ob die Dateien Inhalt haben und ob versehentlich nicht-ASCII-Zeichen hineingerutscht sind.
</details>

Erledigt. Ich habe die drei vorhandenen Dateien befüllt:

- [02_Architektur.md](<./docs/02_Architektur.md>)
- [03_Module.md](<./docs/03_Module.md>)
- [04_Datenmodell.md](<./docs/04_Datenmodell.md>)

Ich habe die Inhalte bewusst leicht verständlich geschrieben: mit Architekturübersicht, Erklärung der Module, Datenfluss, Tabellenbeschreibung und Bezug zum aktuellen Code. Die Datei heißt im Projekt `04_Datenmodell.md`, deshalb habe ich diese statt einer neuen `Datenmodelle.md` gefüllt.

> Bitte passe das Projekt so an, dass es wirklich als verteilte Anwendung erkennbar ist.
>
> Eine normale Webanwendung mit Frontend, Backend und Datenbank reicht nicht aus.
> Die Anwendung soll aus mindestens zwei getrennten Prozessen bestehen, die miteinander kommunizieren.
>
> Idee:
> Die Bandshirt-Datenbank besitzt ein Backend zur Verwaltung von Bands. Zusätzlich soll es einen separaten Export-Service geben.
>
> Prozess 1: Backend/API
> - verwaltet Bands
> - speichert und liest Banddaten
> - nimmt eine Export-Anfrage entgegen
> - schreibt einen Export-Auftrag in eine einfache Job-Datei oder Job-Liste
>
> Prozess 2: Export-Service
> - läuft getrennt vom Backend
> - liest Export-Aufträge aus der Job-Datei oder Job-Liste
> - verarbeitet den Auftrag
> - erstellt eine CSV-Datei mit Bandname und Status
> - markiert den Auftrag anschließend als erledigt
>
> Bitte erkläre einfach und verständlich:
> - warum das jetzt eine verteilte Anwendung ist
> - welche Prozesse es gibt
> - welche Aufgabe jeder Prozess hat
> - wie die Prozesse miteinander kommunizieren
> - warum der Export-Service getrennt vom Backend ist
>
> Bitte halte die Umsetzung weiterhin bewusst einfach. Ich möchte den Code vollständig verstehen können.
>
> Bitte erpasse die Dokumentation in docs/02_Architektur.md und docs/03_Module.md entsprechend an.

<details><summary>12 previous messages</summary>

> Ich passe das jetzt als echten kleinen Zwei-Prozess-Entwurf an: Backend/API erzeugt Export-Jobs, ein separater Export-Service arbeitet diese Jobs ab. Ich lese kurz den aktuellen Backend-Stand, damit die Ergänzung sauber in die vorhandene einfache Struktur passt.
>
> Ich baue dafür eine sehr einfache Datei-Kommunikation ein: Das Backend schreibt Export-Aufträge in `shared/export-jobs.csv`, der getrennte Export-Service liest diese Datei und schreibt fertige CSVs nach `shared/exports/`. So sieht man zwei Prozesse ohne Message-Broker oder Framework-Ballast.
>
> <details><summary>Created 8 files, edited a file</summary>
>
> - Wrote `./backend/src/main/java/bandshirt/controller/BandController.java` (+13 -1)
> - Wrote `./backend/src/main/java/bandshirt/model/ExportJob.java` (+64 -0)
> - Wrote `./backend/src/main/java/bandshirt/model/ExportJobStatus.java` (+21 -0)
> - Wrote `./backend/src/main/java/bandshirt/repository/ExportJobFileRepository.java` (+78 -0)
> - Wrote `./backend/src/main/java/bandshirt/repository/ExportJobRepository.java` (+24 -0)
> - Wrote `./backend/src/main/java/bandshirt/service/ExportRequestService.java` (+62 -0)
> - Wrote `./export-service/src/main/java/bandshirt/exportservice/ExportServiceApplication.java` (+170 -0)
> - Wrote `./shared/export-jobs.csv` (+1 -0)
> - Wrote `./shared/exports/.gitkeep` (+1 -0)
>
> </details>
>
> Der Code-Teil für die zwei Prozesse ist angelegt: neue Backend-Klassen für Export-Aufträge und ein eigener `export-service`-Ordner mit separater Startklasse. Jetzt passe ich die Architektur- und Modul-Doku so an, dass genau diese Prozessaufteilung verständlich beschrieben ist.
>
> <details><summary>Created 2 files</summary>
>
> - Wrote `./docs/02_Architektur.md` (+152 -0)
> - Wrote `./docs/03_Module.md` (+276 -0)
>
> </details>
>
> Ich ergänze noch eine kleine Startklasse für den Backend/API-Prozess. Damit ist nicht nur der Export-Service separat startbar, sondern beide Prozesse sind im Code klar als eigene Programme erkennbar.
>
> <details><summary>Created a file</summary>
>
> - Wrote `./backend/src/main/java/bandshirt/BackendApiApplication.java` (+49 -0)
>
> </details>
>
> Die Backend-Startklasse ist drin. Ich aktualisiere die Modul-Doku noch an der passenden Stelle, damit `BackendApiApplication` als Prozess 1 ausdrücklich auftaucht.
>
> <details><summary>Edited a file</summary>
>
> - Wrote `./docs/03_Module.md` (+9 -0)
>
> </details>
>
> Die Struktur ist jetzt im Code und in der Doku sichtbar. Ich ergänze noch eine kurze README-Notiz, damit auch die Projektübersicht sofort zeigt, dass es jetzt zwei Prozesse gibt.
>
> <details><summary>Edited a file</summary>
>
> - Wrote `./README.md` (+12 -0)
>
> </details>
>
> Ich mache noch eine letzte Kontrolle auf Dateistruktur, relevante neue Klassen und Zeichencodierung. Einen Java-Compile lasse ich wegen `C:` mit 0 freiem Speicher nicht nochmal laufen, damit wir keinen neuen Build-Abfall erzeugen.
</details>

Erledigt. Das Projekt ist jetzt als einfache verteilte Anwendung erkennbar: Es gibt zwei getrennte Prozesse, die über eine gemeinsame Job-Datei kommunizieren.

Neu hinzugefügt:
- [BackendApiApplication.java](<./backend/src/main/java/bandshirt/BackendApiApplication.java>) als Prozess 1: Backend/API
- [ExportServiceApplication.java](<./export-service/src/main/java/bandshirt/exportservice/ExportServiceApplication.java>) als Prozess 2: Export-Service
- [ExportJob.java](<./backend/src/main/java/bandshirt/model/ExportJob.java>) und [ExportJobStatus.java](<./backend/src/main/java/bandshirt/model/ExportJobStatus.java>)
- [ExportRequestService.java](<./backend/src/main/java/bandshirt/service/ExportRequestService.java>)
- [ExportJobFileRepository.java](<./backend/src/main/java/bandshirt/repository/ExportJobFileRepository.java>)
- gemeinsamer Austauschbereich: [shared/export-jobs.csv](<./shared/export-jobs.csv>) und `shared/exports/`

Die Idee ist jetzt:

1. Das Backend verwaltet Bands.
2. Das Backend nimmt eine Export-Anfrage an.
3. Das Backend schreibt einen Export-Job in `shared/export-jobs.csv`.
4. Der separate Export-Service liest diese Datei.
5. Der Export-Service erzeugt eine CSV mit `Bandname;Status`.
6. Der Export-Service markiert den Job als `ERLEDIGT`.

Aktualisiert wurden außerdem:
- [docs/02_Architektur.md](<./docs/02_Architektur.md>)
- [docs/03_Module.md](<./docs/03_Module.md>)
- [README.md](<./README.md>)

Ich habe die Dokumentation ausdrücklich um die Punkte ergänzt, warum es jetzt verteilt ist, welche Prozesse existieren, welche Aufgaben sie haben, wie sie kommunizieren und warum der Export-Service getrennt ist.

Hinweis: Einen Java-Compile-Check habe ich nicht erneut ausgeführt, weil Laufwerk `C:` weiterhin `0` freien Speicher meldet. Die neuen Dateien wurden aber auf Struktur und Zeichencodierung geprüft.
