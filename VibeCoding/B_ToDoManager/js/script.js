/*
  ToDo-App mit LocalStorage

  Diese Datei enthält die gesamte JavaScript-Logik der ToDo-App.

  Die Aufgaben werden im LocalStorage des Browsers gespeichert.
  Dadurch bleiben sie auch dann erhalten, wenn die Seite neu geladen
  oder der Browser geschlossen und später wieder geöffnet wird.

  Wichtig:
  LocalStorage speichert Daten immer als Text.
  Deshalb werden die Aufgaben vor dem Speichern mit JSON.stringify()
  in Text umgewandelt und beim Laden mit JSON.parse() wieder in
  JavaScript-Objekte zurückverwandelt.
*/


// -----------------------------------------------------------------------------
// 1. GRUNDLEGENDE EINSTELLUNGEN
// -----------------------------------------------------------------------------

/*
  Dies ist der Name, unter dem die Aufgaben im LocalStorage gespeichert werden.

  Der LocalStorage funktioniert ungefähr wie ein kleines Schlüssel-Wert-Archiv:

  Schlüssel: "todo-manager-items"
  Wert:      alle gespeicherten Aufgaben als Text

  Über diesen Schlüssel kann die Anwendung die Aufgaben später wiederfinden.
*/
const STORAGE_KEY = "todo-manager-items";


// -----------------------------------------------------------------------------
// 2. ZUGRIFF AUF DIE HTML-ELEMENTE
// -----------------------------------------------------------------------------

/*
  Mit document.querySelector() suchen wir bestimmte Elemente in der HTML-Datei.

  Das Zeichen # bedeutet:
  Suche ein HTML-Element mit dieser ID.

  Beispiel:

  document.querySelector("#todo-form")

  sucht in der HTML-Datei nach:

  <form id="todo-form">

  Die gefundenen HTML-Elemente werden in Konstanten gespeichert.
  Dadurch können wir später mit JavaScript auf diese Elemente zugreifen.
*/


// Das Formular, über das eine neue Aufgabe angelegt wird.
const todoForm = document.querySelector("#todo-form");

// Das Eingabefeld für den Namen beziehungsweise Text der Aufgabe.
const todoInput = document.querySelector("#todo-input");

// Das Auswahlfeld für die Priorität: hoch, mittel oder niedrig.
const priorityInput = document.querySelector("#priority-input");

// Das Eingabefeld für das Fälligkeitsdatum.
const dueDateInput = document.querySelector("#due-date-input");

// Die Liste, in der alle Aufgaben auf der Seite angezeigt werden.
const todoList = document.querySelector("#todo-list");

// Das HTML-Element, das die Anzahl der offenen Aufgaben anzeigt.
const todoCounter = document.querySelector("#todo-counter");

// Das Auswahlfeld für die Sortierung der Aufgaben.
const sortSelect = document.querySelector("#sort-select");

// Das Auswahlfeld zum Filtern nach einer bestimmten Priorität.
const priorityFilter = document.querySelector("#priority-filter");

// Der Button, mit dem alle erledigten Aufgaben gelöscht werden können.
const clearDoneButton = document.querySelector("#clear-done-button");


// -----------------------------------------------------------------------------
// 3. PRIORITÄTEN
// -----------------------------------------------------------------------------

/*
  Für die Sortierung nach Priorität brauchen wir eine feste Reihenfolge.

  Eine hohe Priorität soll vor einer mittleren Priorität angezeigt werden.
  Eine mittlere Priorität soll vor einer niedrigen Priorität erscheinen.

  Deshalb bekommt jede Priorität eine Zahl:

  high   = 1
  medium = 2
  low    = 3

  Beim Sortieren gilt:
  Eine kleinere Zahl wird vor einer größeren Zahl angezeigt.
*/
const PRIORITY_ORDER = {
  high: 1,
  medium: 2,
  low: 3,
};

/*
  Intern werden englische Werte verwendet:

  high
  medium
  low

  In der Benutzeroberfläche sollen aber deutsche Begriffe angezeigt werden.

  Dieses Objekt ordnet jedem gespeicherten Wert eine deutsche Beschriftung zu.
*/
const PRIORITY_LABELS = {
  high: "Hoch",
  medium: "Mittel",
  low: "Niedrig",
};


