package com.example.thomas.augmento;

public class StickerViewLayout
{
    private String Date, Description, ProfileImage, Time, UserId, Username;
    //private String [] stickerArray;

    public StickerViewLayout(String date, String description, String profileImage, String time, String userId, String username/*, String[] stickerArray*/) {
        Date = date;
        Description = description;
        ProfileImage = profileImage;
        Time = time;
        UserId = userId;
        Username = username;
        //this.stickerArray = stickerArray;
    }

    public StickerViewLayout()
    {

    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    /*
    public String[] getStickerArray() {
        return stickerArray;
    }

    public void setStickerArray(String[] stickerArray) {
        this.stickerArray = stickerArray;
    }*/
}
