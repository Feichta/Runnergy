package com.ffeichta.runnergy.model;

import java.util.Date;

/**
 * Created by Fabian on 19.11.2015.
 */
public class Activity {
    private int id = 0;
    private Type type = null;
    private Date date = null;
    private int duration = 0;

    public Activity() {
    }

    public Activity(int id, Type type, Date date, int duration) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.duration = duration;
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

    public enum Type {
        RUNNING,
        JOGGING,
        HIKING,
        CYCLING,
        DOWNHILL
    }
}



