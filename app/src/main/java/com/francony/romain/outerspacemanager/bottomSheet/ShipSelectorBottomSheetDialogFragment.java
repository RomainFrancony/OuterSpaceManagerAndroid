package com.francony.romain.outerspacemanager.bottomSheet;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.activity.AttackActivity;
import com.francony.romain.outerspacemanager.adapter.ShipSelectorAdapter;
import com.francony.romain.outerspacemanager.model.Ship;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class ShipSelectorBottomSheetDialogFragment extends BottomSheetDialogFragment implements ShipSelectorAdapter.ItemClickListener {
    private ArrayList<Ship> ships = new ArrayList<>();
    private ProgressBar progressBar;
    private RecyclerView rvShips;
    private ShipSelectorAdapter adapter;


    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.bottom_sheet_ship_selector, null);
        dialog.setContentView(contentView);
        this.progressBar = contentView.findViewById(R.id.layout_loader);
        this.rvShips = contentView.findViewById(R.id.rv_ships);


        this.rvShips.setLayoutManager(new LinearLayoutManager(getContext()));
        this.adapter = new ShipSelectorAdapter(getContext(), ships);
        this.adapter.setClickListener(this);
        this.rvShips.setAdapter(this.adapter);

        // Hide the loader if we already have the ships
        if (this.ships.size() > 0) {
            this.rvShips.setVisibility(View.VISIBLE);
            this.progressBar.setVisibility(View.GONE);
        }


        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        dismiss();
        ((AttackActivity) getActivity()).selectShip(this.ships.get(position));
    }

    public void updateShips(ArrayList<Ship> ships) {
        this.ships.addAll(ships);

        if (this.rvShips != null) {
            this.rvShips.setVisibility(View.VISIBLE);
        }
        if (this.progressBar != null) {
            this.progressBar.setVisibility(View.GONE);
        }

        if (this.adapter != null) {
            this.adapter.notifyDataSetChanged();
        }
    }
}