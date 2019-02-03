package com.example.thomas.augmento;




public class User
{
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String description;

    public User()
    {
        //empty constructor to help creating objects
    }

    public User(String username, String firstName, String lastName, String email, String description)
    {
        this.username=username;
        this.firstName=firstName;
        this.lastName=lastName;
        this.email=email;
        this.description=description;
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
    public void setDescription(String description){
        this.description=description;
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
    public String getDescription(){
        return description;
}
}
