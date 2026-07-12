// Diese Datei enthaelt die einfache Frontend-Logik der Bandshirt-Datenbank.
//
// Wichtig für das Studienprojekt:
// Das Frontend arbeitet in diesem ersten Schritt noch ohne echtes Backend.
// Die Daten werden nur in einem JavaScript-Array gespeichert. Dadurch kann man
// die Bedienung verstehen, bevor spaeter REST-Aufrufe zum Java-Backend
// eingebaut werden.

// Dieses Objekt übersetzt die technischen Statuswerte in gut lesbare Texte.
// Der technische Wert "GRUEN" passt zum Java-Enum ResearchStatus im Backend.
const statusTexte = {
  GRUEN: "gruen",
  GELB: "gelb",
  ROT: "rot",
  UNRECHERCHIERT: "unrecherchiert"
};

// Diese Reihenfolge nutzen wir beim Sortieren nach Status. So ist die Anzeige
// nachvollziehbar und nicht zufällig alphabetisch.
const statusReihenfolge = ["GRUEN", "GELB", "ROT", "UNRECHERCHIERT"];

// bands ist unsere vorläufige "Datenquelle" im Browser.
// Später würde diese Liste nicht mehr direkt hier stehen, sondern über eine
// REST-API aus dem Backend geladen werden.
const bands = [
  {
    id: 1,
    primaryName: "Beispielband",
    secondaryNames: ["Demo Name", "Testprojekt"],
    researchStatus: "UNRECHERCHIERT",
    sources: ["https://example.org"],
    comment: "Erster Beispieldatensatz fuer die Oberfläche.",
    createdBy: "System",
    lastChangedAt: new Date(),
    lastChangedBy: "System"
  }
];

// selectedBandId merkt sich, welche Band rechts in der Detailansicht angezeigt
// werden soll.
let selectedBandId = 1;

// editMode merkt sich, ob die Detailansicht gerade als Formular angezeigt wird.
let editMode = false;

// nextId ist ein einfacher Zähler für neue Bands. In einer echten Datenbank
// würde die ID normalerweise von der Datenbank erzeugt.
let nextId = 2;

// Hier werden die HTML-Elemente gesucht, mit denen JavaScript arbeiten muss.
// querySelector gibt jeweils das erste passende Element aus der HTML-Datei
// zurück.
const form = document.querySelector("#band-form");
const bandList = document.querySelector("#band-list");
const bandDetail = document.querySelector("#band-detail");
const bandCount = document.querySelector("#band-count");
const searchInput = document.querySelector("#search-input");
const statusFilter = document.querySelector("#status-filter");
const sortSelect = document.querySelector("#sort-select");
const currentUserInput = document.querySelector("#current-user");
const exportCsvButton = document.querySelector("#export-csv");
const exportXlsButton = document.querySelector("#export-xls");

// Wenn das Formular abgeschickt wird, legen wir eine neue Band im Array an.
// event.preventDefault verhindert das normale Neuladen der Seite.
form.addEventListener("submit", (event) => {
  event.preventDefault();

  // FormData liest die Werte aus allen Formularfeldern mit name-Attribut.
  const formData = new FormData(form);
  const currentUser = getCurrentUser();

  // Dieses Objekt hat bewusst ähnliche Felder wie die Java-Klasse Band.
  // So erkennt man leicht, wie Frontend und Backend später zusammenpassen.
  // createdBy und lastChangedBy werden automatisch aus dem aktuellen Nutzer
  // gesetzt. Genau das wuerde später ein echtes Login-System liefern.
  const band = {
    id: nextId,
    primaryName: formData.get("primaryName").trim(),
    secondaryNames: splitCommaList(formData.get("secondaryNames")),
    researchStatus: formData.get("researchStatus"),
    sources: splitCommaList(formData.get("sources")),
    comment: formData.get("comment").trim(),
    createdBy: currentUser,
    lastChangedAt: new Date(),
    lastChangedBy: currentUser
  };

  nextId += 1;
  bands.push(band);
  selectedBandId = band.id;
  editMode = false;
  form.reset();
  render();
});

