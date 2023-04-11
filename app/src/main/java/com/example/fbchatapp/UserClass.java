package com.example.fbchatapp;

public class UserClass {

    String ID,UserName,Email,Password;


    public UserClass(String ID, String userName, String email, String password) {
        this.ID = ID;
        UserName = userName;
        Email = email;
        Password = password;
    }

    public UserClass(){
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
