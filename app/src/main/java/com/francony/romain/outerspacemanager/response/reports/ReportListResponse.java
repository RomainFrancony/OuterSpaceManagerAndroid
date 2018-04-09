package com.francony.romain.outerspacemanager.response.reports;

import java.util.ArrayList;

public class ReportListResponse {
    private ArrayList<Report> reports = null;

    public ArrayList<Report> getReports() {
        return reports;
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
}
