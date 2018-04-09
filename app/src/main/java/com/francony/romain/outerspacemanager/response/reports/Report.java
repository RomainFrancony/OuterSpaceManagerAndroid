package com.francony.romain.outerspacemanager.response.reports;

import com.francony.romain.outerspacemanager.model.Ship;
import com.francony.romain.outerspacemanager.response.SpaceyardResponse;

import java.util.List;

public class Report {
    private List<SpaceyardResponse> attackerFleet = null;
    private FleetAfterBattle attackerFleetAfterBattle;
    private Long date;
    private Long dateInv;
    private List<Ship> defenderFleet = null;
    private FleetAfterBattle defenderFleetAfterBattle;
    private String from;
    private Integer gasWon;
    private Integer mineralsWon;
    private String to;
    private String type;

    public List<SpaceyardResponse> getAttackerFleet() {
        return attackerFleet;
    }

    public void setAttackerFleet(List<SpaceyardResponse> attackerFleet) {
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

    public void setDefenderFleet(List<Ship> defenderFleet) {
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

    public Integer getGasWon() {
        return gasWon;
    }

    public void setGasWon(Integer gasWon) {
        this.gasWon = gasWon;
    }

    public Integer getMineralsWon() {
        return mineralsWon;
    }

    public void setMineralsWon(Integer mineralsWon) {
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
