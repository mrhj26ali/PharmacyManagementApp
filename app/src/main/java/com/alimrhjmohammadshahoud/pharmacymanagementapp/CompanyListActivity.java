package com.alimrhjmohammadshahoud.pharmacymanagementapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CompanyListActivity extends AppCompatActivity {

    private CompanyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerCompanies);
        FloatingActionButton fabAddCompany = findViewById(R.id.fab_add_company);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Company> companyList = new ArrayList<>();
        companyList.add(new Company(1, "Pfizer"));
        companyList.add(new Company(2, "Novartis"));
        companyList.add(new Company(3, "Sanofi"));

        adapter = new CompanyAdapter(this, companyList);
        recyclerView.setAdapter(adapter);

        fabAddCompany.setOnClickListener(v -> showAddCompanyDialog());
    }

    private void showAddCompanyDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_add_company, null);
        EditText editCompanyName = view.findViewById(R.id.editCompanyName);

        new AlertDialog.Builder(this)
                .setTitle("Add Company")
                .setView(view)
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = editCompanyName.getText().toString().trim();
                    if (!name.isEmpty()) {
                        adapter.addCompany(new Company(0, name));
                        Toast.makeText(this, "Company added successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
