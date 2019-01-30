package com.example.thomas.augmento;

public class Posts
{
    public String Date, Description, PostImage, ProfileImage, Time, UserId, Username;

    public Posts()
    {

    }

    public Posts(String date, String description, String postImage, String profileImage, String time, String userId, String username) {
        Date = date;
        Description = description;
        PostImage = postImage;
        ProfileImage = profileImage;
        Time = time;
        UserId = userId;
        Username = username;
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

    public String getPostImage() {
        return PostImage;
    }

    public void setPostImage(String postImage) {
        PostImage = postImage;
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
}
