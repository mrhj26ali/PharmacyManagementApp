package com.alimrhjmohammadshahoud.pharmacymanagementapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {
    private List<Medicine> medicines;

    public MedicineAdapter(List<Medicine> medicines) {
        this.medicines = medicines;
    }


    @NonNull
    @Override
    public MedicineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medicine, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineAdapter.ViewHolder holder, int position) {
    holder.text_medicine_name.setText(medicines.get(position).getName());
    holder.medicinePrice.setText("Price: $" + medicines.get(position).getPrice());
        holder.btnDelete.setOnClickListener(v -> {
            medicines.remove(position);

            notifyItemRemoved(position);
            notifyItemRangeChanged(position, medicines.size());
        });
    }

    @Override
    public int getItemCount() {
        return  medicines != null ? medicines.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_medicine_name;
        TextView medicinePrice;
        ImageButton btnDelete;
        ImageButton btnEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text_medicine_name = itemView.findViewById(R.id.text_medicine_name);
            medicinePrice = itemView.findViewById(R.id.text_medicine_price);
            btnDelete = itemView.findViewById(R.id.btn_delete_medicine);
            btnEdit = itemView.findViewById(R.id.btn_edit_medicine);
        }
    }
}