// Suche, Filter und Sortierung sollen sofort reagieren, wenn sich ein Feld
// ändert. Darum rufen alle drei Steuerelemente wieder render() auf.
searchInput.addEventListener("input", render);
statusFilter.addEventListener("change", render);
sortSelect.addEventListener("change", render);

// Die Export-Buttons erzeugen Dateien direkt im Browser.
// CSV ist eine Textdatei fuer Tabellenprogramme.
// XLS wird hier als einfache HTML-Tabelle mit .xls-Endung erzeugt, die Excel
// und viele andere Tabellenprogramme öffnen können.
exportCsvButton.addEventListener("click", exportBandsAsCsv);
exportXlsButton.addEventListener("click", exportBandsAsXls);

// Die Detailansicht besitzt eigene Buttons und ein eigenes Bearbeitungsformular.
// Deshalb nutzen wir auch hier Event Delegation auf dem Container.
bandDetail.addEventListener("click", (event) => {
  const button = event.target.closest("button");

  if (!button) {
    return;
  }

  if (button.dataset.action === "edit-detail") {
    editMode = true;
    renderBandDetail();
  }

  if (button.dataset.action === "cancel-edit") {
    editMode = false;
    renderBandDetail();
  }
});

// submit wird fuer das Bearbeitungsformular in der Detailansicht gebraucht.
bandDetail.addEventListener("submit", (event) => {
  if (!event.target.matches("#detail-edit-form")) {
    return;
  }

  event.preventDefault();
  saveDetailEdit(new FormData(event.target));
});

// Der aktuelle Nutzer kommt im Prototyp aus einem Eingabefeld.
// Wenn dort nichts steht, verwenden wir einen neutralen Ersatznamen.
function getCurrentUser() {
  const userName = currentUserInput.value.trim();
  return userName.length > 0 ? userName : "Unbekannter Nutzer";
}

// Aus einem Text wie "Name 1, Name 2" wird eine Liste ["Name 1", "Name 2"].
// Leere Einträge werden entfernt, damit später keine unsauberen Daten
// angezeigt werden.
function splitCommaList(value) {
  return value
    .split(",")
    .map((entry) => entry.trim())
    .filter((entry) => entry.length > 0);
}

// render ist die zentrale Zeichenfunktion fuer die Oberfläche.
// Sie aktualisiert Liste, Detailansicht und Zähler.
function render() {
  const visibleBands = getVisibleBands();

  renderBandList(visibleBands);
  renderBandDetail();

  // Der Zähler zeigt sowohl die sichtbaren Treffer als auch die Gesamtzahl.
  // Das ist hilfreich, wenn Suche oder Filter aktiv sind.
  bandCount.textContent = `${visibleBands.length} von ${bands.length} Bands`;
}

// Diese Funktion kombiniert Suche, Filter und Sortierung.
// Wichtig: Das originale bands-Array wird nicht verändert. Wir erzeugen eine
// neue Ergebnisliste und sortieren nur diese Anzeige-Liste.
function getVisibleBands() {
  const searchTerm = searchInput.value.trim().toLowerCase();
  const selectedStatus = statusFilter.value;
  const selectedSort = sortSelect.value;

  let result = bands.filter((band) => matchesSearch(band, searchTerm));

  if (selectedStatus !== "ALLE") {
    result = result.filter((band) => band.researchStatus === selectedStatus);
  }

  return sortBands(result, selectedSort);
}

// Eine Band passt zur Suche, wenn der Suchtext im primären Namen oder in einem
// sekundären Namen vorkommt. Gross- und Kleinschreibung wird ignoriert.
function matchesSearch(band, searchTerm) {
  if (searchTerm.length === 0) {
    return true;
  }

  const primaryNameMatches = band.primaryName.toLowerCase().includes(searchTerm);
  const secondaryNameMatches = band.secondaryNames.some((name) =>
    name.toLowerCase().includes(searchTerm)
  );

  return primaryNameMatches || secondaryNameMatches;
}

