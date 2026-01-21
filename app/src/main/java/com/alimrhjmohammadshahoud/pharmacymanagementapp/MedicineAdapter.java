package com.alimrhjmohammadshahoud.pharmacymanagementapp;

import android.content.Context;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MedicineAdapter extends ListAdapter<Medicine, MedicineAdapter.ViewHolder> {
    private final Context context;
    private long lastClickTime = 0;

    public MedicineAdapter(Context context) {
        super(new MedicineDiffCallback());
        this.context = context;
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
        Medicine medicine = getItem(position);
        holder.bind(medicine, context, this);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_medicine_name, medicinePrice, quantityAdded;
        ImageButton btnDelete, btnEdit, btnAddQuantity;
        private long lastClickTime = 0;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            quantityAdded = itemView.findViewById(R.id.text_medicine_quantity);
            text_medicine_name = itemView.findViewById(R.id.text_medicine_name);
            medicinePrice = itemView.findViewById(R.id.text_medicine_price);
            btnDelete = itemView.findViewById(R.id.btn_delete_medicine);
            btnEdit = itemView.findViewById(R.id.btn_edit_medicine);
            btnAddQuantity = itemView.findViewById(R.id.btn_addQuantity_medicine);
        }

        void bind(@NonNull Medicine medicine, Context context, MedicineAdapter adapter) {
            text_medicine_name.setText(medicine.getName());
            medicinePrice.setText(String.format("$%.2f", medicine.getPrice()));
            quantityAdded.setText("Stock: " + medicine.getQuantity());

            if (medicine.getQuantity() < 5) {
                quantityAdded.setTextColor(0xFFD32F2F); // Red
            } else {
                quantityAdded.setTextColor(0xFF757575); // Gray
            }

            btnEdit.setOnClickListener(v -> {
                if (SystemClock.elapsedRealtime() - lastClickTime < 600) return;
                lastClickTime = SystemClock.elapsedRealtime();
                if (context instanceof MedicineListActivity) {
                    ((MedicineListActivity) context).showEditDialog(medicine, getAdapterPosition());
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (SystemClock.elapsedRealtime() - lastClickTime < 600) return;
                lastClickTime = SystemClock.elapsedRealtime();
                if (context instanceof MedicineListActivity) {
                    ((MedicineListActivity) context).showConfirmeToDeleteMedicine(medicine, getAdapterPosition());
                }
            });

            btnAddQuantity.setOnClickListener(v -> {
                if (SystemClock.elapsedRealtime() - lastClickTime < 600) return;
                lastClickTime = SystemClock.elapsedRealtime();
                if (context instanceof MedicineListActivity) {
                    ((MedicineListActivity) context).showAddQuantityToMedicine(medicine, getAdapterPosition());
                }
            });
        }
    }

    static class MedicineDiffCallback extends DiffUtil.ItemCallback<Medicine> {
        @Override
        public boolean areItemsTheSame(@NonNull Medicine oldItem, @NonNull Medicine newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Medicine oldItem, @NonNull Medicine newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                   oldItem.getQuantity() == newItem.getQuantity() &&
                   Double.compare(oldItem.getPrice(), newItem.getPrice()) == 0;
        }
    }
}
