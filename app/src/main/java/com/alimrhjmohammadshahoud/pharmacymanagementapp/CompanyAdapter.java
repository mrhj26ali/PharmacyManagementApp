package com.alimrhjmohammadshahoud.pharmacymanagementapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder> {

    private List<Company> companyList;
    private Context context;

    public CompanyAdapter(Context context, List<Company> companyList) {
        this.context = context;
        this.companyList = companyList;
    }

    @NonNull
    @Override
    public CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_company, parent, false);
        return new CompanyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyViewHolder holder, int position) {
        Company company = companyList.get(position);
        holder.companyName.setText(company.getName());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MedicineListActivity.class);
            intent.putExtra("company_id", company.getId());
            context.startActivity(intent);
        });

        // Edit Button Logic
        holder.btnEdit.setOnClickListener(v -> {
            if (context instanceof CompanyListActivity) {
                int actualPosition = holder.getAdapterPosition();
                if (actualPosition != RecyclerView.NO_POSITION) {
                    ((CompanyListActivity) context).showEditCompanyDialog(company, actualPosition);
                }
            }
        });

        // Delete Button Logic
        holder.btnDelete.setOnClickListener(v -> {
            if (context instanceof CompanyListActivity) {
                int actualPosition = holder.getAdapterPosition();
                if (actualPosition != RecyclerView.NO_POSITION) {
                    ((CompanyListActivity) context).showConfirmDeleteCompany(company, actualPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() { return (companyList != null) ? companyList.size() : 0; }

    public void addCompany(Company company) {
        companyList.add(company);
        notifyItemInserted(companyList.size() - 1);
    }

    static class CompanyViewHolder extends RecyclerView.ViewHolder {
        TextView companyName;
        ImageButton btnDelete, btnEdit;

        public CompanyViewHolder(@NonNull View itemView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.text_company_name);
            btnDelete = itemView.findViewById(R.id.btn_delete_company);
            btnEdit = itemView.findViewById(R.id.btn_edit_company);
        }
    }
}