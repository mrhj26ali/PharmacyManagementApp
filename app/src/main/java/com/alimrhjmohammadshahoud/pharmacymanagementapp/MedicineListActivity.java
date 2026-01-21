package com.alimrhjmohammadshahoud.pharmacymanagementapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
public class MedicineListActivity extends AppCompatActivity {

    FloatingActionButton fabAddMedicine;
    List<Medicine> filteredMedicines = new ArrayList<>();
    MedicineAdapter adapter;
    DBHelper dbHelper;
    int companyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_list);
        dbHelper = new DBHelper(this);
        companyId = getIntent().getIntExtra("company_id", -1);

        RecyclerView recyclerView = findViewById(R.id.recycler_Medicines);
        fabAddMedicine = findViewById(R.id.fab_add_medicine);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshMedicineList();
        //We no more need filtering by company
        /*filteredMedicines = filterMedicinesByCompany(companyId);*/
        adapter = new MedicineAdapter(this, filteredMedicines);
        recyclerView.setAdapter(adapter);

        fabAddMedicine.setOnClickListener(v -> showAddMedicineDialog());
    }
    private void refreshMedicineList() {
        filteredMedicines = dbHelper.getMedicinesByCompany(companyId);
        adapter = new MedicineAdapter(this, filteredMedicines);
        RecyclerView recyclerView = findViewById(R.id.recycler_Medicines);
        recyclerView.setAdapter(adapter);
    }
    //We removed this function because we don't need it anymore
    /*private List<Medicine> filterMedicinesByCompany(int id) {
        List<Medicine> result = new ArrayList<>();
        for (Medicine med : allMedicines) {
            if (med.getCompanyId() == id) {
                result.add(med);
            }
        }
        return result;
    }*/
    @SuppressLint("MissingInflatedId")
    private void showAddMedicineDialog() {
        View view = getLayoutInflater().inflate(R.layout.activity_dialog_add_medicine, null);
        EditText editMedicineName = view.findViewById(R.id.addMedicineName);
        EditText editMedicinePrice = view.findViewById(R.id.addMedicinePrice);
        EditText editMedicineQuantity = view.findViewById(R.id.addMedicineQuantity);
        EditText editMedicineId = view.findViewById(R.id.addMedicineID);

        new AlertDialog.Builder(this)
                .setTitle("Add Medicine")
                .setView(view)
                .setPositiveButton("Add", (dialog, which) -> {
                    try {
                        int id = Integer.parseInt(editMedicineId.getText().toString());
                        String name = editMedicineName.getText().toString();
                        double price = Double.parseDouble(editMedicinePrice.getText().toString());
                        int qty = Integer.parseInt(editMedicineQuantity.getText().toString());

                        // Check if ID exists in DB
                        if (dbHelper.isMedicineIdExists(id)) {
                            Toast.makeText(this, "Error: ID " + id + " already exists!", Toast.LENGTH_LONG).show();
                            return;
                        }

                        Medicine newMed = new Medicine(id, name, companyId, qty, price);

                        //Save to Database
                        boolean success = dbHelper.addMedicine(newMed);

                        if (success) {
                            adapter.addMedicine(newMed);
                            Toast.makeText(this, "Medicine added successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Failed to save to Database", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    public void showEditDialog(Medicine med, int position) {
        View view = getLayoutInflater().inflate(R.layout.activity_dialog_edit_price_maedicine, null);
        EditText editMedicinePrice = view.findViewById(R.id.editMedicinePrice);
        editMedicinePrice.setText(String.valueOf(med.getPrice()));
        new AlertDialog.Builder(this)
                .setTitle("Edit Price")
                .setView(view)
                .setPositiveButton("Update", (dialog, which) -> {
                    try {
                        double newPrice = Double.parseDouble(editMedicinePrice.getText().toString());
                        adapter.changePriceMedicine(med,newPrice);
                        adapter.notifyItemChanged(position);
                        Toast.makeText(this, "Price updated successfully", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    @SuppressLint("MissingInflatedId")
    public void showAddQuantityToMedicine(Medicine med, int position) {
        View view = getLayoutInflater().inflate(R.layout.activity_dialog_add_quantity_to_medicine, null);
        EditText editMedicineQuantity = view.findViewById(R.id.add_Quantity);
        editMedicineQuantity.setText(String.valueOf(0));
        new AlertDialog.Builder(this)
                .setTitle("Enter the quantity to add")
                .setView(view)
                .setPositiveButton("Update", (dialog, which) -> {
                    try {
                        int quantity_Added = Integer.parseInt(editMedicineQuantity.getText().toString());
                        int new_Quantity = quantity_Added + med.getQuantity();
                        adapter.addToQuantity(med,new_Quantity);
                        Toast.makeText(this, "Quantity updated successfully", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    public void showConfirmeToDeleteMedicine(Medicine med, int position) {
        View view = getLayoutInflater().inflate(R.layout.activity_confim_delete_medicine, null);
        new AlertDialog.Builder(this)
                .setTitle("Are you sure you want to delete this Medicine?")
                .setView(view)
                .setPositiveButton("Confirme", (dialog, which) -> {
                    try {
                        adapter.deleteMedicine(med,position);
                        Toast.makeText(this, "Medicine deleted successfully", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}

