package com.example.demo11.utils;

import com.example.demo11.domain.Utilizator;

public class UserStatusEvent implements Event{
    private TaskExecutionStatusEventType type;
    private Utilizator user;

    public UserStatusEvent(TaskExecutionStatusEventType type, Utilizator user) {
        this.type = type;
        this.user = user;
    }

    public TaskExecutionStatusEventType getType() {
        return type;
    }

    public Utilizator getUser() {
        return user;
    }

    public void setType(TaskExecutionStatusEventType type) {
        this.type = type;
    }

    public void setUser(Utilizator user) {
        this.user = user;
    }

}
