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
        dbHelper = new DBHelper(this);

        int invoiceId = getIntent().getIntExtra("invoiceId", -1);

        if (invoiceId != -1) {
            saleItems = dbHelper.getSalesByInvoice(invoiceId);

            adapter = new InvoiceDetailsAdapter(this, saleItems);

            // Set to Grid Layout with 2 columns
            recyclerView.setLayoutManager(new androidx.recyclerview.widget.GridLayoutManager(this, 2));
            recyclerView.setAdapter(adapter);
        }
    }


}