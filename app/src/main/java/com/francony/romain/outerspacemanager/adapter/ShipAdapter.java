package com.francony.romain.outerspacemanager.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.CardView;
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


    @Override
    public int getItemViewType(int position) {
        return this.shipsDataset.get(position) == null ? TYPE_ADDSHIP : TYPE_SHIP;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_SHIP){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            AdapterShipBinding binding = DataBindingUtil.inflate(inflater, R.layout.adapter_ship, parent, false);
            return new ShipAdapterViewHolder(binding);
        }
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ship_add, parent, false);
        return new ShipAdapterAddViewHolder(v);
    }


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


    public class ShipAdapterViewHolder extends RecyclerView.ViewHolder {
        private final AdapterShipBinding binding;

        public ShipAdapterViewHolder(AdapterShipBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final Ship ship, Context context) {
            ShipViewModel shipViewModel = new ShipViewModel(ship, binding.getRoot(), context, cardType);

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


    public void setRemoveShipListener(ShipViewModel.RemoveShipListener removeShipListener) {
        this.removeShipListener = removeShipListener;
    }

    public class ShipAdapterAddViewHolder extends RecyclerView.ViewHolder {

        public ShipAdapterAddViewHolder(View v) {
            super(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onClickAddShipListener != null){
                        onClickAddShipListener.onClickAddShip();
                    }
                }
            });
        }

        public void bind() {
        }
    }


    public void setOnClickAddShipListener(ShipAdapter.OnClickAddShipListener onClickAddShipListener) {
        this.onClickAddShipListener = onClickAddShipListener;
    }

    public interface OnClickAddShipListener {
        void onClickAddShip();
    }
}
