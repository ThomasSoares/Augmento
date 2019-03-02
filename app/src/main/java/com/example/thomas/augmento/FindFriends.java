package com.example.thomas.augmento;

public class FindFriends
{
    public String profileImage, firstName, lastName, username;

    public FindFriends(String profileImage, String firstName, String lastName, String username)
    {
        this.profileImage = profileImage;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    public FindFriends()
    {

    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