// -----------------------------------------------------------------------------
// 4. ZENTRALE AUFGABENLISTE
// -----------------------------------------------------------------------------

/*
  In der Variable todos befinden sich alle Aufgaben der Anwendung.

  Beim Start der Seite wird die Funktion loadTodos() aufgerufen.
  Diese Funktion versucht, bereits gespeicherte Aufgaben aus dem
  LocalStorage zu laden.

  Falls noch keine Aufgaben gespeichert wurden, liefert loadTodos()
  eine leere Liste zurück.
*/
let todos = loadTodos();

/*
  Nachdem die Aufgaben geladen wurden, werden sie sofort auf der Seite angezeigt.

  Die Funktion renderTodos() erstellt für jede Aufgabe die passenden HTML-Elemente.
*/
renderTodos();


// -----------------------------------------------------------------------------
// 5. NEUE AUFGABE ANLEGEN
// -----------------------------------------------------------------------------

/*
  addEventListener() bedeutet:

  Warte darauf, dass bei einem HTML-Element ein bestimmtes Ereignis passiert.

  Hier warten wir auf das Ereignis "submit".
  Dieses Ereignis tritt auf, wenn das Formular abgeschickt wird,
  zum Beispiel durch einen Klick auf den Button "Aufgabe hinzufügen".
*/
todoForm.addEventListener("submit", function (event) {
  /*
    Normalerweise würde der Browser beim Absenden eines Formulars
    die Seite neu laden.

    event.preventDefault() verhindert dieses Standardverhalten.

    Dadurch können wir die Aufgabe mit JavaScript verarbeiten,
    ohne dass die Seite neu geladen wird.
  */
  event.preventDefault();

  /*
    todoInput.value enthält den aktuellen Inhalt des Texteingabefeldes.

    trim() entfernt Leerzeichen am Anfang und Ende.

    Beispiel:

    "   Einkaufen   "

    wird zu:

    "Einkaufen"

    Dadurch verhindern wir, dass eine Aufgabe nur aus Leerzeichen besteht.
  */
  const text = todoInput.value.trim();

  // Liest die ausgewählte Priorität aus dem entsprechenden Auswahlfeld.
  const priority = priorityInput.value;

  // Liest das ausgewählte Fälligkeitsdatum aus dem Datumsfeld.
  const dueDate = dueDateInput.value;

  /*
    Hier prüfen wir, ob alle notwendigen Angaben vorhanden sind.

    Das Zeichen || bedeutet "oder".

    Die Bedingung ist also erfüllt, wenn:

    - kein Aufgabentext eingegeben wurde
    oder
    - keine Priorität gewählt wurde
    oder
    - kein Fälligkeitsdatum angegeben wurde

    In diesem Fall wird keine Aufgabe gespeichert.
  */
  if (text === "" || priority === "" || dueDate === "") {
    /*
      focus() setzt den Mauszeiger wieder in das Texteingabefeld.

      Dadurch kann die Benutzerin oder der Benutzer direkt eine Eingabe machen.
    */
    todoInput.focus();

    /*
      return beendet die aktuelle Funktion sofort.

      Der darunterliegende Code wird dann nicht mehr ausgeführt.
    */
    return;
  }

  /*
    Hier wird eine neue Aufgabe als JavaScript-Objekt erstellt.

    Ein Objekt fasst mehrere zusammengehörige Informationen zusammen.

    Beispiel:

    {
      text: "Einkaufen",
      priority: "high",
      done: false
    }
  */
  const newTodo = {
    /*
      Jede Aufgabe bekommt eine eindeutige ID.

      Die ID wird benötigt, damit eine bestimmte Aufgabe später
      eindeutig gefunden und gelöscht werden kann.
    */
    id: createTodoId(),

    // Der eingegebene Aufgabentext.
    text: text,

    // Die ausgewählte Priorität.
    priority: priority,

    // Das ausgewählte Fälligkeitsdatum.
    dueDate: dueDate,

    /*
      Neue Aufgaben sind zunächst nicht erledigt.

      false bedeutet:
      Die Aufgabe ist noch offen.

      true würde bedeuten:
      Die Aufgabe ist erledigt.
    */
    done: false,

    /*
      Date.now() liefert die aktuelle Zeit als Zahl.

      Diese Zahl entspricht den vergangenen Millisekunden seit dem 1. Januar 1970.

      Sie wird hier gespeichert, damit später nachvollzogen werden kann,
      wann die Aufgabe erstellt wurde.

      Außerdem wird dieser Wert für die Sortierung nach Erstellungszeit verwendet.
    */
    createdAt: Date.now(),
  };

  /*
    push() fügt die neue Aufgabe am Ende der Aufgabenliste hinzu.

    Die Variable todos enthält danach die bereits vorhandenen Aufgaben
    plus die neue Aufgabe.
  */
  todos.push(newTodo);

  /*
    Nachdem sich die Aufgabenliste verändert hat,
    wird sie im LocalStorage gespeichert.
  */
  saveTodos();

  /*
    Danach wird die sichtbare Liste auf der Seite neu aufgebaut.

    So erscheint die neue Aufgabe direkt in der Benutzeroberfläche.
  */
  renderTodos();

  /*
    Nach dem Anlegen wird das Texteingabefeld geleert.
  */
  todoInput.value = "";

  /*
    Die Priorität wird wieder auf "mittel" gesetzt.

    Dadurch ist beim nächsten Anlegen einer Aufgabe bereits
    eine sinnvolle Standardpriorität ausgewählt.
  */
  priorityInput.value = "medium";

  // Das Datumsfeld wird ebenfalls geleert.
  dueDateInput.value = "";

  /*
    Der Mauszeiger wird wieder in das Texteingabefeld gesetzt.

    Dadurch kann direkt die nächste Aufgabe eingegeben werden.
  */
  todoInput.focus();
});


