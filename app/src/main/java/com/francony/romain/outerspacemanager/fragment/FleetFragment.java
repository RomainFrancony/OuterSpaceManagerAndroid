package com.francony.romain.outerspacemanager.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.activity.MainActivity;
import com.francony.romain.outerspacemanager.adapter.ShipAdapter;
import com.francony.romain.outerspacemanager.helpers.Helpers;
import com.francony.romain.outerspacemanager.helpers.SharedPreferencesHelper;
import com.francony.romain.outerspacemanager.model.Ship;
import com.francony.romain.outerspacemanager.response.SpaceyardResponse;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerService;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerServiceFactory;
import com.francony.romain.outerspacemanager.viewModel.ShipViewModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FleetFragment extends Fragment {
    private OuterSpaceManagerService service = OuterSpaceManagerServiceFactory.create();

    private LinearLayout laEmptyFleet;
    private RecyclerView rvShips;
    private LinearLayoutManager rvLayoutManager;
    private ArrayList<Ship> ships = new ArrayList<>();
    private ShipAdapter shipAdapter;


    public FleetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fleet, container, false);

        // Empty fleet layout
        this.laEmptyFleet = v.findViewById(R.id.fleet_empty_layout);
        Button button = laEmptyFleet.findViewById(R.id.fleet_empty_spaceyard_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                if(activity == null){
                    return;
                }
                activity.navigationView.getMenu().performIdentifierAction(R.id.nav_spaceyard, 0);
                activity.navigationView.getMenu().getItem(MainActivity.SPACEYARD_DRAWER_INDEX).setChecked(true);
            }
        });


        // Set recycler view
        this.rvShips = v.findViewById(R.id.fleet_rv);
        this.rvShips.setHasFixedSize(true);
        this.rvLayoutManager = new LinearLayoutManager(getContext());
        this.rvShips.setLayoutManager(rvLayoutManager);
        this.shipAdapter = new ShipAdapter(this.ships, this.getContext(), ShipViewModel.VIEW);
        this.rvShips.setAdapter(this.shipAdapter);
        this.getShips();
        return v;
    }

    public void getShips() {
        Call<SpaceyardResponse> request = this.service.fleetList(SharedPreferencesHelper.getToken(getActivity().getApplicationContext()));

        request.enqueue(new Callback<SpaceyardResponse>() {

            @Override
            public void onResponse(Call<SpaceyardResponse> call, Response<SpaceyardResponse> response) {
                // Error
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), Helpers.getResponseErrorMessage(response), Toast.LENGTH_LONG).show();
                    return;
                }

                FleetFragment.this.ships.addAll(response.body().getShips());
                FleetFragment.this.rvShips.setVisibility(View.VISIBLE);
                FleetFragment.this.shipAdapter.notifyDataSetChanged();

                // Display card for going to spaceyard if the use don't have any ships
                if (FleetFragment.this.ships.size() == 0) {
                    FleetFragment.this.laEmptyFleet.setVisibility(View.VISIBLE);
                }
            }

            // Network error
            @Override
            public void onFailure(Call<SpaceyardResponse> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), R.string.error_network, Toast.LENGTH_LONG).show();
            }
        });
    }


}
