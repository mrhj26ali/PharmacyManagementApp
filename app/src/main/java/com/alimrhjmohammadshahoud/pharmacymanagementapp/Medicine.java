package com.alimrhjmohammadshahoud.pharmacymanagementapp;

public class Medicine {

    private int id;
    private String name;
    private int companyId;
    private int quantity;
    private double price;

    public Medicine(int id, String name, int companyId, int quantity, double price) {
        this.id = id;
        this.name = name;
        this.companyId = companyId;
        this.quantity = quantity;
        this.price = price;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCompanyId() {
        return companyId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setPrice(double price)
    {
        this.price=price;
    }

    public double getPrice() {
        return price;
    }
}
