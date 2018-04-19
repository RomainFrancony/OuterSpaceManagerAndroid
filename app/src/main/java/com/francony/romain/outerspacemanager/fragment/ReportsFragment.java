package com.francony.romain.outerspacemanager.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.activity.MainActivity;
import com.francony.romain.outerspacemanager.adapter.ReportAdapter;
import com.francony.romain.outerspacemanager.adapter.SearchAdapter;
import com.francony.romain.outerspacemanager.adapter.ShipAdapter;
import com.francony.romain.outerspacemanager.helpers.Helpers;
import com.francony.romain.outerspacemanager.helpers.SharedPreferencesHelper;
import com.francony.romain.outerspacemanager.model.Report;
import com.francony.romain.outerspacemanager.model.Ship;
import com.francony.romain.outerspacemanager.response.ReportListResponse;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerService;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerServiceFactory;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReportsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ReportAdapter.OnLoadMoreListener {
    private ArrayList<Report> reports = new ArrayList<>();
    private OuterSpaceManagerService service = OuterSpaceManagerServiceFactory.create();

    private RelativeLayout laLoader;
    private SwipeRefreshLayout laRefreshReports;

    private RecyclerView rvReports;
    private LinearLayoutManager rvLayoutManager;
    private ReportAdapter reportAdapter;
    private LinearLayout laEmptyReports;
    private int page = 0;
    private Boolean loading = false;

    public ReportsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reports, container, false);

        this.laLoader = v.findViewById(R.id.layout_loader);
        this.laEmptyReports = v.findViewById(R.id.reports_empty_layout);
        Button button = this.laEmptyReports.findViewById(R.id.reports_empty_galaxy_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                if (activity == null) {
                    return;
                }
                activity.navigationView.getMenu().performIdentifierAction(R.id.nav_galaxy, 0);
                activity.navigationView.getMenu().getItem(MainActivity.GALAXY_DRAWER_INDEX).setChecked(true);
            }
        });

        // Recycler view
        this.rvReports = v.findViewById(R.id.reports_rv);
        this.rvReports.setHasFixedSize(true);
        this.rvLayoutManager = new LinearLayoutManager(getContext());
        this.rvReports.setLayoutManager(rvLayoutManager);
        this.reportAdapter = new ReportAdapter(this.reports, getContext());
        this.rvReports.setAdapter(this.reportAdapter);
        this.reportAdapter.setOnLoadMoreListener(this);

        this.getReports();

        // Swipe to refresh
        this.laRefreshReports = v.findViewById(R.id.refresh_reports);
        this.laRefreshReports.setOnRefreshListener(this);

        return v;
    }


    private void getReports() {
        this.loading = true;
        Call<ReportListResponse> request = this.service.reportsList(SharedPreferencesHelper.getToken(getContext()), this.page * 20, 20);
        request.enqueue(new Callback<ReportListResponse>() {
            @Override
            public void onResponse(Call<ReportListResponse> call, Response<ReportListResponse> response) {
                ReportsFragment.this.laLoader.setVisibility(View.GONE);
                // Error
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), Helpers.getResponseErrorMessage(response), Toast.LENGTH_LONG).show();
                    return;
                }
                // Delete loading item
                if (!ReportsFragment.this.reports.isEmpty()) {
                    int index = ReportsFragment.this.reports.size() - 1;
                    ReportsFragment.this.reports.remove(index);
                    ReportsFragment.this.reportAdapter.notifyItemRemoved(index);
                }

                // We are already on last page no need to do anything
                if (response.body().getReports().isEmpty() && ReportsFragment.this.reports.isEmpty()) {
                    ReportsFragment.this.laEmptyReports.setVisibility(View.VISIBLE);
                    ReportsFragment.this.rvReports.setVisibility(View.GONE);
                    return;
                }

                if(!response.body().getReports().isEmpty()){
                    ReportsFragment.this.reports.addAll(response.body().getReports());
                    ReportsFragment.this.reports.add(null);
                    ReportsFragment.this.reportAdapter.notifyItemRangeInserted(0, response.body().getReports().size());
                    ReportsFragment.this.loading = false;
                }
            }

            // Network error
            @Override
            public void onFailure(Call<ReportListResponse> call, Throwable t) {
                ReportsFragment.this.loading = false;
                Toast.makeText(getActivity().getApplicationContext(), R.string.error_network, Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public void onRefresh() {
        // We only check if the 20 first reports (max allowed by api) but is not really probable that some got 20+ reports in short time
        Call<ReportListResponse> request = this.service.reportsList(SharedPreferencesHelper.getToken(getContext()), 0, 20);
        request.enqueue(new Callback<ReportListResponse>() {
            @Override
            public void onResponse(Call<ReportListResponse> call, Response<ReportListResponse> response) {
                ReportsFragment.this.laRefreshReports.setRefreshing(false);
                // Error
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), Helpers.getResponseErrorMessage(response), Toast.LENGTH_LONG).show();
                    return;
                }

                if (response.body().getReports() == null) {
                    return;
                }

                ArrayList<Report> refreshed = response.body().getReports();
                if (refreshed.isEmpty()) {
                    return;
                }

                // Remove common reports
                refreshed.removeAll(ReportsFragment.this.reports);
                if (refreshed.isEmpty()) {
                    return;
                }

                ReportsFragment.this.reports.addAll(0, refreshed);
                ReportsFragment.this.reportAdapter.notifyItemRangeInserted(0, refreshed.size());
                ReportsFragment.this.rvReports.scrollToPosition(0);

                // Remove card for empty reports
                if (!ReportsFragment.this.reports.isEmpty()) {
                    ReportsFragment.this.laEmptyReports.setVisibility(View.GONE);
                    ReportsFragment.this.rvReports.setVisibility(View.VISIBLE);
                }
            }

            // Network error
            @Override
            public void onFailure(Call<ReportListResponse> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), R.string.error_network, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onLoadMore() {
        if (this.loading) {
            return;
        }
        this.page++;
        this.getReports();
    }
}
