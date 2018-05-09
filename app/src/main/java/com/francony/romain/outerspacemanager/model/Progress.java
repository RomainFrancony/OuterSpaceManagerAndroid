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
public class Progress extends BaseObservable {
    public static final int TYPE_ATTACK = 0;
    public static final int TYPE_BUILDING = 1;
    public static final int TYPE_SEARCH = 2;


    @Column
    @PrimaryKey
    private UUID id;

    @Column
    private long endTime;

    @Column
    private int type;

    @Column
    private int constructionObjectId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Bindable
    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
        this.notifyPropertyChanged(BR.endTime);
    }

    public Progress(UUID id, long endTime, int type) {
        this.id = id;
        this.type = type;
        this.endTime = endTime;
    }

    public Progress(UUID id, long endTime, int type, int constructionObjectId) {
        this.id = id;
        this.constructionObjectId = constructionObjectId;
        this.type = type;
        this.endTime = endTime;
    }

    // Default constructor needed for DBFlow
    public Progress() {
    }

    @Override
    public String toString() {
        return "Progress{" +
                "id=" + id +
                ", endTime=" + endTime +
                '}';
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getConstructionObjectId() {
        return constructionObjectId;
    }

    public void setConstructionObjectId(int constructionObjectId) {
        this.constructionObjectId = constructionObjectId;
    }
}
