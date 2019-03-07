package com.example.thomas.augmento;

import java.util.ArrayList;
import java.util.List;

public class StickerViewLayout
{
    private String Date, Description, ProfileImage, Time, UserId, Username;
    private String[] stickerArray;
    private String[] positionXArray;
    private String[] positionYArray;
    private String[] positionZArray;

    public StickerViewLayout(String date, String description, String profileImage, String time, String userId, String username, String[] stickerArray, String[] positionXArray,String[] positionYArray,String[] positionZArray) {
        Date = date;
        Description = description;
        ProfileImage = profileImage;
        Time = time;
        UserId = userId;
        Username = username;
        this.stickerArray = stickerArray;
        this.positionXArray=positionXArray;
        this.positionYArray=positionYArray;
        this.positionZArray=positionZArray;
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


    public String[] getStickerArray() {
        return stickerArray;
    }

    public void setStickerArray(String[] stickerArray) {
        this.stickerArray = stickerArray;
    }

    public String[] getPositionXArray() {
        return positionXArray;
    }

    public void setPositionXArray(String[] positionXArray) {
        this.positionXArray = positionXArray;
    }

    public String[] getPositionYArray() {
        return positionYArray;
    }

    public void setPositionYArray(String[] positionYArray) {
        this.positionYArray = positionYArray;
    }

    public String[] getPositionZArray() {
        return positionZArray;
    }

    public void setPositionZArray(String[] positionZArray) {
        this.positionZArray = positionZArray;
    }
}
