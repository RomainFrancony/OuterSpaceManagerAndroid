package com.francony.romain.outerspacemanager.response;


import com.francony.romain.outerspacemanager.model.Search;

import java.util.ArrayList;

public class SearchesResponse {
    private int  size;
    private ArrayList<Search> searches;

    public ArrayList<Search> getSearches() {
        return searches;
    }

    public void setSearches(ArrayList<Search> searches) {
        this.searches = searches;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
