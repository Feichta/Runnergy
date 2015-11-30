package com.ffeichta.runnergy.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Fabian on 19.11.2015.
 */
public class Activity {
    private int id = 0;
    private Type type = null;
    private Date date = null;
    private int duration = 0;
    private ArrayList<Coordinate> coordinates = null;

    public Activity() {
    }

    public Activity(int id, Type type, Date date, int duration, ArrayList<Coordinate> coordinates) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.duration = duration;
        this.coordinates = coordinates;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public ArrayList<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    public enum Type {
        RUNNING,
        JOGGING,
        HIKING,
        CYCLING,
        DOWNHILL
    }
}