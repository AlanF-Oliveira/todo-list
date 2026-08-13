package org.alan.todolist.model;

import org.alan.todolist.model.enums.Status;

import java.time.LocalDate;

public class Todo {
    private static int nextId = 1;
    private final int id;
    private String name;
    private String description;
    private LocalDate finalDate;
    private Integer priority;
    private String category;
    private Status status;

    public Todo(String name, String description, LocalDate finalDate, Integer priority, String category, Status status) {
        this.id = nextId++;
        this.name = name;
        this.description = description;
        this.finalDate = finalDate;
        setPriority(priority);
        this.category = category;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(LocalDate finalDate) {
        this.finalDate = finalDate;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        if (priority != null && priority > 0 && priority <= 5) {
            this.priority = priority;
        }else {
            throw  new IllegalArgumentException("Valor inválido.");
        }

    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public static int getNextId() {
        return nextId;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", finalDate=" + finalDate +
                ", priority=" + priority +
                ", category='" + category + '\'' +
                ", status=" + status +
                '}';
    }
}
