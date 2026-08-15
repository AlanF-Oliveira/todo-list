package org.alan.todolist.model;

import java.time.Duration;

public class Alarm {

    private final Duration reminderOffset;

    public Alarm(Duration reminderOffset) {
        this.reminderOffset = reminderOffset;
    }

    public Duration getReminderOffset() {
        return reminderOffset;
    }

    @Override
    public String toString() {
        return reminderOffset.toHours() + "h antes do prazo";
    }
}