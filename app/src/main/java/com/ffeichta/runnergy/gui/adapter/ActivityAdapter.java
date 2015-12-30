package com.ffeichta.runnergy.gui.adapter;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ffeichta.runnergy.R;
import com.ffeichta.runnergy.gui.activities.ActivitiesActivity;
import com.ffeichta.runnergy.model.Activity;

import java.util.ArrayList;

/**
 * Created by Fabian on 29.12.2015.
 */
public class ActivityAdapter extends ArrayAdapter<Activity> {

    public ActivityAdapter(android.app.Activity context, ArrayList<Activity> activities) {
        super(context, R.layout.item_activity, activities);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(ActivitiesActivity.class.getSimpleName(), "#### ");
        View ret = convertView;
        ActivityHolder activityHolder;
        if (ret == null) {
            LayoutInflater inflater = ((android.app.Activity) getContext())
                    .getLayoutInflater();
            ret = inflater.inflate(R.layout.item_activity, parent, false);
            activityHolder = new ActivityHolder();
            activityHolder.distance = (TextView) ret.findViewById(R.id.item_activity_distance);
            activityHolder.avg = (TextView) ret.findViewById(R.id.item_activity_avg);
            activityHolder.duration = (TextView) ret.findViewById(R.id.item_activity_duration);
            activityHolder.date = (TextView) ret.findViewById(R.id.item_activity_date);
            ret.setTag(activityHolder);
        } else {
            activityHolder = (ActivityHolder) ret.getTag();
        }
        Activity a = getItem(position);
        Log.d(ActivitiesActivity.class.getSimpleName(), "#### " + a.getCoordinates().size());

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());

        String unit = sp.getString("unit", "km");
        String dateFormat = sp.getString("format", "dd.MM.yyyy");

        String distance = a.getFormattedDistance(unit);
        String avg = a.getFormattedAvg(unit);
        String date = a.getFormattedDate(dateFormat);
        String duration = a.getFormattedDuration();

        activityHolder.distance.setText(distance);
        activityHolder.avg.setText(avg);
        activityHolder.duration.setText(duration);
        activityHolder.date.setText(date);
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