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

    private Scanner sc = new Scanner(System.in);
    private TodoService todoService = new TodoService();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void start() {
        boolean isActive = true;
        while (isActive) {
            checkAlarms();
            System.out.println();
            System.out.println("O que deseja fazer: ");
            System.out.println(" 1) Adicionar uma tarefa.");
            System.out.println(" 2) Listar todas as tarefas.");
            System.out.println(" 3) Listar tarefas por categoria.");
            System.out.println(" 4) Listar tarefas por prioridade.");
            System.out.println(" 5) Listar tarefas por status.");
            System.out.println(" 6) Apagar uma tarefa.");
            System.out.println(" 7) Apagar todas as tarefas.");
            System.out.println(" 8) Fechar o programa.");
            System.out.print("Escolha entre 1 a 8: ");
            int option = sc.nextInt();
            switch (option) {
                case 1:
                    createTodo();
                    break;
                case 2:
                    listAll();
                    break;
                case 3:
                    sc.nextLine();
                    System.out.println();
                    System.out.print("Digite a categoria: ");
                    String category = sc.nextLine();
                    listByCategory(category);
                    break;
                case 4:
                    System.out.println();
                    System.out.print("Digite a prioridade: ");
                    int priority = sc.nextInt();
                    listByPriority(priority);
                    break;
                case 5:
                    System.out.println();
                    System.out.print("Digite o status: ");
                    sc.nextLine();
                    Status status = Status.valueOf(sc.nextLine());
                    listByStatus(status);
                    break;
                case 6:
                    System.out.println();
                    System.out.print("Digite o id da tarefa: ");
                    int id = sc.nextInt();
                    deleteById(id);
                    break;
                case 7:
                    System.out.println();
                    deleteALlTodo();
                    break;
                case 8:
                    isActive = false;
                    break;
            }
        }
        sc.close();
    }

    public void createTodo() {
        System.out.println();
        sc.nextLine();
        System.out.println("Digite a tarefa que voê quer adicionar: ");
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

        System.out.print("Habilitar alarme? (s/n): ");
        String alarm = sc.nextLine();
        if (alarm.equalsIgnoreCase("s")) {
            boolean addingMore = true;
            while (addingMore) {
                System.out.print("Avisar quantas horas antes do prazo: ");
                long hours = Long.parseLong(sc.nextLine());
                todo.addAlarm(new Alarm(Duration.ofHours(hours)));

                System.out.print("Adicionar outro alarme? (s/n): ");
                addingMore = sc.nextLine().equalsIgnoreCase("s");
            }
        }

        todoService.addTodo(todo);
        System.out.println();
        System.out.println("Tarefa adicionada:");
        System.out.println();
        System.out.println(todo);
    }

    public void listAll() {
        List<Todo> todoList = todoService.listAll();
        System.out.println();
        for (Todo todo : todoList) {
            System.out.println(todo);
        }
    }

    public void listByCategory(String category) {
        List<Todo> todoList = todoService.listByCategory(category);
        System.out.println();
        for (Todo todo : todoList) {
            System.out.println(todo);
        }
    }

    public void listByPriority(int priority) {
        if (priority >= 1 && priority <= 5) {
            List<Todo> todoList = todoService.listByPriority(priority);
            System.out.println();
            for (Todo todo : todoList) {
                System.out.println(todo);
            }
        } else {
            System.out.println("Valor inválido.");
        }
    }

    public void listByStatus(Status status) {
        List<Todo> todoList = todoService.listByStatus(status);
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

    public void deleteALlTodo() {
        todoService.delelteAllTodo();
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
}

