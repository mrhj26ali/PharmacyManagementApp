package com.alimrhjmohammadshahoud.pharmacymanagementapp;

public class Invoice {

    private int id;
    private String date;
    private double totalPrice;

    public Invoice(int id, String date, double totalPrice) {
        this.id = id;
        this.date = date;
        this.totalPrice = totalPrice;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}