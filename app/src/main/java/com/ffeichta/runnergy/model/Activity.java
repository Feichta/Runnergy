package com.ffeichta.runnergy.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Fabian on 19.11.2015.
 */
public class Activity {
    private int id = -1;
    private Type type = null;
    private long date = 0;
    private int duration = -1;
    private Track track = null;
    private ArrayList<Coordinate> coordinates = null;

    public Activity() {
    }

    public Activity(int id, Type type, long date, int duration) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.duration = duration;
    }

    public Activity(int id, Type type, long date, int duration, Track track) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.duration = duration;
        this.track = track;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public ArrayList<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Returns the date in a certain format
     *
     * @return
     */
    public String getFormattedDate(String format) {
        String ret = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        ret = sdf.format(new java.util.Date(this.date));
        return ret;
    }

    public String toString() {
        return this.id + ";" + this.type + ";" + getFormattedDate("dd.MM.yyyy") + ";" + this.duration;
    }

    public enum Type {
        RUNNING,
        JOGGING,
        HIKING,
        CYCLING,
        DOWNHILL
    }
}