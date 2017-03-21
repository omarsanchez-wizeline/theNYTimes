package com.wizeline.omarsanchez.nytimessearch.models;

/**
 * Created by omarsanchez on 3/19/17.
 */

public class Filter {
    private String date;
    private int sort;
    private boolean arts;
    private boolean fashion;
    private boolean sports;

    public Filter(String date, int sort, boolean arts, boolean fashion, boolean sports) {
        this.date = date;
        this.sort = sort;
        this.arts = arts;
        this.fashion = fashion;
        this.sports = sports;
    }

    public String getDate() {
        return date;
    }

    public int getSort() {
        return sort;
    }

    public boolean isArts() {
        return arts;
    }

    public boolean isFashion() {
        return fashion;
    }

    public boolean isSports() {
        return sports;
    }
}
