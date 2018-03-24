package com.francony.romain.outerspacemanager.response;


import com.francony.romain.outerspacemanager.model.Building;

import java.util.ArrayList;


public class BuildingsResponse {
    private Integer size;
    private ArrayList<Building> buildings;

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public ArrayList<Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(ArrayList<Building> buildings) {
        this.buildings = buildings;
    }

    public BuildingsResponse(Integer size, ArrayList<Building> buildings) {
        this.size = size;
        this.buildings = buildings;
    }
}
