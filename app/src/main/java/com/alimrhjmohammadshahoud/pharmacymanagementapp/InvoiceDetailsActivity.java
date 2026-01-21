package com.alimrhjmohammadshahoud.pharmacymanagementapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class InvoiceDetailsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    InvoiceDetailsAdapter adapter;
    List<Sale> saleItems;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_details);

        recyclerView = findViewById(R.id.recyclerInvoiceDetails);
        dbHelper = new DBHelper(this); // Make sure you declare DBHelper dbHelper at the top

        int invoiceId = getIntent().getIntExtra("invoiceId", -1);

        if (invoiceId != -1) {
            // Fetch real items for this specific invoice
            saleItems = dbHelper.getSalesByInvoice(invoiceId);

            adapter = new InvoiceDetailsAdapter(this, saleItems);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        }
    }


}