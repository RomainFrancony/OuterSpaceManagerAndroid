package com.francony.romain.outerspacemanager.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.adapter.ShipAdapter;
import com.francony.romain.outerspacemanager.bottomSheet.ShipSelectorBottomSheetDialogFragment;
import com.francony.romain.outerspacemanager.helpers.Helpers;
import com.francony.romain.outerspacemanager.helpers.SharedPreferencesHelper;
import com.francony.romain.outerspacemanager.model.Progress;
import com.francony.romain.outerspacemanager.model.Ship;
import com.francony.romain.outerspacemanager.model.UserScore;
import com.francony.romain.outerspacemanager.response.ActionResponse;
import com.francony.romain.outerspacemanager.response.SpaceyardResponse;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerService;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerServiceFactory;
import com.francony.romain.outerspacemanager.viewModel.ShipViewModel;
import com.google.gson.Gson;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Predicate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttackActivity extends AppCompatActivity implements ShipViewModel.RemoveShipListener, ShipAdapter.OnClickAddShipListener, View.OnClickListener {
    private ShipSelectorBottomSheetDialogFragment bottomSheet;
    private OuterSpaceManagerService service = OuterSpaceManagerServiceFactory.create();
    private ArrayList<Ship> ships = new ArrayList<>();
    private ArrayList<Ship> selectedShips = new ArrayList<>();
    private RecyclerView rvSelectedShips;
    private LinearLayoutManager rvLayoutManager;
    private ShipAdapter selectedShipAdapter;
    private UserScore user;
    private FloatingActionButton fab;
    private RelativeLayout laLoader;


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

        // FAB
        this.fab = findViewById(R.id.fab_attack);
        this.fab.setOnClickListener(this);
        this.fab.hide();

        // Loader
        this.laLoader = findViewById(R.id.layout_loader);
        this.laLoader.setVisibility(View.GONE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        return true;
    }


    /**
     * API call
     */
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
                AttackActivity.this.ships.removeIf(new Predicate<Ship>() {
                    @Override
                    public boolean test(Ship ship) {
                        return ship.getAmount() <= 0;
                    }
                });

                // User doesn't have fleet
                if(AttackActivity.this.ships.size() == 0){
                    AttackActivity.this.finish();
                    Toast.makeText(getApplicationContext(), R.string.fleet_no_fleet_short, Toast.LENGTH_LONG).show();
                }
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

    /**
     * Save progress with DB Flow
     * @param attackTime
     */
    private void saveAttackTime(long attackTime) {
        ModelAdapter<Progress> attackProgressAdapter = FlowManager.getModelAdapter(Progress.class);
        Progress attackProgress = new Progress(UUID.randomUUID(), attackTime, Progress.TYPE_ATTACK);
        attackProgressAdapter.insert(attackProgress);
    }

    /**
     * Add ship to selected ships and remove it from the bottom sheet
     * @param ship
     */
    public void selectShip(Ship ship) {
        this.fab.show();
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


    /**
     * Remove ship from selected ships and add it back to the bottom sheet
     * @param ship
     */
    @Override
    public void onRemoveShip(Ship ship) {
        int index = selectedShips.indexOf(ship);
        this.selectedShips.remove(ship);
        this.ships.add(ship);
        this.bottomSheet.updateShips(this.ships);
        this.selectedShipAdapter.notifyItemRemoved(index);

        if(this.selectedShips.size() == 0){
            this.fab.hide();
            this.selectedShips.add(null);
            this.selectedShipAdapter.notifyItemInserted(this.selectedShips.size() - 1);
            return;
        }

        if(this.selectedShips.get(this.selectedShips.size() - 1) != null){
            this.selectedShips.add(null);
            this.selectedShipAdapter.notifyItemInserted(this.selectedShips.size() - 1);
        }

        if(this.selectedShips.size() == 1){
            this.fab.hide();
        }

    }

    /**
     * Show bottom sheet
     */
    @Override
    public void onClickAddShip() {
        this.bottomSheet.show(getSupportFragmentManager(), this.bottomSheet.getTag());
    }

    /**
     * API call for attack
     * @param v
     */
    @Override
    public void onClick(View v) {
        this.laLoader.setVisibility(View.VISIBLE);
        this.fab.hide();

        int lastIndex = this.selectedShips.size() -1;
        if(this.selectedShips.get(lastIndex) == null){
            this.selectedShips.remove(lastIndex);
            this.selectedShipAdapter.notifyItemRemoved(lastIndex);
        }


        HashMap<Object, Object> body = new HashMap<>();
        body.put("ships", this.selectedShips);

        Call<ActionResponse> request = this.service.attackPlayer(SharedPreferencesHelper.getToken(getApplicationContext()), this.user.getUsername(),body);

        request.enqueue(new Callback<ActionResponse>() {

            @Override
            public void onResponse(Call<ActionResponse> call, Response<ActionResponse> response) {
                // Error
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), Helpers.getResponseErrorMessage(response), Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(getApplicationContext(), R.string.attacks_started, Toast.LENGTH_LONG).show();
                AttackActivity.this.saveAttackTime(response.body().getAttackTime());
                AttackActivity.this.finish();
            }

            // Network error
            @Override
            public void onFailure(Call<ActionResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.error_network, Toast.LENGTH_LONG).show();
            }
        });
    }
}
