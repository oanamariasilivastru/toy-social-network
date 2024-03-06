package com.example.demo11;

import com.example.demo11.domain.Utilizator;
import com.example.demo11.repository.paging.Page;
import com.example.demo11.repository.paging.Pageable;
import com.example.demo11.repository.paging.PageableImplementation;
import com.example.demo11.service.InviteService;
import com.example.demo11.service.PrietenService;
import com.example.demo11.service.UtilizatoriService;
import com.example.demo11.utils.ChatManager;
import com.example.demo11.utils.Observer;
import com.example.demo11.utils.UserChangeEvent;
import com.example.demo11.utils.WindowManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserController implements Observer<UserChangeEvent> {
    UtilizatoriService service;
    PrietenService prietenieService;
    InviteService inviteService;
    ObservableList<Utilizator> model = FXCollections.observableArrayList();


    @FXML
    TableView<Utilizator> tableView;

    @FXML
    TableColumn<Utilizator,Long> tableColumnID;
    @FXML
    TableColumn<Utilizator,String> tableColumnFirstName;
    @FXML
    TableColumn<Utilizator,String> tableColumnLastName;

    @FXML
    private TextField textFieldUser1;

    @FXML
    private TextField textFieldUser2;

    @FXML
    private TextField textFieldNumarPagina;

    @FXML
    private TextField textFieldRanduriPagina;

    private Pageable pageable = new PageableImplementation(1,99);

    Integer pageNumber = 1;

    public void setUtilizatorService(UtilizatoriService userService, PrietenService prietenService, InviteService inviteService){
        service = userService;
        service.addObserver(this);
        prietenieService = prietenService;
        this.inviteService = inviteService;
        initModel();
    }

    @FXML
    public void initialize(){
        tableColumnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        tableView.setItems(model);

        textFieldRanduriPagina.textProperty().addListener(o->handlePagination());


        //initModel();
    }

    private void initModel() {
//        Iterable<Utilizator> users = service.getUserList();
//        List<Utilizator> userTaskList = StreamSupport.stream(users.spliterator(), false)
//                .collect(Collectors.toList());
//        model.setAll(userTaskList);
        Page<Utilizator> usersPage = service.getUserPaging(pageable);
        List<Utilizator> usersList = usersPage.getContent().collect(Collectors.toList());
        System.out.println(usersList);
        model.setAll(usersList);

    }
    public void handleDeleteMessage(ActionEvent actionEvent){
        Utilizator selected = (Utilizator) tableView.getSelectionModel().getSelectedItem();
        if(selected != null) {
            Boolean deleted = service.deleteUser(selected.getId());
            if (deleted == true) {
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Delete", "Utilizatorul a fost sters cu succes!");

            }
        }
        else MessageAlert.showErrorMessage(null,"Nu ati selectat niciun utilizator");


    }

    public void update(UserChangeEvent userChangeEvent){
        initModel();
    }


    @FXML
    public  void handleUpdateMessage(ActionEvent ev){
        Utilizator selected = tableView.getSelectionModel().getSelectedItem();
        if(selected != null){
            showUserEditDialog(selected);
        }else
            MessageAlert.showErrorMessage(null, "Nu ati selectat niciun utilizator!");

    }
    @FXML
    public void handleAddMessage(ActionEvent ev){
        showUserEditDialog(null);
    }

    @FXML
    public void handleViewProfile(ActionEvent ev){
        Utilizator selected = tableView.getSelectionModel().getSelectedItem();
        if(selected != null){
            showUserViewProfile(selected);
        }else
            MessageAlert.showErrorMessage(null, "Nu ati selectat niciun utilizator!");

    }

    public void showUserViewProfile(Utilizator utilizator){
        try{
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("profile-view.fxml"));


            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Profilul utilizatorului");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            UsersViewController usersViewController = loader.getController();
            usersViewController.setService(service, dialogStage, utilizator, prietenieService, inviteService);
            WindowManager.openWindow(usersViewController);
            WindowManager.showAllWindows();
            WindowManager.closeWindow(usersViewController);

            dialogStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void handleLogin(ActionEvent ev){
        showLoginUserDialog();

    }

    public void showLoginUserDialog(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("login.fxml"));

            AnchorPane root = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Login");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            LoginController loginController = loader.getController();
            loginController.setService(service, prietenieService, inviteService, dialogStage);
            dialogStage.show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showUserEditDialog(Utilizator utilizator){
        try{
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("edit-users.fxml"));


            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Message");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            EditUserController editUserController = loader.getController();
            editUserController.setService(service, dialogStage, utilizator);
            dialogStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void showConversationDialog() {

        String firstNameUser1 = textFieldUser1.getText();
        String firstNameUser2 = textFieldUser2.getText();

        if (firstNameUser1.isEmpty() || firstNameUser2.isEmpty()) {
            MessageAlert.showErrorMessage(null, "Completați ambele câmpuri pentru utilizatori!");
            return;
        }

        Utilizator user1 = service.findUserByFirstName(firstNameUser1);
        Utilizator user2 = service.findUserByFirstName(firstNameUser2);


        if (user1 == null || user2 == null) {
            MessageAlert.showErrorMessage(null, "Unul sau ambii utilizatori nu există!");
            return;
        }
        service.sendUseri(user1, user2);
        MessageController messageController = new MessageController();

        messageController.setService(service, user1, user2);

        // Afiseaza fereastra
        Stage conversationStage = new Stage();
        conversationStage.setTitle("Conversație cu " + firstNameUser1 + " " + firstNameUser2);
        conversationStage.setScene(new Scene(new VBox(messageController.getvBox()), 400, 400));
        conversationStage.show();
        ChatManager.openWindow(conversationStage, messageController);

        conversationStage.setOnCloseRequest(event -> {
            // La închiderea ferestrei, apelează metoda pentru închiderea ferestrei în ChatManager
             ChatManager.closeWindow(conversationStage, messageController);
        });

    }
    public void handleSeeConversation(){
        showConversationDialog();
    }
    protected void onHelloButtonClick() {
        //welcomeText.setText("Welcome to JavaFX Application!");
    }

    private void handlePagination() {
        try {
            String pageSizeString = textFieldRanduriPagina.getText();
            Integer pageSize = 99;

            if (!pageSizeString.isEmpty()) {
                pageSize = Integer.parseInt(pageSizeString);
            }

            Pageable pageable = new PageableImplementation(pageNumber, pageSize);
            Page<Utilizator> usersPage = service.getUserPaging(pageable);
            List<Utilizator> usersList = usersPage.getContent().collect(Collectors.toList());
            model.setAll(usersList);


        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public  void handlePreviousPage(ActionEvent ev){
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
    public void handleNextPage(ActionEvent ev){
        if(pageNumber < 99)
        {
            pageNumber++;
            handlePagination();
        }
        else{
            MessageAlert.showErrorMessage(null, "Nu se poate trece la pagina urmatoare!");
        }
    }

}