package com.francony.romain.outerspacemanager.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.model.AttackProgress;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class AttackProgressAdapter extends RecyclerView.Adapter<AttackProgressAdapter.AttackProgressAdapterViewHolder> {
    private ArrayList<AttackProgress> attackProgressDataset;
    private Context context;

    public AttackProgressAdapter(ArrayList<AttackProgress> attackProgress, Context context) {
        this.attackProgressDataset = attackProgress;
        this.context = context;
    }


    @Override
    public AttackProgressAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_attack_progress, parent, false);
        return new AttackProgressAdapterViewHolder(v);
    }


    @Override
    public void onBindViewHolder(AttackProgressAdapterViewHolder holder, int position) {
        AttackProgress attackProgress = this.attackProgressDataset.get(position);
        holder.textView.setText(attackProgress.toString());
    }

    @Override
    public int getItemCount() {
        return attackProgressDataset.size();
    }



    public class AttackProgressAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public AttackProgressAdapterViewHolder(View v) {
            super(v);
            this.textView = v.findViewById(R.id.attack_progress_textview);
        }
    }
}
