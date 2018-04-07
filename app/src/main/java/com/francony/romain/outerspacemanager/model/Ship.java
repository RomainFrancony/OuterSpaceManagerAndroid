package com.francony.romain.outerspacemanager.model;


import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.francony.romain.outerspacemanager.BR;

public class Ship extends BaseObservable {
    private Integer gasCost;
    private Integer life;
    private Integer maxAttack;
    private Integer minAttack;
    private Integer mineralCost;
    private String name;
    private Integer shipId;
    private Integer shield;
    private Integer spatioportLevelNeeded;
    private Integer speed;
    private Integer amount;
    private Integer timeToBuild;

    @Bindable
    public Integer getGasCost() {
        return gasCost;
    }

    public void setGasCost(Integer gasCost) {
        this.gasCost = gasCost;
        notifyPropertyChanged(BR.gasCost);
    }

    public Integer getLife() {
        return life;
    }

    public void setLife(Integer life) {
        this.life = life;
    }

    public Integer getMaxAttack() {
        return maxAttack;
    }

    public void setMaxAttack(Integer maxAttack) {
        this.maxAttack = maxAttack;
    }

    public Integer getMinAttack() {
        return minAttack;
    }

    public void setMinAttack(Integer minAttack) {
        this.minAttack = minAttack;
    }

    @Bindable
    public Integer getMineralCost() {
        return mineralCost;
    }

    public void setMineralCost(Integer mineralCost) {
        this.mineralCost = mineralCost;
        notifyPropertyChanged(BR.mineralCost);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getShipId() {
        return shipId;
    }

    public void setShipId(Integer shipId) {
        this.shipId = shipId;
    }

    public Integer getShield() {
        return shield;
    }

    public void setShield(Integer shield) {
        this.shield = shield;
    }

    public Integer getSpatioportLevelNeeded() {
        return spatioportLevelNeeded;
    }

    public void setSpatioportLevelNeeded(Integer spatioportLevelNeeded) {
        this.spatioportLevelNeeded = spatioportLevelNeeded;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    @Bindable
    public Integer getTimeToBuild() {
        return timeToBuild;
    }

    public void setTimeToBuild(Integer timeToBuild) {
        this.timeToBuild = timeToBuild;
        notifyPropertyChanged(BR.timeToBuild);
    }

    @Bindable
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
        notifyPropertyChanged(BR.amount);
    }
}
