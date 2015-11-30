package com.ffeichta.runnergy.model;

/**
 * Created by Fabian on 19.11.2015.
 */
public class Track {
    private String name = null;
    private double distance = 0.0;

    public Track() {
    }

    public Track(String name, double distance) {
        this.name = name;
        this.distance = distance;
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
}
