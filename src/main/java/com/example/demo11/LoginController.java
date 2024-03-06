package com.example.demo11;

import com.example.demo11.domain.Message;
import com.example.demo11.domain.Utilizator;
import com.example.demo11.service.InviteService;
import com.example.demo11.service.PrietenService;
import com.example.demo11.service.UtilizatoriService;
import com.example.demo11.utils.WindowManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private UtilizatoriService service;

    private PrietenService prietenService;

    private InviteService inviteService;
    Stage dialogStage;

    Utilizator user;

    @FXML
    private void initialize(){

    }
    public void setService(UtilizatoriService service, PrietenService prietenService, InviteService inviteService, Stage stage){
            this.service = service;
            this.prietenService = prietenService;
            this.inviteService = inviteService;
            this.dialogStage = stage;
    }

    private void clearFields(){
        usernameField.setText("");
        passwordField.setText("");
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hashedBytes = digest.digest(password.getBytes());

            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    @FXML
    public void handleLogin(){
        String username = usernameField.getText();
        String password = passwordField.getText();

        String hashedPassword = hashPassword(password);

        Utilizator user = service.findUserByFirstNameAndPassword(username, hashedPassword);
        System.out.println("Aici in controller");
        System.out.println(user);
        if(user == null){
            MessageAlert.showErrorMessage(null, "Parola si/sau username incorecte!");
        }
        else{
            try{

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("profile-view.fxml"));
                AnchorPane root = (AnchorPane) loader.load();
                Stage dialogStage1 = new Stage();
                dialogStage1.setTitle("Profilul utilizatorului");
                dialogStage1.initModality(Modality.WINDOW_MODAL);

                Scene scene = new Scene(root);
                dialogStage1.setScene(scene);

                UsersViewController usersViewController = loader.getController();
                usersViewController.setService(service, dialogStage1, user, prietenService, inviteService);

                dialogStage1.show();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }
}
