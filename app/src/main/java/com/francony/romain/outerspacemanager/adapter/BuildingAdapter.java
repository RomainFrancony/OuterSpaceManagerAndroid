package com.francony.romain.outerspacemanager.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.databinding.AdapterBuildingBinding;
import com.francony.romain.outerspacemanager.fragment.BuildingsFragment;
import com.francony.romain.outerspacemanager.model.Building;
import com.francony.romain.outerspacemanager.viewModel.BuildingViewModel;

import java.util.ArrayList;

public class BuildingAdapter extends RecyclerView.Adapter<BuildingAdapter.BuildingAdapterViewHolder> {
    private ArrayList<Building> buildingsDataset;
    private BuildingsFragment fragment;
    private RecyclerView recyclerView;

    public BuildingAdapter(ArrayList<Building> buildings, BuildingsFragment fragment) {
        this.buildingsDataset = buildings;
        this.fragment = fragment;
    }


    /**
     * Create view holder with data binding
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public BuildingAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        AdapterBuildingBinding binding = DataBindingUtil.inflate(inflater, R.layout.adapter_building, parent, false);
        return new BuildingAdapterViewHolder(binding);
    }


    /**
     * Bind building to view holder
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(BuildingAdapterViewHolder holder, int position) {
        Building building = this.buildingsDataset.get(position);
        holder.bind(building, fragment);
    }

    /**
     * Get the attached recycler view
     *
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
        this.initRecyclerViewListener();
    }

    /**
     * Prevent view models to continue updating the UI when the view is detached from the recycler
     * while scrolling (onBindViewHolder is not necessary called and therefore no view model is created)
     */
    private void initRecyclerViewListener() {

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

    /**
     * Stop all view model from updating the UI when quiting the fragment
     * @param recyclerView
     */
    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        for (int childCount = recyclerView.getChildCount(), i = 0; i < childCount; ++i) {
            final BuildingAdapterViewHolder holder = (BuildingAdapterViewHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
            holder.buildingViewModel.setViewVisible(false);
        }
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

        /**
         * Bind building to UI
         * @param building
         * @param fragment
         */
        public void bind(Building building, BuildingsFragment fragment) {
            this.buildingViewModel = new BuildingViewModel(building, binding.getRoot(), fragment);
            binding.setBuildingViewModel(this.buildingViewModel);
            binding.executePendingBindings();
        }
    }
}
