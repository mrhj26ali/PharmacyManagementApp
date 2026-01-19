package com.alimrhjmohammadshahoud.pharmacymanagementapp;

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

        CompanyAdapter adapter = new CompanyAdapter(companyList);
        recyclerView.setAdapter(adapter);

        btnAddCompany.setOnClickListener(v -> showAddCompanyDialog(adapter));
    }
    private void showAddCompanyDialog(CompanyAdapter adapter) {
        View view = getLayoutInflater().inflate(R.layout.dialog_add_company, null);

        EditText editCompanyName = view.findViewById(R.id.editCompanyName);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add Company")
                .setView(view)
                .setPositiveButton("Add", (d, which) -> {
                    String name = editCompanyName.getText().toString().trim();
                    if (!name.isEmpty()) {
                        Company company = new Company(0, name);
                        adapter.addCompany(company);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();

        dialog.show();
    }


}
