package com.alimrhjmohammadshahoud.pharmacymanagementapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.alimrhjmohammadshahoud.pharmacymanagementapp.Company;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alimrhjmohammadshahoud.pharmacymanagementapp.R;


import java.util.List;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder> {

    public interface OnCompanyClickListener {
        void onCompanyClick(Company company);
    }

    private List<Company> companyList;
    private OnCompanyClickListener listener;

    public CompanyAdapter(List<Company> companyList, OnCompanyClickListener listener) {
        this.companyList = companyList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_company, parent, false);
        return new CompanyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyViewHolder holder, int position) {
        Company company = companyList.get(position);
        holder.companyName.setText(company.getName());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCompanyClick(company);
            }
        });
    }

    @Override
    public int getItemCount() {
        return companyList.size();
    }

    public void addCompany(Company company) {
        companyList.add(company);
        notifyItemInserted(companyList.size() - 1);
    }

    static class CompanyViewHolder extends RecyclerView.ViewHolder {

        TextView companyName;

        public CompanyViewHolder(@NonNull View itemView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.text_company_name);
        }
    }
}
