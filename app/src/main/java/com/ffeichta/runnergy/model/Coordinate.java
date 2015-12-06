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
    private Activity activity = null;

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

    public Coordinate(int id, double longitude, double latitude, boolean start, boolean end, int timeFromStart, Activity activity) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.start = start;
        this.end = end;
        this.timeFromStart = timeFromStart;
        this.activity = activity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public int getTimeFromStart() {
        return timeFromStart;
    }

    public void setTimeFromStart(int timeFromStart) {
        this.timeFromStart = timeFromStart;
    }
}
