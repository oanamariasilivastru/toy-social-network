package com.example.demo11;

import com.example.demo11.repository.FriendshipDBRepository;
import com.example.demo11.repository.UserDBRepository;
import com.example.demo11.service.PrietenService;
import com.example.demo11.service.UtilizatoriService;
import com.example.demo11.ui.Console;
import com.example.demo11.validator.PrietenieValidator;
import com.example.demo11.validator.UtilizatorValidator;

public class Main {
    public static void main(String[] args) {
        //@SuppressWarnings("unchecked")

        String url = "jdbc:postgresql://localhost:5432/socialnetwork";
        String username = "postgres";
        String password = "Superman1994";




































        //InMemoryRepository<Long, Utilizator> repoMemory=new InMemoryRepository<>(new UtilizatorValidator());
        //PrietenieRepository<Prietenie> repoFriendship = new PrietenieRepository<>(new PrietenieValidator());
//        UserDBRepository repoDB = new UserDBRepository(url, username, password, new UtilizatorValidator());
//        FriendshipDBRepository repoFriendshipDB = new FriendshipDBRepository(url, username, password, new PrietenieValidator());
//        UtilizatoriService srv_user = new UtilizatoriService(repoDB);
//        PrietenService srv_friend = new PrietenService(repoFriendshipDB);
//        Console ui = new Console(srv_user, srv_friend);
//        ui.run();


    }
}
