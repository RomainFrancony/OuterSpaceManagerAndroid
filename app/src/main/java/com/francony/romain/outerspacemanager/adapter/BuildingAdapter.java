package com.francony.romain.outerspacemanager.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.databinding.AdapterBuildingBinding;
import com.francony.romain.outerspacemanager.fragment.BuildingsFragment;
import com.francony.romain.outerspacemanager.model.Building;
import com.francony.romain.outerspacemanager.viewModel.BuildingViewModel;

import java.util.ArrayList;
import java.util.List;

public class BuildingAdapter extends RecyclerView.Adapter<BuildingAdapter.BuildingAdapterViewHolder> {
    private ArrayList<Building> buildingsDataset;
    private BuildingsFragment fragment;
    private RecyclerView recyclerView;

    public BuildingAdapter(ArrayList<Building> buildings, BuildingsFragment fragment) {
        this.buildingsDataset = buildings;
        this.fragment = fragment;
    }


    @Override
    public BuildingAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        AdapterBuildingBinding binding = DataBindingUtil.inflate(inflater, R.layout.adapter_building, parent, false);
        return new BuildingAdapterViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(BuildingAdapterViewHolder holder, int position) {
        Building building = this.buildingsDataset.get(position);
        holder.bind(building, fragment);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;


        // Prevent view model to continue update the ui when the view is detached from the recycler
        this.recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                BuildingAdapterViewHolder holder = (BuildingAdapterViewHolder) BuildingAdapter.this.recyclerView.getChildViewHolder(view);
                holder.buildingViewModel.setViewVisible(true);
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                BuildingAdapterViewHolder holder = (BuildingAdapterViewHolder) BuildingAdapter.this.recyclerView.getChildViewHolder(view);
                holder.buildingViewModel.setViewVisible(false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return buildingsDataset.size();
    }


    public class BuildingAdapterViewHolder extends RecyclerView.ViewHolder {
        private final AdapterBuildingBinding binding;
        private BuildingViewModel buildingViewModel;


        public BuildingAdapterViewHolder(AdapterBuildingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Building building, BuildingsFragment fragment) {
            this.buildingViewModel = new BuildingViewModel(building, binding.getRoot(), fragment);
            binding.setBuildingViewModel(this.buildingViewModel);
            binding.executePendingBindings();
        }
    }
}
