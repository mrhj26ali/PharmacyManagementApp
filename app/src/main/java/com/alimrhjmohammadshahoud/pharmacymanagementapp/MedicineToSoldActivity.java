package com.alimrhjmohammadshahoud.pharmacymanagementapp;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_medicine_to_sold);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cart), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        medicineList.add(new Medicine(1, "Paracetamol", 101, 10, 5.0));
        medicineList.add(new Medicine(2, "Ibuprofen", 102, 15, 8.0));
        medicineList.add(new Medicine(3, "Amoxicillin", 103, 20, 12.0));

        RecyclerView recyclerView = findViewById(R.id.recycler_AllMedicines);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MedicineToSoldAdapter(medicineList, medicine -> {
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
}