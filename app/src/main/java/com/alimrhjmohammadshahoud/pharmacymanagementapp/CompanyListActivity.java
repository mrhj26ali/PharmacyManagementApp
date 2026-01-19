package com.alimrhjmohammadshahoud.pharmacymanagementapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alimrhjmohammadshahoud.pharmacymanagementapp.CompanyAdapter;
import com.alimrhjmohammadshahoud.pharmacymanagementapp.Company;
import com.alimrhjmohammadshahoud.pharmacymanagementapp.R;
import android.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class CompanyListActivity extends AppCompatActivity {

    private CompanyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerCompanies);
        Button btnAddCompany = findViewById(R.id.btnAddCompany);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Company> companyList = new ArrayList<>();
        companyList.add(new Company(1, "Pfizer"));
        companyList.add(new Company(2, "Novartis"));
        companyList.add(new Company(3, "Sanofi"));

        adapter = new CompanyAdapter(companyList, company -> {
            // Navigation to partner screen
            Intent intent = new Intent(this, MedicinesActivity.class);
            intent.putExtra("company_id", company.getId());
            intent.putExtra("company_name", company.getName());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        btnAddCompany.setOnClickListener(v -> showAddCompanyDialog());
    }

    private void showAddCompanyDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_add_company, null);
        EditText editCompanyName = view.findViewById(R.id.editCompanyName);

        new AlertDialog.Builder(this)
                .setTitle("Add Company")
                .setView(view)
                .setPositiveButton("Add", (d, which) -> {
                    String name = editCompanyName.getText().toString().trim();
                    if (!name.isEmpty()) {
                        adapter.addCompany(new Company(0, name));
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
