package com.francony.romain.outerspacemanager.viewModel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.ImageView;

import com.francony.romain.outerspacemanager.BR;
import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.activity.BuildingActivity;
import com.francony.romain.outerspacemanager.fragment.BuildingsFragment;
import com.francony.romain.outerspacemanager.helpers.Helpers;
import com.francony.romain.outerspacemanager.model.Building;
import com.francony.romain.outerspacemanager.model.Progress;
import com.francony.romain.outerspacemanager.model.Progress_Table;
import com.google.gson.Gson;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

import java.util.ArrayList;
import java.util.Comparator;

public class BuildingViewModel extends BaseObservable {
    private Building building;
    private View view;
    private BuildingsFragment fragment;
    private ModelAdapter<Progress> progressModelAdapter = FlowManager.getModelAdapter(Progress.class);
    private Progress progress;
    private Handler handler;
    private int progressPercent;
    private boolean isViewVisible = true;


    public BuildingViewModel(Building building, View view, BuildingsFragment fragment) {
        this.building = building;
        this.view = view;
        this.fragment = fragment;


        // Get progress of building using DB Flow
        this.getProgress();

        // Already building
        if (building.isBuilding()) {

            this.setProgressPercent(1);

            if (this.progress == null) {
                return;
            }

            this.initCountdown();

        }
    }

    /**
     * Get progress from DB Flow
     */
    private void getProgress() {
        ArrayList<Progress> progresses = (ArrayList<Progress>) SQLite.select().from(Progress.class)
                .where(Progress_Table.type.eq(Progress.TYPE_BUILDING))
                .and(Progress_Table.constructionObjectId.eq(this.getBuilding().getBuildingId()))
                .queryList();

        // Sort by end time
        progresses.sort(new Comparator<Progress>() {
            @Override
            public int compare(Progress o1, Progress o2) {
                return (int) (o2.getEndTime() - o1.getEndTime());
            }
        });

        // Delete all except the latest
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
     * Init handler for automatic update in UI
     */
    private void initCountdown() {
        if (this.handler == null) {
            this.handler = new Handler();
        }

        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // Check if the view is still visible
                if (!BuildingViewModel.this.isViewVisible || BuildingViewModel.this.progress == null) {
                    return;
                }

                // Stop handler, update building and clean things
                if (BuildingViewModel.this.getProgressPercent() >= 100) {
                    BuildingViewModel.this.setProgressPercent(0);
                    BuildingViewModel.this.progressModelAdapter.delete(BuildingViewModel.this.progress);
                    BuildingViewModel.this.progress = null;
                    BuildingViewModel.this.getBuilding().setBuilding(false);
                    BuildingViewModel.this.getBuilding().setLevel(BuildingViewModel.this.getBuilding().getLevel() + 1);
                    return;
                }

                BuildingViewModel.this.updateCountdown();
                BuildingViewModel.this.handler.postDelayed(this, (BuildingViewModel.this.getBuilding().getTimeToBuild() / 100) * 1000);
            }
        }, 0);
    }

    /**
     * Update the button progress
     */
    private void updateCountdown() {
        long startTime = this.progress.getEndTime() - this.getBuilding().getTimeToBuild();
        int progress = (int) (((System.currentTimeMillis() / 1000) - startTime) * 100 / (this.progress.getEndTime() - startTime));
        this.setProgressPercent(progress);
    }


    /**
     * Data binding image loader
     *
     * @param view
     * @param url
     */
    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String url) {
        Helpers.loadExternalImageWithAnimation(view, url);
    }


    /**
     * Start single building activity
     */
    public void startIntent() {
        Intent buildingIntent = new Intent(this.fragment.getContext(), BuildingActivity.class);
        Gson gson = new Gson();
        String building_json = gson.toJson(building);
        buildingIntent.putExtra("building", building_json);

        // Transition
        ImageView image = view.findViewById(R.id.building_image);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this.fragment.getActivity(), image, "building");
        this.fragment.startActivityForResult(buildingIntent, BuildingsFragment.BUILDING_ACTIVITY_REQUEST, options.toBundle());
    }


    public void setViewVisible(boolean viewVisible) {
        isViewVisible = viewVisible;

        // Reinit the countdown because it may be stopped
        if (viewVisible) {
            this.initCountdown();
        }
    }

    @Bindable
    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
        notifyPropertyChanged(BR.building);
    }

    @Bindable
    public int getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(int progressPercent) {
        this.progressPercent = progressPercent;
        notifyPropertyChanged(BR.progressPercent);
    }
}
