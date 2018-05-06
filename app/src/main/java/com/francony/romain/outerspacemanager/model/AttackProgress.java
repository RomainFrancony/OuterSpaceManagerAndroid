package com.francony.romain.outerspacemanager.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.francony.romain.outerspacemanager.BR;
import com.francony.romain.outerspacemanager.OuterSpaceManagerDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import java.util.UUID;

@Table(database = OuterSpaceManagerDatabase.class)
public class AttackProgress extends BaseObservable {
    @Column
    @PrimaryKey
    private UUID id;

    @Column
    private long attackTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Bindable
    public long getAttackTime() {
        return attackTime;
    }

    public void setAttackTime(long attackTime) {
        this.attackTime = attackTime;
        this.notifyPropertyChanged(BR.attackTime);
    }

    public AttackProgress(UUID id, long attackTime) {
        this.id = id;
        this.attackTime = attackTime;
    }

    // Default constructor needed for DBFlow
    public AttackProgress() {
    }

    @Override
    public String toString() {
        return "AttackProgress{" +
                "id=" + id +
                ", attackTime=" + attackTime +
                '}';
    }
}