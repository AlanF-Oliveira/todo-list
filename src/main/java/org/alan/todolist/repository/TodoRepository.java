package org.alan.todolist.repository;

import org.alan.todolist.model.Todo;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.List;

public class TodoRepository {

    private List<Todo> list = new ArrayList<>();

    public void create(int index, Todo todo) {
        list.add(index, todo);
    }

    public List<Todo> listAll() {
        return new ArrayList<>(list);
    }

    public void deleteTodoById(int id) {
        for (Todo todo : new ArrayList<>(list)) {
            if (todo.getId() == id) {
                list.remove(todo);
                return;
            }
        }
        throw new NoSuchElementException("Todo não encontrado");
    }

    public Todo findById(int id) {
        for (Todo todo : list) {
            if (todo.getId() == id) {
                return todo;
            }
        }
        throw new NoSuchElementException("Todo não encontrado");
    }

    public void deleteAllTodo() {
        list.clear();
    }

    public Todo updateTodo(int id, Todo todoRequest) {
        Todo todo = findById(id);
        if (todoRequest != null) {
            todo.setName(todoRequest.getName());
            todo.setCategory((todoRequest.getCategory()));
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
