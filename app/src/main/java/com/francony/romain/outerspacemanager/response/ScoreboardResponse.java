package com.francony.romain.outerspacemanager.response;



import com.francony.romain.outerspacemanager.model.UserScore;

import java.util.ArrayList;

public class ScoreboardResponse {
    private ArrayList<UserScore> users;
    private int size;

    public ScoreboardResponse(ArrayList<UserScore> users, int size) {
        this.users = users;
        this.size = size;
    }

    public ArrayList<UserScore> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<UserScore> users) {
        this.users = users;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
