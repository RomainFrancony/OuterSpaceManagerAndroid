package com.francony.romain.outerspacemanager.adapter;

import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.databinding.AdapterAttackProgressBinding;
import com.francony.romain.outerspacemanager.databinding.AdapterShipBinding;
import com.francony.romain.outerspacemanager.model.AttackProgress;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class AttackProgressAdapter extends RecyclerView.Adapter<AttackProgressAdapter.AttackProgressAdapterViewHolder> {
    private OnTimerEndListener onTimerEndListener;
    private ArrayList<AttackProgress> attackProgressDataset;
    private Context context;
    private Timer timer;

    public AttackProgressAdapter(ArrayList<AttackProgress> attackProgress, final Context context) {
        this.attackProgressDataset = attackProgress;
        this.context = context;
        this.timer = new Timer();
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                AttackProgressAdapter.this.updateCountdown();
            }
        }, 0, 1000);


    }

    private void updateCountdown(){
        ArrayList<AttackProgress> finished = new ArrayList<>();
        for (AttackProgress attackProgress: attackProgressDataset) {
            attackProgress.setAttackTime(attackProgress.getAttackTime());
            if(attackProgress.getAttackTime() - System.currentTimeMillis() <= 1000){
                finished.add(attackProgress);
            }
        }

        if(AttackProgressAdapter.this.onTimerEndListener != null){
            for (AttackProgress attackProgress: finished) {
                AttackProgressAdapter.this.onTimerEndListener.onTimerEnd(attackProgress);
            }
        }
    }


    @Override
    public AttackProgressAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        AdapterAttackProgressBinding binding = DataBindingUtil.inflate(inflater, R.layout.adapter_attack_progress, parent, false);
        return new AttackProgressAdapterViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(AttackProgressAdapterViewHolder holder, int position) {
        AttackProgress attackProgress = this.attackProgressDataset.get(position);
        holder.bind(attackProgress);
    }

    @Override
    public int getItemCount() {
        return attackProgressDataset.size();
    }



    public class AttackProgressAdapterViewHolder extends RecyclerView.ViewHolder {
        private AdapterAttackProgressBinding binding;

        public AttackProgressAdapterViewHolder(AdapterAttackProgressBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(AttackProgress attackProgress){
            this.binding.setAttackProgress(attackProgress);
        }
    }


    public void setOnTimerEndListener(OnTimerEndListener onTimerEndListener) {
        this.onTimerEndListener = onTimerEndListener;
    }

    public interface OnTimerEndListener {
        void onTimerEnd(AttackProgress attackProgress);
    }
}
