package com.francony.romain.outerspacemanager.response;

import com.francony.romain.outerspacemanager.model.Report;

import java.util.ArrayList;

public class ReportListResponse {
    private ArrayList<Report> reports = new ArrayList<>();
    private int size;

    public ArrayList<Report> getReports() {
        return reports;
    }

    public ReportListResponse(ArrayList<Report> reports) {
        this.reports = reports;
    }

    public void setReports(ArrayList<Report> reports) {
        this.reports = reports;
    }

    @Override
    public String toString() {
        return "ReportListResponse{" +
                "reports=" + reports +
                '}';
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
