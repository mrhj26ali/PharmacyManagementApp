package com.alimrhjmohammadshahoud.pharmacymanagementapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Medicine> cartItems;

    public CartAdapter(List<Medicine> cartItems) {
        this.cartItems = cartItems;
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
        holder.txtQuantity.setText("Qty: " + medicine.getQuantity());
        holder.txtPrice.setText("Price: $" + medicine.getPrice());

        double subtotal = medicine.getPrice() * medicine.getQuantity();
        holder.txtSubtotal.setText("Subtotal: $"+ subtotal);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtQuantity, txtPrice, txtSubtotal;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.text_cart_name);
            txtQuantity = itemView.findViewById(R.id.text_cart_quantity);
            txtPrice = itemView.findViewById(R.id.text_cart_price);
            txtSubtotal = itemView.findViewById(R.id.text_cart_subtotal);
        }
    }
}