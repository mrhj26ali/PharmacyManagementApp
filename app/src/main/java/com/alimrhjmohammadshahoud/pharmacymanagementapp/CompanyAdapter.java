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

    private List<Company> companyList;

    public CompanyAdapter(List<Company> companyList) {
        this.companyList = companyList;
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
        holder.companyName.setText(companyList.get(position).getName());
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
            companyName = itemView.findViewById(R.id.textCompanyName);
        }
    }
}
