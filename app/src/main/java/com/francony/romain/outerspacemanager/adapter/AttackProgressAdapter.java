package com.francony.romain.outerspacemanager.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.databinding.AdapterAttackProgressBinding;
import com.francony.romain.outerspacemanager.model.Progress;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;

public class AttackProgressAdapter extends RecyclerView.Adapter<AttackProgressAdapter.AttackProgressAdapterViewHolder> {
    private OnTimerEndListener onTimerEndListener;
    private ArrayList<Progress> progressDataset;
    private Context context;
    private Timer timer;

    public AttackProgressAdapter(ArrayList<Progress> progresses, final Context context) {
        this.progressDataset = progresses;
        this.context = context;
        this.timer = new Timer();
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                AttackProgressAdapter.this.updateCountdown();
            }
        }, 0, 1000);


    }

    /**
     * Update visible countdown
     */
    private void updateCountdown(){
        ArrayList<Progress> finished = new ArrayList<>();
        for (Progress progress : progressDataset) {
            progress.setEndTime(progress.getEndTime());
            if(progress.getEndTime() - System.currentTimeMillis() <= 1000){
                finished.add(progress);
            }
        }

        // Call custom event listener
        if(AttackProgressAdapter.this.onTimerEndListener != null){
            for (Progress progress : finished) {
                AttackProgressAdapter.this.onTimerEndListener.onTimerEnd(progress);
            }
        }
    }


    /**
     * Create view holder with data binding
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public AttackProgressAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        AdapterAttackProgressBinding binding = DataBindingUtil.inflate(inflater, R.layout.adapter_attack_progress, parent, false);
        return new AttackProgressAdapterViewHolder(binding);
    }


    /**
     * Bind progress to UI
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(AttackProgressAdapterViewHolder holder, int position) {
        Progress progress = this.progressDataset.get(position);
        holder.bind(progress);
    }

    @Override
    public int getItemCount() {
        return progressDataset.size();
    }



    public class AttackProgressAdapterViewHolder extends RecyclerView.ViewHolder {
        private AdapterAttackProgressBinding binding;

        public AttackProgressAdapterViewHolder(AdapterAttackProgressBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Progress progress){
            this.binding.setProgress(progress);
        }
    }


    /**
     * Set custom listener
     * @param onTimerEndListener
     */
    public void setOnTimerEndListener(OnTimerEndListener onTimerEndListener) {
        this.onTimerEndListener = onTimerEndListener;
    }

    /**
     * Custom listener interface
     */
    public interface OnTimerEndListener {
        void onTimerEnd(Progress progress);
    }
}
