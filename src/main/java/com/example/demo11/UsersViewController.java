package com.example.demo11;

import com.example.demo11.domain.Invite;
import com.example.demo11.domain.Prietenie;
import com.example.demo11.domain.Utilizator;
import com.example.demo11.repository.paging.Page;
import com.example.demo11.repository.paging.Pageable;
import com.example.demo11.repository.paging.PageableImplementation;
import com.example.demo11.service.InviteService;
import com.example.demo11.service.PrietenService;
import com.example.demo11.service.UtilizatoriService;
import com.example.demo11.utils.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UsersViewController implements Observer<InviteChangeEvent> {
    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    ObservableList<Utilizator> modelFriends = FXCollections.observableArrayList();

    ObservableList<Utilizator> modelUsers = FXCollections.observableArrayList();

    ObservableList<Utilizator> modelRequests = FXCollections.observableArrayList();

    @FXML
    private TableView<Utilizator> friendsTable;
    @FXML
    TableColumn<Utilizator,String> nameColumn;
    @FXML
    TableColumn<Utilizator,String> surnameColumn;
    @FXML
    private TableView<Utilizator> usersTable;
    @FXML
    TableColumn<Utilizator,String> nameColumnUser;

    @FXML
    TableColumn<Utilizator,String> surnameColumnUser;
    @FXML
    private TableView<String> inviteTable;
    @FXML
    private TableColumn<String, String> invitationColumn;

    @FXML
    private TextField textFieldRanduriPagina;

    @FXML
    private Button btnCancelInvite;

    private UtilizatoriService service;
    private PrietenService prietenService;

    private InviteService inviteService;
    private List<Utilizator> initPrietenieList = new ArrayList<>();

    private List<Utilizator> initInviteList = new ArrayList<>();

    private ObservableList<String> stringData =  FXCollections.observableArrayList();

    Stage dialogStage;

    Utilizator user;

    private Pageable pageable = new PageableImplementation(1,99);

    Integer pageNumber = 1;

    public void setService(UtilizatoriService service, Stage stage, Utilizator u, PrietenService prietenService, InviteService inviteService){
        this.service = service;
        this.dialogStage = stage;
        this.user = u;
        this.prietenService = prietenService;
        this.inviteService = inviteService;
        this.inviteService.addObserver(this);
        if(null !=u) {
            setFields(u);
        }
        initialize();
    }
    public void update(InviteChangeEvent userChangeEvent){
        initialize();
    }

    public void handleSendInvite(ActionEvent actionEvent){
        Utilizator selected = (Utilizator) usersTable.getSelectionModel().getSelectedItem();
        if(selected != null) {
            if(hasInvitation(user.getId(), selected.getId()) == false){
                this.inviteService.createInvite(user.getId(), selected.getId(), "PENDING");
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Cerere", "Cerere trimisa cu succes!");
                WindowManager.refreshAllWindows();

            }
            else
            {
                MessageAlert.showErrorMessage(null, "Deja s-a trimis o cerere spre utilizator!");
            }

        }
        else MessageAlert.showErrorMessage(null, "Nu ati selectat niciun utilizator");
    }

    @FXML
    public void refresh() {
        //System.out.println("Before refresh - Friends: " + modelFriends.size() + ", Users: " + modelUsers.size() + ", Requests: " + stringData.size());
        initializeFriends();
        initializeUsers();
        initializeRequests();
        //System.out.println("After refresh - Friends: " + modelFriends.size() + ", Users: " + modelUsers.size() + ", Requests: " + stringData.size());
    }

    @FXML
    public  void initializeFriends(){
        modelFriends.clear();
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        initializelistfriends();
        friendsTable.setItems(modelFriends);
    }

    @FXML
    public void initializeUsers(){
        modelUsers.clear();
        nameColumnUser.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        surnameColumnUser.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        initializelistusers();
        usersTable.setItems(modelUsers);
    }

    @FXML
    public void initializeRequests(){
        modelRequests.clear();
        stringData.clear();
        initializelistRequests();
        invitationColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()));
        inviteTable.setItems(stringData);
        inviteTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


    }
    @FXML
    public void initialize(){
        initializeFriends();
        initializeUsers();
        initializeRequests();
        textFieldRanduriPagina.textProperty().addListener(o->handlePagination());


    }
    private void initializelistusers() {
        if (this.service != null) {
            Iterable<Utilizator> users = this.service.getUserList();
            List<Utilizator> usersList = StreamSupport.stream(users.spliterator(), false)
                    .collect(Collectors.toList());
            usersList.removeIf(u -> u.equals(user));
            initPrietenieList.forEach(usersList::remove);
            modelUsers.setAll(usersList);
        }
    }
    private void initializelistfriends() {
        if (this.prietenService != null) {
            initPrietenieList.clear();
            List<Prietenie> prietenieList = prietenService.getFriendshipsList();

            if (prietenieList != null) {
                for (Prietenie p : prietenieList) {
                    Long id;
                    if (p.getFriend1().equals(user.getId())) {
                        id = p.getFriend2();
                        Utilizator friendUser = service.findUser(id);
                        initPrietenieList.add(friendUser);
                    }
                    if (p.getFriend2().equals(user.getId())) {
                        id = p.getFriend1();
                        Utilizator friendUser = service.findUser(id);
                        initPrietenieList.add(friendUser);
                    }
                }
            }

            //System.out.println(initPrietenieList);
            modelFriends.setAll(initPrietenieList);

        }
    }

    private void initializelistRequests(){
        if (this.inviteService != null) {
            initInviteList.clear();
            List<Invite> invitationList = this.inviteService.getInvitations();
            //System.out.println(invitationList);

            if (invitationList != null) {
                for (Invite inv : invitationList) {
                    if(inv.getToInvite().equals(user.getId()) && inv.getStatus().equals("PENDING")){
                        Utilizator u = service.findUser(inv.getFromInvite());
                        String string = u.getFirstName() + " " + u.getLastName() + " a trimis o cerere de prietenie.";
                        stringData.add(string);
                        initInviteList.add(u);

                    }

                }
            }

        }
    }

    private StringProperty getInvitationMessage(Utilizator utilizator){
        String string = utilizator.getFirstName() + " " + utilizator.getLastName() + " a trimis o cerere de prietenie.";
        return new SimpleStringProperty(string);
    }

    public boolean hasInvitation(Long fromUser, Long toUser) {
        List<Invite> invitationList = this.inviteService.getInvitations();
        for (Invite inv : invitationList) {
            if (inv.getFromInvite().equals(fromUser) && inv.getToInvite().equals(toUser)
                && inv.getStatus().equals("PENDING")) {
                return true; // Invitația există deja
            }
        }
        return false; // Invitația nu există în listă
    }
    @FXML

    // Adaugă aici metodele pentru gestionarea acțiunilor utilizatorului

    private void setFields(Utilizator u){
        nameField.setText(u.getFirstName());
        surnameField.setText(u.getLastName());

    }

    @FXML
    private void handleCancelInvite(ActionEvent event) {
        String selected = (String) inviteTable.getSelectionModel().getSelectedItem();
        if(selected != null){
            String[] parts = selected.split(" ");
            String firstName = parts[0];
            String lastName = parts[1];
            Utilizator u = service.findUserByNames(firstName, lastName);
            Invite invite = new Invite(u.getId(), user.getId(), "PENDING");
            Invite inv1 = inviteService.updateInvite(invite, "REJECTED");
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Cerere de prietenie", "Cererea de prietenie a fost respinsa!");
            //WindowManager.refreshAllWindows();
        }
        else{
            MessageAlert.showErrorMessage(null, "Nu ati selectat niciun utilizator!");
        }
    }
    @FXML
    private void handleAcceptInvite() {
        String selected = (String) inviteTable.getSelectionModel().getSelectedItem();
        if(selected != null){
            String[] parts = selected.split(" ");
            String firstName = parts[0];
            String lastName = parts[1];
            Utilizator u = service.findUserByNames(firstName, lastName);
            Invite invite = new Invite(u.getId(), user.getId(), "PENDING");
            Invite inv1 = inviteService.updateInvite(invite, "APPROVED");
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDateTime = now.format(formatter);
            prietenService.createFriendship(u.getId(), user.getId(),formattedDateTime);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Cerere de prietenie", "Cererea de prietenie a acceptata!");
            WindowManager.refreshAllWindows();
        }
        else{
            MessageAlert.showErrorMessage(null, "Nu ati selectat niciun utilizator!");
        }
    }

    private void handlePagination(){
        try{
            String pageSizeString = textFieldRanduriPagina.getText();
            Integer pageSize = 99;

            if (!pageSizeString.isEmpty()) {
                pageSize = Integer.parseInt(pageSizeString);
            }
            Pageable pageable = new PageableImplementation(pageNumber, pageSize);
            Page<Utilizator> prieteniePage = prietenService.getFriendshipsPaging(pageable, user);
            List<Utilizator> usersList = prieteniePage.getContent().collect(Collectors.toList());
            modelFriends.setAll(usersList);

            Page<Utilizator> usersPage = service.getAnotherPaging(pageable, user);
            List<Utilizator> userAnotherList = usersPage.getContent().collect(Collectors.toList());
            modelUsers.setAll(userAnotherList);
            stringData.clear();
            Page<Utilizator> invitePage = inviteService.getInvitationsPaging(pageable, user);
            List<Utilizator> inviteList = invitePage.getContent().collect(Collectors.toList());
            for(Utilizator u : inviteList){
                String string = u.getFirstName() + " " + u.getLastName() + " a trimis o cerere de prietenie.";
                stringData.add(string);
            }
            modelRequests.setAll(inviteList);

        }catch (NumberFormatException e){
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
