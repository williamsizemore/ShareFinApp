package com.example.sharefinapp;

public class User {
    private String userID;
    private String userEmail;
    private String displayName;

    private String photoURI;

    public User(String userID, String userEmail, String displayName) {
        this.userID = userID;
        this.userEmail = userEmail;
        this.displayName = displayName;
        this.photoURI = ""; // set this to an empty string by default, but allow photos to be uploaded later
    }
    public String getPhotoURI() {
        return photoURI;
    }

    public void setPhotoURI(String photoURI) {
        this.photoURI = photoURI;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
