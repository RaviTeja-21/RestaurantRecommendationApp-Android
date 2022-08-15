package com.example.foodworld;

public class cuisineDetails {

    String name,imageURL;
    String cusineID;

    public cuisineDetails(String name, String imageURL) {
        this.name = name;
        this.imageURL = imageURL;
    }

    public cuisineDetails() {
    }

    public String getCusineID() {
        return cusineID;
    }

    public void setCusineID(String cusineID) {
        this.cusineID = cusineID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
