package com.ffeichta.runnergy.model;

import com.ffeichta.runnergy.model.enums.ActivityTypes;
import com.ffeichta.runnergy.model.utils.TimeUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Fabian on 19.11.2015.
 */
public class Activity implements Serializable {
    private static final double FACTOR_MILE = 0.621371;

    private int id = -1;
    private ActivityTypes.Type type = null;
    private long date = 0;
    private int duration = -1;
    private Track track = null;
    private ArrayList<Coordinate> coordinates = null;

    public Activity() {
    }

    public Activity(int id, ActivityTypes.Type type, long date, int duration) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.duration = duration;
    }

    public Activity(int id, ActivityTypes.Type type, long date, int duration, Track track) {
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

    public ActivityTypes.Type getType() {
        return type;
    }

    public void setType(ActivityTypes.Type type) {
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

    public String getFormattedDistance(String unit) {
        String ret = "";
        double distanceInMeter = 0.0;
        for (Coordinate c : getCoordinates()) {
            distanceInMeter += c.getDistanceFromPrevious();
        }
        switch (unit) {
            case "km":
                if (distanceInMeter >= 1000) {
                    ret = distanceInMeter / 1000 + " km";
                } else {
                    ret = Math.round(distanceInMeter * 100.0) / 100.0 + " m";
                }
                break;
            case "mi":
                ret = Math.round(distanceInMeter / 1000 * FACTOR_MILE * 100.0) / 100.0 + " mi";
                break;
            default:
                break;
        }
        return ret;
    }

    public String getFormattedAvg(String unit) {
        String ret = "";
        double distanceInMeter = 0.0;
        for (Coordinate c : this.getCoordinates()) {
            distanceInMeter += c.getDistanceFromPrevious();
        }
        switch (unit) {
            case "km":
                ret = Math.round(distanceInMeter / this.duration * 3.6 * 100.0) / 100.0 + " km/h";
                break;
            case "mi":
                ret = Math.round(distanceInMeter / this.duration * 3.6 * FACTOR_MILE * 100.0) / 100.0 + " mph";
                break;
            default:
                break;
        }
        return ret;
    }

    public String getFormattedDuration() {
        return TimeUtils.convertDurationToString(this.duration);
    }

    /**
     * Returns the date in a certain format
     *
     * @return
     */
    public String getFormattedDate(String format) {
        String ret;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        ret = sdf.format(new java.util.Date(this.date));
        return ret;
    }

    public String toString() {
        return this.id + ";" + this.type + ";" + getFormattedDate("dd.MM.yyyy") + ";" + this.duration;
    }
}