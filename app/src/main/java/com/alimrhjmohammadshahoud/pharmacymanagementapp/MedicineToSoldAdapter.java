package com.alimrhjmohammadshahoud.pharmacymanagementapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alimrhjmohammadshahoud.pharmacymanagementapp.Medicine;
import com.alimrhjmohammadshahoud.pharmacymanagementapp.R;

import java.util.List;

public class MedicineToSoldAdapter extends RecyclerView.Adapter<MedicineToSoldAdapter.MedicineViewHolder> {

    private List<Medicine> medicineList;
    private OnAddToCartListener listener;

    public interface OnAddToCartListener {
        void onAddToCart(Medicine medicine);
    }

    public MedicineToSoldAdapter(List<Medicine> medicineList, OnAddToCartListener listener) {
        this.medicineList = medicineList;
        this.listener = listener;
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
        holder.txtQuantity.setText("Qty: " + medicine.getQuantity());

        holder.btnAddToCart.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAddToCart(medicine);
            }
            // منع الكمية من أن تصبح سالبة
            if (medicine.getQuantity() > 0) {
                medicine.setQuantity(medicine.getQuantity() - 1);
                // تحديث النص مباشرة
                holder.txtQuantity.setText("Qty: " + medicine.getQuantity());
                // أو يمكنك استخدام notifyItemChanged(position) لتحديث العنصر بالكامل
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public static class MedicineViewHolder extends RecyclerView.ViewHolder {
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