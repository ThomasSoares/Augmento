package com.example.thomas.augmento;

import android.graphics.Bitmap;

public class Post
{
    private String username, description;
    //private Bitmap image,profile;

    public Post()
    {

    }

    public Post(String username, String description/*, Bitmap image, Bitmap profile*/)
    {
        this.username=username;
        this.description=description;
        //this.image=image;
        //this.profile=profile;
    }

    //GETTERS
    public String getUsername()
    {
        return username;
    }
    public String getDescription()
    {
        return description;
    }
    /*
    public Bitmap getImage()
    {
        return image;
    }
    public Bitmap getProfile()
    {
        return profile;
    }*/

    //SETTERS
    public void setUsername(String username)
    {
        this.username=username;
    }
    public void setDescription(String description)
    {
        this.description=description;
    }
    /*
    public void setImage(Bitmap image)
    {
        this.image=image;
    }
    public void setProfile(Bitmap profile)
    {
        this.profile=profile;
    }*/
}
