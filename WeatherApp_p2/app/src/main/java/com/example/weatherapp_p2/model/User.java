package com.example.weatherapp_p2.model;

public class User {
    private  String uid;
    private String name;
    private String mail;

    public User(){ }

    public User(String uid, String name, String mail){
        this.uid = uid;
        this.name = name;
        this.mail = mail;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
