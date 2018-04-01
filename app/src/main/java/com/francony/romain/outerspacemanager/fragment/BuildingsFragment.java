package com.francony.romain.outerspacemanager.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.activity.LoginActivity;
import com.francony.romain.outerspacemanager.adapter.BuildingAdapter;
import com.francony.romain.outerspacemanager.helpers.SharedPreferencesHelper;
import com.francony.romain.outerspacemanager.model.Building;
import com.francony.romain.outerspacemanager.response.BuildingsResponse;
import com.francony.romain.outerspacemanager.response.UserResponse;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerService;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerServiceFactory;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BuildingsFragment extends Fragment {
    private OuterSpaceManagerService service = OuterSpaceManagerServiceFactory.create();

    private RelativeLayout laLoader;
    private RecyclerView rvBuildings;
    private LinearLayoutManager rvLayoutManager;
    private ArrayList<Building> buildings = new ArrayList<>();
    private BuildingAdapter buildingAdapter = new BuildingAdapter(this.buildings);


    public BuildingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_buildings, container, false);


        this.laLoader = v.findViewById(R.id.layout_loader);
        this.rvBuildings = v.findViewById(R.id.buildings_rv);
        this.rvBuildings.setHasFixedSize(true);
        rvLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvBuildings.setLayoutManager(rvLayoutManager);
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
                if (response.code() != 200) {
                    Toast.makeText(getActivity().getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
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
