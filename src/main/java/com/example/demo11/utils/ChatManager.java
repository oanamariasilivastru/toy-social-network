package com.example.demo11.utils;

import com.example.demo11.ConversationView;
import com.example.demo11.MessageController;
import com.example.demo11.UsersViewController;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class ChatManager {

    private static List<Stage> openWindows = Collections.synchronizedList(new ArrayList<>());
    private static List<MessageController> messageControllers = Collections.synchronizedList(new ArrayList<>());

    public static void openWindow(Stage window, MessageController messageController) {
        Platform.runLater(() -> {
            openWindows.add(window);
            messageControllers.add(messageController);
            window.show(); // Deschide fereastra
        });
    }

    public static void closeWindow(Stage window, MessageController messageController) {
        Platform.runLater(() -> {
            openWindows.remove(window);
            messageControllers.remove(messageController);
            window.close(); // Închide fereastra
        });
    }

    public static void notifyMessageControllers() {
        for (MessageController controller : messageControllers) {
            controller.refresh();
        }
    }


    // ... alte metode existente

}
