package com.francony.romain.outerspacemanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.model.Ship;

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

    /**
     * Create view holder
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ShipSelectorAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_ship_selector, parent, false);
        return new ShipSelectorAdapterViewHolder(view);
    }

    /**
     * Bind ship to view holder
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ShipSelectorAdapterViewHolder holder, int position) {
        Ship ship = ships.get(position);
        holder.myTextView.setText(ship.getName());
    }

    @Override
    public int getItemCount() {
        return ships.size();
    }


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

    /**
     * Click listener
     * @param itemClickListener
     */
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    /**
     * Click interface
     */
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
