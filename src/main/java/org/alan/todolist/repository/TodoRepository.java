package org.alan.todolist.repository;

import org.alan.todolist.model.Todo;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class TodoRepository {

    private final List<Todo> todos = new ArrayList<>();

    public void create(int index, Todo todo) {
        todos.add(index, todo);
    }

    public List<Todo> listAll() {
        return new ArrayList<>(todos);
    }

    public void deleteTodoById(int id) {
        for (Todo todo : new ArrayList<>(todos)) {
            if (todo.getId() == id) {
                todos.remove(todo);
                return;
            }
        }
        throw new NoSuchElementException("Todo não encontrado");
    }

    public Todo findById(int id) {
        for (Todo todo : todos) {
            if (todo.getId() == id) {
                return todo;
            }
        }
        throw new NoSuchElementException("Todo não encontrado");
    }

    public void deleteAllTodo() {
        todos.clear();
    }

    public Todo updateTodo(int id, Todo todoRequest) {
        Todo todo = findById(id);
        if (todoRequest != null) {
            todo.setName(todoRequest.getName());
            todo.setCategory(todoRequest.getCategory());
            todo.setDescription(todoRequest.getDescription());
            todo.setFinalDateTime(todoRequest.getFinalDateTime());
            todo.setPriority(todoRequest.getPriority());
            todo.setStatus(todoRequest.getStatus());
        } else {
            throw new NullPointerException("Parâmetro inválido");
        }
        return todo;
    }
}
