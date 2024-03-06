package com.example.demo11;

import com.example.demo11.domain.Utilizator;
import com.example.demo11.repository.FriendshipDBRepository;
import com.example.demo11.repository.InviteDBRepository;
import com.example.demo11.repository.MessageDBRepository;
import com.example.demo11.repository.UserDBRepository;
import com.example.demo11.repository.paging.Page;
import com.example.demo11.repository.paging.Pageable;
import com.example.demo11.repository.paging.PageableImplementation;
import com.example.demo11.service.InviteService;
import com.example.demo11.service.PrietenService;
import com.example.demo11.service.UtilizatoriService;
import com.example.demo11.validator.PrietenieValidator;
import com.example.demo11.validator.UtilizatorValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.scene.Scene;

public class StartApplication extends Application{
    private UtilizatoriService service;

    private PrietenService prietenService;

    private InviteService inviteService;

    public static void main(String[] args) {
        //System.out.println("ok");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        String url = "jdbc:postgresql://localhost:5432/socialnetwork";
        String username = "postgres";
        String password = "Superman1994";

        UserDBRepository repoDB = new UserDBRepository(url, username, password, new UtilizatorValidator());
//        Pageable pageable = new PageableImplementation(1, 99);
//        Page<Utilizator> page = repoDB.findAllPaging(pageable);
//        page.getContent().forEach(System.out::println);
        FriendshipDBRepository repoFriendshipDB = new FriendshipDBRepository(url, username, password, new PrietenieValidator());
        InviteDBRepository inviteDBRepository = new InviteDBRepository(url, username, password);
        MessageDBRepository messageDBRepository = new MessageDBRepository(url, username, password, repoDB);
        UtilizatoriService srv_user = new UtilizatoriService(repoDB, messageDBRepository);
        PrietenService srv_friend = new PrietenService(repoFriendshipDB, srv_user);
        InviteService srv_invite = new InviteService(inviteDBRepository);


        this.service = srv_user;
        this.prietenService = srv_friend;
        this.inviteService = srv_invite;


        initView(primaryStage);
        primaryStage.setWidth(800);
        //primaryStage.setFullScreen(true);
        primaryStage.show();


    }

    private void initView(Stage primaryStage) throws IOException {
        try {
            FXMLLoader userLoader = new FXMLLoader();
            userLoader.setLocation(getClass().getResource("users-view.fxml"));
            AnchorPane userLayout = userLoader.load();
            primaryStage.setScene(new Scene(userLayout));

            UserController userController = userLoader.getController();
            userController.setUtilizatorService(service, prietenService, inviteService);

        }
        catch(RuntimeException ex){
            ex.getMessage();
        }
    }

}
