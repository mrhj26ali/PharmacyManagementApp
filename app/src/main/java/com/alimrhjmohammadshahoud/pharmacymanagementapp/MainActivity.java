package com.alimrhjmohammadshahoud.pharmacymanagementapp;

import android.content.Intent; // ضروري للانتقال بين الشاشات
import android.os.Bundle;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // استخدام النسخة المتوافقة
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // تعريف المتغيرات
    private Button button_ManageInventory;
    private Button button_InitiateSale;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupClickListeners();
        handleWindowInsets();
    }

    private void initializeViews() {
        button_ManageInventory = findViewById(R.id.button_manage_inventory);
        button_InitiateSale = findViewById(R.id.button_initiate_sale);
        toolbar = findViewById(R.id.topAppBar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    private void setupClickListeners() {
        button_ManageInventory.setOnClickListener(mng -> manageInventory());
        button_InitiateSale.setOnClickListener(initiate -> initiateSale());
    }

    private void manageInventory() {

        Intent intent = new Intent(MainActivity.this, CompanyListActivity.class);
        startActivity(intent);
    }

    private void initiateSale() {

        Intent intent = new Intent(MainActivity.this, MedicineToSoldActivity.class);
        startActivity(intent);
    }

    private void handleWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}