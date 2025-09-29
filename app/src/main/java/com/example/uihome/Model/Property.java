package com.example.uihome.Model;

public class Property {
    private String price;
    private String location;
    private String details;
    private int imageRes;

    public Property(String price, String location, String details, int imageRes) {
        this.price = price;
        this.location = location;
        this.details = details;
        this.imageRes = imageRes;
    }

    public String getPrice() { return price; }
    public String getLocation() { return location; }
    public String getDetails() { return details; }
    public int getImageRes() { return imageRes; }
}
