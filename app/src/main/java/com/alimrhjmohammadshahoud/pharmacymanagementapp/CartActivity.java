package com.alimrhjmohammadshahoud.pharmacymanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
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

public class CartActivity extends AppCompatActivity {
    List<Medicine> Soldmedicines = new ArrayList<>();
    RecyclerView recyclerView;
    TextView totalPriceTextView;
    Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recycler_cart_items);
        totalPriceTextView = findViewById(R.id.text_total_price);
        btnConfirm = findViewById(R.id.btn_confirm);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.medicine_sold), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        Soldmedicines = (ArrayList<Medicine>) intent.getSerializableExtra("cart_items");

        if (Soldmedicines != null) {
            showSoldMedicine();
        } else {
            Toast.makeText(this, "No items received", Toast.LENGTH_SHORT).show();
        }
    }


    private void showSoldMedicine() {
        double total = 0;
        for (Medicine m : Soldmedicines) {
            total += m.getPrice() * m.getQuantity();
        }
        totalPriceTextView.setText("Total: $" + total);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CartAdapter cartAdapter = new CartAdapter(Soldmedicines);
        recyclerView.setAdapter(cartAdapter);

        btnConfirm.setOnClickListener(v -> {
            Toast.makeText(this, "Purchase Confirmed!", Toast.LENGTH_SHORT).show();

            Soldmedicines.clear();
            recyclerView.getAdapter().notifyDataSetChanged();
            updateTotalPrice();


        });
    }
    private void updateTotalPrice() {
        double total = 0;
        for (Medicine m : Soldmedicines) {
            total += m.getPrice() * m.getQuantity();
        }
        totalPriceTextView.setText(String.format("Total: $%.2f", total));
    }
}
