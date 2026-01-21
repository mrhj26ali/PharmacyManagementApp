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
    private DBHelper dbHelper; // DBHelper
    // List of companies
    private List<Company> companyList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_list);
        dbHelper = new DBHelper(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerCompanies);
        FloatingActionButton fabAddCompany = findViewById(R.id.fab_add_company);

        recyclerView.setLayoutManager(new androidx.recyclerview.widget.GridLayoutManager(this, 2));

        refreshCompanyList();

        adapter = new CompanyAdapter(this, companyList);
        recyclerView.setAdapter(adapter);

        fabAddCompany.setOnClickListener(v -> showAddCompanyDialog());
    }
    private void refreshCompanyList() {
        List<Company> newList = dbHelper.getAllCompanies();
        companyList.clear();
        companyList.addAll(newList);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
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
                        //Save to the Database first

                        long idFromDB = dbHelper.addCompany(name);

                        if (idFromDB != -1) {
                            //Add to the adapter using the REAL ID from the database
                            adapter.addCompany(new Company((int) idFromDB, name));
                            Toast.makeText(this, "Company saved to database!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Error: Could not save to database", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
