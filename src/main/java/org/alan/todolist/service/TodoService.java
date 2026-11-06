package org.alan.todolist.service;

import org.alan.todolist.model.Todo;
import org.alan.todolist.model.enums.Status;
import org.alan.todolist.repository.TodoRepository;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class TodoService {

    private final TodoRepository repository;
    private final Clock clock;

    public TodoService() {
        this(new TodoRepository(), Clock.systemDefaultZone());
    }

    public TodoService(TodoRepository repository) {
        this(repository, Clock.systemDefaultZone());
    }

    public TodoService(TodoRepository repository, Clock clock) {
        if (repository == null || clock == null) {
            throw new NullPointerException("Parâmetro inválido");
        }
        this.repository = repository;
        this.clock = clock;
    }

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
        List<Todo> todoList = repository.listAll();
        if (todoList.isEmpty()) {
            System.out.println("===== Lista Vazia =====");
        }
        return todoList;
    }

    public Todo findById(int id) {
        return repository.findById(id);
    }

    public List<Todo> listByCategory(String category) {
        return filter(todo -> todo.getCategory().equalsIgnoreCase(category));
    }

    public List<Todo> listByPriority(int priority) {
        return filter(todo -> todo.getPriority() == priority);
    }

    public List<Todo> listByStatus(Status status) {
        return filter(todo -> todo.getStatus() == status);
    }

    private List<Todo> filter(Predicate<Todo> condition) {
        List<Todo> matchingTodos = new ArrayList<>();
        for (Todo todo : repository.listAll()) {
            if (condition.test(todo)) {
                matchingTodos.add(todo);
            }
        }
        return matchingTodos;
    }

    public void deleteTodoById(int id) {
        repository.deleteTodoById(id);
    }

    public void deleteAllTodo() {
        repository.deleteAllTodo();
    }

    public Todo updateTodo(int id, Todo todo) {
        return repository.updateTodo(id, todo);
    }

    public List<Todo> checkPendingAlarms() {
        List<Todo> pendingTodos = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now(clock);
        for (Todo todo : repository.listAll()) {
            if (todo.hasPendingAlarm(now)) {
                pendingTodos.add(todo);
            }
        }
        return pendingTodos;
    }
}