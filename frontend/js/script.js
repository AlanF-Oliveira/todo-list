
let todoList = [];
let nextId = 1;

const nameInput = document.getElementById("name");
const descriptionInput = document.getElementById("description");
const dateInput = document.getElementById("date");
const timeInput = document.getElementById("time");
const priorityInput = document.getElementById("priority");
const statusInput = document.getElementById("status");
const categoryInput = document.getElementById("category");
const alarmHoursInput = document.getElementById("alarmHours");
const todoIdInput = document.getElementById("todo-id");
const btnSave = document.getElementById("btn-save");
const btnCancel = document.getElementById("btn-cancel");
const todoListDiv = document.getElementById("todo-list");

function salvarTarefa() {
    console.log("todoId:", todoIdInput.value);
    console.log("todoList:", todoList);
    let name = nameInput.value;
    let description = descriptionInput.value;
    let date = dateInput.value;
    let time = timeInput.value;
    let priority = priorityInput.value;
    let status = statusInput.value;
    let category = categoryInput.value;
    let alarmHours = alarmHoursInput.value;
    let finalDateTime = new Date(`${date}T${time}`)
    let alarm = null;

    if (alarmHours !== "") {
        alarm = { reminderOffsetHours: alarmHours };
    }

    if (todoIdInput.value === "") {

        const todo = {
            id: nextId,
            name: name,
            description: description,
            finalDateTime: finalDateTime,
            priority: priority,
            status: status,
            category: category,
            alarm: alarm
        };

        todoList.push(todo);
        nextId++;

    } else {

        console.log("idProcurado:", idProcurado);
        console.log("tarefa encontrada:", tarefa);
        const idProcurado = Number(todoIdInput.value);

        const tarefa = todoList.find(function (todo) {
            return todo.id === idProcurado;
        });

        tarefa.name = name;
        tarefa.description = description;
        tarefa.finalDateTime = finalDateTime;
        tarefa.priority = priority;
        tarefa.status = status;
        tarefa.category = category;
        tarefa.alarm = alarm;

    }
    listarTarefas();
    todoIdInput.value = "";
    document.getElementById("form-global").reset();
    btnSave.textContent = "Criar tarefa";
    btnCancel.hidden = true;
}

function listarTarefas() {
    todoListDiv.innerHTML = "";
    todoList.forEach(function (todo) {
        let alarmDateTime = null;

        if (todo.alarm) {
            alarmDateTime = new Date(todo.finalDateTime);

            alarmDateTime.setHours(
                alarmDateTime.getHours() -
                Number(todo.alarm.reminderOffsetHours)
            );
        }
        const html = `<div class="card mb-3">
            <div class="card-body">
                <div class="d-flex justify-content-between">
                 <h5 class="card-title">${todo.name}</h5>
                    <div class="d-flex flex-column align-items-end gap-2">
                        <span class="badge bg-primary">${todo.status}</span>
                        <div class="d-flex gap-1">
                            <button type="button" class="btn btn-sm btn-outline-secondary" onclick="editarTarefa(${todo.id})">Editar</button>
                            <button type="button" class="btn btn-sm btn-outline-danger" onclick="excluirTarefa(${todo.id})">Excluir</button>
                        </div>
                    </div>
                </div>
                <p class="card-text text-muted">${todo.description}</p>
                <p class="card-text">${todo.category}</p>
                <p class="card-text">Prioridade: ${todo.priority}</p>
                <p class="card-text">${todo.alarm ? `Alarme às: ${alarmDateTime.toLocaleTimeString("pt-BR", { hour: "2-digit", minute: "2-digit" })}` : "Sem alarme"} </p>
                <p class="card-text">${todo.finalDateTime.toLocaleString("pt-BR")}</p>
            </div>
        </div>`;
        todoListDiv.innerHTML += html;
    });
}

function excluirTarefa(id) {
    todoList = todoList.filter(function (todo) {
        return todo.id !== id;
    })
    listarTarefas();
}

function editarTarefa(id) {

    const tarefa = todoList.find(function (todo) {
        return todo.id === id;
    });
    let ano = tarefa.finalDateTime.getFullYear();
    let mes = tarefa.finalDateTime.getMonth() + 1;
    let dia = tarefa.finalDateTime.getDate();
    let horas = tarefa.finalDateTime.getHours();
    let minutos = tarefa.finalDateTime.getMinutes();
    todoIdInput.value = tarefa.id;
    nameInput.value = tarefa.name;
    descriptionInput.value = tarefa.description;
    let mesFormatado = String(mes).padStart(2, "0");
    let diaFormatado = String(dia).padStart(2, "0");
    dateInput.value = `${ano}-${mesFormatado}-${diaFormatado}`;
    let horasFormatadas = String(horas).padStart(2, "0");
    let minutosFormatados = String(minutos).padStart(2, "0");
    timeInput.value = `${horasFormatadas}:${minutosFormatados}`;
    priorityInput.value = tarefa.priority;
    statusInput.value = tarefa.status;
    categoryInput.value = tarefa.category;
    alarmHoursInput.value = tarefa.alarm ? tarefa.alarm.reminderOffsetHours : "";
    btnSave.textContent = "Salvar alterações";
    btnCancel.hidden = false;
    window.scrollTo({
        top: 0,
        behavior: "smooth"
    });
}

btnCancel.addEventListener("click", function () {
    document.getElementById("form-global").reset();

    todoIdInput.value = "";

    btnSave.textContent = "Criar tarefa";
    btnCancel.hidden = true;
});

const form = document.getElementById("form-global");

form.addEventListener("submit", function (event) {
    event.preventDefault();

    salvarTarefa();
});