// -----------------------------------------------------------------------------
// 6. SORTIERUNG UND FILTER
// -----------------------------------------------------------------------------

/*
  Sobald im Auswahlfeld eine andere Sortierung gewählt wird,
  wird die Aufgabenliste neu dargestellt.

  Die Aufgaben selbst werden dadurch nicht verändert.
  Nur die Reihenfolge in der Anzeige ändert sich.
*/
sortSelect.addEventListener("change", function () {
  renderTodos();
});

/*
  Sobald eine andere Priorität im Filter ausgewählt wird,
  wird die Liste ebenfalls neu dargestellt.

  Nicht passende Aufgaben werden nur ausgeblendet.
  Sie werden dabei nicht gelöscht.
*/
priorityFilter.addEventListener("change", function () {
  renderTodos();
});


// -----------------------------------------------------------------------------
// 7. ALLE ERLEDIGTEN AUFGABEN LÖSCHEN
// -----------------------------------------------------------------------------

/*
  Dieser Code wird ausgeführt, wenn auf den Button
  "Alle erledigten Aufgaben löschen" geklickt wird.
*/
clearDoneButton.addEventListener("click", function () {
  /*
    filter() erstellt eine neue Liste.

    In dieser neuen Liste bleiben nur Aufgaben enthalten,
    für die die angegebene Bedingung true ergibt.

    Hier behalten wir nur Aufgaben, bei denen:

    todo.done === false

    gilt.

    Das bedeutet:
    Es bleiben nur offene Aufgaben erhalten.

    Alle erledigten Aufgaben werden aus der Liste entfernt.
  */
  todos = todos.filter(function (todo) {
    return todo.done === false;
  });

  // Die veränderte Aufgabenliste wird gespeichert.
  saveTodos();

  // Die sichtbare Liste wird aktualisiert.
  renderTodos();
});


// -----------------------------------------------------------------------------
// 8. AUFGABEN AUS DEM LOCALSTORAGE LADEN
// -----------------------------------------------------------------------------

