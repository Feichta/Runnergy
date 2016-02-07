package com.ffeichta.runnergy.model;

import com.ffeichta.runnergy.model.enums.ActivityTypes;
import com.ffeichta.runnergy.model.utils.StringFormatter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Fabian on 19.11.2015.
 */
public class Activity implements Serializable {

    public static final int worst = 0;
    public static final int avg = 1;
    public static final int best = 2;

    private int id = -1;
    private ActivityTypes.Type type = null;
    private long date = 0;
    private int duration = -1;
    private int ranking = -1;
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

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
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
        return StringFormatter.getFormattedDistance(getCoordinates(), unit);
    }

    public String getFormattedAvg(String unit) {
        return StringFormatter.getFormattedAvg(this, unit);
    }

    public String getFormattedDuration() {
        return StringFormatter.getFormattedDuration(this.duration);
    }

    public String getFormattedDate(String format) {
        return StringFormatter.getFormattedDate(this.date, format);
    }


    public String toString() {
        return this.id + ";" + this.type + ";" + getFormattedDate("dd.MM.yyyy") + ";" + this.duration + ";" + this.ranking + ";" + this.track.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Activity activity = (Activity) o;

        if (id != activity.id) return false;
        if (date != activity.date) return false;
        if (duration != activity.duration) return false;
        return type == activity.type;
    }
}