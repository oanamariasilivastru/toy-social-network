package com.example.demo11.domain;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Prietenie extends Entity<Tuple<Long,Long>> {
    Tuple<Long, Long> prietenie;
    LocalDate friendsFrom;


    @Override
    public String toString() {
        return "Prietenie{" +
                "prietenie=" + prietenie +
                ", friendsFrom=" + friendsFrom +
                '}';
    }

    public Prietenie(Long user1, Long user2, LocalDate friendsFrom){
        super(new Tuple<>(user1, user2));
        this.prietenie = new Tuple<>(user1, user2);
        this.friendsFrom = friendsFrom;


    }

    /***
     * Constructer pentru prietenie
     * @param user1: id ul userul 1
     * @param user2: id-ul userul 2
     * @return un nou obiect Prietenie
     */
    public static Prietenie create(Long user1, Long user2, String friendsFromString) {
        String errMsg = "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String[] rez = friendsFromString.split("-");
        if(rez.length !=3)
            throw new IllegalArgumentException("Formatul de data nu este valid! ");
        if(rez[0].length() != 4 && rez[1].length() != 2 && rez[2].length() !=2)
            throw new IllegalArgumentException("Formatul de data este invalid!");
        if(Integer.parseInt(rez[0]) < 1970 || Integer.parseInt(rez[0]) > 2023)
            errMsg+="Anul nu este valid!";
        if(Integer.parseInt(rez[1]) < 1 || Integer.parseInt(rez[1]) > 12)
            errMsg+="Luna nu e valida.";
        if(Integer.parseInt(rez[2]) < 1 || Integer.parseInt(rez[2]) > 31)
            errMsg+="Ziua nu e valida.";
        if(errMsg.length() > 0)
            throw new IllegalArgumentException(errMsg);
        LocalDate friendsFrom = LocalDate.parse(friendsFromString, formatter);
        return new Prietenie(user1, user2, friendsFrom);
    }

    /***
     * Verifica daca exista pritenie pentru 2 useri
     * @param id_user: tuplu - o pereche cu id-urile userilor
     * @return boolean
     */
    public boolean exists(Tuple<Long, Long> id_user) {
        return prietenie != null && ((prietenie.getLeft() != null && prietenie.getLeft().equals(id_user)) || (prietenie.getRight() != null && prietenie.getRight().equals(id_user)));
    }

    /***
     * Getter pentru prietenie
     * @return Prietenie
     */
    public Tuple<Long, Long> getPrietenie() {
        return prietenie;
    }

    /***
     * Getter pentru prietenul din stanga
     * @return un id de tip Long
     */
    public Long getFriend1(){
        return prietenie.getLeft();
    }

    /***
     * Getter pentru prietenul din dreapta
     * @return un id de tip Long
     */
    public Long getFriend2(){
        return prietenie.getRight();
    }

    /***
     * Setter pentru prietenie
     * @param prietenie: obiect de tip prietenie
     */
    public void setPrietenie(Tuple<Long, Long> prietenie) {
        this.prietenie = prietenie;
    }

    /**
     *
     * @return the date when the friendship was created
     */
    public LocalDate getDate() {
        return friendsFrom;
    }

    public void setFriend1(Long value){
        prietenie.setLeft(value);
    }

    public void setFriend2(Long value){
        prietenie.setRight(value);
    }
}