function loadTodos() {
  /*
    localStorage.getItem() liest einen gespeicherten Wert aus.

    Als Suchbegriff wird STORAGE_KEY verwendet.

    LocalStorage gibt immer entweder:

    - einen Text
    oder
    - null

    zurück.

    null bedeutet:
    Unter diesem Schlüssel wurde noch nichts gespeichert.
  */
  const savedTodos = localStorage.getItem(STORAGE_KEY);

  /*
    Falls noch keine Aufgaben gespeichert wurden,
    geben wir eine leere Liste zurück.

    [] ist eine leere JavaScript-Liste.
  */
  if (savedTodos === null) {
    return [];
  }

  /*
    Hier bereiten wir eine leere Variable vor.

    In parsedTodos sollen später die aus dem Text
    zurückverwandelten Aufgaben gespeichert werden.
  */
  let parsedTodos = [];

  /*
    try und catch dienen zur Fehlerbehandlung.

    Der Code im try-Block wird ausprobiert.

    Falls dabei ein Fehler passiert,
    wird stattdessen der catch-Block ausgeführt.
  */
  try {
    /*
      JSON.parse() wandelt den gespeicherten Text
      wieder in JavaScript-Daten um.

      Aus einem Text wie:

      '[{"text":"Einkaufen","done":false}]'

      wird wieder eine echte JavaScript-Liste mit Objekten.
    */
    parsedTodos = JSON.parse(savedTodos);
  } catch (error) {
    /*
      Falls die gespeicherten Daten beschädigt oder ungültig sind,
      würde JSON.parse() einen Fehler auslösen.

      In diesem Fall startet die App sicherheitshalber
      mit einer leeren Aufgabenliste.
    */
    return [];
  }

  /*
    map() geht jede gespeicherte Aufgabe einzeln durch
    und erstellt daraus ein neues, vollständiges Aufgabenobjekt.

    Dieser Schritt ist vor allem wichtig, wenn ältere Aufgaben
    noch nicht alle heute verwendeten Eigenschaften besitzen.

    Beispiel:
    Eine alte Aufgabe hat vielleicht noch keine Priorität.
    Dann bekommt sie automatisch die Standardpriorität "medium".
  */
  return parsedTodos.map(function (todo) {
    return {
      /*
        Falls bereits eine ID vorhanden ist, wird sie übernommen.

        Falls keine ID vorhanden ist, wird eine neue erzeugt.

        Das Zeichen || bedeutet:
        Verwende den linken Wert, wenn er vorhanden ist.
        Andernfalls verwende den rechten Wert.
      */
      id: todo.id || createTodoId(),

      // Fehlender Aufgabentext wird durch einen leeren Text ersetzt.
      text: todo.text || "",

      // Fehlende Priorität wird automatisch auf "mittel" gesetzt.
      priority: todo.priority || "medium",

      // Fehlendes Datum wird durch einen leeren Wert ersetzt.
      dueDate: todo.dueDate || "",

      /*
        Boolean() wandelt einen Wert ausdrücklich in true oder false um.

        Dadurch ist sichergestellt, dass done immer ein richtiger
        Wahrheitswert ist.
      */
      done: Boolean(todo.done),

      /*
        Falls kein Erstellungszeitpunkt vorhanden ist,
        wird die aktuelle Zeit verwendet.
      */
      createdAt: todo.createdAt || Date.now(),
    };
  });
}


// -----------------------------------------------------------------------------
// 9. AUFGABEN IM LOCALSTORAGE SPEICHERN
// -----------------------------------------------------------------------------

function saveTodos() {
  /*
    LocalStorage kann keine JavaScript-Listen oder Objekte direkt speichern.

    Deshalb wird die Aufgabenliste mit JSON.stringify()
    in einen Text umgewandelt.

    Danach wird dieser Text unter dem STORAGE_KEY gespeichert.
  */
  localStorage.setItem(STORAGE_KEY, JSON.stringify(todos));
}


// -----------------------------------------------------------------------------
// 10. EINDEUTIGE ID FÜR EINE AUFGABE ERZEUGEN
// -----------------------------------------------------------------------------

function createTodoId() {
  /*
    Jede Aufgabe braucht eine möglichst eindeutige Kennung.

    Die ID besteht aus drei Teilen:

    1. dem Text "todo-"
    2. der aktuellen Zeit durch Date.now()
    3. einer zusätzlichen Zufallszahl durch Math.random()

    Beispiel:

    todo-1761234567890-a3f92c

    Die Wahrscheinlichkeit, dass zwei Aufgaben genau dieselbe ID bekommen,
    ist dadurch sehr gering.

    Diese Methode funktioniert auch dann, wenn die HTML-Datei
    direkt über file:// geöffnet wird und kein Server verwendet wird.
  */
  return `todo-${Date.now()}-${Math.random().toString(16).slice(2)}`;
}


// -----------------------------------------------------------------------------
// 11. AUFGABEN AUF DER SEITE ANZEIGEN
// -----------------------------------------------------------------------------

