package com.francony.romain.outerspacemanager.model;

import android.databinding.BindingAdapter;
import android.widget.ImageView;
import com.francony.romain.outerspacemanager.helpers.Helpers;

public class Building {
    private int level;
    private int amountOfEffectByLevel;
    private int amountOfEffectLevel0;
    private int buildingId;

    private long buildingEndTime;

    public Building() {
    }

    private boolean building;
    private String effect;
    private int gasCostByLevel;
    private int gasCostLevel0;
    private String imageUrl;
    private int mineralCostByLevel;
    private int mineralCostLevel0;
    private String name;
    private int timeToBuildByLevel;
    private int timeToBuildLevel0;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getAmountOfEffectByLevel() {
        return amountOfEffectByLevel;
    }

    public void setAmountOfEffectByLevel(int amountOfEffectByLevel) {
        this.amountOfEffectByLevel = amountOfEffectByLevel;
    }

    public int getAmountOfEffectLevel0() {
        return amountOfEffectLevel0;
    }

    public void setAmountOfEffectLevel0(int amountOfEffectLevel0) {
        this.amountOfEffectLevel0 = amountOfEffectLevel0;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    public boolean isBuilding() {
        return building;
    }

    public void setBuilding(boolean building) {
        this.building = building;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public int getGasCostByLevel() {
        return gasCostByLevel;
    }

    public void setGasCostByLevel(int gasCostByLevel) {
        this.gasCostByLevel = gasCostByLevel;
    }

    public int getGasCostLevel0() {
        return gasCostLevel0;
    }

    public void setGasCostLevel0(int gasCostLevel0) {
        this.gasCostLevel0 = gasCostLevel0;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getMineralCostByLevel() {
        return mineralCostByLevel;
    }

    public void setMineralCostByLevel(int mineralCostByLevel) {
        this.mineralCostByLevel = mineralCostByLevel;
    }

    public int getMineralCostLevel0() {
        return mineralCostLevel0;
    }

    public void setMineralCostLevel0(int mineralCostLevel0) {
        this.mineralCostLevel0 = mineralCostLevel0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTimeToBuildByLevel() {
        return timeToBuildByLevel;
    }

    public void setTimeToBuildByLevel(int timeToBuildByLevel) {
        this.timeToBuildByLevel = timeToBuildByLevel;
    }

    public int getTimeToBuildLevel0() {
        return timeToBuildLevel0;
    }

    public void setTimeToBuildLevel0(int timeToBuildLevel0) {
        this.timeToBuildLevel0 = timeToBuildLevel0;
    }

    public Building(int level, int amountOfEffectByLevel, int amountOfEffectLevel0, int buildingId, boolean building, String effect, int gasCostByLevel, int gasCostLevel0, String imageUrl, int mineralCostByLevel, int mineralCostLevel0, String name, int timeToBuildLevel0) {
        this.level = level;
        this.amountOfEffectByLevel = amountOfEffectByLevel;
        this.amountOfEffectLevel0 = amountOfEffectLevel0;
        this.buildingId = buildingId;
        this.building = building;
        this.effect = effect;
        this.gasCostByLevel = gasCostByLevel;
        this.gasCostLevel0 = gasCostLevel0;
        this.imageUrl = imageUrl;
        this.mineralCostByLevel = mineralCostByLevel;
        this.mineralCostLevel0 = mineralCostLevel0;
        this.name = name;
        this.timeToBuildLevel0 = timeToBuildLevel0;
    }

    public int getGasCost() {
        return this.getGasCostByLevel() * this.getLevel() + this.getGasCostLevel0();
    }

    public int getMineralCost() {
        return this.getMineralCostByLevel() * this.getLevel() + this.getMineralCostLevel0();
    }

    @Override
    public String toString() {
        return "{" +
                "level:" + level +
                ", amountOfEffectByLevel:" + amountOfEffectByLevel +
                ", amountOfEffectLevel0:" + amountOfEffectLevel0 +
                ", buildingId:" + buildingId +
                ", building:" + building +
                ", effect:'" + effect + '\'' +
                ", gasCostByLevel:" + gasCostByLevel +
                ", gasCostLevel0:" + gasCostLevel0 +
                ", imageUrl:'" + imageUrl + '\'' +
                ", mineralCostByLevel:" + mineralCostByLevel +
                ", mineralCostLevel0:" + mineralCostLevel0 +
                ", name:'" + name + '\'' +
                ", timeToBuildByLevel:" + timeToBuildByLevel +
                ", timeToBuildLevel0:" + timeToBuildLevel0 +
                '}';
    }

    public int getTimeToBuild() {
        return this.getTimeToBuildByLevel() * this.getLevel() + this.getTimeToBuildLevel0();
    }

    public long getBuildingEndTime() {
        return buildingEndTime;
    }

    public void setBuildingEndTime(long buildingEndTime) {
        this.buildingEndTime = buildingEndTime;
    }


    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String url) {
        Helpers.loadExternalImageWithAnimation(view,url);
    }
}
