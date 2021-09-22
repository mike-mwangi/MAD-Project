package com.example.madproject.Models;

public class Like {
    private String userID;

    public Like() {
    }

    public Like(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
