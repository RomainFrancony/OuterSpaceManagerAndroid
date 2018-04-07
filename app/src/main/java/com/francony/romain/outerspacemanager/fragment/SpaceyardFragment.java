package com.francony.romain.outerspacemanager.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.adapter.ShipAdapter;
import com.francony.romain.outerspacemanager.helpers.SharedPreferencesHelper;
import com.francony.romain.outerspacemanager.model.Ship;
import com.francony.romain.outerspacemanager.response.SpaceyardResponse;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerService;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerServiceFactory;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class SpaceyardFragment extends Fragment {
    private OuterSpaceManagerService service = OuterSpaceManagerServiceFactory.create();

    private RelativeLayout laLoader;
    private RecyclerView rvShips;
    private LinearLayoutManager rvLayoutManager;
    private ArrayList<Ship> ships = new ArrayList<>();
    private ShipAdapter shipAdapter = new ShipAdapter(this.ships,this);


    public SpaceyardFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_spaceyard, container, false);


        this.laLoader = v.findViewById(R.id.layout_loader);
        this.rvShips = v.findViewById(R.id.ships_rv);
        this.rvShips.setHasFixedSize(true);
        rvLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvShips.setLayoutManager(rvLayoutManager);
        rvShips.setAdapter(this.shipAdapter);
        this.getShips();
        return v;
    }


    public void getShips() {
        Call<SpaceyardResponse> request = this.service.spaceyardList(SharedPreferencesHelper.getToken(getActivity().getApplicationContext()));

        request.enqueue(new Callback<SpaceyardResponse>() {

            @Override
            public void onResponse(Call<SpaceyardResponse> call, Response<SpaceyardResponse> response) {
                // Error
                if (response.code() != 200) {
                    Toast.makeText(getActivity().getApplicationContext(), response.message(), Toast.LENGTH_LONG).show();
                    return;
                }

                SpaceyardFragment.this.ships.addAll(response.body().getShips());
                SpaceyardFragment.this.laLoader.setVisibility(View.GONE);
                SpaceyardFragment.this.rvShips.setVisibility(View.VISIBLE);
                SpaceyardFragment.this.shipAdapter.notifyDataSetChanged();
            }

            // Network error
            @Override
            public void onFailure(Call<SpaceyardResponse> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), R.string.error_network, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void startConstruction(Ship ship){
        Log.wtf("ah","construct");
    }

    public void test(){
        Log.wtf("ah","test");
    }
}
