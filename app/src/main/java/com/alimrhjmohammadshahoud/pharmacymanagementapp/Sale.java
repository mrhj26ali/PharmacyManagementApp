package com.alimrhjmohammadshahoud.pharmacymanagementapp;

public class Sale {

    private int saleId;
    private int invoiceId;
    private int medicineId;
    private String medicineName;
    private int quantity;
    private double totalPrice  ;
    private String date;

    public Sale(int saleId, int invoiceId, int medicineId, String medicineName,
                int quantity, double totalPrice, String date) {

        this.saleId = saleId;
        this.invoiceId = invoiceId;
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.date = date;
    }

    // Constructor بدون saleId (عند الإدخال لأول مرة)
    public Sale(int invoiceId, int medicineId, String medicineName,
                int quantity,  double totalPrice, String date) {

        this.invoiceId = invoiceId;
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.date = date;
    }

    // Getters
    public int getSaleId() {
        return saleId;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public int getQuantity() {
        return quantity;
    }
    public double getTotalPrice() {
        return totalPrice;
    }

    public String getDate() {
        return date;
    }
}
