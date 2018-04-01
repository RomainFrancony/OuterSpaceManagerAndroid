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

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.adapter.BuildingAdapter;
import com.francony.romain.outerspacemanager.model.Building;

import java.lang.reflect.Array;
import java.util.ArrayList;



public class BuildingsFragment extends Fragment {
    private RecyclerView rvBuildings;
    private LinearLayoutManager rvLayoutManager;


    public BuildingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_buildings, container, false);


        this.rvBuildings = v.findViewById(R.id.buildings_rv);
        this.rvBuildings.setHasFixedSize(true);
        rvLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvBuildings.setLayoutManager(rvLayoutManager);

        ArrayList<Building> buildings = new ArrayList<>();
        Building b1 = new Building();
        b1.setName("Hello");
        Building b2 = new Building();
        b2.setName("World");
        Building b3 = new Building();
        b3.setName("Yolo");

        buildings.add(b1);
        buildings.add(b2);
        buildings.add(b3);
        rvBuildings.setAdapter(new BuildingAdapter(buildings));
        return v;
    }

}