function renderTodos() {
  /*
    Bevor die Liste neu aufgebaut wird,
    wird der bisherige Inhalt vollständig entfernt.

    innerHTML = "" bedeutet:
    Lösche alle aktuell enthaltenen HTML-Elemente.
  */
  todoList.innerHTML = "";

  /*
    getVisibleTodos() liefert nur die Aufgaben zurück,
    die nach dem aktuell gewählten Filter sichtbar sein sollen.

    Außerdem befinden sich die Aufgaben bereits
    in der gewählten Sortierreihenfolge.
  */
  const visibleTodos = getVisibleTodos();

  /*
    forEach() führt den folgenden Code einmal für jede sichtbare Aufgabe aus.

    Für jede Aufgabe werden neue HTML-Elemente erzeugt.
  */
  visibleTodos.forEach(function (todo) {
    /*
      document.createElement() erstellt ein neues HTML-Element.

      Diese Elemente existieren zunächst nur im JavaScript.
      Erst später werden sie in die sichtbare HTML-Seite eingefügt.
    */

    // Äußerer Listeneintrag für eine einzelne Aufgabe.
    const listItem = document.createElement("li");

    // Checkbox zum Markieren als erledigt.
    const checkbox = document.createElement("input");

    // Container für Aufgabentext und Zusatzinformationen.
    const content = document.createElement("div");

    // Element für den eigentlichen Aufgabentext.
    const text = document.createElement("span");

    // Container für Priorität und Fälligkeitsdatum.
    const meta = document.createElement("div");

    // Kleine Anzeige für die Priorität.
    const priorityBadge = document.createElement("span");

    // HTML-Zeitelement für das Fälligkeitsdatum.
    const dueDate = document.createElement("time");

    // Button zum Löschen der Aufgabe.
    const deleteButton = document.createElement("button");

    /*
      Den erstellten Elementen werden CSS-Klassen zugewiesen.

      Diese Klassen werden in der CSS-Datei verwendet,
      um das Aussehen der Elemente festzulegen.
    */

    /*
      Die Priorität wird direkt in die CSS-Klasse eingebaut.

      Bei hoher Priorität entsteht zum Beispiel:

      todo-item todo-item--high

      Dadurch kann jede Priorität unterschiedlich gestaltet werden.
    */
    listItem.className = `todo-item todo-item--${todo.priority}`;

    content.className = "todo-item__content";
    text.className = "todo-item__text";
    meta.className = "todo-item__meta";
    priorityBadge.className = "todo-item__priority";
    dueDate.className = "todo-item__date";

    /*
      Wenn die Aufgabe erledigt ist, erhält sie eine zusätzliche CSS-Klasse.

      Diese Klasse kann zum Beispiel dafür sorgen, dass:

      - der Text durchgestrichen wird,
      - die Aufgabe blasser angezeigt wird,
      - oder der Hintergrund verändert wird.
    */
    if (todo.done) {
      listItem.classList.add("todo-item--done");
    }

    /*
      Die Art des Eingabeelements wird auf Checkbox gesetzt.

      Dadurch erscheint ein anklickbares Kontrollkästchen.
    */
    checkbox.type = "checkbox";

    /*
      checked bestimmt, ob die Checkbox angehakt ist.

      Ist todo.done true, ist auch die Checkbox angehakt.
      Ist todo.done false, ist die Checkbox nicht angehakt.
    */
    checkbox.checked = todo.done;

    /*
      aria-label verbessert die Barrierefreiheit.

      Bildschirmleseprogramme können dadurch erklären,
      welche Aufgabe mit dieser Checkbox verbunden ist.
    */
    checkbox.setAttribute(
      "aria-label",
      `Aufgabe "${todo.text}" erledigen`
    );

    /*
      textContent fügt normalen Text in ein HTML-Element ein.

      Hier wird der Aufgabentext angezeigt.
    */
    text.textContent = todo.text;

    /*
      Die interne Priorität wird über PRIORITY_LABELS
      in einen deutschen Text umgewandelt.

      Aus "high" wird beispielsweise "Hoch".
    */
    priorityBadge.textContent =
      `Prio: ${PRIORITY_LABELS[todo.priority]}`;

    /*
      dateTime enthält das Datum in einem maschinenlesbaren Format.

      Das ist für Browser und Hilfsprogramme sinnvoll.
    */
    dueDate.dateTime = todo.dueDate;

    /*
      Für die sichtbare Anzeige wird das Datum
      über formatDate() ins deutsche Format umgewandelt.
    */
    dueDate.textContent =
      `Fällig: ${formatDate(todo.dueDate)}`;

    // Der Button soll das Formular nicht versehentlich abschicken.
    deleteButton.type = "button";

    // CSS-Klasse für das Aussehen des Löschen-Buttons.
    deleteButton.className = "todo-item__delete";

    // Sichtbare Beschriftung des Buttons.
    deleteButton.textContent = "Löschen";

    /*
      Diese Funktion wird ausgeführt,
      wenn der Zustand der Checkbox verändert wird.

      Das geschieht beispielsweise,
      wenn eine offene Aufgabe als erledigt markiert wird.
    */
    checkbox.addEventListener("change", function () {
      /*
        Der Erledigt-Status der Aufgabe wird an den Zustand
        der Checkbox angepasst.

        checkbox.checked ist entweder true oder false.
      */
      todo.done = checkbox.checked;

      // Die Änderung wird im LocalStorage gespeichert.
      saveTodos();

      /*
        Die Liste wird neu aufgebaut,
        damit die CSS-Darstellung sofort aktualisiert wird.
      */
      renderTodos();
    });

    /*
      Diese Funktion wird ausgeführt,
      wenn der Löschen-Button dieser Aufgabe angeklickt wird.
    */
    deleteButton.addEventListener("click", function () {
      /*
        Mit filter() erstellen wir eine neue Aufgabenliste.

        Es bleiben nur Aufgaben übrig,
        deren ID nicht der ID der zu löschenden Aufgabe entspricht.

        Dadurch wird genau diese eine Aufgabe entfernt.
      */
      todos = todos.filter(function (currentTodo) {
        return currentTodo.id !== todo.id;
      });

      // Die neue Aufgabenliste wird gespeichert.
      saveTodos();

      // Die sichtbare Liste wird aktualisiert.
      renderTodos();
    });

    /*
      Jetzt werden die einzelnen HTML-Elemente zusammengesetzt.

      Zuerst kommen Priorität und Datum in den Meta-Bereich.
    */
    meta.append(priorityBadge, dueDate);

    /*
      Danach kommen Aufgabentext und Meta-Bereich
      in den Inhaltscontainer.
    */
    content.append(text, meta);

    /*
      Der vollständige Listeneintrag besteht aus:

      - Checkbox
      - Inhaltsbereich
      - Löschen-Button
    */
    listItem.append(checkbox, content, deleteButton);

    /*
      Zum Schluss wird der fertige Listeneintrag
      in die Aufgabenliste der HTML-Seite eingefügt.
    */
    todoList.append(listItem);
  });

  /*
    Nachdem alle Aufgaben angezeigt wurden,
    wird die Zahl der offenen Aufgaben aktualisiert.
  */
  updateCounter();
}


