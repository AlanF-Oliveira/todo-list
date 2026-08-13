package org.alan.todolist.model.enums;

public enum Status {

    TODO("Todo"),

    DOING("Doing"),

    DONE("Done");

    private String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return status;
    }
}
