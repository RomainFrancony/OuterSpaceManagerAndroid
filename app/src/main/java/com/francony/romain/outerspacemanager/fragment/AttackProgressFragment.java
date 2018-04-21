package com.francony.romain.outerspacemanager.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.francony.romain.outerspacemanager.R;
import com.francony.romain.outerspacemanager.activity.MainActivity;
import com.francony.romain.outerspacemanager.adapter.AttackProgressAdapter;
import com.francony.romain.outerspacemanager.adapter.ReportAdapter;
import com.francony.romain.outerspacemanager.adapter.UserAdapter;
import com.francony.romain.outerspacemanager.model.AttackProgress;
import com.francony.romain.outerspacemanager.model.UserScore;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;


public class AttackProgressFragment extends Fragment {
    private ModelAdapter<AttackProgress> attackProgressModelAdapter = FlowManager.getModelAdapter(AttackProgress.class);

    private ArrayList<AttackProgress> attacksProgress = new ArrayList<>();

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

        this.getAttacksProgress();
        return v;
    }


    private void getAttacksProgress() {
        ArrayList<AttackProgress> attacks = (ArrayList<AttackProgress>) SQLite.select().from(AttackProgress.class).queryList();
        Timestamp current =  new Timestamp(System.currentTimeMillis());
        ArrayList<AttackProgress> toDelete = new ArrayList<>();

        for (AttackProgress attack : attacks) {
            if( attack.getAttackTime() <= current.getTime()){
                toDelete.add(attack);
            }
        }
        this.attackProgressModelAdapter.deleteAll(toDelete);
        attacks.removeAll(toDelete);

        this.laLoader.setVisibility(View.GONE);
        if(attacks.isEmpty()){
            this.laEmptyAttackProgress.setVisibility(View.VISIBLE);
        }

        attacks.sort(new Comparator<AttackProgress>() {
            @Override
            public int compare(AttackProgress o1, AttackProgress o2) {
                return (int)(o1.getAttackTime() - o2.getAttackTime());
            }
        });

        this.attacksProgress.addAll(attacks);
        this.attackProgressAdapter.notifyItemRangeInserted(0, this.attacksProgress.size());
    }

}
