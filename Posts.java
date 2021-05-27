package com.example.myapplication;

import androidx.annotation.NonNull;
import java.io.Serializable;

public class Posts implements Serializable {
    private String title;
    private double price;
    private String description;
    private String contact;
    private String email;

    public Posts(){

    }

    public Posts(String title, double price, String description, String contact, String email) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.contact = contact;
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NonNull
    @Override
    public String toString() {
        return this.title + " $" + String.format("%.2f", this.price);
    }
}