// Diese Funktion sortiert die angezeigte Bandliste nach der Auswahl im Dropdown.
function sortBands(bandsToSort, selectedSort) {
  const sortedBands = [...bandsToSort];

  if (selectedSort === "LETZTE_AENDERUNG") {
    sortedBands.sort((a, b) => b.lastChangedAt - a.lastChangedAt);
  } else if (selectedSort === "STATUS") {
    sortedBands.sort((a, b) =>
      statusReihenfolge.indexOf(a.researchStatus) - statusReihenfolge.indexOf(b.researchStatus)
    );
  } else {
    sortedBands.sort((a, b) => a.primaryName.localeCompare(b.primaryName, "de"));
  }

  return sortedBands;
}

// Die Bandliste wird bei jedem render neu aufgebaut.
// Das ist fuer diesen kleinen Prototyp einfacher zu verstehen als einzelne
// HTML-Elemente aufwendig zu aktualisieren.
function renderBandList(visibleBands) {
  bandList.innerHTML = "";

  if (visibleBands.length === 0) {
    bandList.innerHTML = `<p class="empty-state">Keine passende Band gefunden.</p>`;
    return;
  }

  visibleBands.forEach((band) => {
    const article = document.createElement("article");
    article.className = "band-card";

    // innerHTML ist hier uebersichtlich. Werte aus Formularfeldern werden vor
    // dem Einsetzen mit escapeHtml entschaerft, damit kein HTML eingeschleust
    // wird.
    article.innerHTML = `
      <div class="band-card-header">
        <h3>${escapeHtml(band.primaryName)}</h3>
        <span class="status status-${band.researchStatus}">${statusTexte[band.researchStatus]}</span>
      </div>
      <p>${escapeHtml(band.comment || "Noch kein Kommentar vorhanden.")}</p>
      <div class="meta-line">Letzte Aenderung: ${formatDate(band.lastChangedAt)}</div>
      <div class="card-actions">
        <button type="button" data-action="show" data-id="${band.id}">Anzeigen</button>
        <button type="button" class="delete-button" data-action="delete" data-id="${band.id}">Löschen</button>
      </div>
    `;

    bandList.appendChild(article);
  });
}

// Event Delegation:
// Wir haengen nur einen click-Listener an die ganze Liste. Wenn ein Button in
// einer Bandkarte geklickt wurde, lesen wir data-action und data-id aus.
bandList.addEventListener("click", (event) => {
  const button = event.target.closest("button");

  if (!button) {
    return;
  }

  const id = Number(button.dataset.id);
  const action = button.dataset.action;

  if (action === "show") {
    selectedBandId = id;
    editMode = false;
  }

  if (action === "delete") {
    deleteBand(id);
  }

  render();
});

// Löscht eine Band aus dem Array. Spaeter waere das ein DELETE-Aufruf an das
// Backend, zum Beispiel DELETE /bands/{id}.
function deleteBand(id) {
  const index = bands.findIndex((band) => band.id === id);

  if (index >= 0) {
    bands.splice(index, 1);
  }

  selectedBandId = bands.length > 0 ? bands[0].id : null;
  editMode = false;
}

// Die Detailansicht zeigt entweder die gespeicherten Informationen einer Band
// oder ein Bearbeitungsformular fuer genau diese Band.
function renderBandDetail() {
  const band = bands.find((entry) => entry.id === selectedBandId);

  if (!band) {
    bandDetail.className = "band-detail empty-state";
    bandDetail.textContent = "Noch keine Band ausgewaehlt.";
    return;
  }

  bandDetail.className = "band-detail";

  if (editMode) {
    renderBandEditForm(band);
  } else {
    renderBandReadView(band);
  }
}

