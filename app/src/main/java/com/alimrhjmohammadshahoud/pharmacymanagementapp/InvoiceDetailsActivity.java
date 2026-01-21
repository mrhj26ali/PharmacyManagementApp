package com.alimrhjmohammadshahoud.pharmacymanagementapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class InvoiceDetailsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    InvoiceDetailsAdapter adapter;
    List<Sale> saleItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_details);

        recyclerView = findViewById(R.id.recyclerInvoiceDetails);

        int invoiceId = getIntent().getIntExtra("invoiceId", -1);

        saleItems = getDummySales(invoiceId);

        adapter = new InvoiceDetailsAdapter(this, saleItems);
        recyclerView.setAdapter(adapter);
    }

    private List<Sale> getDummySales(int invoiceId) {
        List<Sale> list = new ArrayList<>();

        if (invoiceId == 101) {
            list.add(new Sale(1, 101, 5, "Panadol", 2, 20.0, "2024-01-10"));
            list.add(new Sale(2, 101, 8, "Aspirin", 1, 10.0, "2024-01-10"));
        }
        if (invoiceId == 102) {
            list.add(new Sale(3, 102, 12, "Vitamin C", 3, 45.0, "2024-01-11"));
        }
        if (invoiceId == 103) {
            list.add(new Sale(4, 103, 7, "Cough Syrup", 1, 15.0, "2024-01-12"));
            list.add(new Sale(5, 103, 9, "Amoxicillin", 2, 30.0, "2024-01-12"));
        }
        if (invoiceId == 104) {
            list.add(new Sale(6, 104, 3, "Ibuprofen", 1, 10.0, "2024-01-13"));
        }

        if (invoiceId == 105) {
            list.add(new Sale(7, 105, 11, "Antibiotic", 4, 80.0, "2024-01-14"));
            list.add(new Sale(8, 105, 2, "Zinc", 2, 20.0, "2024-01-14"));
        }

        return list;
    }
}