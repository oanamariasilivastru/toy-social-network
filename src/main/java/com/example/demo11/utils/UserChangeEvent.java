package com.example.demo11.utils;

import com.example.demo11.domain.Utilizator;

public class UserChangeEvent implements Event{
    private ChangeEventType type;
    private Utilizator data, oldData;

    public UserChangeEvent(ChangeEventType type, Utilizator data) {
        this.type = type;
        this.data = data;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Utilizator getData() {
        return data;
    }

    public Utilizator getOldData() {
        return oldData;
    }
}
