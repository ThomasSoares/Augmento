package com.example.thomas.augmento;

public class ProfilePosts
{
    public String postImage, userID;

    public ProfilePosts()
    {

    }

    public ProfilePosts(String postImage, String userID)
    {
        this.postImage = postImage;
        this.userID=userID;
    }

    public String getPostImage()
    {
        return postImage;
    }

    public void setPostImage(String postImage)
    {
        this.postImage = postImage;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