// -----------------------------------------------------------------------------
// 12. AUFGABEN FILTERN UND SORTIEREN
// -----------------------------------------------------------------------------

function getVisibleTodos() {
  /*
    Hier wird gelesen, welche Priorität im Filter ausgewählt wurde.

    Mögliche Werte könnten zum Beispiel sein:

    all
    high
    medium
    low
  */
  const selectedPriority = priorityFilter.value;

  /*
    Hier wird gelesen, welche Sortierung ausgewählt wurde.

    Mögliche Werte könnten zum Beispiel sein:

    createdAt
    dueDate
    priority
  */
  const selectedSort = sortSelect.value;

  /*
    Zuerst werden die Aufgaben gefiltert.

    Eine Aufgabe bleibt sichtbar, wenn:

    - im Filter "all" gewählt wurde
    oder
    - ihre Priorität genau der gewählten Priorität entspricht
  */
  const filteredTodos = todos.filter(function (todo) {
    return (
      selectedPriority === "all" ||
      todo.priority === selectedPriority
    );
  });

  /*
    slice() erstellt zunächst eine Kopie der gefilterten Liste.

    Das ist sinnvoll, weil sort() die ursprüngliche Liste verändern würde.

    Durch die Kopie bleibt die eigentliche Aufgabenliste todos
    in ihrer bisherigen Reihenfolge erhalten.
  */
  return filteredTodos.slice().sort(function (firstTodo, secondTodo) {
    /*
      Falls nach Fälligkeitsdatum sortiert werden soll,
      wird dieser Abschnitt ausgeführt.
    */
    if (selectedSort === "dueDate") {
      /*
        Aufgaben ohne Fälligkeitsdatum sollen am Ende erscheinen.

        Wenn die erste Aufgabe kein Datum besitzt,
        wird 1 zurückgegeben.

        Dadurch wird sie hinter die zweite Aufgabe sortiert.
      */
      if (firstTodo.dueDate === "") {
        return 1;
      }

      /*
        Wenn nur die zweite Aufgabe kein Datum besitzt,
        wird -1 zurückgegeben.

        Dadurch wird die erste Aufgabe vor der zweiten angezeigt.
      */
      if (secondTodo.dueDate === "") {
        return -1;
      }

      /*
        Die Datumswerte besitzen das Format:

        YYYY-MM-DD

        Beispiel:

        2026-05-10

        Dieses Format kann direkt als Text verglichen werden.

        localeCompare() liefert:

        - eine negative Zahl, wenn der erste Wert vorher kommt
        - eine positive Zahl, wenn der zweite Wert vorher kommt
        - 0, wenn beide gleich sind
      */
      return firstTodo.dueDate.localeCompare(secondTodo.dueDate);
    }

    /*
      Falls nach Priorität sortiert werden soll,
      wird die vorher definierte Zahlenreihenfolge verwendet.

      Beispiel:

      high   = 1
      medium = 2
      low    = 3

      Eine kleinere Zahl wird zuerst angezeigt.
    */
    if (selectedSort === "priority") {
      return (
        PRIORITY_ORDER[firstTodo.priority] -
        PRIORITY_ORDER[secondTodo.priority]
      );
    }

    /*
      Falls keine der vorherigen Sortierungen gewählt wurde,
      wird nach Erstellungszeit sortiert.

      Neuere Aufgaben besitzen einen größeren createdAt-Wert.

      Deshalb wird der Wert der zweiten Aufgabe
      vom Wert der ersten Aufgabe abgezogen.

      Dadurch erscheinen neuere Aufgaben zuerst.
    */
    return secondTodo.createdAt - firstTodo.createdAt;
  });
}


