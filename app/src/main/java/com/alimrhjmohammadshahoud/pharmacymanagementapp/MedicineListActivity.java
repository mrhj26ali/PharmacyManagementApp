package com.alimrhjmohammadshahoud.pharmacymanagementapp;

import android.annotation.SuppressLint;
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
        fabAddMedicine.setOnClickListener(v -> showAddMedicineDialog());

    }
    private void refreshMedicineList() {
        filteredMedicines = dbHelper.getMedicinesByCompany(companyId);
        adapter = new MedicineAdapter(this, filteredMedicines);
        RecyclerView recyclerView = findViewById(R.id.recycler_Medicines);
        recyclerView.setAdapter(adapter);
    }

    @SuppressLint("MissingInflatedId")
    private void showAddMedicineDialog() {

        View view = getLayoutInflater().inflate(R.layout.activity_dialog_add_medicine, null);

        EditText editMedicineName = view.findViewById(R.id.addMedicineName);
        EditText editMedicinePrice = view.findViewById(R.id.addMedicinePrice);
        EditText editMedicineQuantity = view.findViewById(R.id.addMedicineQuantity);
        EditText editMedicineId = view.findViewById(R.id.addMedicineID);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add Medicine")
                .setView(view)
                .setPositiveButton("Add", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(d -> {

            Button addButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

            addButton.setOnClickListener(v -> {

                if (!validEditText(editMedicineName) |
                        !validEditText(editMedicinePrice) |
                        !validEditText(editMedicineQuantity) |
                        !validEditText(editMedicineId)) {

                    Toast.makeText(this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show();
                    return; // لا تكمل
                }

                try {


                    int id = Integer.parseInt(editMedicineId.getText().toString());
                    String name = editMedicineName.getText().toString().trim();
                    double price = Double.parseDouble(editMedicinePrice.getText().toString());
                    int qty = Integer.parseInt(editMedicineQuantity.getText().toString());

                    if (dbHelper.isMedicineIdExists(id)) {
                        Toast.makeText(this, "Error: ID " + id + " already exists!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    Medicine newMed = new Medicine(id, name, companyId, qty, price);

                    boolean success = dbHelper.addMedicine(newMed);

                    if (success) {
                        adapter.addMedicine(newMed);
                        Toast.makeText(this, "Medicine added successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss(); // إغلاق بعد نجاح الإضافة
                    } else {
                        Toast.makeText(this, "Failed to save to Database", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(this, "Invalid input format", Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialog.show();
    }


    public void showEditDialog(Medicine med, int position) {
        View view = getLayoutInflater().inflate(R.layout.activity_dialog_edit_price_or_name_maedicine, null);
        EditText editMedicinePrice = view.findViewById(R.id.editMedicinePrice);
        EditText changeMedicineName = view.findViewById(R.id.changeMedicineName);

        editMedicinePrice.setText(String.valueOf(med.getPrice()));

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Edit Price Or Name")
                .setView(view)
                .setPositiveButton("Update", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(d -> {
            android.widget.Button updateButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

            updateButton.setEnabled(true);

            updateButton.setAlpha(0.4f);

            editMedicinePrice.addTextChangedListener(new android.text.TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    checkChangesInEdit(updateButton, med, editMedicinePrice, changeMedicineName);
                }
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void afterTextChanged(android.text.Editable s) {}
            });

            changeMedicineName.addTextChangedListener(new android.text.TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    checkChangesInEdit(updateButton, med, editMedicinePrice, changeMedicineName);
                }
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void afterTextChanged(android.text.Editable s) {}
            });

            updateButton.setOnClickListener(v -> {
                if (!updateButton.isSelected()) {
                    Toast.makeText(this, "You didn't add anything", Toast.LENGTH_SHORT).show();
                    editMedicinePrice.requestFocus();
                    changeMedicineName.requestFocus();
                    return;
                }

                try {
                    double newPrice = Double.parseDouble(editMedicinePrice.getText().toString());
                    String newName = changeMedicineName.getText().toString().trim();

                    if (newPrice != med.getPrice()) {
                        adapter.changePriceMedicine(med, newPrice);
                        adapter.notifyItemChanged(position);
                        Toast.makeText(this, "Price updated successfully", Toast.LENGTH_SHORT).show();
                    }

                    if (!newName.isEmpty() && !newName.equals(med.getName())) {
                        adapter.changeNameMedicine(med, newName);
                        adapter.notifyItemChanged(position);
                        Toast.makeText(this, "Name updated successfully", Toast.LENGTH_SHORT).show();
                    }

                    dialog.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }); dialog.show();
    }
    private void checkChangesInEdit(android.widget.Button updateButton,
                              Medicine med,
                              EditText editMedicinePrice,
                              EditText changeMedicineName) {
        try {
            double newPrice = Double.parseDouble(editMedicinePrice.getText().toString());
            String newName = changeMedicineName.getText().toString().trim();

            boolean changed =
                    (newPrice != med.getPrice()) ||
                            (!newName.isEmpty() && !newName.equals(med.getName()));

            updateButton.setSelected(changed);
            updateButton.setAlpha(changed ? 1f : 0.4f);

        } catch (Exception e) {
            updateButton.setSelected(false);
            updateButton.setAlpha(0.4f);
        }

    }
    @SuppressLint("MissingInflatedId")
    public void showAddQuantityToMedicine(Medicine med, int position) {
        View view = getLayoutInflater().inflate(R.layout.activity_dialog_add_quantity_to_medicine, null);
        EditText editMedicineQuantity = view.findViewById(R.id.add_Quantity);
        editMedicineQuantity.setText(String.valueOf(0));
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Enter the added quantity")
                .setView(view)
                .setPositiveButton("Update", null) // نتركه فارغ مؤقتًا
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(d -> {
            android.widget.Button updateButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            updateButton.setEnabled(true);

            updateButton.setAlpha(0.4f);

            editMedicineQuantity.addTextChangedListener(new android.text.TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    checkChangesInAddQuantity(updateButton, med, editMedicineQuantity);
                }
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void afterTextChanged(android.text.Editable s) {}
            });


            updateButton.setOnClickListener(v -> {
                if (!updateButton.isSelected()) {
                    Toast.makeText(this, "You didn't add anything", Toast.LENGTH_SHORT).show();
                    editMedicineQuantity.requestFocus();
                    return;
                }
                try {
                    if(!updateButton.isEnabled())
                    {
                        Toast.makeText(this, "You Didn't Add Anything", Toast.LENGTH_SHORT).show();
                    }
                    int quantityAdded = Integer.parseInt(editMedicineQuantity.getText().toString());
                    int newQuantity = med.getQuantity() + quantityAdded;

                    adapter.addToQuantity(med, newQuantity);
                    adapter.notifyItemChanged(position);

                    Toast.makeText(this, "Quantity updated successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
        dialog.show();
    }


    private void checkChangesInAddQuantity(android.widget.Button updateButton,
                                           Medicine med,
                                           EditText editMedicineQuantity) {
        try {
            int quantityAded = Integer.parseInt(editMedicineQuantity.getText().toString());
            boolean changed = (quantityAded > 0) ;
            updateButton.setSelected(changed);
            updateButton.setAlpha(changed ? 1f : 0.4f);

        } catch (Exception e) {
            updateButton.setSelected(false);
            updateButton.setAlpha(0.4f);
        }
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
    private boolean validEditText(EditText editText)
    {
        if(editText.getText().toString().isEmpty())
        {
            editText.setError("Required");
            editText.requestFocus();
            return false;
        }
        return true;
    }
}

