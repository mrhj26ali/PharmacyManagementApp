package com.alimrhjmohammadshahoud.pharmacymanagementapp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MedicineListActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_medicine_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.medicine_list_activity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            List<Medicine> medicines = new ArrayList<Medicine>();
            medicines.add(new Medicine(1,"fe",4,5,6.0));
            medicines.add(new Medicine(3,"fu",3,5,6.0));
            medicines.add(new Medicine(4,"fy",5,5,6.0));
            return insets;
        });
    }
}