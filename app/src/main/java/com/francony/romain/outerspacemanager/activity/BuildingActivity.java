package com.francony.romain.outerspacemanager.activity;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.databinding.ActivityBuildingBinding;
import com.francony.romain.outerspacemanager.helpers.Helpers;
import com.francony.romain.outerspacemanager.model.Building;
import com.google.gson.Gson;

public class BuildingActivity extends AppCompatActivity {
    public Building building;
    private ActivityBuildingBinding binding;

    @Override
    public boolean onSupportNavigateUp() {
        finishAfterTransition();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_building);
        // Toolbar conf
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Status bar transparent
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);

        Gson gson = new Gson();
        this.building = gson.fromJson(getIntent().getStringExtra("building"), Building.class);
        this.binding.setBuildingActivityViewModel(this);
    }

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String url) {
        Helpers.loadExternalImageWithAnimation(view,url);
    }


    public String getEffectString() {
        String packageName = getPackageName();
        int resId = getResources().getIdentifier("effect." + this.building.getEffect(), "string", packageName);
        return getString(resId);
    }

    public void startBuild(){

    }

}
