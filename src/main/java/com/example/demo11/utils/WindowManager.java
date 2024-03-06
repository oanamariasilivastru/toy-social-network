package com.example.demo11.utils;

import com.example.demo11.UserController;
import com.example.demo11.UsersViewController;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WindowManager {
    private static List<UsersViewController> openWindows = new ArrayList<>();

    public static void openWindow(UsersViewController window) {
        openWindows.add(window);
        // Deschide fereastra...
    }

    public static void closeWindow(UsersViewController window) {
        openWindows.remove(window);
        // Închide fereastra...
    }

    public static void refreshAllWindows() {
        for (UsersViewController window : openWindows) {
            window.refresh();
        }
    }
    public static void showAllWindows() {
        for (UsersViewController window : openWindows) {
            System.out.println(window);
        }
    }
}
