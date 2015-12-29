package com.ffeichta.runnergy.gui;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ffeichta.runnergy.R;
import com.ffeichta.runnergy.model.Activity;
import com.ffeichta.runnergy.model.Coordinate;

import java.util.ArrayList;

/**
 * Created by Fabian on 29.12.2015.
 */
public class ActivityAdapter extends ArrayAdapter<Activity> {

    public ActivityAdapter(android.app.Activity context, ArrayList<Activity> activities) {
        super(context, R.layout.item_track, activities);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ret = convertView;
        ActivityHolder activityHolder = null;
        if (ret == null) {
            LayoutInflater inflater = ((android.app.Activity) getContext())
                    .getLayoutInflater();
            ret = inflater.inflate(R.layout.item_track, parent, false);
            activityHolder = new ActivityHolder();
            activityHolder.distance = (TextView) ret.findViewById(R.id.item_activity_distance);
            activityHolder.avg = (TextView) ret.findViewById(R.id.item_activity_avg);
            activityHolder.duration = (TextView) ret.findViewById(R.id.item_activity_duration);
            activityHolder.date = (TextView) ret.findViewById(R.id.item_activity_date);
        } else {
            activityHolder = (ActivityHolder) ret.getTag();
        }

        Activity a = getItem(position);

        double distance = 0.0;
        double avg = 0.0;
        String duration = "";
        String date = "";

        double distanceInMeter = 0.0;
        for (Coordinate c : a.getCoordinates()) {
            distanceInMeter += c.getDistanceFromPrevious();
        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        switch (sp.getString("unit", "km")) {
            case "km":
                if (distanceInMeter >= 1000) {
                    distance = distanceInMeter / 1000;
                } else {
                    distance = distanceInMeter;
                }
                break;
            case "mi":

                break;
        }


        activityHolder.distance.setText(String.valueOf(distance));
        activityHolder.avg.setText(String.valueOf(avg));
        activityHolder.duration.setText(duration);
        activityHolder.duration.setText(date);
        return ret;
    }

    // GUI components
    private class ActivityHolder {
        TextView distance = null;
        TextView avg = null;
        TextView duration = null;
        TextView date = null;
    }
}