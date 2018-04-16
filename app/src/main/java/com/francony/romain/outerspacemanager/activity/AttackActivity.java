package com.francony.romain.outerspacemanager.activity;

import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AppCompatActivity;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.adapter.ShipAdapter;
import com.francony.romain.outerspacemanager.bottomSheet.ShipSelectorBottomSheetDialogFragment;
import com.francony.romain.outerspacemanager.fragment.FleetFragment;
import com.francony.romain.outerspacemanager.helpers.Helpers;
import com.francony.romain.outerspacemanager.helpers.SharedPreferencesHelper;
import com.francony.romain.outerspacemanager.model.Building;
import com.francony.romain.outerspacemanager.model.Ship;
import com.francony.romain.outerspacemanager.model.UserScore;
import com.francony.romain.outerspacemanager.response.SpaceyardResponse;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerService;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerServiceFactory;
import com.francony.romain.outerspacemanager.viewModel.ShipViewModel;
import com.google.gson.Gson;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttackActivity extends AppCompatActivity implements ShipViewModel.RemoveShipListener, ShipAdapter.OnClickAddShipListener {
    private ShipSelectorBottomSheetDialogFragment bottomSheet;
    private OuterSpaceManagerService service = OuterSpaceManagerServiceFactory.create();
    private ArrayList<Ship> ships = new ArrayList<>();
    private ArrayList<Ship> selectedShips = new ArrayList<>();
    private RecyclerView rvSelectedShips;
    private LinearLayoutManager rvLayoutManager;
    private ShipAdapter selectedShipAdapter;
    private UserScore user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attack);

        // Toolbar conf
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Recycler view
        this.rvSelectedShips = findViewById(R.id.rv_selected_ships);
        this.rvSelectedShips.setHasFixedSize(true);
        this.rvLayoutManager = new LinearLayoutManager(getApplicationContext());
        this.rvSelectedShips.setLayoutManager(rvLayoutManager);
        // Add a null item for the "add card"
        this.selectedShips.add(null);
        this.selectedShipAdapter = new ShipAdapter(this.selectedShips, this.getApplicationContext(), ShipViewModel.SELECT);
        this.rvSelectedShips.setAdapter(this.selectedShipAdapter);
        this.selectedShipAdapter.setRemoveShipListener(this);
        this.selectedShipAdapter.setOnClickAddShipListener(this);

        this.bottomSheet = new ShipSelectorBottomSheetDialogFragment();
        this.getShips();

        // Get extra
        Gson gson = new Gson();
        this.user = gson.fromJson(getIntent().getStringExtra("user"), UserScore.class);
        TextView textView = findViewById(R.id.textview_attack_user);
        textView.setText(String.format(getString(R.string.attacks_user_to_attack), this.user.getUsername()));
    }

    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        return true;
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
                    Toast.makeText(getApplicationContext(), R.string.fleet_no_fleet_short, Toast.LENGTH_SHORT).show();
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

        // Insert selected ship before the "add card"
        this.selectedShips.add(this.selectedShips.size() - 1, ship);
        this.selectedShipAdapter.notifyItemInserted(this.selectedShips.indexOf(ship));

        // Remove the "add card" if all available type of ships are selected
        if (this.ships.size() == 0) {
            this.selectedShips.remove(this.selectedShips.size() - 1);
            this.selectedShipAdapter.notifyItemRemoved(this.selectedShips.size());
        }
    }


    @Override
    public void onRemoveShip(Ship ship) {
        int index = selectedShips.indexOf(ship);
        this.selectedShips.remove(ship);
        this.ships.add(ship);
        this.bottomSheet.updateShips(this.ships);
        this.selectedShipAdapter.notifyItemRemoved(index);

        // Add the "add card" if the user deleted a ship when all available type were selected
        if (this.ships.size() > 0 && this.selectedShips.get(this.selectedShips.size() - 1) != null) {
            this.selectedShips.add(null);
            this.selectedShipAdapter.notifyItemInserted(this.selectedShips.size() - 1);
        }
    }

    @Override
    public void onClickAddShip() {
        this.bottomSheet.show(getSupportFragmentManager(), this.bottomSheet.getTag());
    }
}
