package com.alimrhjmohammadshahoud.pharmacymanagementapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SalesHistory extends AppCompatActivity {

    RecyclerView recyclerView;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_history);

        recyclerView = findViewById(R.id.recycler_invoices);

        dbHelper = new DBHelper(this);

        SalesHistoryAdapter adapter = new SalesHistoryAdapter(this);
        recyclerView.setAdapter(adapter);

    }
}
