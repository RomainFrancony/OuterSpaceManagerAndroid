package com.francony.romain.outerspacemanager.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.activity.MainActivity;
import com.francony.romain.outerspacemanager.adapter.AttackProgressAdapter;
import com.francony.romain.outerspacemanager.model.Progress;
import com.francony.romain.outerspacemanager.model.Progress_Table;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;


public class AttackProgressFragment extends Fragment implements AttackProgressAdapter.OnTimerEndListener {
    private ModelAdapter<Progress> attackProgressModelAdapter = FlowManager.getModelAdapter(Progress.class);

    private ArrayList<Progress> attacksProgress = new ArrayList<>();

    private RelativeLayout laLoader;
    private RecyclerView rvAttackProgress;
    private LinearLayoutManager rvLayoutManager;
    private AttackProgressAdapter attackProgressAdapter;
    private LinearLayout laEmptyAttackProgress;


    public AttackProgressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_attack_progress, container, false);
        this.laLoader = v.findViewById(R.id.layout_loader);

        // Empty attack card for asking to go to galaxy
        this.laEmptyAttackProgress = v.findViewById(R.id.attack_progress_empty_layout);
        Button button = this.laEmptyAttackProgress.findViewById(R.id.reports_empty_galaxy_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                if (activity == null) {
                    return;
                }
                activity.navigationView.getMenu().performIdentifierAction(R.id.nav_galaxy, 0);
                activity.navigationView.getMenu().getItem(MainActivity.GALAXY_DRAWER_INDEX).setChecked(true);
            }
        });

        // Recycler view
        this.rvAttackProgress = v.findViewById(R.id.attack_progress_rv);
        this.rvAttackProgress.setHasFixedSize(true);
        this.rvLayoutManager = new LinearLayoutManager(getContext());
        this.rvAttackProgress.setLayoutManager(rvLayoutManager);
        this.attackProgressAdapter = new AttackProgressAdapter(this.attacksProgress, getContext());
        this.rvAttackProgress.setAdapter(this.attackProgressAdapter);
        this.attackProgressAdapter.setOnTimerEndListener(this);

        this.getAttacksProgress();
        return v;
    }

    /**
     * Get attack in progress
     */
    private void getAttacksProgress() {
        ArrayList<Progress> attacks = (ArrayList<Progress>) SQLite.select().from(Progress.class).where(Progress_Table.type.eq(Progress.TYPE_ATTACK)).queryList();
        Timestamp current = new Timestamp(System.currentTimeMillis());
        ArrayList<Progress> toDelete = new ArrayList<>();

        // Remove the one that have happened since last time
        for (Progress attack : attacks) {
            if (attack.getEndTime() <= current.getTime()) {
                toDelete.add(attack);
            }
        }
        this.attackProgressModelAdapter.deleteAll(toDelete);
        attacks.removeAll(toDelete);

        this.laLoader.setVisibility(View.GONE);
        if (attacks.isEmpty()) {
            this.laEmptyAttackProgress.setVisibility(View.VISIBLE);
        }

        attacks.sort(new Comparator<Progress>() {
            @Override
            public int compare(Progress o1, Progress o2) {
                return (int) (o1.getEndTime() - o2.getEndTime());
            }
        });

        this.attacksProgress.addAll(attacks);
        this.attackProgressAdapter.notifyItemRangeInserted(0, this.attacksProgress.size());
    }

    /**
     * Remove progress when it ends
     *
     * @param progress
     */
    @Override
    public void onTimerEnd(Progress progress) {
        int index = this.attacksProgress.indexOf(progress);
        this.attacksProgress.remove(index);
        this.attackProgressAdapter.notifyItemRemoved(index);

        if (this.attackProgressAdapter.getItemCount() == 0 && getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AttackProgressFragment.this.laEmptyAttackProgress.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}
