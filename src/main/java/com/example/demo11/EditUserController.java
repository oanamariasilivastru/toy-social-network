package com.example.demo11;

import com.example.demo11.domain.Utilizator;
import com.example.demo11.service.UtilizatoriService;
import com.example.demo11.validator.ValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

public class EditUserController {
    @FXML
    private TextField textFieldFirstName;

    @FXML
    private TextField textFieldLastName;

    @FXML
    private PasswordField TextFieldPassword;

    private UtilizatoriService service;

    Stage dialogStage;

    Utilizator user;

    @FXML
    private void initialize() {
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


    public void setService(UtilizatoriService service, Stage stage, Utilizator u){
        this.service = service;
        this.dialogStage = stage;
        this.user = u;
        if(null !=u){
            setFields(u);
        }
    }

    @FXML
    public void handleSave(){
        String first_name = textFieldFirstName.getText();
        String last_name = textFieldLastName.getText();
        String password = hashPassword(TextFieldPassword.getText());
        Utilizator u = new Utilizator(first_name, last_name, password);
        if (null == this.user)
            saveUser(u);
        else {
            u.setId(user.getId());
            updateUser(u);
        }

    }
    private void updateUser(Utilizator user){
        try{
            boolean rez = this.service.updateUser(user);
            if(rez == true)
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Modificare utilizator", "Utilizatorul a fost modificat!");

        }catch (ValidationException e){
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
        dialogStage.close();
    }

    private void saveUser(Utilizator u){
        try{
            user = this.service.saveUser(u.getFirstName(), u.getLastName(), u.getPassword());
            if(user == null)
                dialogStage.close();
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Salvare user", "Userul a fost salvat");
        }catch(ValidationException e){
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
        dialogStage.close();
    }

    private void clearFields(){
        textFieldFirstName.setText("");
        textFieldLastName.setText("");
        TextFieldPassword.setText("");
    }

    private void setFields(Utilizator u){
        textFieldLastName.setText(u.getLastName());
        textFieldFirstName.setText(u.getFirstName());
    }

    @FXML
    public void handleCancel(){
        dialogStage.close();
    }
}
