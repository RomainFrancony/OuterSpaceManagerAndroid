package com.francony.romain.outerspacemanager.activity;

import android.content.Intent;
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
import android.widget.Toast;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.databinding.ActivityBuildingBinding;
import com.francony.romain.outerspacemanager.fragment.BuildingsFragment;
import com.francony.romain.outerspacemanager.helpers.Helpers;
import com.francony.romain.outerspacemanager.helpers.SharedPreferencesHelper;
import com.francony.romain.outerspacemanager.model.Building;
import com.francony.romain.outerspacemanager.model.Progress;
import com.francony.romain.outerspacemanager.response.ActionResponse;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerService;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerServiceFactory;
import com.francony.romain.outerspacemanager.viewModel.SearchViewModel;
import com.google.gson.Gson;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuildingActivity extends AppCompatActivity {
    public Building building;
    private ActivityBuildingBinding binding;

    private OuterSpaceManagerService service = OuterSpaceManagerServiceFactory.create();
    private ModelAdapter<Progress> progressModelAdapter = FlowManager.getModelAdapter(Progress.class);
    private Progress progress;

    @Override
    public boolean onSupportNavigateUp() {
        this.getReturnValue();
        finishAfterTransition();
        return true;
    }

    public void getReturnValue() {
        Intent data = new Intent();
        Gson gson = new Gson();
        data.putExtra("building", gson.toJson(this.building));
        setResult(RESULT_OK, data);
    }

    @Override
    public void onBackPressed() {
        this.getReturnValue();
        super.onBackPressed();
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
        Helpers.loadExternalImageWithAnimation(view, url);
    }


    public String getEffectString() {
        String packageName = getPackageName();
        if (this.building.getEffect() == null) {
            return "";
        }
        int resId = getResources().getIdentifier("effect." + this.building.getEffect(), "string", packageName);
        return getString(resId);
    }

    public void startBuild() {
        Call<ActionResponse> request = BuildingActivity.this.service.buildingBuild(SharedPreferencesHelper.getToken(getApplicationContext()), BuildingActivity.this.building.getBuildingId());

        request.enqueue(new Callback<ActionResponse>() {
            @Override
            public void onResponse(Call<ActionResponse> call, Response<ActionResponse> response) {
                // Error
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), Helpers.getResponseErrorMessage(response), Toast.LENGTH_LONG).show();
                    return;
                }

                BuildingActivity.this.building.setBuilding(true);
                BuildingActivity.this.saveBuildingTime();
            }

            // Network error
            @Override
            public void onFailure(Call<ActionResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.error_network, Toast.LENGTH_LONG).show();
            }
        });

    }

    private void saveBuildingTime() {
        this.progressModelAdapter = FlowManager.getModelAdapter(Progress.class);
        this.progress = new Progress(UUID.randomUUID(), this.building.getTimeToBuild() + (System.currentTimeMillis() / 1000), Progress.TYPE_BUILDING, this.building.getBuildingId());
        this.progressModelAdapter.insert(this.progress);
    }

}
