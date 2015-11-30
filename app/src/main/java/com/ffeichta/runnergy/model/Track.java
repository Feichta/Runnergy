package com.ffeichta.runnergy.model;

import java.util.ArrayList;

/**
 * Created by Fabian on 19.11.2015.
 */
public class Track {
    private int id = 0;
    private String name = null;
    private double distance = 0.0;
    private ArrayList<Activity> activities = null;

    public Track() {
    }

    public Track(int id, String name, double distance, ArrayList<Activity> activities) {
        this.id = id;
        this.name = name;
        this.distance = distance;
        this.activities = activities;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public ArrayList<Activity> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<Activity> activities) {
        this.activities = activities;
    }
}