package com.francony.romain.outerspacemanager.viewModel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.francony.romain.outerspacemanager.BR;
import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.helpers.Helpers;
import com.francony.romain.outerspacemanager.helpers.SharedPreferencesHelper;
import com.francony.romain.outerspacemanager.model.Ship;
import com.francony.romain.outerspacemanager.response.ShipBuildingResponse;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerService;
import com.francony.romain.outerspacemanager.services.OuterSpaceManagerServiceFactory;
import com.warkiz.widget.IndicatorSeekBar;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShipViewModel extends BaseObservable {
    public static final int BUILD = 0;
    public static final int VIEW = 1;
    public static final int SELECT = 2;


    private Ship ship;
    private View view;
    private Context context;
    private IndicatorSeekBar indicatorSeekBar;
    private int cardType;
    private RemoveShipListener removeShipListener;

    private OuterSpaceManagerService service = OuterSpaceManagerServiceFactory.create();

    private Boolean willBuild = false;
    private Boolean shipBuildLoading = false;


    public ShipViewModel(final Ship ship, View view, Context context, int cardType) {
        this.ship = ship;
        if (this.ship.getAmount() == null) {
            this.ship.setAmount(1);
        }
        this.view = view;
        this.context = context;
        this.cardType = cardType;


        this.indicatorSeekBar = this.view.findViewById(R.id.ship_quantity_seekbar);

        if (this.cardType == ShipViewModel.SELECT) {
            this.indicatorSeekBar.setMax(this.ship.getAmount());
            this.indicatorSeekBar.setProgress(this.ship.getAmount());

            // Hide seekbar if the use only have 1 ship
            if (this.ship.getAmount() == 1) {
                this.indicatorSeekBar.setVisibility(View.GONE);
            }
        }

        // Set the progress handler here because there is no attribute for setting it in xml
        this.indicatorSeekBar.setOnSeekChangeListener(new IndicatorSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(IndicatorSeekBar seekBar, int progress, float progressFloat, boolean fromUserTouch) {
                ShipViewModel.this.ship.setAmount(progress);
            }

            @Override
            public void onSectionChanged(IndicatorSeekBar seekBar, int thumbPosOnTick, String textBelowTick, boolean fromUserTouch) {
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar, int thumbPosOnTick) {
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
            }
        });
    }


    /**
     * API call
     */
    public void startConstruction() {
        this.indicatorSeekBar.setEnabled(false);
        this.setShipBuildLoading(true);
        HashMap<Object, Object> body = new HashMap<>();
        body.put("shipId", this.ship.getShipId());
        body.put("amount", this.ship.getAmount());


        Call<ShipBuildingResponse> request = this.service.shipBuild(SharedPreferencesHelper.getToken(this.context), this.ship.getShipId(), body);
        request.enqueue(new Callback<ShipBuildingResponse>() {
            @Override
            public void onResponse(Call<ShipBuildingResponse> call, Response<ShipBuildingResponse> response) {
                ShipViewModel.this.indicatorSeekBar.setEnabled(true);
                ShipViewModel.this.setShipBuildLoading(false);

                if (!response.isSuccessful()) {
                    Toast.makeText(context, Helpers.getResponseErrorMessage(response), Toast.LENGTH_LONG).show();
                    return;
                }

                ShipViewModel.this.setWillBuild(false);
                Snackbar snackbar = Snackbar.make(view, context.getResources().getString(R.string.ship_ordered, ship.getAmount(), ship.getName()), Snackbar.LENGTH_LONG);
                Helpers.showSnackbarWithAnimation(snackbar);
                ShipViewModel.this.indicatorSeekBar.setProgress(1); // Can't do it via databinding (don't know why but it doesn't work)
                ShipViewModel.this.ship.setAmount(1);
            }

            // Network error
            @Override
            public void onFailure(Call<ShipBuildingResponse> call, Throwable t) {
                Toast.makeText(context, R.string.error_network, Toast.LENGTH_LONG).show();
                ShipViewModel.this.setShipBuildLoading(false);
                ShipViewModel.this.indicatorSeekBar.setEnabled(true);
            }
        });
    }

    /**
     * Attach custom event listener
     *
     * @param removeShipListener
     */
    public void setRemoveShipListener(ShipViewModel.RemoveShipListener removeShipListener) {
        this.removeShipListener = removeShipListener;
    }

    /**
     * Custom event interface
     */
    public interface RemoveShipListener {
        void onRemoveShip(Ship ship);
    }


    @Bindable
    public Boolean getShipBuildLoading() {
        return shipBuildLoading;
    }

    public void setShipBuildLoading(Boolean shipBuildLoading) {
        this.shipBuildLoading = shipBuildLoading;
        notifyPropertyChanged(BR.shipBuildLoading);
    }

    @Bindable
    public Boolean getWillBuild() {
        return willBuild;
    }

    public void setWillBuild(Boolean willBuild) {
        this.willBuild = willBuild;
        notifyPropertyChanged(BR.willBuild);
    }


    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public void removeShip() {
        // Reset to the actual ship amount
        this.ship.setAmount(Math.round(this.indicatorSeekBar.getMax()));
        if (removeShipListener != null) removeShipListener.onRemoveShip(this.ship);
    }
}
