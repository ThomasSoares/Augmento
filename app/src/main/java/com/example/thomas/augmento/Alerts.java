package com.example.thomas.augmento;

public class Alerts {

    private String username, alertMessage, postImage, profileImage;

    public Alerts(String username, String alertMessage, String postImage, String profileImage) {
        this.username = username;
        this.alertMessage = alertMessage;
        this.postImage = postImage;
        this.profileImage = profileImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
