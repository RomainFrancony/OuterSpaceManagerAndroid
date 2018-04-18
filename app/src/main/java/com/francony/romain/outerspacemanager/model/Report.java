package com.francony.romain.outerspacemanager.model;

import com.francony.romain.outerspacemanager.response.SpaceyardResponse;

import java.util.ArrayList;
import java.util.List;

public class Report {
    private ArrayList<Ship> attackerFleet = new ArrayList<>();
    private FleetAfterBattle attackerFleetAfterBattle;
    private Long date;
    private Long dateInv;
    private ArrayList<Ship> defenderFleet = new ArrayList<>();
    private FleetAfterBattle defenderFleetAfterBattle;
    private String from;
    private float gasWon;
    private float mineralsWon;
    private String to;
    private String type;

    @Override
    public boolean equals(Object obj) {
        Report compare = (Report) obj;

        if(compare == null){
            return  false;
        }

        return compare.getDate().equals(this.getDate()) && compare.getFrom().equals(this.getFrom()) && compare.getType().equals(this.getType());

    }

    public ArrayList<Ship> getAttackerFleet() {
        return attackerFleet;
    }

    public void setAttackerFleet(ArrayList<Ship> attackerFleet) {
        this.attackerFleet = attackerFleet;
    }

    public FleetAfterBattle getAttackerFleetAfterBattle() {
        return attackerFleetAfterBattle;
    }

    public void setAttackerFleetAfterBattle(FleetAfterBattle attackerFleetAfterBattle) {
        this.attackerFleetAfterBattle = attackerFleetAfterBattle;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Long getDateInv() {
        return dateInv;
    }

    public void setDateInv(Long dateInv) {
        this.dateInv = dateInv;
    }

    public List<Ship> getDefenderFleet() {
        return defenderFleet;
    }

    public void setDefenderFleet(ArrayList<Ship> defenderFleet) {
        this.defenderFleet = defenderFleet;
    }

    public FleetAfterBattle getDefenderFleetAfterBattle() {
        return defenderFleetAfterBattle;
    }

    public void setDefenderFleetAfterBattle(FleetAfterBattle defenderFleetAfterBattle) {
        this.defenderFleetAfterBattle = defenderFleetAfterBattle;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public float getGasWon() {
        return gasWon;
    }

    public void setGasWon(float gasWon) {
        this.gasWon = gasWon;
    }

    public float getMineralsWon() {
        return mineralsWon;
    }

    public void setMineralsWon(float mineralsWon) {
        this.mineralsWon = mineralsWon;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Report{" +
                "attackerFleet=" + attackerFleet +
                ", attackerFleetAfterBattle=" + attackerFleetAfterBattle +
                ", date=" + date +
                ", dateInv=" + dateInv +
                ", defenderFleet=" + defenderFleet +
                ", defenderFleetAfterBattle=" + defenderFleetAfterBattle +
                ", from='" + from + '\'' +
                ", gasWon=" + gasWon +
                ", mineralsWon=" + mineralsWon +
                ", to='" + to + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