// -----------------------------------------------------------------------------
// 13. DATUM FÜR DIE ANZEIGE FORMATIEREN
// -----------------------------------------------------------------------------

function formatDate(dateValue) {
  /*
    Falls kein Datum vorhanden ist,
    wird ein verständlicher Platzhalter angezeigt.
  */
  if (dateValue === "") {
    return "nicht gesetzt";
  }

  /*
    Das Datumsfeld liefert normalerweise ein Datum im ISO-Format:

    YYYY-MM-DD

    Beispiel:

    2026-05-10

    Durch "T00:00:00" wird eine vollständige Zeitangabe ergänzt.

    Danach wandelt toLocaleDateString("de-DE")
    das Datum in das deutsche Anzeigeformat um.

    Aus:

    2026-05-10

    wird:

    10.05.2026
  */
  return new Date(
    `${dateValue}T00:00:00`
  ).toLocaleDateString("de-DE");
}


// -----------------------------------------------------------------------------
// 14. ANZAHL DER OFFENEN AUFGABEN AKTUALISIEREN
// -----------------------------------------------------------------------------

function updateCounter() {
  /*
    Zuerst werden nur die offenen Aufgaben herausgefiltert.

    Offen bedeutet:

    todo.done === false
  */
  const openTodos = todos.filter(function (todo) {
    return todo.done === false;
  }).length;

  /*
    .length liefert die Anzahl der Elemente in der gefilterten Liste.

    Beispiel:

    Wenn drei Aufgaben offen sind,
    enthält openTodos den Wert 3.
  */

  /*
    Für genau eine Aufgabe wird die Einzahl verwendet:

    "1 offene Aufgabe"

    Dadurch ist die Anzeige grammatikalisch korrekt.
  */
  if (openTodos === 1) {
    todoCounter.textContent = "1 offene Aufgabe";
  } else {
    /*
      Bei null oder mehreren offenen Aufgaben
      wird die Mehrzahl verwendet.

      Der aktuelle Zahlenwert wird mit ${openTodos}
      in den Text eingesetzt.
    */
    todoCounter.textContent =
      `${openTodos} offene Aufgaben`;
  }
}