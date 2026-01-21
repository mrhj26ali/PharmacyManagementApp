package com.alimrhjmohammadshahoud.pharmacymanagementapp;

import android.annotation.SuppressLint;
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

import java.util.List;

public class MedicineListActivity extends AppCompatActivity {

    private MedicineAdapter adapter;
    private DBHelper dbHelper;
    private int companyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_list);
        dbHelper = new DBHelper(this);
        companyId = getIntent().getIntExtra("company_id", -1);

        RecyclerView recyclerView = findViewById(R.id.recycler_Medicines);
        FloatingActionButton fabAddMedicine = findViewById(R.id.fab_add_medicine);

        adapter = new MedicineAdapter(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        refreshMedicineList();
        fabAddMedicine.setOnClickListener(v -> showAddMedicineDialog());
    }

    private void refreshMedicineList() {
        List<Medicine> list = dbHelper.getMedicinesByCompany(companyId);
        adapter.submitList(list);
    }

    @SuppressLint("MissingInflatedId")
    private void showAddMedicineDialog() {
        View view = getLayoutInflater().inflate(R.layout.activity_dialog_add_medicine, null);
        EditText editName = view.findViewById(R.id.addMedicineName);
        EditText editPrice = view.findViewById(R.id.addMedicinePrice);
        EditText editQty = view.findViewById(R.id.addMedicineQuantity);
        EditText editBarcode = view.findViewById(R.id.addMedicineID);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add Medicine")
                .setView(view)
                .setPositiveButton("Add", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(d -> {
            Button addButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            addButton.setOnClickListener(v -> {
                if (editName.getText().toString().isEmpty() || editPrice.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    String barcode = editBarcode.getText().toString().trim();
                    String name = editName.getText().toString().trim();
                    double price = Double.parseDouble(editPrice.getText().toString());
                    int qty = Integer.parseInt(editQty.getText().toString());

                    if (dbHelper.isBarcodeExists(barcode)) {
                        Toast.makeText(this, "Barcode exists!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Medicine newMed = new Medicine(0, barcode, name, companyId, qty, price);
                    if (dbHelper.addMedicine(newMed)) {
                        refreshMedicineList();
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
                }
            });
        });
        dialog.show();
    }

    public void showEditDialog(Medicine med, int position) {
        View view = getLayoutInflater().inflate(R.layout.activity_dialog_edit_price_or_name_maedicine, null);
        EditText editPrice = view.findViewById(R.id.editMedicinePrice);
        EditText editName = view.findViewById(R.id.changeMedicineName);

        editPrice.setText(String.valueOf(med.getPrice()));
        editName.setText(med.getName());

        new AlertDialog.Builder(this)
                .setTitle("Edit Medicine")
                .setView(view)
                .setPositiveButton("Update", (dialog, which) -> {
                    try {
                        double newPrice = Double.parseDouble(editPrice.getText().toString());
                        String newName = editName.getText().toString().trim();

                        dbHelper.updateMedicinePrice(med.getId(), newPrice);
                        dbHelper.updateMedicineName(med.getId(), newName);

                        refreshMedicineList();
                        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) { e.printStackTrace(); }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    public void showAddQuantityToMedicine(Medicine med, int position) {
        View view = getLayoutInflater().inflate(R.layout.activity_dialog_add_quantity_to_medicine, null);
        EditText editQty = view.findViewById(R.id.add_Quantity);

        new AlertDialog.Builder(this)
                .setTitle("Add Stock")
                .setView(view)
                .setPositiveButton("Add", (dialog, which) -> {
                    try {
                        int added = Integer.parseInt(editQty.getText().toString());
                        dbHelper.updateMedicineQuantity(med.getId(), med.getQuantity() + added);
                        refreshMedicineList();
                    } catch (Exception e) { e.printStackTrace(); }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    public void showConfirmeToDeleteMedicine(Medicine med, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Are you sure you want to delete this Medicine?")
                .setView(view)
                .setPositiveButton("Confirm", (dialog, which) -> {
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
