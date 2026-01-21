package com.alimrhjmohammadshahoud.pharmacymanagementapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InvoiceDetailsAdapter extends RecyclerView.Adapter<InvoiceDetailsAdapter.SaleViewHolder> {

    private Context context;
    private List<Sale> saleList;

    public InvoiceDetailsAdapter(Context context, List<Sale> saleList) {
        this.context = context;
        this.saleList = saleList;
    }

    @NonNull
    @Override
    public SaleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_sale_in_invoice, parent, false);
        return new SaleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SaleViewHolder holder, int position) {
        Sale sale = saleList.get(position);

        holder.name.setText(sale.getMedicineName());
        holder.qty.setText("Qty: " + sale.getQuantity());
        holder.total.setText("Total: $" + sale.getTotalPrice());
    }

    @Override
    public int getItemCount() {
        return saleList.size();
    }

    static class SaleViewHolder extends RecyclerView.ViewHolder {

        TextView name, qty, total, date;

        public SaleViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.textSaleName);
            qty = itemView.findViewById(R.id.textSaleQty);
            total = itemView.findViewById(R.id.textSaleTotal);

        }
    }
}