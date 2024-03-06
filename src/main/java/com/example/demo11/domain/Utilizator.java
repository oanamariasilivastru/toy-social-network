package com.example.demo11.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Utilizator extends Entity<Long> {
    private String firstName;
    private String lastName;
    private List<Utilizator> friends;

    private String password;

    public Utilizator(String firstName, String lastName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Utilizator> getFriends() {
        return this.friends;
    }

    public void setFriends(List<Utilizator> friends) {
        this.friends = friends;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addFriend(Utilizator utilizator) {
        if (this.friends == null) {
            this.friends = new ArrayList();
        }

        if (!this.friends.contains(utilizator)) {
            this.friends.add(utilizator);
        }

    }

    public void deleteFriend(Utilizator utilizator) {
        Optional.ofNullable(this.friends).ifPresent((f) -> {
            f.removeIf((u) -> {
                return u.equals(utilizator);
            });
        });
    }

    public String toString() {
        List<String> ListaNume = this.friends != null ? (List)this.friends.stream().map((user) -> {
            String var10000 = user.getId().toString();
            return var10000 + " : " + user.getFirstName() + " - " + user.getLastName();
        }).collect(Collectors.toList()) : new ArrayList();
        String var10000 = String.valueOf(this.getId());
        return "Utilizator{ ID: " + var10000 + " firstName='" + this.firstName + "', lastName='" + this.lastName + ", Parola: " + this.password +  ", Lista prieteni: " + String.valueOf(ListaNume) + "}";
    }

    public boolean isfriend(Utilizator utilizator) {
        return this.friends != null ? this.friends.contains(utilizator) : false;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilizator that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getFirstName(), that.getFirstName()) && Objects.equals(getLastName(), that.getLastName()) && Objects.equals(getFriends(), that.getFriends()) && Objects.equals(getPassword(), that.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getFirstName(), getLastName(), getFriends(), getPassword());
    }
}
