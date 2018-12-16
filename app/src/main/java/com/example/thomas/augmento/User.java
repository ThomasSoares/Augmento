package com.example.thomas.augmento;




public class User
{
    private String username;
    private String firstName;
    private String lastName;
    private String email;

    public User()
    {
        //empty constructor to help creating objects
    }

    public User(String username, String firstName, String lastName, String email)
    {
        this.username=username;
        this.firstName=firstName;
        this.lastName=lastName;
        this.email=email;
    }

    //setters
    public void setUsername(String username)
    {
        this.username=username;
    }
    public void setFirstName(String firstName)
    {
        this.firstName=firstName;
    }
    public void setLastName(String lastName)
    {
        this.lastName=lastName;
    }
    public void setEmail(String email)
    {
        this.email=email;
    }

    //getters
    public String getUsername()
    {
        return username;
    }
    public String getFirstName()
    {
        return firstName;
    }
    public String getLastName()
    {
        return lastName;
    }
    public String getEmail()
    {
        return email;
    }
}
