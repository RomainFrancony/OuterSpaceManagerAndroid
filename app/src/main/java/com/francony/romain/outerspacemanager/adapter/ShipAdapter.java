package com.francony.romain.outerspacemanager.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.databinding.AdapterShipBinding;
import com.francony.romain.outerspacemanager.fragment.SpaceyardFragment;
import com.francony.romain.outerspacemanager.model.Ship;
import com.warkiz.widget.IndicatorSeekBar;

import java.util.ArrayList;

public class ShipAdapter extends RecyclerView.Adapter<ShipAdapter.ShipAdapterViewHolder> {
    private ArrayList<Ship> shipsDataset;
    private SpaceyardFragment spaceyardFragment;

    public ShipAdapter(ArrayList<Ship> ships, SpaceyardFragment spaceyardFragment) {
        this.shipsDataset = ships;
        this.spaceyardFragment = spaceyardFragment;
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
        holder.bind(ship, this.spaceyardFragment);
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

        public void bind(final Ship ship, SpaceyardFragment spaceyardFragment){
            IndicatorSeekBar indicatorSeekBar = binding.getRoot().findViewById(R.id.ship_quantity_seekbar);


            indicatorSeekBar.setOnSeekChangeListener(new IndicatorSeekBar.OnSeekBarChangeListener() {

                @Override
                public void onProgressChanged(IndicatorSeekBar seekBar, int progress, float progressFloat, boolean fromUserTouch) {
                    // TODO pass it to fragment ? make ship observable so we don't need to rebind the whole object
                    ship.setAmount(progress);
                    binding.setShip(ship);
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


            ship.setAmount(1);
            binding.setShip(ship);
            binding.setSpaceyardFragment(spaceyardFragment);
            binding.executePendingBindings();
        }
    }
}
