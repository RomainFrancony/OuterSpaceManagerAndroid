package com.francony.romain.outerspacemanager.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.databinding.AdapterReportBinding;
import com.francony.romain.outerspacemanager.model.Report;

import java.util.ArrayList;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportAdapterViewHolder> {
    private ArrayList<Report> reportsDataset;
    private Context context;

    public ReportAdapter(ArrayList<Report> reports, Context context) {
        this.reportsDataset = reports;
        this.context = context;
    }


    @Override
    public ReportAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        AdapterReportBinding binding = DataBindingUtil.inflate(inflater, R.layout.adapter_report, parent, false);
        return new ReportAdapterViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(ReportAdapterViewHolder holder, int position) {
        Report report = this.reportsDataset.get(position);
        holder.bind(report);
    }

    @Override
    public int getItemCount() {
        return reportsDataset.size();
    }


    public class ReportAdapterViewHolder extends RecyclerView.ViewHolder {
        private final AdapterReportBinding binding;

        public ReportAdapterViewHolder(AdapterReportBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final Report report) {
            binding.setReport(report);
            binding.executePendingBindings();
        }
    }
}
