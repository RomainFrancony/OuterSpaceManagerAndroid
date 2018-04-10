package com.francony.romain.outerspacemanager.model;

import java.util.List;

public class FleetAfterBattle {
    private Integer capacity;
    private List<FleetAmount> fleet = null;
    private Integer survivingShips;

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public List<FleetAmount> getFleet() {
        return fleet;
    }

    public void setFleet(List<FleetAmount> fleet) {
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
