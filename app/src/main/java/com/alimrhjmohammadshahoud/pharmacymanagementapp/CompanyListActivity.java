package com.alimrhjmohammadshahoud.pharmacymanagementapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    Button addButton;
    private DBHelper dbHelper;
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

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add New Company")
                .setView(view)
                .setPositiveButton("Add", null) // نضع null هنا لنبرمجه يدوياً بالأسفل
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(dialogInterface -> {
            Button addButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            addButton.setOnClickListener(v -> {
                String name = editCompanyName.getText().toString().trim();

                if (name.isEmpty()) {
                    // إظهار خطأ داخل الـ EditText نفسه
                    editCompanyName.setError("Company name is required!");
                    Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
                } else {
                    // إذا كان الحقل ممتلئاً، نقوم بالحفظ
                    long idFromDB = dbHelper.addCompany(name);

                    if (idFromDB != -1) {
                        adapter.addCompany(new Company((int) idFromDB, name));
                        Toast.makeText(this, "Company saved!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss(); // نغلق الحوار فقط عند النجاح
                    } else {
                        Toast.makeText(this, "Error saving to database", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

        dialog.show();
    }
}
