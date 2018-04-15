package com.francony.romain.outerspacemanager.activity;

import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AppCompatActivity;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.bottomSheet.ShipSelectorBottomSheetDialogFragment;
import com.francony.romain.outerspacemanager.fragment.FleetFragment;
import com.francony.romain.outerspacemanager.helpers.Helpers;
import com.francony.romain.outerspacemanager.helpers.SharedPreferencesHelper;
import com.francony.romain.outerspacemanager.model.Ship;
import com.francony.romain.outerspacemanager.response.SpaceyardResponse;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerService;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerServiceFactory;
import com.google.gson.Gson;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttackActivity extends AppCompatActivity implements View.OnClickListener {
    private ShipSelectorBottomSheetDialogFragment bottomSheet;
    private OuterSpaceManagerService service = OuterSpaceManagerServiceFactory.create();
    private ArrayList<Ship> ships = new ArrayList<>();
    private Button buttonAddShip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attack);

        this.buttonAddShip = findViewById(R.id.button_add_ship);
        this.buttonAddShip.setOnClickListener(this);
        this.bottomSheet = new ShipSelectorBottomSheetDialogFragment();
        this.getShips();
    }


    @Override
    public void onClick(View v) {
        this.bottomSheet.show(getSupportFragmentManager(), this.bottomSheet.getTag());
    }


    public void getShips() {
        Call<SpaceyardResponse> request = this.service.fleetList(SharedPreferencesHelper.getToken(getApplicationContext()));

        request.enqueue(new Callback<SpaceyardResponse>() {

            @Override
            public void onResponse(Call<SpaceyardResponse> call, Response<SpaceyardResponse> response) {
                // Error
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), Helpers.getResponseErrorMessage(response), Toast.LENGTH_LONG).show();
                    return;
                }

                AttackActivity.this.ships.addAll(response.body().getShips());
                AttackActivity.this.bottomSheet.updateShips(AttackActivity.this.ships);

                if (response.body().getSize() == 0) {
                    Toast.makeText(getApplicationContext(),R.string.fleet_no_fleet_short,Toast.LENGTH_SHORT).show();
                    AttackActivity.this.finish();
                }
            }

            // Network error
            @Override
            public void onFailure(Call<SpaceyardResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.error_network, Toast.LENGTH_LONG).show();
            }
        });
    }


    public void selectShip(Ship ship) {
        this.ships.remove(ship);
        this.bottomSheet.updateShips(this.ships);
        if(this.ships.size() == 0){
            this.buttonAddShip.setVisibility(View.GONE);
        }
    }

}
