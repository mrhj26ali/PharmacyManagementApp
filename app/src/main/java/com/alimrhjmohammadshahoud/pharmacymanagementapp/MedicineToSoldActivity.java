package com.alimrhjmohammadshahoud.pharmacymanagementapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MedicineToSoldActivity extends AppCompatActivity {

    private List<Medicine> cartList = new ArrayList<>();
    private List<Medicine> medicineList = new ArrayList<>();
    private MedicineToSoldAdapter adapter;
    private DBHelper dbHelper;
    SearchView searchAboutMedicine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_medicine_to_sold);
        dbHelper = new DBHelper(this);

        //Fetch all medicines  your DB instead of hardcoding
        medicineList = dbHelper.getAllMedicinesForSale();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cart), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        RecyclerView recyclerView = findViewById(R.id.recycler_AllMedicines);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchAboutMedicine = findViewById(R.id.searchView_about_medicine);
        searchAboutMedicine.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // عند الضغط على زر البحث
                adapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // عند الكتابة مباشرة
                adapter.filter(newText);
                return false;
            }
        });

        /*adapter = new MedicineToSoldAdapter(medicineList, medicine -> {
            boolean found = false;
            for (Medicine item : cartList) {
                if (item.getId() == medicine.getId()) {
                    item.setQuantity(item.getQuantity() + 1);
                    found = true;
                    break;
                }
            }
            if (!found) {

                Medicine cartItem = new Medicine(
                        medicine.getId(),
                        medicine.getName(),
                        medicine.getCompanyId(),
                        1,
                        medicine.getPrice()
                );
                cartList.add(cartItem);
            }
            Toast.makeText(this, medicine.getName() + " added to cart", Toast.LENGTH_SHORT).show();
        });*/
        adapter = new MedicineToSoldAdapter(this,medicineList, (medicine, position) -> {
            if (medicine.getQuantity() > 0) {
                //Update the medicine object's quantity (Logic)
                medicine.setQuantity(medicine.getQuantity() - 1);

                //Add/Update the cartList
                boolean found = false;
                for (Medicine item : cartList) {
                    if (item.getId() == medicine.getId()) {
                        item.setQuantity(item.getQuantity() + 1);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    //Create a copy for the cart with qty 1
                    cartList.add(new Medicine(medicine.getId(), medicine.getName(),
                            medicine.getCompanyId(), 1, medicine.getPrice()));
                }

                //Tell the adapter only ONE item changed !!
                adapter.notifyItemChanged(position);

                Toast.makeText(this, medicine.getName() + " added to cart", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Out of stock!", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(adapter);

        findViewById(R.id.fab_view_cart).setOnClickListener(v -> {
            if (cartList.isEmpty()) {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, CartActivity.class);
                intent.putExtra("cart_items", new ArrayList<>(cartList));
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        List<Medicine> freshData = dbHelper.getAllMedicinesForSale();

        medicineList.clear();
        medicineList.addAll(freshData);

        cartList.clear();

        //Notify the adapter to refresh the screen
        adapter.notifyDataSetChanged();
    }
}
