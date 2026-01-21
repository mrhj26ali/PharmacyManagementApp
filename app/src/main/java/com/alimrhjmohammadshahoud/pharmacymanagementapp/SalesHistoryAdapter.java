package com.alimrhjmohammadshahoud.pharmacymanagementapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SalesHistoryAdapter extends RecyclerView.Adapter<SalesHistoryAdapter.InvoiceViewHolder> {

    private Context context;
    private List<Invoice> invoiceList;

    public SalesHistoryAdapter(Context context, List<Invoice> invoiceList) {
        this.context = context;
        this.invoiceList = invoiceList;
    }

    @NonNull
    @Override
    public InvoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the elegant card layout
        View view = LayoutInflater.from(context).inflate(R.layout.item_invoice, parent, false);
        return new InvoiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceViewHolder holder, int position) {
        Invoice invoice = invoiceList.get(position);

        // Binding data to views
        holder.id.setText("Invoice #" + invoice.getId());
        holder.date.setText(invoice.getDate());
        holder.total.setText(String.format("$%.2f", invoice.getTotalPrice()));

        // Taking advantage of ripple effect (foreground) defined in XML
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, InvoiceDetailsActivity.class);
            intent.putExtra("invoiceId", invoice.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return invoiceList != null ? invoiceList.size() : 0;
    }

    // RecyclerView Feature: Helper method to update data smoothly
    public void updateList(List<Invoice> newList) {
        this.invoiceList = newList;
        notifyDataSetChanged();
    }

    static class InvoiceViewHolder extends RecyclerView.ViewHolder {
        TextView id, date, total;
        ImageView invoiceIcon;

        public InvoiceViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.textInvoiceId);
            date = itemView.findViewById(R.id.textInvoiceDate);
            total = itemView.findViewById(R.id.textInvoiceTotal);
            invoiceIcon = itemView.findViewById(R.id.image_invoice_icon);
        }
    }
}