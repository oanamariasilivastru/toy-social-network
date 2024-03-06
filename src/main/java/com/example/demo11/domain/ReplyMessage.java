package com.example.demo11.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

//class ReplyMessage extends Message{
//    private  Message repliedMessage;
//
//    public ReplyMessage(Utilizator from, List<Utilizator> to, String message, LocalDateTime data, Message repliedMessage) {
//        super(from, to, message, data);
//        this.repliedMessage = repliedMessage;
//    }
//
//    public String toString() {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        String data_mesaj = this.getData().format(formatter);
//        return new String(this.getFrom().getFirstName() + " " + this.getFrom().getLastName() +": reply la: '" + this.repliedMessage.getMessage() + "' reply cu " + this.getMessage() + "->" + data_mesaj);
//    }
//}
