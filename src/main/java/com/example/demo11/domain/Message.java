package com.example.demo11.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Message extends Entity<Long> {
    private Message replyMessage;
    private Utilizator from;
    private List<Utilizator> to;
    private String message;
    private LocalDateTime data;

    public Message(Utilizator from, List<Utilizator> to, String message, LocalDateTime data, Message replyMessage) {

        this.from = from;
        this.to = to;
        this.message = message;
        this.data = data;
        this.replyMessage = replyMessage;
    }

    public Utilizator getFrom() {
        return from;
    }

    public void setFrom(Utilizator from) {
        this.from = from;
    }

    public List<Utilizator> getTo() {
        return to;
    }

    public void setTo(List<Utilizator> to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Message getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(Message replyMessage) {
        this.replyMessage = replyMessage;
    }

    @Override
    public String toString() {
        return "Message{" +
                "replyMessage=" + replyMessage +
                ", from=" + from +
                ", to=" + to +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}