// Normale Leseansicht für die Details einer Band.
function renderBandReadView(band) {
  bandDetail.innerHTML = `
    <dl>
      <dt>Primaerer Name</dt>
      <dd>${escapeHtml(band.primaryName)}</dd>
      <dt>Sekundaere Namen</dt>
      <dd>${escapeHtml(formatList(band.secondaryNames))}</dd>
      <dt>Status</dt>
      <dd>${statusTexte[band.researchStatus]}</dd>
      <dt>Qüllen</dt>
      <dd>${escapeHtml(formatList(band.sources))}</dd>
      <dt>Kommentar</dt>
      <dd>${escapeHtml(band.comment || "-")}</dd>
      <dt>Erstellt von</dt>
      <dd>${escapeHtml(band.createdBy)}</dd>
      <dt>Letzte Aenderung</dt>
      <dd>${formatDate(band.lastChangedAt)} durch ${escapeHtml(band.lastChangedBy)}</dd>
    </dl>
    <div class="detail-actions">
      <button type="button" data-action="edit-detail">Band bearbeiten</button>
    </div>
  `;
}

// Bearbeitungsformular für die Detailansicht.
// Die Felder werden mit den aktuellen Werten der ausgewaehlten Band gefüllt.
function renderBandEditForm(band) {
  bandDetail.innerHTML = `
    <form id="detail-edit-form" class="edit-form">
      <label>
        Primaerer Bandname
        <input name="primaryName" type="text" required value="${escapeAttribute(band.primaryName)}">
      </label>

      <label>
        Sekundaere Bandnamen
        <input name="secondaryNames" type="text" value="${escapeAttribute(band.secondaryNames.join(", "))}">
      </label>

      <label>
        Recherche-Status
        <select name="researchStatus">
          <option value="UNRECHERCHIERT" ${selectedIf(band.researchStatus, "UNRECHERCHIERT")}>unrecherchiert</option>
          <option value="GRUEN" ${selectedIf(band.researchStatus, "GRUEN")}>grün</option>
          <option value="GELB" ${selectedIf(band.researchStatus, "GELB")}>gelb</option>
          <option value="ROT" ${selectedIf(band.researchStatus, "ROT")}>rot</option>
        </select>
      </label>

      <label>
        Quellen / Links
        <input name="sources" type="text" value="${escapeAttribute(band.sources.join(", "))}">
      </label>

      <label>
        Kommentar
        <textarea name="comment" rows="4">${escapeHtml(band.comment)}</textarea>
      </label>

      <div class="meta-line">
        Bearbeiter wird automatisch gesetzt: ${escapeHtml(getCurrentUser())}
      </div>

      <div class="detail-actions">
        <button type="submit">Aenderungen speichern</button>
        <button class="secondary-button" type="button" data-action="cancel-edit">Abbrechen</button>
      </div>
    </form>
  `;
}

// Speichert die Werte aus dem Detailformular zurück in das Band-Objekt.
// Danach wird die letzte Aenderung automatisch mit Nutzer und Zeitpunkt gesetzt.
function saveDetailEdit(formData) {
  const band = bands.find((entry) => entry.id === selectedBandId);

  if (!band) {
    return;
  }

  band.primaryName = formData.get("primaryName").trim();
  band.secondaryNames = splitCommaList(formData.get("secondaryNames"));
  band.researchStatus = formData.get("researchStatus");
  band.sources = splitCommaList(formData.get("sources"));
  band.comment = formData.get("comment").trim();
  band.lastChangedAt = new Date();
  band.lastChangedBy = getCurrentUser();

  editMode = false;
  render();
}

