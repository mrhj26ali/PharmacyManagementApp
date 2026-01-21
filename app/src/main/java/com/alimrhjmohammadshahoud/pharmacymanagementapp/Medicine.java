package com.alimrhjmohammadshahoud.pharmacymanagementapp;

import java.io.Serializable;

public class Medicine implements Serializable {

    private int id;
    private String name;
    private int companyId;
    private int quantity;
    private double price;
    private String barcode;


    public Medicine(int id,String barcode, String name, int companyId, int quantity, double price) {
        this.id = id;
        this.barcode = barcode;
        this.name = name;
        this.companyId = companyId;
        this.quantity = quantity;
        this.price = price;
    }


    public int getId() {
        return id;
    }
    public String getBarcode() { return barcode; }

    public String getName() {
        return name;
    }
    public double getPrice() {
        return price;
    }
    public int getCompanyId() {
        return companyId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void  setName(String name){this.name=name;}
    public void setQuantity(int quantity){this.quantity=quantity;}
    public void setBarcode(String barcode) { this.barcode = barcode; }

    public void setPrice(double price)
    {
        this.price=price;
    }


}
