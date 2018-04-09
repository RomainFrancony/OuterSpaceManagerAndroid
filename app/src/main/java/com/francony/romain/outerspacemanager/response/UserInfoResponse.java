package com.francony.romain.outerspacemanager.response;


import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.francony.romain.outerspacemanager.BR;

public class UserInfoResponse  extends BaseObservable{
    private float gas;
    private Integer gasModifier;
    private float minerals;
    private int points;
    private Integer mineralsModifier;
    private String username;


    public UserInfoResponse(float gas, Integer gasModifier, float minerals, int points, Integer mineralsModifier, String username) {
        this.gas = gas;
        this.gasModifier = gasModifier;
        this.minerals = minerals;
        this.points = points;
        this.mineralsModifier = mineralsModifier;
        this.username = username;
    }

    @Bindable
    public float getGas() {
        return gas;
    }

    public void setGas(float gas) {
        this.gas = gas;
        notifyPropertyChanged(BR.gas);
    }


    @Bindable
    public Integer getGasModifier() {
        return gasModifier;
    }

    public void setGasModifier(Integer gasModifier) {
        this.gasModifier = gasModifier;
        notifyPropertyChanged(BR.gasModifier);
    }


    @Bindable
    public float getMinerals() {
        return minerals;
    }

    public void setMinerals(float minerals) {
        this.minerals = minerals;
        notifyPropertyChanged(BR.minerals);
    }


    @Bindable
    public Integer getMineralsModifier() {
        return mineralsModifier;
    }

    public void setMineralsModifier(Integer mineralsModifier) {
        this.mineralsModifier = mineralsModifier;
        notifyPropertyChanged(BR.mineralsModifier);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @Bindable
    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
        notifyPropertyChanged(BR.points);
    }
}