// Exportiert die komplette Bandliste als CSV-Datei.
// CSV steht für "Comma-Separated Values"; hier verwenden wir Semikolon als
// Trennzeichen, weil deutsche Tabellenprogramme damit oft besser umgehen.
function exportBandsAsCsv() {
  const rows = [
    getExportHeader(),
    ...bands.map((band) => getExportRow(band))
  ];

  const csvContent = rows
    .map((row) => row.map(escapeCsvCell).join(";"))
    .join("\n");

  downloadFile("bandshirt-datenbank.csv", "text/csv;charset=utf-8", csvContent);
}

// Exportiert die komplette Bandliste als einfache XLS-Datei.
// Technisch ist das eine HTML-Tabelle mit .xls-Endung. Für ein Studienprojekt
// ist das gut nachvollziehbar und ohne externe Bibliothek möglich.
function exportBandsAsXls() {
  const headerCells = getExportHeader()
    .map((cell) => `<th>${escapeHtml(cell)}</th>`)
    .join("");

  const bodyRows = bands
    .map((band) => {
      const cells = getExportRow(band)
        .map((cell) => `<td>${escapeHtml(cell)}</td>`)
        .join("");
      return `<tr>${cells}</tr>`;
    })
    .join("");

  const xlsContent = `
    <html>
      <head><meta charset="UTF-8"></head>
      <body>
        <table>
          <thead><tr>${headerCells}</tr></thead>
          <tbody>${bodyRows}</tbody>
        </table>
      </body>
    </html>
  `;

  downloadFile("bandshirt-datenbank.xls", "application/vnd.ms-excel;charset=utf-8", xlsContent);
}

// Kopfzeile für beide Exportformate.
function getExportHeader() {
  return [
    "ID",
    "Primaerer Bandname",
    "Sekundaere Bandnamen",
    "Recherche-Status",
    "Quellen",
    "Kommentar",
    "Erstellt von",
    "Letzte Änderung am",
    "Letzte Änderung von"
  ];
}

// Datenzeile für beide Exportformate.
function getExportRow(band) {
  return [
    String(band.id),
    band.primaryName,
    band.secondaryNames.join(", "),
    statusTexte[band.researchStatus],
    band.sources.join(", "),
    band.comment,
    band.createdBy,
    formatDate(band.lastChangedAt),
    band.lastChangedBy
  ];
}

// Erzeugt im Browser eine Datei und startet den Download.
function downloadFile(fileName, mimeType, content) {
  const blob = new Blob([content], { type: mimeType });
  const url = URL.createObjectURL(blob);
  const link = document.createElement("a");

  link.href = url;
  link.download = fileName;
  link.click();

  URL.revokeObjectURL(url);
}

// Eine CSV-Zelle muss in Anführungszeichen stehen, wenn sie Sonderzeichen wie
// Semikolon, Zeilenumbrüche oder Anführungszeichen enthaelt.
function escapeCsvCell(value) {
  const text = String(value ?? "");
  const escapedText = text.replaceAll('"', '""');
  return `"${escapedText}"`;
}

// Macht aus einer Liste einen gut lesbaren Text.
function formatList(values) {
  return values.length > 0 ? values.join(", ") : "-";
}

// Formatiert ein Date-Objekt für die deutsche Anzeige.
function formatDate(value) {
  return value.toLocaleString("de-DE");
}

// Hilfsfunktion für option-Elemente im Bearbeitungsformular.
function selectedIf(currentValue, optionValue) {
  return currentValue === optionValue ? "selected" : "";
}

// Diese Funktion schützt Text, der in HTML eingefügt wird.
// Beispiel: Aus <script> wird harmloser Text, der nur angezeigt und nicht
// ausgeführt wird.
function escapeHtml(value) {
  return String(value ?? "")
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}

// Diese Funktion schützt Werte, die in HTML-Attribute geschrieben werden.
// Sie verwendet dieselbe Ersetzung wie escapeHtml, ist aber als eigener Name
// leichter zu verstehen, wenn man den Formular-Code liest.
function escapeAttribute(value) {
  return escapeHtml(value);
}

// Initialer Aufruf: Beim Laden der Seite wird die erste Ansicht gezeichnet.
render();
