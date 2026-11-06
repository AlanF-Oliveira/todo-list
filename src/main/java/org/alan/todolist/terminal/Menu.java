package org.alan.todolist.terminal;

import org.alan.todolist.model.Alarm;
import org.alan.todolist.model.Todo;
import org.alan.todolist.model.enums.Status;
import org.alan.todolist.service.TodoService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private final Scanner sc = new Scanner(System.in);
    private final TodoService todoService = new TodoService();
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void start() {
        boolean isActive = true;
        while (isActive) {
            checkAlarms();
            showMenu();
            int option = sc.nextInt();
            isActive = handleOption(option);
        }
        sc.close();
    }

    private boolean handleOption(int option) {
        switch (option) {
            case 1:
                createTodo();
                break;
            case 2:
                listAll();
                break;
            case 3:
                listByCategory(readCategory());
                break;
            case 4:
                listByPriority(readPriority());
                break;
            case 5:
                listByStatus(readStatus());
                break;
            case 6:
                deleteById(readId("Digite o id da tarefa: "));
                break;
            case 7:
                deleteAllTodo();
                break;
            case 8:
                updateTodo(readId("Digite o id da tarefa que você deseja alterar: "));
                break;
            case 9:
                return false;
            default:
                break;
        }
        return true;
    }

    private void showMenu() {
        System.out.println();
        System.out.println("O que deseja fazer: ");
        System.out.println(" 1) Adicionar uma tarefa.");
        System.out.println(" 2) Listar todas as tarefas.");
        System.out.println(" 3) Listar tarefas por categoria.");
        System.out.println(" 4) Listar tarefas por prioridade.");
        System.out.println(" 5) Listar tarefas por status.");
        System.out.println(" 6) Apagar uma tarefa.");
        System.out.println(" 7) Apagar todas as tarefas.");
        System.out.println(" 8) Alterar um TODO.");
        System.out.println(" 9) Fechar o programa.");
        System.out.print("Escolha entre 1 a 8: ");
    }

    public void createTodo() {
        Todo todo = readTodo(false);
        todoService.addTodo(todo);
        System.out.println();
        System.out.println("Tarefa adicionada:");
        System.out.println();
        System.out.println(todo);
    }

    private Todo readTodo(boolean updating) {
        if (!updating) {
            System.out.println();
        }
        sc.nextLine();
        System.out.println("Digite a tarefa que você quer " + (updating ? "alterar" : "adicionar") + ": ");
        System.out.print("Tarefa: ");
        String name = sc.nextLine();

        System.out.print("Descricão da tarefa: ");
        String description = sc.nextLine();

        System.out.print("Data a data final da tarefa (dd/MM/yyyy HH:mm): ");
        LocalDateTime date = LocalDateTime.parse(sc.nextLine(), dtf);

        System.out.print("Nível de prioridade (1~5): ");
        int priority = sc.nextInt();
        while (priority < 1 || priority > 5) {
            System.out.println("Valor inválido");
            System.out.print("Nível de prioridade (1~5): ");
            priority = sc.nextInt();
        }

        System.out.print("Categoria: ");
        sc.nextLine();
        String category = sc.nextLine();

        Status status = null;
        while (status == null) {
            System.out.print("Digite o Status (TODO/DOING/DONE): ");

            try {
                status = Status.valueOf(sc.nextLine().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Status inválido.");
            }
        }

        Todo todo = new Todo(name, description, date, priority, category, status);
        addAlarms(todo);
        return todo;
    }

    public void listAll() {
        printTodos(todoService.listAll());
    }

    public void listByCategory(String category) {
        printTodos(todoService.listByCategory(category));
    }

    public void listByPriority(int priority) {
        if (priority >= 1 && priority <= 5) {
            printTodos(todoService.listByPriority(priority));
        } else {
            System.out.println("Valor inválido.");
        }
    }

    public void listByStatus(Status status) {
        printTodos(todoService.listByStatus(status));
    }

    private void printTodos(List<Todo> todoList) {
        System.out.println();
        for (Todo todo : todoList) {
            System.out.println(todo);
        }
    }

    public void deleteById(int id) {
        todoService.deleteTodoById(id);
        System.out.println();
        System.out.println("Tarefa deletada.");
    }

    public void deleteAllTodo() {
        todoService.deleteAllTodo();
    }

    private void checkAlarms() {
        List<Todo> pending = todoService.checkPendingAlarms();
        String red = "\u001B[31m";
        String reset = "\u001B[0m";
        if (!pending.isEmpty()){
            System.out.println();
            System.out.println("============================================================================================================");
        }
        for (Todo todo : pending) {
            System.out.println("//=======// " + red + " Alarme: " + reset + todo.getName() + " vence em " + todo.getFinalDateTime().format(dtf) + " //=======//");
        }
        if (!pending.isEmpty()){
            System.out.println("============================================================================================================");
            System.out.println();
        }
    }

    public void updateTodo(int id){
        Todo todo = readTodo(true);
        todoService.updateTodo(id, todo);
    }

    private String readCategory() {
        sc.nextLine();
        System.out.println();
        System.out.print("Digite a categoria: ");
        return sc.nextLine();
    }

    private int readPriority() {
        System.out.println();
        System.out.print("Digite a prioridade: ");
        return sc.nextInt();
    }

    private Status readStatus() {
        System.out.println();
        System.out.print("Digite o status: ");
        sc.nextLine();
        return Status.valueOf(sc.nextLine());
    }

    private int readId(String prompt) {
        System.out.println();
        System.out.print(prompt);
        return sc.nextInt();
    }

    private void addAlarms(Todo todo) {
        System.out.print("Habilitar alarme? (s/n): ");
        if (sc.nextLine().equalsIgnoreCase("s")) {
            boolean addingMore = true;
            while (addingMore) {
                System.out.print("Avisar quantas horas antes do prazo: ");
                long hours = Long.parseLong(sc.nextLine());
                todo.addAlarm(new Alarm(Duration.ofHours(hours)));
                System.out.print("Adicionar outro alarme? (s/n): ");
                addingMore = sc.nextLine().equalsIgnoreCase("s");
            }
        }
    }
}
