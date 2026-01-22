package com.alimrhjmohammadshahoud.pharmacymanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MedicineToSoldActivity extends AppCompatActivity {
    // Global cart list to persist data between activities
    public static List<Medicine> cartList = new ArrayList<>();
    private List<Medicine> medicineList = new ArrayList<>();
    private MedicineToSoldAdapter adapter;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_to_sold);
        dbHelper = new DBHelper(this);

        RecyclerView recyclerView = findViewById(R.id.recycler_AllMedicines);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Initialize adapter with Add to Cart logic
        adapter = new MedicineToSoldAdapter(this, medicineList, (medicine, position) -> {
            if (medicine.getQuantity() > 0) {
                // 1. Reduce display quantity
                medicine.setQuantity(medicine.getQuantity() - 1);

                // 2. Add or Update in Cart
                boolean found = false;
                for (Medicine item : cartList) {
                    if (item.getId() == medicine.getId()) {
                        item.setQuantity(item.getQuantity() + 1);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    cartList.add(new Medicine(medicine.getId(), medicine.getBarcode(), medicine.getName(),
                            medicine.getCompanyId(), 1, medicine.getPrice()));
                }

                adapter.notifyItemChanged(position);
                Toast.makeText(this, medicine.getName() + " added to cart", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Out of stock!", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(adapter);

        // Connect SearchView to Adapter Filter
        SearchView searchView = findViewById(R.id.searchView_about_medicine);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });

        // Navigation Buttons
        findViewById(R.id.fab_view_cart).setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class)));

        findViewById(R.id.fab_new_cart).setOnClickListener(v -> {
            if (cartList.isEmpty()) {
                Toast.makeText(this, "Cart is already empty", Toast.LENGTH_SHORT).show();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("New Cart")
                        .setMessage("Are you sure you want to clear the current cart?")
                        .setPositiveButton("Yes, Clear", (dialog, which) -> {
                            cartList.clear();
                            onResume(); // Re-sync the view with DB
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Sync display stock: Database Quantity - Cart Quantity
        List<Medicine> freshData = dbHelper.getAllMedicinesForSale();
        for (Medicine dbMed : freshData) {
            for (Medicine cartMed : cartList) {
                if (dbMed.getId() == cartMed.getId()) {
                    dbMed.setQuantity(dbMed.getQuantity() - cartMed.getQuantity());
                }
            }
        }
        adapter.updateList(freshData);
    }
}