package com.example.demo11.utils;

import com.example.demo11.domain.Invite;
import com.example.demo11.domain.Utilizator;

public class InviteChangeEvent implements Event{
    private ChangeEventType type;
    private Invite data, oldData;

    public InviteChangeEvent(ChangeEventType type, Invite data) {
        this.type = type;
        this.data = data;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Invite getData() {
        return data;
    }

    public Invite getOldData() {
        return oldData;
    }
}
