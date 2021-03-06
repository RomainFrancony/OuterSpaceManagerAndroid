package com.francony.romain.outerspacemanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.activity.ReportActivity;
import com.francony.romain.outerspacemanager.databinding.AdapterReportBinding;
import com.francony.romain.outerspacemanager.model.Report;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ReportAdapter extends RecyclerView.Adapter implements View.OnScrollChangeListener {
    private final static int TYPE_REPORT = 0;
    private final static int TYPE_LOADING = 1;
    private ArrayList<Report> reportsDataset;
    private Context context;
    private RecyclerView recyclerView;
    private OnLoadMoreListener onLoadMoreListener;

    public ReportAdapter(ArrayList<Report> reports, Context context) {
        this.reportsDataset = reports;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return this.reportsDataset.get(position) == null ? TYPE_LOADING : TYPE_REPORT;
    }

    /**
     * Create view holder for item type
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_REPORT) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            AdapterReportBinding binding = DataBindingUtil.inflate(inflater, R.layout.adapter_report, parent, false);
            return new ReportAdapterViewHolder(binding);
        }

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_user_loading, parent, false);
        return new ReportAdapterLoadingViewHolder(v);
    }


    /**
     * Bind the report if needed
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ReportAdapterViewHolder) {
            Report report = this.reportsDataset.get(position);
            ((ReportAdapterViewHolder) holder).bind(report);
        }
    }

    @Override
    public int getItemCount() {
        return reportsDataset.size();
    }

    /**
     * Get attached recycler view and add listener
     *
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
        this.recyclerView.setOnScrollChangeListener(this);
    }

    /**
     * Check if scroll at the end the recycler view
     *
     * @param v
     * @param scrollX
     * @param scrollY
     * @param oldScrollX
     * @param oldScrollY
     */
    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (((LinearLayoutManager) this.recyclerView.getLayoutManager()).findLastVisibleItemPosition() == this.getItemCount() - 1) {
            if (this.onLoadMoreListener != null) {
                this.onLoadMoreListener.onLoadMore();
            }
        }
    }


    /**
     * View holder for report
     */
    public class ReportAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final AdapterReportBinding binding;

        public ReportAdapterViewHolder(AdapterReportBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        /**
         * Bind report to UI
         * @param report
         */
        public void bind(final Report report) {
            binding.setReport(report);
            binding.executePendingBindings();
        }


        /**
         * Start single report activity
         * @param v
         */
        @Override
        public void onClick(View v) {
            Gson gson = new Gson();
            String report_string = gson.toJson(this.binding.getReport());
            Intent intent = new Intent(context, ReportActivity.class);
            intent.putExtra("report",report_string);
            context.startActivity(intent);
        }
    }

    /**
     * View holder for loader
     */
    public class ReportAdapterLoadingViewHolder extends RecyclerView.ViewHolder {

        public ReportAdapterLoadingViewHolder(View v) {
            super(v);
        }
    }

    /**
     * Custom event listener
     * @param onLoadMoreListener
     */
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    /**
     * Custom event interface
     */
    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
