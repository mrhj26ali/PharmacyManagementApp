package com.alimrhjmohammadshahoud.pharmacymanagementapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class CompanyListActivity extends AppCompatActivity {

    private CompanyAdapter adapter;
    private DBHelper dbHelper;
    private List<Company> companyList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_list);
        dbHelper = new DBHelper(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerCompanies);
        FloatingActionButton fabAddCompany = findViewById(R.id.fab_add_company);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        refreshCompanyList();

        adapter = new CompanyAdapter(this, companyList);
        recyclerView.setAdapter(adapter);

        fabAddCompany.setOnClickListener(v -> showAddCompanyDialog());
    }

    private void refreshCompanyList() {
        List<Company> newList = dbHelper.getAllCompanies();
        companyList.clear();
        companyList.addAll(newList);
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    private void showAddCompanyDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_add_company, null);
        EditText editName = view.findViewById(R.id.editCompanyName);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add New Company")
                .setView(view)
                .setPositiveButton("Add", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(dI -> {
            Button addButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            addButton.setOnClickListener(v -> {
                String name = editName.getText().toString().trim();
                if (name.isEmpty()) {
                    editName.setError("Required");
                } else {
                    long id = dbHelper.addCompany(name);
                    if (id != -1) {
                        adapter.addCompany(new Company((int) id, name));
                        dialog.dismiss();
                    }
                }
            });
        });
        dialog.show();
    }

    public void showEditCompanyDialog(Company company, int position) {
        // Use dialog_add_company so we use the correct layout and IDs
        View view = getLayoutInflater().inflate(R.layout.dialog_add_company, null);
        EditText editName = view.findViewById(R.id.editCompanyName);
        editName.setText(company.getName());

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Edit Company")
                .setView(view)
                .setPositiveButton("Update", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(d -> {
            Button updateButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            updateButton.setOnClickListener(v -> {
                String newName = editName.getText().toString().trim();
                if (newName.isEmpty()) {
                    editName.setError("Required");
                } else if (dbHelper.updateCompanyName(company.getId(), newName)) {
                    company.setName(newName);
                    adapter.notifyItemChanged(position);
                    dialog.dismiss();
                }
            });
        });
        dialog.show();
    }

    public void showConfirmDeleteCompany(Company company, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete " + company.getName())
                .setMessage("Are you sure? All associated medicines will be deleted.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    if (dbHelper.deleteCompany(company.getId())) {
                        companyList.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, companyList.size());
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}