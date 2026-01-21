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

import com.alimrhjmohammadshahoud.pharmacymanagementapp.Medicine;
import com.alimrhjmohammadshahoud.pharmacymanagementapp.R;

import java.util.ArrayList;
import java.util.List;

public class MedicineToSoldAdapter extends RecyclerView.Adapter<MedicineToSoldAdapter.MedicineViewHolder> {

    private List<Medicine> medicineList;
    private OnAddToCartListener listener;
    private List<Medicine> medicineListFull;
    private Context context;

    public void filter(String text) {
        List<Medicine> filteredList = new ArrayList<>();
        if (text.isEmpty()) {
            filteredList.addAll(medicineListFull);
        } else {
            text = text.toLowerCase();
            for (Medicine item : medicineListFull) {
                if (item.getName().toLowerCase().contains(text)) {
                    filteredList.add(item);
                }
            }
        }
        medicineList.clear();
        medicineList.addAll(filteredList);
        notifyDataSetChanged();
    }


    public interface OnAddToCartListener {
        // Pass the position so the Activity can tell the adapter exactly what to refresh
        void onAddToCart(Medicine medicine, int position);
    }

    public MedicineToSoldAdapter(Context context,List<Medicine> medicineList, OnAddToCartListener listener) {
        this.medicineList = medicineList;
        this.context=context;
        this.listener = listener;
        this.medicineListFull = new ArrayList<>(medicineList);

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

        if (medicine.getQuantity() < 5) {
            holder.txtQuantity.setTextColor(0xFFD32F2F);
        } else {
            holder.txtQuantity.setTextColor(0xFF666666);
        }

        holder.btnAddToCart.setOnClickListener(v -> {
            if (listener != null && medicine.getQuantity() > 0) {
                listener.onAddToCart(medicine, position);
                holder.txtQuantity.setText("Quantity: " + medicine.getQuantity());
                notifyItemChanged(position);
            }
             else {
                Toast.makeText(context, "Out Of Stock", Toast.LENGTH_SHORT).show();
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
    public void updateList(List<Medicine> newList) {
        medicineList.clear();
        medicineList.addAll(newList);
        medicineListFull.clear();
        medicineListFull.addAll(newList);
        notifyDataSetChanged();
    }
}