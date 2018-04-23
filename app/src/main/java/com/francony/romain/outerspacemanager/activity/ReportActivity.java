package com.francony.romain.outerspacemanager.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.databinding.ActivityReportBinding;
import com.francony.romain.outerspacemanager.model.Report;
import com.google.gson.Gson;

public class ReportActivity extends AppCompatActivity {
    private ActivityReportBinding binding;
    private Report report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_report);

        // Toolbar conf
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Gson gson = new Gson();
        this.report = gson.fromJson(getIntent().getStringExtra("report"), Report.class);
        this.binding.setReport(this.report);
    }

    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        return true;
    }
}
