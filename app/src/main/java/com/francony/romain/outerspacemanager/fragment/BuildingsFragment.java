package com.francony.romain.outerspacemanager.fragment;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.activity.LoginActivity;
import com.francony.romain.outerspacemanager.adapter.BuildingAdapter;
import com.francony.romain.outerspacemanager.helpers.Helpers;
import com.francony.romain.outerspacemanager.helpers.SharedPreferencesHelper;
import com.francony.romain.outerspacemanager.model.Building;
import com.francony.romain.outerspacemanager.response.BuildingsResponse;
import com.francony.romain.outerspacemanager.response.UserResponse;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerService;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerServiceFactory;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BuildingsFragment extends Fragment {
    private OuterSpaceManagerService service = OuterSpaceManagerServiceFactory.create();
    public static int BUILDING_ACTIVITY_REQUEST = 42;

    private RelativeLayout laLoader;
    private RecyclerView rvBuildings;
    private LinearLayoutManager rvLayoutManager;
    private ArrayList<Building> buildings = new ArrayList<>();
    private BuildingAdapter buildingAdapter;


    public BuildingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rvBuildings.setAdapter(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != BuildingsFragment.BUILDING_ACTIVITY_REQUEST) {
            return;
        }

        // Get data
        Building building;
        Gson gson = new Gson();
        String building_json = data.getStringExtra("building");

        if (building_json == null) {
            return;
        }
        building = gson.fromJson(building_json, Building.class);
        if (building == null) {
            return;
        }

        // Check if building exists in current dataset
        int index = this.buildings.indexOf(building);
        if (index == -1) {
            return;
        }

        // Update the item only if the level or the building state has changed (only thing that alter the UI)
        if (building.getLevel() == this.buildings.get(index).getLevel() && building.isBuilding() == this.buildings.get(index).isBuilding()) {
            return;
        }
        this.buildings.set(index, building);
        this.buildingAdapter.notifyItemChanged(index);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_buildings, container, false);


        this.laLoader = v.findViewById(R.id.layout_loader);
        this.rvBuildings = v.findViewById(R.id.buildings_rv);
        this.rvBuildings.setHasFixedSize(true);
        rvLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvBuildings.setLayoutManager(rvLayoutManager);
        this.buildingAdapter = new BuildingAdapter(this.buildings, this);
        rvBuildings.setAdapter(this.buildingAdapter);
        this.getBuildings();
        return v;
    }

    public void getBuildings() {
        Call<BuildingsResponse> request = this.service.buildingList(SharedPreferencesHelper.getToken(getActivity().getApplicationContext()));

        request.enqueue(new Callback<BuildingsResponse>() {

            @Override
            public void onResponse(Call<BuildingsResponse> call, Response<BuildingsResponse> response) {
                // Error
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), Helpers.getResponseErrorMessage(response), Toast.LENGTH_LONG).show();
                    return;
                }

                BuildingsFragment.this.buildings.addAll(response.body().getBuildings());
                BuildingsFragment.this.laLoader.setVisibility(View.GONE);
                BuildingsFragment.this.rvBuildings.setVisibility(View.VISIBLE);
                BuildingsFragment.this.buildingAdapter.notifyDataSetChanged();
            }

            // Network error
            @Override
            public void onFailure(Call<BuildingsResponse> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), R.string.error_network, Toast.LENGTH_LONG).show();
            }
        });
    }
}
