package com.francony.romain.outerspacemanager.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.databinding.AdapterShipBinding;
import com.francony.romain.outerspacemanager.model.Ship;
import com.francony.romain.outerspacemanager.viewModel.ShipViewModel;

import java.util.ArrayList;

public class ShipAdapter extends RecyclerView.Adapter {
    private static final int TYPE_SHIP = 0;
    private static final int TYPE_ADDSHIP = 1;

    private ShipAdapter.OnClickAddShipListener onClickAddShipListener;
    private ArrayList<Ship> shipsDataset;
    private Context context;
    private int cardType;
    private ShipViewModel.RemoveShipListener removeShipListener;

    public ShipAdapter(ArrayList<Ship> ships, Context context, int cardType) {
        this.shipsDataset = ships;
        this.context = context;
        this.cardType = cardType;
    }


    /**
     * Check if item is a ship or a "Add ship" card (used in AttackActivity)
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return this.shipsDataset.get(position) == null ? TYPE_ADDSHIP : TYPE_SHIP;
    }

    /**
     * Create view holder with data binding depending on the card type
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_SHIP) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            AdapterShipBinding binding = DataBindingUtil.inflate(inflater, R.layout.adapter_ship, parent, false);
            return new ShipAdapterViewHolder(binding);
        }
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ship_add, parent, false);
        return new ShipAdapterAddViewHolder(v);
    }

    /**
     * Bind ship to view holder
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ShipAdapterViewHolder) {
            Ship ship = this.shipsDataset.get(position);
            ((ShipAdapterViewHolder) holder).bind(ship, this.context);
        }
    }

    @Override
    public int getItemCount() {
        return shipsDataset.size();
    }

    /**
     * View holder displaying a ship
     */
    public class ShipAdapterViewHolder extends RecyclerView.ViewHolder {
        private final AdapterShipBinding binding;

        public ShipAdapterViewHolder(AdapterShipBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        /**
         * Bind ship to UI
         *
         * @param ship
         * @param context
         */
        public void bind(final Ship ship, Context context) {
            ShipViewModel shipViewModel = new ShipViewModel(ship, binding.getRoot(), context, cardType);

            // Bind the event to the view model (it start in the view model and bubble up to the fragment)
            if (removeShipListener != null) {
                shipViewModel.setRemoveShipListener(new ShipViewModel.RemoveShipListener() {
                    @Override
                    public void onRemoveShip(Ship ship) {
                        removeShipListener.onRemoveShip(ship);
                    }
                });
            }
            binding.setShipViewModel(shipViewModel);
            binding.executePendingBindings();
        }
    }


    /**
     * Set custom event listener
     *
     * @param removeShipListener
     */
    public void setRemoveShipListener(ShipViewModel.RemoveShipListener removeShipListener) {
        this.removeShipListener = removeShipListener;
    }

    /**
     * View holder for "Add ship" card
     */
    public class ShipAdapterAddViewHolder extends RecyclerView.ViewHolder {

        public ShipAdapterAddViewHolder(View v) {
            super(v);
            // Add click handler
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickAddShipListener != null) {
                        onClickAddShipListener.onClickAddShip();
                    }
                }
            });
        }
    }


    public void setOnClickAddShipListener(ShipAdapter.OnClickAddShipListener onClickAddShipListener) {
        this.onClickAddShipListener = onClickAddShipListener;
    }

    public interface OnClickAddShipListener {
        void onClickAddShip();
    }
}
