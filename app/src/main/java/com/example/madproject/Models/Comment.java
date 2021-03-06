package com.example.madproject.Models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Comment {
    private String userID;
    private String commentText;
    String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Comment() {
    }

    public Comment(String userID, String commentText, String date) {
        this.userID = userID;
        this.commentText = commentText;
        this.date = date;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
}
