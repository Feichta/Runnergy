package com.ffeichta.runnergy.model;

/**
 * Created by Fabian on 19.11.2015.
 */
public class Coordinate {
    private int id = 0;
    private double longitude = 0.0;
    private double latitude = 0.0;
    private boolean start = false;
    private boolean end = false;
    private int timeFromStart = 0;

    public Coordinate() {
    }

    public Coordinate(int id, double longitude, double latitude, boolean start, boolean end, int timeFromStart) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.start = start;
        this.end = end;
        this.timeFromStart = timeFromStart;
    }
}
