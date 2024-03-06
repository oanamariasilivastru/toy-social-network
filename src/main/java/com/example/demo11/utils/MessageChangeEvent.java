package com.example.demo11.utils;

import com.example.demo11.domain.Message;

public class MessageChangeEvent implements Event {
    private MessageType messageType;
    private Message changedMessage;

    public MessageChangeEvent(MessageType messageType, Message changedMessage) {
        this.messageType = messageType;
        this.changedMessage = changedMessage;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public Message getChangedMessage() {
        return changedMessage;
    }
}