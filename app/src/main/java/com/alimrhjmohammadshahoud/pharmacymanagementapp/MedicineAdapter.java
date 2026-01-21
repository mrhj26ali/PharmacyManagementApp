package com.alimrhjmohammadshahoud.pharmacymanagementapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {
    private Context context;
    private List<Medicine> medicines;
    private DBHelper db;

    public MedicineAdapter(Context context, List<Medicine> medicines) {
        this.context = context;
        this.medicines = medicines;
        this.db = new DBHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medicine, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medicine medicine = medicines.get(position);
        viewMedicine(holder, medicine);

        holder.btnEdit.setOnClickListener(v -> {
            if (context instanceof MedicineListActivity) {
                ((MedicineListActivity) context).showEditDialog(medicine, position);
            }
        });
        holder.btnDelete.setOnClickListener(v-> {
            int currentPosition = holder.getAdapterPosition();
            ((MedicineListActivity) context).showConfirmeToDeleteMedicine(medicine ,currentPosition );
        });
        holder.btnAddQuantity.setOnClickListener(v -> {
            if (context instanceof MedicineListActivity) {
                ((MedicineListActivity) context).showAddQuantityToMedicine(medicine, position);
            }
        });
    }

    private void viewMedicine(@NonNull ViewHolder holder, Medicine medicine) {
        holder.text_medicine_name.setText(medicine.getName());
        holder.medicinePrice.setText(String.format("$%.2f", medicine.getPrice()));
        holder.quantityAdded.setText("In Stock: " + medicine.getQuantity());

        if (medicine.getQuantity() < 5) {
            holder.quantityAdded.setTextColor(0xFFD32F2F); // Bright Red for Alert
            holder.quantityAdded.setText("Low Stock: " + medicine.getQuantity());
        } else {
            holder.quantityAdded.setTextColor(0xFF757575); // Professional Gray
        }
    }

    @Override
    public int getItemCount() {
        return medicines != null ? medicines.size() : 0;
    }

    public void addMedicine(Medicine medicine) {
        medicines.add(medicine);
        notifyItemInserted(medicines.size() - 1);

    }

    public void deleteMedicine(Medicine medicine, int currentPosition) {
        if (currentPosition != RecyclerView.NO_POSITION) {

            db.deleteMedicine(medicine.getId());


            medicines.remove(currentPosition);
            notifyItemRemoved(currentPosition);
            notifyItemRangeChanged(currentPosition, medicines.size());
        }
    }

    public void changePriceMedicine(Medicine medicine, double price) {


        db.updateMedicinePrice(medicine.getId(), price);

        medicine.setPrice(price);
        notifyDataSetChanged();
    }
    public void changeNameMedicine(Medicine medicine, String newName) {


        db.updateMedicineName(medicine.getId(), newName);

        medicine.setName(newName);
        notifyDataSetChanged();
    }
    public void addToQuantity(Medicine medicine, int quantity) {


        db.updateMedicineQuantity(medicine.getId(), quantity);


        medicine.setQuantity(quantity);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_medicine_name, medicinePrice , quantityAdded;
        ImageButton btnDelete, btnEdit , btnAddQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            quantityAdded = itemView.findViewById(R.id.text_medicine_quantity);
            text_medicine_name = itemView.findViewById(R.id.text_medicine_name);
            medicinePrice = itemView.findViewById(R.id.text_medicine_price);
            btnDelete = itemView.findViewById(R.id.btn_delete_medicine);
            btnEdit = itemView.findViewById(R.id.btn_edit_medicine);
            btnAddQuantity = itemView.findViewById(R.id.btn_addQuantity_medicine);
        }
    }
}