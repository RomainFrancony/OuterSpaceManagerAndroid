package com.francony.romain.outerspacemanager.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.databinding.AdapterShipBinding;
import com.francony.romain.outerspacemanager.model.Ship;
import com.francony.romain.outerspacemanager.viewModel.ShipViewModel;

import java.util.ArrayList;

public class ShipAdapter extends RecyclerView.Adapter<ShipAdapter.ShipAdapterViewHolder> {
    private ArrayList<Ship> shipsDataset;
    private Context context;
    private int cardType;
    private ShipViewModel.RemoveShipListener removeShipListener;

    public ShipAdapter(ArrayList<Ship> ships, Context context, int cardType) {
        this.shipsDataset = ships;
        this.context = context;
        this.cardType = cardType;
    }


    @Override
    public ShipAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        AdapterShipBinding binding = DataBindingUtil.inflate(inflater, R.layout.adapter_ship, parent, false);
        return new ShipAdapterViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(ShipAdapterViewHolder holder, int position) {
        Ship ship = this.shipsDataset.get(position);
        holder.bind(ship, this.context);
    }

    @Override
    public int getItemCount() {
        return shipsDataset.size();
    }


    public class ShipAdapterViewHolder extends RecyclerView.ViewHolder {
        private final AdapterShipBinding binding;

        public ShipAdapterViewHolder(AdapterShipBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final Ship ship, Context context) {
            ShipViewModel shipViewModel = new ShipViewModel(ship, binding.getRoot(), context, cardType);

            if(removeShipListener != null){
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


    public void setRemoveShipListener(ShipViewModel.RemoveShipListener removeShipListener) {
        this.removeShipListener = removeShipListener;
    }
}
