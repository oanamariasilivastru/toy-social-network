package com.example.demo11.domain;

import java.util.Date;

public class UserFrienshipTuple {
    private String firstName;
    private String lastName;
    private Date friendshipDate;

    public UserFrienshipTuple(String firstName, String lastName, Date friendshipDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.friendshipDate = friendshipDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getFriendshipDate() {
        return friendshipDate;
    }
}
