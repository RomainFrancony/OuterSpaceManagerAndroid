package com.francony.romain.outerspacemanager.model;

import java.util.ArrayList;

public class FleetAfterBattle {
    private Integer capacity;
    private ArrayList<Ship> fleet = null;
    private Integer survivingShips;

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public ArrayList<Ship> getFleet() {
        return fleet;
    }

    public void setFleet(ArrayList<Ship> fleet) {
        this.fleet = fleet;
    }

    public Integer getSurvivingShips() {
        return survivingShips;
    }

    public void setSurvivingShips(Integer survivingShips) {
        this.survivingShips = survivingShips;
    }

    @Override
    public String toString() {
        return "FleetAfterBattle{" +
                "capacity=" + capacity +
                ", fleet=" + fleet +
                ", survivingShips=" + survivingShips +
                '}';
    }
}
