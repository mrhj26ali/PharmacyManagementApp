package com.alimrhjmohammadshahoud.pharmacymanagementapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MedicineToSoldActivity extends AppCompatActivity {
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

        adapter = new MedicineToSoldAdapter(this, medicineList, (medicine, position) -> {
            if (medicine.getQuantity() > 0) {
                medicine.setQuantity(medicine.getQuantity() - 1);

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

        findViewById(R.id.fab_view_cart).setOnClickListener(v -> {
            startActivity(new Intent(this, CartActivity.class));
        });
        findViewById(R.id.fab_new_cart).setOnClickListener(v -> {
            if (cartList.isEmpty()) {
                Toast.makeText(this, "Cart is already empty", Toast.LENGTH_SHORT).show();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("New Cart")
                        .setMessage("Are you sure you want to clear the current cart and start a new one?")
                        .setPositiveButton("Yes, Clear", (dialog, which) -> {
                            cartList.clear();
                            onResume();
                            Toast.makeText(this, "New cart started", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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