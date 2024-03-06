package com.example.demo11;

import com.example.demo11.service.UtilizatoriService;
import com.example.demo11.utils.SpeechDirection;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public class SpeechBox extends HBox {
    private UtilizatoriService service;
    private Color DEFAULT_SENDER_COLOR = Color.GOLD;
    private Color DEFAULT_RECEIVER_COLOR = Color.LAVENDER;
    private Background DEFAULT_SENDER_BACKGROUND, DEFAULT_RECEIVER_BACKGROUND;

    private String message;
    private SpeechDirection direction;

    private Label displayedText;
    private SVGPath directionIndicator;

    public SpeechBox(String message, SpeechDirection direction, UtilizatoriService service){
        this.message = message;
        this.direction = direction;
        this.service = service;
        initialiseDefaults();
        setupElements();
    }

    private void initialiseDefaults(){
        DEFAULT_SENDER_BACKGROUND = new Background(
                new BackgroundFill(DEFAULT_SENDER_COLOR, new CornerRadii(5,0,5,5,false), Insets.EMPTY));
        DEFAULT_RECEIVER_BACKGROUND = new Background(
                new BackgroundFill(DEFAULT_RECEIVER_COLOR, new CornerRadii(0,5,5,5,false), Insets.EMPTY));
    }

    private void setupElements(){
        displayedText = new Label(message);
        displayedText.setPadding(new Insets(5));
        displayedText.setWrapText(true);
        directionIndicator = new SVGPath();

        displayedText.setOnMouseClicked(event -> {
            String textulMesajului = message;
            System.out.println("Textul mesajului: " + textulMesajului);
            service.getMessage(textulMesajului);

        });

        if(direction == SpeechDirection.LEFT)
            configureForReceiver();
        else
            configureForSender();
    }

    private void configureForSender(){
        displayedText.setBackground(DEFAULT_SENDER_BACKGROUND);
        displayedText.setAlignment(Pos.CENTER_RIGHT);
        directionIndicator.setContent("M10 0 L0 10 L0 0 Z");
        directionIndicator.setFill(DEFAULT_SENDER_COLOR);

        HBox container = new HBox(displayedText, directionIndicator);
        container.maxWidthProperty().bind(widthProperty().multiply(0.75));
        getChildren().setAll(container);
        setAlignment(Pos.CENTER_RIGHT);
    }

    private void configureForReceiver(){
        displayedText.setBackground(DEFAULT_RECEIVER_BACKGROUND);
        displayedText.setAlignment(Pos.CENTER_LEFT);
        directionIndicator.setContent("M0 0 L10 0 L10 10 Z");
        directionIndicator.setFill(DEFAULT_RECEIVER_COLOR);

        HBox container = new HBox(displayedText, directionIndicator);
        container.maxWidthProperty().bind(widthProperty().multiply(0.75));
        getChildren().setAll(container);
        setAlignment(Pos.CENTER_LEFT);
    }
}
