package org.alan.todolist;

import org.alan.todolist.model.Todo;
import org.alan.todolist.model.enums.Status;
import org.alan.todolist.service.TodoService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TodoList {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        TodoService todoService = new TodoService();

        System.out.println("Digite a tarefa que voĉe quer adicionar: ");
        System.out.print("Nome: ");
        String name = sc.nextLine();

        System.out.print("Descricão da tarefa: ");
        String description = sc.nextLine();

        System.out.print("Data final da tarefa: ");
        LocalDate date = LocalDate.parse(sc.nextLine(), dtf);

        System.out.println("Nível de prioridade (1~5): ");
        int priority = sc.nextInt();

        System.out.print("Categoria: ");
        sc.nextLine();
        String category = sc.nextLine();

        System.out.print("Digite o Status (TODO/DOING/DONE): ");
        Status status = Status.valueOf(sc.nextLine());
        Todo todo1 = new Todo(name, description, date, priority, category,status);
        Todo todo2 = new Todo("jOAO", "TESTE2", LocalDate.now(), 3, "Urgente",Status.TODO);
        Todo todo3 = new Todo("Ana", "TESTE3", LocalDate.now(), 1, "Casa",Status.TODO);
        Todo todo4 = new Todo("DAvi", "TESTE3", LocalDate.now(), 5, "CArro",Status.TODO);
        todoService.addTodo(todo1);
        todoService.addTodo(todo2);
        todoService.addTodo(todo3);
        todoService.addTodo(todo4);
        List<Todo>  todoList = todoService.listAll();
        for (Todo todo : todoList){
            System.out.println(todo);
        }
        System.out.println("===========================//===========================");

        for (Todo todo : todoService.listByCategory("Carro")){
            System.out.println(todo);
        }
    }
}
