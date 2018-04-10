package com.francony.romain.outerspacemanager.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.adapter.ReportAdapter;
import com.francony.romain.outerspacemanager.adapter.SearchAdapter;
import com.francony.romain.outerspacemanager.adapter.ShipAdapter;
import com.francony.romain.outerspacemanager.model.Report;
import com.francony.romain.outerspacemanager.model.Ship;

import java.util.ArrayList;


public class ReportsFragment extends Fragment {
    private ArrayList<Report> reports = new ArrayList<>();

    private RecyclerView rvReports;
    private LinearLayoutManager rvLayoutManager;
    private ReportAdapter reportAdapter;

    public ReportsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reports, container, false);


        this.rvReports = v.findViewById(R.id.reports_rv);
        this.rvReports.setHasFixedSize(true);
        this.rvLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        this.rvReports.setLayoutManager(rvLayoutManager);
        this.reportAdapter = new ReportAdapter(this.reports, getContext());
        this.rvReports.setAdapter(this.reportAdapter);

        return v;
    }


    public ArrayList<Report> getReports() {
        return reports;
    }

    public void setReports(ArrayList<Report> reports) {
        this.reports = reports;
        this.reportAdapter.notifyDataSetChanged();
    }
}
