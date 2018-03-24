package com.francony.romain.outerspacemanager.response;


import com.francony.romain.outerspacemanager.model.Ship;

import java.util.ArrayList;


public class SpaceyardResponse {
    private float currentUserMinerals;
    private float currentUserGas;
    private int  size;
    private ArrayList<Ship> ships;

    public float getCurrentUserMinerals() {
        return currentUserMinerals;
    }

    public void setCurrentUserMinerals(float currentUserMinerals) {
        this.currentUserMinerals = currentUserMinerals;
    }

    public float getCurrentUserGas() {
        return currentUserGas;
    }

    public void setCurrentUserGas(float currentUserGas) {
        this.currentUserGas = currentUserGas;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ArrayList<Ship> getShips() {
        return ships;
    }

    public void setShips(ArrayList<Ship> ships) {
        this.ships = ships;
    }
}
