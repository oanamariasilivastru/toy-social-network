package com.example.demo11;


import com.example.demo11.ConversationView;
import com.example.demo11.domain.Message;
import com.example.demo11.domain.Utilizator;
import com.example.demo11.service.MessageService;
import com.example.demo11.service.UtilizatoriService;
import com.example.demo11.utils.MessageChangeEvent;
import com.example.demo11.utils.Observer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class MessageController{
    private UtilizatoriService service;
    private Utilizator user_app = null;
    private Utilizator user_comunicate = null;
    private ConversationView cv;
    ObservableList<Message> modelUserMessage = FXCollections.observableArrayList();
    @FXML
    private VBox vBox = new VBox();

    public void setService(UtilizatoriService service, Utilizator user_app, Utilizator user_comunicate) {
        this.service = service;
        this.user_app = user_app;
        this.user_comunicate = user_comunicate;
        cv = new ConversationView(user_app, user_comunicate, service, this);
        vBox.getChildren().add(cv);
        initModel();
        loadMessages();
    }

    private void initModel() {
        List<Message> mesaje = service.getMessagesfromUsers(user_app.getId(), user_comunicate.getId());
        modelUserMessage.addAll(service.getAllMessages());
    }

    public VBox getvBox() {
        return vBox;
    }

    public void loadMessages() {

        for (Message msg : modelUserMessage) {
            if (msg.getFrom().equals(user_app) && msg.getTo().contains(user_comunicate)) {
                if(msg.getReplyMessage() != null) {
                    String replyText = "Reply la mesajul: " + msg.getReplyMessage().getMessage() + "-->" + msg.getMessage();
                    cv.sendMessage(replyText);
                }
                else
                    cv.sendMessage(msg.getMessage());
            } else if (msg.getFrom().equals(user_comunicate) && msg.getTo().contains(user_app)) {
                if(msg.getReplyMessage() != null) {
                    String replyText = "Reply la mesajul: " + msg.getReplyMessage().getMessage() + "-->" + msg.getMessage();
                    cv.receiveMessage(replyText);
                }
                else
                    cv.receiveMessage(msg.getMessage());
            }
        }

    }
    public void refresh() {
          Message latestMessage = service.getLatestMessage();
            if (latestMessage.getFrom().equals(user_comunicate) && latestMessage.getTo().contains(user_app)) {
                if(latestMessage.getReplyMessage() != null) {
                    String replyText = "Reply la mesajul: " + latestMessage.getReplyMessage().getMessage() + "-->" + latestMessage.getMessage();
                    cv.receiveMessage(replyText);
            }
            else
                cv.receiveMessage(latestMessage.getMessage());
        }
//        List<Message> mesaje = service.getAllMessages();
//        List<Message> newMesaje = new ArrayList<>();
//        newMesaje.clear();
//        if (!mesaje.isEmpty()) {
//            Message latestMessage = mesaje.get(mesaje.size() - 1);
//            newMesaje.add(latestMessage);
//        }
//        modelUserMessage.clear();
//        modelUserMessage.addAll(newMesaje);
//        loadMessages();
        System.out.println("Aici se da refresh!");
    }



}

