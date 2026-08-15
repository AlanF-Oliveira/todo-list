package org.alan.todolist.model;

import org.alan.todolist.model.enums.Status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class Todo {
    private static int nextId = 1;
    private final int id;
    private String name;
    private String description;
    private LocalDateTime finalDateTime;
    private Integer priority;
    private String category;
    private Status status;
    private List<Alarm> alarms = new ArrayList<>();

    public Todo(String name, String description, LocalDateTime finalDateTime, Integer priority, String category, Status status) {
        this.id = nextId++;
        this.name = name;
        this.description = description;
        this.finalDateTime = finalDateTime;
        setPriority(priority);
        this.category = category;
        this.status = status;
    }

    public LocalDateTime getFinalDateTime() {
        return finalDateTime;
    }

    public void setFinalDateTime(LocalDateTime finalDateTime) {
        this.finalDateTime = finalDateTime;
    }

    public List<Alarm> getAlarms() {
        return alarms;
    }

    public void addAlarm(Alarm alarm) {
        alarms.add(alarm);
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

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        if (priority != null && priority > 0 && priority <= 5) {
            this.priority = priority;
        } else {
            System.out.println("Valor inválido.");
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

    public boolean hasPendingAlarm(LocalDateTime now) {
        if (status == Status.DONE || alarms.isEmpty()) {
            return false;
        }
        for (Alarm alarm : alarms) {
            LocalDateTime triggerAt = finalDateTime.minus(alarm.getReminderOffset());
            if (!now.isBefore(triggerAt)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "id = " + id
                + " | Tarefa = " + name
                + " | Descrição = " + description
                + " | Data de término = " + finalDateTime.format(dtf)
                + " | Prioridade = " + priority
                + " | Categoria = " + category
                + " | Status = " + getStatus();
    }
}
