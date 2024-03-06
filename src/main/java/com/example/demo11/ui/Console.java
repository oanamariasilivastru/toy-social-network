package com.example.demo11.ui;

import com.example.demo11.domain.UserFrienshipTuple;
import com.example.demo11.service.PrietenService;
import com.example.demo11.service.UtilizatoriService;
import com.example.demo11.validator.ValidationException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

public class Console {
    private UtilizatoriService srv_user;
    private PrietenService srv_friend;

    public Console(UtilizatoriService srv_user, PrietenService srv_friend) {
        this.srv_user = srv_user;
        this.srv_friend = srv_friend;
    }


    public void addUtilizator() {
        String firstName, LastName, password;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduceti numele: ");
        firstName = scanner.nextLine();
        System.out.println("Introduceti prenumele");
        LastName = scanner.nextLine();
        System.out.println("Introduceti parola: ");
        password = scanner.nextLine();
        srv_user.saveUser(firstName, LastName, password);
        System.out.println("Utilizator adaugat cu succes! ");

    }

    public void deleteUtilizator() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Introduceti id: ");
        long id = scanner.nextLong();
        if(srv_user.deleteUser(id)) {
            srv_friend.deleteFrienshipsUtilizator(id);
            System.out.println("Utilizator sters cu succes! ");
        }
        else
            System.out.println("Utilizatorul nu exista! ");

    }

    public void addPrieten() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Dati id 1: " );
        long user1 = scanner.nextLong();
        scanner.nextLine();
        System.out.println("Dati id 2: ");
        long user2 = scanner.nextLong();
        scanner.nextLine();
        System.out.println("Dati data (YYYY-MM-DD): ");
        String data = scanner.nextLine();
        srv_user.createFriendship(user1, user2);
        srv_friend.createFriendship(user1, user2, data);

    }

    public void deletePrieten() {
        Scanner scanner = new Scanner(System.in);
        long user1 = scanner.nextLong();
        long user2 = scanner.nextLong();
        srv_friend.deleteFriendship(user1, user2);
        System.out.println("Prietenie stearsa cu succes!");

    }

    public void showNumberComunities(){
        System.out.println(srv_user.comunitate());
    }

    public void showLargestCommuniy(){
        if(srv_user.sociabil().size() < 2)
            System.out.println("Nu avem o comunitate de minim 2 persoane.");
        else
            srv_user.sociabil().forEach(System.out::println);
    }

    public void showUserList(){
        srv_user.getUserList().forEach(System.out::println);
    }

    public void showFriendshipsList(){
        srv_friend.getFriendshipsList().forEach(System.out::println);
    }

    public void getFriendshipsforUser(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Dati id-ul utilizatorului: ");
        long user = scanner.nextLong();
        System.out.println("Dati luna: (format: MM):");
        String luna = scanner.next();
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("MM");
            sdf.setLenient(false);
            sdf.parse(luna);
            List<UserFrienshipTuple> lista = srv_user.getFrienshipsforUser(user, luna);
            if(lista.size() > 0)
                lista.forEach(u -> System.out.println("Nume: " + u.getFirstName() + " | " + "Prenume: " + u.getLastName() + " | " + "Data prieteniei: " + u.getFriendshipDate()));
            else
                System.out.println("Nu avem prietenii!");
        } catch (ParseException e){
            throw new IllegalArgumentException(e);
        }

    }

    public void printMenu() {
        System.out.println("Meniu:\n");
        System.out.println("1 - Adaugare utilizator");
        System.out.println("2 - Stergere utilizator");
        System.out.println("3 - Adaugare prietenie");
        System.out.println("4 - Stergere prietenie");
        System.out.println("5 - Afisare numar de comunitati");
        System.out.println("6 - Afisare cea mai mare comunitate");
        System.out.println("7 - Afisare lista de utilizatori");
        System.out.println("8 - Afisare lista de prietenii");
        System.out.println("9 - Afisare prietenii dintr-o luna pentru un utilizator");
        System.out.println("10 - Iesire");
    }

    public void run() {
        //srv_user.populate();
        while (true) {
            printMenu();
            int optiune;
            Scanner scanner = new Scanner(System.in);
            System.out.println("Introduceti o optiune: ");
            optiune = scanner.nextInt();
            try {
                switch (optiune) {
                    case 1:
                        addUtilizator();
                        break;
                    case 2:
                        deleteUtilizator();
                        break;
                    case 3:
                        addPrieten();
                        break;
                    case 4:
                        deletePrieten();
                        break;
                    case 5:
                        showNumberComunities();
                        break;
                    case 6:
                        showLargestCommuniy();
                        break;
                    case 7:
                        showUserList();
                        break;
                    case 8:
                        showFriendshipsList();
                        break;
                    case 9:
                        getFriendshipsforUser();
                        break;
                    case 10:
                        return;
                    default:
                        break;
                }
            }
            catch(ValidationException err){
                System.out.println(err.getMessage());
            }
            catch(IllegalArgumentException err){
                System.out.println(err.getMessage());
            }


        }
    }
}
