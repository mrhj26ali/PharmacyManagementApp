package com.alimrhjmohammadshahoud.pharmacymanagementapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<Medicine> cartItems;
    private Context context;
    private DBHelper dbHelper;

    public CartAdapter(Context context, List<Medicine> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
        this.dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Medicine medicine = cartItems.get(position);

        holder.txtName.setText(medicine.getName());
        holder.txtPrice.setText("Price: $" + medicine.getPrice());
        holder.txtQuantity.setText(String.valueOf(medicine.getQuantity()));

        double subtotal = medicine.getPrice() * medicine.getQuantity();
        holder.txtSubtotal.setText(String.format("Subtotal: $%.2f", subtotal));

        // زيادة الكمية (+)
        holder.btnIncreaseQty.setOnClickListener(v -> {
            int stockInDB = dbHelper.getMedicineStock(medicine.getName());
            if (medicine.getQuantity() < stockInDB) {
                medicine.setQuantity(medicine.getQuantity() + 1);
                notifyItemChanged(holder.getAdapterPosition()); // استخدام AdapterPosition أكثر أماناً
                if (context instanceof CartActivity) ((CartActivity) context).updateTotalPrice();
            } else {
                Toast.makeText(context, "Only " + stockInDB + " available", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnDecreaseQty.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (medicine.getQuantity() > 1) {
                medicine.setQuantity(medicine.getQuantity() - 1);
                notifyItemChanged(currentPos);
            } else {
                cartItems.remove(currentPos);
                notifyItemRemoved(currentPos);
                notifyItemRangeChanged(currentPos, cartItems.size());
            }
            if (context instanceof CartActivity) ((CartActivity) context).updateTotalPrice();
        });
    }

    @Override
    public int getItemCount() {
        return cartItems != null ? cartItems.size() : 0;
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtQuantity, txtPrice, txtSubtotal;
        View btnIncreaseQty, btnDecreaseQty;
        public CartViewHolder(@NonNull View v) {
            super(v);
            txtName = v.findViewById(R.id.text_cart_name);
            txtQuantity = v.findViewById(R.id.text_cart_quantity);
            txtPrice = v.findViewById(R.id.text_cart_price);
            txtSubtotal = v.findViewById(R.id.text_cart_subtotal);
            btnIncreaseQty = v.findViewById(R.id.btn_increase_qty);
            btnDecreaseQty = v.findViewById(R.id.btn_decrease_qty);
        }
    }
}