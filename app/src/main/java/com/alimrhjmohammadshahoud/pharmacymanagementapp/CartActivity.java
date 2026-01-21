package com.alimrhjmohammadshahoud.pharmacymanagementapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartActivity extends AppCompatActivity {
    private List<Medicine> Soldmedicines;
    private RecyclerView recyclerView;
    private TextView totalPriceTextView;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        dbHelper = new DBHelper(this);
        totalPriceTextView = findViewById(R.id.text_total_price);
        recyclerView = findViewById(R.id.recycler_cart_items);

        Soldmedicines = MedicineToSoldActivity.cartList;

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        CartAdapter adapter = new CartAdapter(this, Soldmedicines);
        recyclerView.setAdapter(adapter);

        updateTotalPrice();

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // If cart has items, show the dialog
                if (Soldmedicines != null && !Soldmedicines.isEmpty()) {
                    showExitConfirmationDialog();
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        findViewById(R.id.btn_confirm).setOnClickListener(v -> {
            if (Soldmedicines == null || Soldmedicines.isEmpty()) {
                Toast.makeText(this, "Cart is empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            double total = 0;
            for (Medicine m : Soldmedicines) total += m.getPrice() * m.getQuantity();

            if (dbHelper.commitSale(Soldmedicines, total)) {
                Toast.makeText(this, "Sale Confirmed Successfully!", Toast.LENGTH_SHORT).show();
                MedicineToSoldActivity.cartList.clear();
                finish();
            }
        });

        View btnCancel = findViewById(R.id.btn_cancel);
        if (btnCancel != null) {
            btnCancel.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        }
    }

    private void showExitConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Exit Confirmation")
                .setMessage("Your cart contains items. Would you like to keep them for later or clear the cart?")
                .setPositiveButton("Keep Items", (dialog, which) -> {
                    finish(); // Keeps static list intact
                })
                .setNegativeButton("Clear Cart", (dialog, which) -> {
                    MedicineToSoldActivity.cartList.clear();
                    finish();
                })
                .setNeutralButton("Stay Here", null)
                .show();
    }

    public void updateTotalPrice() {
        double total = 0;
        if (Soldmedicines != null) {
            for (Medicine m : Soldmedicines) {
                total += m.getPrice() * m.getQuantity();
            }
        }
        totalPriceTextView.setText(String.format("Total: $%.2f", total));
    }
}