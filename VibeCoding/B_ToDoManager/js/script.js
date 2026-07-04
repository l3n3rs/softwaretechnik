/*
  ToDo-App mit LocalStorage.
  Alle Aufgaben werden im Browser gespeichert und bleiben nach dem Neuladen erhalten.
*/

// Schlüsselname, unter dem die Aufgaben im LocalStorage abgelegt werden.
const STORAGE_KEY = "todo-manager-items";

// Zugriff auf wichtige HTML-Elemente.
const todoForm = document.querySelector("#todo-form");
const todoInput = document.querySelector("#todo-input");
const priorityInput = document.querySelector("#priority-input");
const dueDateInput = document.querySelector("#due-date-input");
const todoList = document.querySelector("#todo-list");
const todoCounter = document.querySelector("#todo-counter");
const sortSelect = document.querySelector("#sort-select");
const priorityFilter = document.querySelector("#priority-filter");
const clearDoneButton = document.querySelector("#clear-done-button");

// Reihenfolge der Prioritäten für die Sortierung.
const PRIORITY_ORDER = {
  high: 1,
  medium: 2,
  low: 3,
};

// Deutsche Beschriftungen für die gespeicherten Prioritätswerte.
const PRIORITY_LABELS = {
  high: "Hoch",
  medium: "Mittel",
  low: "Niedrig",
};

// Zentrale Datenliste der App. Sie wird beim Start aus dem LocalStorage gelesen.
let todos = loadTodos();

// Rendert direkt beim Laden der Seite alle gespeicherten Aufgaben.
renderTodos();

// Reagiert auf das Absenden des Formulars.
todoForm.addEventListener("submit", function (event) {
  event.preventDefault();

  // trim() entfernt Leerzeichen am Anfang und Ende der Eingabe.
  const text = todoInput.value.trim();
  const priority = priorityInput.value;
  const dueDate = dueDateInput.value;

  // Unvollständige Aufgaben werden nicht gespeichert.
  if (text === "" || priority === "" || dueDate === "") {
    todoInput.focus();
    return;
  }

  // Eine neue Aufgabe bekommt ID, Text, Priorität, Fälligkeitsdatum und Status.
  const newTodo = {
    id: createTodoId(),
    text: text,
    priority: priority,
    dueDate: dueDate,
    done: false,
    createdAt: Date.now(),
  };

  todos.push(newTodo);
  saveTodos();
  renderTodos();

  // Eingabefeld leeren und wieder fokussieren.
  todoInput.value = "";
  priorityInput.value = "medium";
  dueDateInput.value = "";
  todoInput.focus();
});

// Sobald sich die Sortierung ändert, wird die Liste neu dargestellt.
sortSelect.addEventListener("change", function () {
  renderTodos();
});

// Sobald sich der Prioritätsfilter ändert, wird die Liste neu dargestellt.
priorityFilter.addEventListener("change", function () {
  renderTodos();
});

// Löscht alle Aufgaben, die bereits erledigt sind.
clearDoneButton.addEventListener("click", function () {
  todos = todos.filter(function (todo) {
    return todo.done === false;
  });

  saveTodos();
  renderTodos();
});

function loadTodos() {
  // Aus dem LocalStorage kommt immer Text zurück.
  const savedTodos = localStorage.getItem(STORAGE_KEY);

  // Wenn noch nichts gespeichert wurde, startet die App mit einer leeren Liste.
  if (savedTodos === null) {
    return [];
  }

  // JSON.parse wandelt den gespeicherten Text wieder in JavaScript-Objekte um.
  let parsedTodos = [];

  try {
    parsedTodos = JSON.parse(savedTodos);
  } catch (error) {
    // Falls gespeicherte Daten beschädigt sind, startet die App wieder mit einer leeren Liste.
    return [];
  }

  // Ältere gespeicherte Aufgaben bekommen Standardwerte für neue Felder.
  return parsedTodos.map(function (todo) {
    return {
      id: todo.id || createTodoId(),
      text: todo.text || "",
      priority: todo.priority || "medium",
      dueDate: todo.dueDate || "",
      done: Boolean(todo.done),
      createdAt: todo.createdAt || Date.now(),
    };
  });
}

function saveTodos() {
  // JSON.stringify wandelt die Aufgabenliste in Text um, damit LocalStorage sie speichern kann.
  localStorage.setItem(STORAGE_KEY, JSON.stringify(todos));
}

