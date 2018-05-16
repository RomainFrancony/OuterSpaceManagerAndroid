package com.francony.romain.outerspacemanager.activity;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.databinding.ActivityBuildingBinding;
import com.francony.romain.outerspacemanager.helpers.Helpers;
import com.francony.romain.outerspacemanager.helpers.SharedPreferencesHelper;
import com.francony.romain.outerspacemanager.model.Building;
import com.francony.romain.outerspacemanager.model.Progress;
import com.francony.romain.outerspacemanager.model.Progress_Table;
import com.francony.romain.outerspacemanager.response.ActionResponse;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerService;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerServiceFactory;
import com.google.gson.Gson;
import com.hkm.ui.processbutton.iml.SubmitProcessButton;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

import java.util.ArrayList;
import java.util.Comparator;
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
    private Handler handler;
    private SubmitProcessButton button;

    /**
     * Finish after shared element transition and give result to fragment calling the activity
     *
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        this.getReturnValue();
        finishAfterTransition();
        return true;
    }

    /**
     * Set return value
     */
    public void getReturnValue() {
        Intent data = new Intent();
        Gson gson = new Gson();
        data.putExtra("building", gson.toJson(this.building));
        setResult(RESULT_OK, data);
    }

    /**
     * Give result to fragment calling the activity
     *
     * @return
     */
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

        // Get current building
        Gson gson = new Gson();
        this.building = gson.fromJson(getIntent().getStringExtra("building"), Building.class);
        this.binding.setBuildingActivityViewModel(this);

        // Progress button
        this.initButtonProgress();

        // Get progress of current building
        this.getProgress();

        // Already building
        if (this.building.isBuilding()) {

            this.button.setEnabled(false);
            this.button.setProgress(1);

            if (this.progress == null) {
                return;
            }

            this.initCountdown();
        }
    }


    /**
     * Get progress of current building from DB Flow
     */
    private void getProgress() {
        ArrayList<Progress> progresses = (ArrayList<Progress>) SQLite.select().from(Progress.class)
                .where(Progress_Table.type.eq(Progress.TYPE_BUILDING))
                .and(Progress_Table.constructionObjectId.eq(this.building.getBuildingId()))
                .queryList();


        // Sort by end time
        progresses.sort(new Comparator<Progress>() {
            @Override
            public int compare(Progress o1, Progress o2) {
                return (int) (o2.getEndTime() - o1.getEndTime());
            }
        });

        // Remove all except the latest to finish
        ArrayList<Progress> toDelete = new ArrayList<>();
        for (Progress progress : progresses) {
            if (progress.getEndTime() <= System.currentTimeMillis() / 1000) {
                toDelete.add(progress);
            }
        }
        this.progressModelAdapter.deleteAll(toDelete);

        if (progresses.isEmpty()) {
            return;
        }

        this.progress = progresses.get(0);
    }

    /**
     * Build custom button
     */
    private void initButtonProgress() {
        this.button = findViewById(R.id.build_action_button);
        this.button.setOnClickNormalState(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuildingActivity.this.startBuild();
            }
        });
        this.button.build();
    }

    /**
     * Update the progress button progress
     */
    private void updateCountdown() {
        long startTime = this.progress.getEndTime() - this.building.getTimeToBuild();
        int progress = (int) (((System.currentTimeMillis() / 1000) - startTime) * 100 / (this.progress.getEndTime() - startTime));
        this.button.setProgress(progress <= 0 ? 1 : progress);
    }

    /**
     * Data binding load image with glide
     *
     * @param view
     * @param url
     */
    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String url) {
        Helpers.loadExternalImageWithAnimation(view, url);
    }


    /**
     * Get resource string for effect
     *
     * @return
     */
    public String getEffectString() {
        String packageName = getPackageName();
        if (this.building.getEffect() == null) {
            return "";
        }
        int resId = getResources().getIdentifier("effect." + this.building.getEffect(), "string", packageName);
        return getString(resId);
    }

    /**
     * API call for start building
     */
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

    /**
     * Save building progress with DB Flow
     */
    private void saveBuildingTime() {
        this.progressModelAdapter = FlowManager.getModelAdapter(Progress.class);
        this.progress = new Progress(UUID.randomUUID(), this.building.getTimeToBuild() + (System.currentTimeMillis() / 1000), Progress.TYPE_BUILDING, this.building.getBuildingId());
        this.progressModelAdapter.insert(this.progress);
        this.initCountdown();
    }

    /**
     * Init handler for updating the UI every time the progress has changed
     */
    private void initCountdown() {
        if (this.handler == null) {
            this.handler = new Handler();
        }

        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (!BuildingActivity.this.getWindow().getDecorView().getRootView().isShown()) {
                    return;
                }

                // Restore button to normal state, update building, clear thing
                if (BuildingActivity.this.button.getProgress() >= 100) {
                    BuildingActivity.this.button.setEnabled(true);
                    BuildingActivity.this.button.setProgress(0);
                    BuildingActivity.this.progressModelAdapter.delete(BuildingActivity.this.progress);
                    BuildingActivity.this.progress = null;
                    BuildingActivity.this.building.setBuilding(false);
                    BuildingActivity.this.building.setLevel(BuildingActivity.this.building.getLevel() + 1);
                    BuildingActivity.this.binding.setBuildingActivityViewModel(BuildingActivity.this);
                    return;
                }

                BuildingActivity.this.updateCountdown();
                BuildingActivity.this.handler.postDelayed(this, (BuildingActivity.this.building.getTimeToBuild() / 100) * 1000);
            }
        }, 0);
    }

}
