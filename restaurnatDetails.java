package com.example.foodworld;

public class restaurnatDetails {

    String address,cuisinenames,restaurnatimageUrl,restaurnatname;
    String restaurnatID;

    public restaurnatDetails() {
    }

    public restaurnatDetails(String address, String cuisinenames, String restaurnatimageUrl, String restaurnatname) {
        this.address = address;
        this.cuisinenames = cuisinenames;
        this.restaurnatimageUrl = restaurnatimageUrl;
        this.restaurnatname = restaurnatname;
    }

    public String getRestaurnatID() {
        return restaurnatID;
    }

    public void setRestaurnatID(String restaurnatID) {
        this.restaurnatID = restaurnatID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCuisinenames() {
        return cuisinenames;
    }

    public void setCuisinenames(String cuisinenames) {
        this.cuisinenames = cuisinenames;
    }

    public String getRestaurnatimageUrl() {
        return restaurnatimageUrl;
    }

    public void setRestaurnatimageUrl(String restaurnatimageUrl) {
        this.restaurnatimageUrl = restaurnatimageUrl;
    }

    public String getRestaurnatname() {
        return restaurnatname;
    }

    public void setRestaurnatname(String restaurnatname) {
        this.restaurnatname = restaurnatname;
    }
}
