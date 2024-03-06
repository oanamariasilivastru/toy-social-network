package com.example.demo11;

import com.example.demo11.domain.Message;
import com.example.demo11.domain.Utilizator;
import com.example.demo11.repository.paging.Page;
import com.example.demo11.repository.paging.Pageable;
import com.example.demo11.repository.paging.PageableImplementation;
import com.example.demo11.service.UtilizatoriService;
import com.example.demo11.utils.ChatManager;
import com.example.demo11.utils.SpeechDirection;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConversationView extends VBox {
    private String message;
    private Utilizator conversationPartner;
    private Utilizator conversationUser;
    private ObservableList<Node> speechBubbles = FXCollections.observableArrayList();
    private UtilizatoriService service;
    private Label contactHeaderLeftUser;
    private ScrollPane messageScroller;
    private VBox messageContainer;
    private HBox inputContainer;

    private MessageController messageController;

    private SpeechBox selectedSpeechBox;

    @FXML
    TextField pagination = new TextField();

    @FXML
    Button handlePrevious;
    @FXML
    Button handleNext;

    private Pageable pageable = new PageableImplementation(1,99);

    Integer pageNumber = 1;

    public ConversationView(Utilizator conversationPartner, Utilizator conversationUser, UtilizatoriService service, MessageController messageController){
        super(5);
        this.conversationPartner = conversationPartner;
        this.conversationUser = conversationUser;
        this.messageController = messageController;
        this.service = service;

        // Verificați dacă pagination nu este null
        if (pagination != null) {
            pagination.textProperty().addListener((observable, oldValue, newValue) -> {
                // Verificați dacă există erori în consolă
                System.out.println("Pagination value changed: " + newValue);
                handlePagination();
            });
        } else {
            System.out.println("Pagination is null!");
        }

        setupElements();
    }


    private void setupElements(){
        setupContactHeader();
        setupMessageDisplay();
        setupInputDisplay();
        getChildren().setAll(contactHeaderLeftUser, messageScroller, inputContainer);
        setPadding(new Insets(0));
    }

    private void setupContactHeader(){
        contactHeaderLeftUser = new Label(conversationPartner.getFirstName()+ "   <------------------------------------------------------>   " +conversationUser.getFirstName());
        contactHeaderLeftUser.setAlignment(Pos.TOP_RIGHT);
        contactHeaderLeftUser.setFont(Font.font("Comic Sans MS", 10));
    }

    private void setupMessageDisplay(){
        messageContainer = new VBox(5);
        Bindings.bindContentBidirectional(speechBubbles, messageContainer.getChildren());

        messageScroller = new ScrollPane(messageContainer);
        messageScroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        messageScroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        messageScroller.setPrefHeight(300);
        messageScroller.prefWidthProperty().bind(messageContainer.prefWidthProperty().subtract(5));
        messageScroller.setFitToWidth(true);
        speechBubbles.addListener((ListChangeListener<Node>) change -> {
            while (change.next()) {
                if(change.wasAdded()){
                    messageScroller.setVvalue(messageScroller.getVmax());
                }
            }
        });
    }

    private void setupInputDisplay(){
        inputContainer = new HBox(5);
        TextField userInput = new TextField();
        userInput.setPromptText("Enter message");
        Button sendMessageButton = new Button("Send");

        sendMessageButton.disableProperty().bind(userInput.lengthProperty().isEqualTo(0));
        sendMessageButton.setOnAction(event-> {
            sendMessage(userInput.getText());
            List<Utilizator> to = new ArrayList<>();
            to.add(conversationUser);
            service.create(conversationPartner, to, userInput.getText(), LocalDateTime.now(), null);
            userInput.setText("");
            ChatManager.notifyMessageControllers();
        });

        Button replyMessageButton = new Button("Reply");
        replyMessageButton.disableProperty().bind(userInput.lengthProperty().isEqualTo(0));

        userInput.setPromptText("Enter number of rows");
        Button handlePrevious = new Button("Previous");
        Button handleNext = new Button("Next");

        replyMessageButton.setOnAction(event-> {
            String text = userInput.getText();
            Message mesajuldereply = service.getMessageReply();
            String replyText = "Reply la mesajul: " + mesajuldereply.getMessage() + "-->" + userInput.getText();
            sendMessage(replyText);
            List<Utilizator> to = new ArrayList<>();
            to.add(conversationUser);
            service.create(conversationPartner, to, userInput.getText(), LocalDateTime.now(), mesajuldereply);
            userInput.setText("");
            ChatManager.notifyMessageControllers();
        });

        handlePrevious.setOnAction(event -> {
            handlePreviousPage();
        });

        handleNext.setOnAction(event -> {
            handleNextPage();
        });
        HBox buttonContainer = new HBox();
        buttonContainer.getChildren().setAll(pagination, handlePrevious, handleNext);
        inputContainer.getChildren().setAll(userInput, sendMessageButton, replyMessageButton, buttonContainer);

    }

    public String getMessage(){
        return message;
    }
    public String sendMessage(String message){
        speechBubbles.add(new SpeechBox(message, SpeechDirection.RIGHT, service));
        return message;
    }

    public void receiveMessage(String message){
        speechBubbles.add(new SpeechBox(message, SpeechDirection.LEFT, service));
    }

    private void handlePagination(){
        try{
            String pageSizeString = pagination.getText();
            Integer pageSize = 99;

            if (!pageSizeString.isEmpty()) {
                pageSize = Integer.parseInt(pageSizeString);
            }
            Pageable pageable = new PageableImplementation(pageNumber, pageSize);
            Page<Message> messagePage = service.getMessagesFromUsersPaging(conversationPartner.getId(), conversationUser.getId(), pageable);
            List<Message> messageList = messagePage.getContent().collect(Collectors.toList());
            clearAllSpeechBubbles();
            messageController.modelUserMessage.setAll(messageList);
            messageController.loadMessages();

        }catch (NumberFormatException e){
            System.out.println(e.getMessage());
        }

    }
    @FXML
    public  void handlePreviousPage(){
        if(pageNumber > 1)
        {
            pageNumber--;
            handlePagination();
        }
        else{
            MessageAlert.showErrorMessage(null, "Nu se poate trece la pagina anterioara!");
        }
    }

    @FXML
    public void handleNextPage(){
        if(pageNumber < 99)
        {
            pageNumber++;
            handlePagination();
        }
        else{
            MessageAlert.showErrorMessage(null, "Nu se poate trece la pagina urmatoare!");
        }
    }

    public void clearAllSpeechBubbles() {
        speechBubbles.clear();
    }
}
