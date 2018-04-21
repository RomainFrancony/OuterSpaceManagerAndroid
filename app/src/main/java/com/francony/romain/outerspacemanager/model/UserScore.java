package com.francony.romain.outerspacemanager.model;


public class UserScore {
    private double points;
    private String username;
    private int position;

    public UserScore(double points, String username) {
        this.points = points;
        this.username = username;
    }

    public long getPoints() {
        return (long)points;
    }

    public String getPointsText() {
        return String.valueOf(this.points);
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
