package com.francony.romain.outerspacemanager.viewModel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.ImageView;

import com.francony.romain.outerspacemanager.BR;
import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.activity.BuildingActivity;
import com.francony.romain.outerspacemanager.fragment.BuildingsFragment;
import com.francony.romain.outerspacemanager.helpers.Helpers;
import com.francony.romain.outerspacemanager.model.Building;
import com.google.gson.Gson;

public class BuildingViewModel extends BaseObservable{
    private Building building;
    private View view;
    private BuildingsFragment fragment;


    public BuildingViewModel(Building building, View view, BuildingsFragment fragment) {
        this.building = building;
        this.view = view;
        this.fragment = fragment;
    }

    @Bindable
    public Building getBuilding() {
        return building;
    }


    public void setBuilding(Building building) {
        this.building = building;
        notifyPropertyChanged(BR.building);
    }

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String url) {
        Helpers.loadExternalImageWithAnimation(view,url);
    }


    public void startIntent(){
        Intent buildingIntent = new Intent(this.fragment.getContext(), BuildingActivity.class);
        Gson gson = new Gson();
        String building_json = gson.toJson(building);
        buildingIntent.putExtra("building", building_json);

        // Transition
        ImageView image = view.findViewById(R.id.building_image);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this.fragment.getActivity(), image, "building");
        this.fragment.getContext().startActivity(buildingIntent, options.toBundle());

    }
}
