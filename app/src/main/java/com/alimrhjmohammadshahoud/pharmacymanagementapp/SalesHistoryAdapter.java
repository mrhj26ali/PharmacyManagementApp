package com.alimrhjmohammadshahoud.pharmacymanagementapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SalesHistoryAdapter extends RecyclerView.Adapter<SalesHistoryAdapter.InvoiceViewHolder> {

    private Context context;
    private List<Invoice> invoiceList;

    // Change constructor to accept the list from outside
    public SalesHistoryAdapter(Context context, List<Invoice> invoiceList) {
        this.context = context;
        this.invoiceList = invoiceList;
    }

    @NonNull
    @Override
    public InvoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_invoice, parent, false);
        return new InvoiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceViewHolder holder, int position) {
        Invoice invoice = invoiceList.get(position);

        // This matches your item_invoice.xml IDs
        holder.id.setText("Invoice #" + invoice.getId());
        holder.date.setText("Date: " + invoice.getDate());
        holder.total.setText(String.format("Total: $%.2f", invoice.getTotalPrice()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, InvoiceDetailsActivity.class);
            intent.putExtra("invoiceId", invoice.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return invoiceList.size();
    }

    static class InvoiceViewHolder extends RecyclerView.ViewHolder {

        TextView id, date, total;

        public InvoiceViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.textInvoiceId);
            date = itemView.findViewById(R.id.textInvoiceDate);
            total = itemView.findViewById(R.id.textInvoiceTotal);
        }
    }
}