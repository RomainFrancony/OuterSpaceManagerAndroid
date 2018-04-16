package com.francony.romain.outerspacemanager.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.databinding.AdapterShipBinding;
import com.francony.romain.outerspacemanager.model.Ship;
import com.francony.romain.outerspacemanager.viewModel.ShipViewModel;

import java.util.ArrayList;

public class ShipSelectorAdapter extends RecyclerView.Adapter<ShipSelectorAdapter.ShipSelectorAdapterViewHolder> {

    private ArrayList<Ship> ships;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public ShipSelectorAdapter(Context context, ArrayList<Ship> ships) {
        this.mInflater = LayoutInflater.from(context);
        this.ships = ships;
    }

    // inflates the row layout from xml when needed
    @Override
    public ShipSelectorAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_ship_selector, parent, false);
        return new ShipSelectorAdapterViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ShipSelectorAdapterViewHolder holder, int position) {
        Ship ship = ships.get(position);
        holder.myTextView.setText(ship.getName());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return ships.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ShipSelectorAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ShipSelectorAdapterViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.ship_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Ship getItem(int id) {
        return ships.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
