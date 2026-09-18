package org.alan.todolist.service;

import org.alan.todolist.model.Todo;
import org.alan.todolist.model.enums.Status;
import org.alan.todolist.repository.TodoRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TodoService {

    private TodoRepository repository = new TodoRepository();

    public Todo addTodo(Todo todo) {
        List<Todo> todoList = repository.listAll();
        for (int i = 0; i < todoList.size(); i++) {
            if (todoList.get(i).getPriority() > todo.getPriority()) {
                repository.create(i, todo);
                return todo;
            }
        }
        repository.create(todoList.size(), todo);
        return todo;
    }

    public List<Todo> listAll() {
        List<Todo> lista= repository.listAll();
        if(lista.isEmpty()){
            System.out.println("===== Lista Vazia =====");
        }
        return lista;
    }

    public Todo findById(int id){
        return repository.findById(id);
    }

    public List<Todo> listByCategory(String category) {
        List<Todo> result = new ArrayList<>();
        for (Todo todo : repository.listAll()) {
            if (todo.getCategory().equalsIgnoreCase(category)) {
                result.add(todo);
            }
        }
        if (result.isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("Categoria não encontrada");
        }
        return result;
    }

    public List<Todo> listByPriority(int priority) {
        List<Todo> result = new ArrayList<>();
        for (Todo todo : repository.listAll()) {
            if (todo.getPriority() == priority) {
                result.add(todo);
            }
        }
        if (result.isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("Prioridade não encontrada");
        }
        return result;
    }

    public List<Todo> listByStatus(Status status) {
        List<Todo> result = new ArrayList<>();
        for (Todo todo : repository.listAll()) {
            if (todo.getStatus() == status) {
                result.add(todo);
            }
        }
        if (result.isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("Status não encontrado");
        }
        return result;
    }

    public void deleteTodoById(int id) {
        repository.deleteTodoById(id);
    }

    public void delelteAllTodo() {
        repository.deleteAllTodo();
    }

    public Todo updateTodo(int id, Todo todo) {
        return repository.updateTodo(id, todo);
    }


    public List<Todo> checkPendingAlarms() {
        List<Todo> result = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (Todo todo : repository.listAll()) {
            if (todo.hasPendingAlarm(now)) {
                result.add(todo);
            }
        }
        return result;
    }
}