function createTodoId() {
  // Diese ID-Erzeugung funktioniert auch, wenn die HTML-Datei direkt per file:// geöffnet wird.
  return `todo-${Date.now()}-${Math.random().toString(16).slice(2)}`;
}

function renderTodos() {
  // Die Liste wird vor jedem neuen Rendern geleert.
  todoList.innerHTML = "";

  const visibleTodos = getVisibleTodos();

  visibleTodos.forEach(function (todo) {
    const listItem = document.createElement("li");
    const checkbox = document.createElement("input");
    const content = document.createElement("div");
    const text = document.createElement("span");
    const meta = document.createElement("div");
    const priorityBadge = document.createElement("span");
    const dueDate = document.createElement("time");
    const deleteButton = document.createElement("button");

    listItem.className = `todo-item todo-item--${todo.priority}`;
    content.className = "todo-item__content";
    text.className = "todo-item__text";
    meta.className = "todo-item__meta";
    priorityBadge.className = "todo-item__priority";
    dueDate.className = "todo-item__date";

    // Erledigte Aufgaben bekommen eine zusätzliche CSS-Klasse.
    if (todo.done) {
      listItem.classList.add("todo-item--done");
    }

    checkbox.type = "checkbox";
    checkbox.checked = todo.done;
    checkbox.setAttribute("aria-label", `Aufgabe "${todo.text}" erledigen`);

    text.textContent = todo.text;
    priorityBadge.textContent = `Prio: ${PRIORITY_LABELS[todo.priority]}`;
    dueDate.dateTime = todo.dueDate;
    dueDate.textContent = `Fällig: ${formatDate(todo.dueDate)}`;

    deleteButton.type = "button";
    deleteButton.className = "todo-item__delete";
    deleteButton.textContent = "Löschen";

    // Ändert den Erledigt-Status einer Aufgabe.
    checkbox.addEventListener("change", function () {
      todo.done = checkbox.checked;
      saveTodos();
      renderTodos();
    });

    // Entfernt genau diese Aufgabe aus der Liste.
    deleteButton.addEventListener("click", function () {
      todos = todos.filter(function (currentTodo) {
        return currentTodo.id !== todo.id;
      });

      saveTodos();
      renderTodos();
    });

    meta.append(priorityBadge, dueDate);
    content.append(text, meta);
    listItem.append(checkbox, content, deleteButton);
    todoList.append(listItem);
  });

  updateCounter();
}

function getVisibleTodos() {
  const selectedPriority = priorityFilter.value;
  const selectedSort = sortSelect.value;

  // Erst wird nach Priorität gefiltert.
  const filteredTodos = todos.filter(function (todo) {
    return selectedPriority === "all" || todo.priority === selectedPriority;
  });

  // Danach wird eine Kopie sortiert, damit die Originaldaten unverändert bleiben.
  return filteredTodos.slice().sort(function (firstTodo, secondTodo) {
    if (selectedSort === "dueDate") {
      if (firstTodo.dueDate === "") {
        return 1;
      }

      if (secondTodo.dueDate === "") {
        return -1;
      }

      return firstTodo.dueDate.localeCompare(secondTodo.dueDate);
    }

    if (selectedSort === "priority") {
      return PRIORITY_ORDER[firstTodo.priority] - PRIORITY_ORDER[secondTodo.priority];
    }

    return secondTodo.createdAt - firstTodo.createdAt;
  });
}

function formatDate(dateValue) {
  // Wenn bei alten Aufgaben kein Datum vorhanden ist, wird ein Platzhalter angezeigt.
  if (dateValue === "") {
    return "nicht gesetzt";
  }

  // Das ISO-Datum aus dem Input wird für die Anzeige ins deutsche Format gebracht.
  return new Date(`${dateValue}T00:00:00`).toLocaleDateString("de-DE");
}

function updateCounter() {
  const openTodos = todos.filter(function (todo) {
    return todo.done === false;
  }).length;

  // Der Text wird je nach Anzahl grammatikalisch passend gesetzt.
  if (openTodos === 1) {
    todoCounter.textContent = "1 offene Aufgabe";
  } else {
    todoCounter.textContent = `${openTodos} offene Aufgaben`;
  }
}
