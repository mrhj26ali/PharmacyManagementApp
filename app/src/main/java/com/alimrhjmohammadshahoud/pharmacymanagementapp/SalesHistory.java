package com.alimrhjmohammadshahoud.pharmacymanagementapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
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

        // Fetch REAL data from DB
        List<Invoice> realInvoices = dbHelper.getAllInvoices();

        // Pass the real list to the adapter
        SalesHistoryAdapter adapter = new SalesHistoryAdapter(this, realInvoices);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
