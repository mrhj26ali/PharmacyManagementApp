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
                String name = editName.getText().toString().trim();
                String priceStr = editPrice.getText().toString().trim();
                String qtyStr = editQty.getText().toString().trim();
                String barcode = editBarcode.getText().toString().trim();
                validationEditText(editBarcode);
                validationEditText(editName);
                validationEditText(editPrice);
                validationEditText(editQty);
                try {
                    double price = Double.parseDouble(priceStr);
                    int qty = Integer.parseInt(qtyStr);

                    if (dbHelper.isBarcodeExists(barcode)) {
                        editBarcode.setError("Barcode already exists!");
                        return;
                    }

                    Medicine newMed = new Medicine(0, barcode, name, companyId, qty, price);
                    if (dbHelper.addMedicine(newMed)) {
                        refreshMedicineList();
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
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

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Edit Medicine")
                .setView(view)
                .setPositiveButton("Update", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(d -> {
            Button updateButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            updateButton.setOnClickListener(v -> {
                String newName = editName.getText().toString().trim();
                String newPriceStr = editPrice.getText().toString().trim();

                if (newName.isEmpty()) { editName.setError("Name required"); return; }
                if (newPriceStr.isEmpty()) { editPrice.setError("Price required"); return; }
                try {
                    double newPrice = Double.parseDouble(newPriceStr);

                    if (newName.equals(med.getName()) && newPrice == med.getPrice()) {
                        editName.requestFocus();
                        editPrice.requestFocus();
                        Toast.makeText(this, "No changes made", Toast.LENGTH_SHORT).show();
                    } else {
                        dbHelper.updateMedicinePrice(med.getId(), newPrice);
                        dbHelper.updateMedicineName(med.getId(), newName);

                        refreshMedicineList();
                        Toast.makeText(this, "Updated successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    editPrice.setError("Invalid number");
                }
            });
        });
        dialog.show();
    }

    public void showAddQuantityToMedicine(Medicine med, int position) {
        View view = getLayoutInflater().inflate(R.layout.activity_dialog_add_quantity_to_medicine, null);
        EditText editQty = view.findViewById(R.id.add_Quantity);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add Stock")
                .setView(view)
                .setPositiveButton("Add", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(d -> {
            Button addButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            addButton.setOnClickListener(v -> {
                String qtyStr = editQty.getText().toString().trim();
                if (qtyStr.isEmpty()) {
                    editQty.setError("Enter quantity");
                    return;
                }
                try {
                    int added = Integer.parseInt(qtyStr);
                    dbHelper.updateMedicineQuantity(med.getId(), med.getQuantity() + added);
                    refreshMedicineList();
                    dialog.dismiss();
                } catch (Exception e) {
                    editQty.setError("Invalid number");
                }
            });
        });
        dialog.show();
    }
    public void showConfirmeToDeleteMedicine(Medicine med, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete " + med.getName())
                .setMessage("Confirm delete?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    dbHelper.deleteMedicine(med.getId());
                    refreshMedicineList();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    private void validationEditText(EditText editText)
    {
        if(editText.getText().toString().isEmpty())
        {
            editText.setError("Required");
            return;
        }
    }
}