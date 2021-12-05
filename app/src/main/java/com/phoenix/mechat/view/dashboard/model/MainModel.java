package com.phoenix.mechat.view.dashboard.model;

public class MainModel {

    String email;
    String UID;
    String PushID;
    String Name;
    String ImageURL;
    //model
    public MainModel(){

    }
    //constructor

    public MainModel(String email, String UID, String pushID, String name, String imageURL) {
        this.email = email;
        this.UID = UID;
        PushID = pushID;
        Name = name;
        ImageURL = imageURL;
    }


    //getter and setter

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getPushID() {
        return PushID;
    }

    public void setPushID(String pushID) {
        PushID = pushID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }
}
