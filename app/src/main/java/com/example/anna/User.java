package com.example.anna;

public class User {
    public String displayname;
    public String change;
    public long type;
    public String email;
    public long createdAt;
    private static User instance;

    public User getInstance(){
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }

    public void setData(String name, long tp, String eml){
        instance.displayname = name;
        instance.type = tp;
        instance.email = eml;
    }

    public User (){};
    public User(String displayname,String email,long createdAt,long type){
        this.displayname=displayname;
        this.email=email;
        this.change="No";
        this.createdAt=createdAt;
        this.type = type;
    }

    public long getType() {
        return type;
    }

    public String getDisplayname() {
        return displayname;
    }

    public String getEmail() {
        return email;
    }

    public String getChange() {
        return change;
    }

    public long getCreatedAt() {
        return createdAt;
    }
}
