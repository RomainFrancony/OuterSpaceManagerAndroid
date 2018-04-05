package com.francony.romain.outerspacemanager.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.databinding.AdapterShipBinding;
import com.francony.romain.outerspacemanager.model.Ship;

import java.util.ArrayList;

public class ShipAdapter extends RecyclerView.Adapter<ShipAdapter.ShipAdapterViewHolder> {
    private ArrayList<Ship> shipsDataset;

    public ShipAdapter(ArrayList<Ship> ships) {
        this.shipsDataset = ships;
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
        holder.bind(ship);
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

        public void bind(Ship ship ){
            binding.setShip(ship);
            binding.executePendingBindings();
        }
    }
}
