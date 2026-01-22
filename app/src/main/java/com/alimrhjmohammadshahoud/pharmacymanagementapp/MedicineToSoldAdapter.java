package com.alimrhjmohammadshahoud.pharmacymanagementapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MedicineToSoldAdapter extends RecyclerView.Adapter<MedicineToSoldAdapter.MedicineViewHolder> {

    private List<Medicine> medicineList;
    private List<Medicine> medicineListFull;
    private OnAddToCartListener listener;
    private Context context;

    public interface OnAddToCartListener {
        void onAddToCart(Medicine medicine, int position);
    }

    public MedicineToSoldAdapter(Context context, List<Medicine> medicineList, OnAddToCartListener listener) {
        this.context = context;
        this.medicineList = medicineList;
        this.medicineListFull = new ArrayList<>(medicineList);
        this.listener = listener;
    }

    // High Priority "Starts With" Filter Logic
    public void filter(String text) {
        List<Medicine> filteredList = new ArrayList<>();
        if (text.isEmpty()) {
            filteredList.addAll(medicineListFull);
        } else {
            String query = text.toLowerCase().trim();
            List<Medicine> startsWithList = new ArrayList<>();
            List<Medicine> containsList = new ArrayList<>();

            for (Medicine item : medicineListFull) {
                String name = item.getName().toLowerCase();
                String barcode = item.getBarcode().toLowerCase();

                if (name.startsWith(query) || barcode.startsWith(query)) {
                    startsWithList.add(item);
                } else if (name.contains(query) || barcode.contains(query)) {
                    containsList.add(item);
                }
            }
            filteredList.addAll(startsWithList);
            filteredList.addAll(containsList);
        }
        medicineList.clear();
        medicineList.addAll(filteredList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.medicine_sold_item, parent, false);
        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine medicine = medicineList.get(position);

        holder.txtName.setText(medicine.getName());
        holder.txtPrice.setText("Price: " + medicine.getPrice());
        holder.txtQuantity.setText("Quantity: " + medicine.getQuantity());

        // Visual low-stock alert
        holder.txtQuantity.setTextColor(medicine.getQuantity() < 5 ? 0xFFD32F2F : 0xFF666666);

        holder.btnAddToCart.setOnClickListener(v -> {
            int actualPos = holder.getAdapterPosition();
            if (actualPos != RecyclerView.NO_POSITION) {
                Medicine currentMed = medicineList.get(actualPos);
                if (currentMed.getQuantity() > 0) {
                    listener.onAddToCart(currentMed, actualPos);
                } else {
                    Toast.makeText(context, "Out Of Stock", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() { return medicineList.size(); }

    public void updateList(List<Medicine> newList) {
        medicineList.clear();
        medicineList.addAll(newList);
        medicineListFull = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    static class MedicineViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice, txtQuantity;
        ImageButton btnAddToCart;

        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.text_medicine_name);
            txtPrice = itemView.findViewById(R.id.text_medicine_price);
            txtQuantity = itemView.findViewById(R.id.text_medicine_quantity);
            btnAddToCart = itemView.findViewById(R.id.btn_addToCart_medicine);
        }
    }
}