package com.ffeichta.runnergy.gui.adapter;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ffeichta.runnergy.R;
import com.ffeichta.runnergy.model.Activity;

import java.util.ArrayList;

/**
 * Created by Fabian on 29.12.2015.
 */
public class ActivityAdapter extends ArrayAdapter<Activity> {

    public ActivityAdapter(android.app.Activity context, ArrayList<Activity> activities) {
        super(context, R.layout.activity_item, activities);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ret = convertView;
        ActivityHolder activityHolder;
        if (ret == null) {
            LayoutInflater inflater = ((android.app.Activity) getContext())
                    .getLayoutInflater();
            ret = inflater.inflate(R.layout.activity_item, parent, false);
            activityHolder = new ActivityHolder();
            activityHolder.image = (ImageView) ret.findViewById(R.id.image_item);
            activityHolder.distance = (TextView) ret.findViewById(R.id.distance_item);
            activityHolder.avg = (TextView) ret.findViewById(R.id.avg_item);
            activityHolder.duration = (TextView) ret.findViewById(R.id.duration_item);
            activityHolder.date = (TextView) ret.findViewById(R.id.date_item);
            ret.setTag(activityHolder);
        } else {
            activityHolder = (ActivityHolder) ret.getTag();
        }
        Activity a = getItem(position);

        // The outputs in the TextViews are based on the Settings
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());

        String unit = sp.getString("unit", "km");
        String dateFormat = sp.getString("date", "dd.MM.yyyy");

        String distance = a.getFormattedDistance(unit);
        String avg = a.getFormattedAvg(unit);
        String date = a.getFormattedDate(dateFormat);
        String duration = a.getFormattedDuration();

        switch (a.getType()) {
            case RUNNING:
                activityHolder.image.setImageResource(R.drawable.running);
                break;
            case WALKING:
                activityHolder.image.setImageResource(R.drawable.walking);
                break;
            case TREKKING:
                activityHolder.image.setImageResource(R.drawable.trekking);
                break;
            case CYCLING:
                activityHolder.image.setImageResource(R.drawable.cycling);
                break;
            case SKIING:
                activityHolder.image.setImageResource(R.drawable.skiing);
                break;
            default:
                break;
        }
        activityHolder.distance.setText(distance);
        activityHolder.avg.setText(avg);
        activityHolder.duration.setText(duration);
        activityHolder.date.setText(date);
        return ret;
    }

    // UI Widgets
    private class ActivityHolder {
        ImageView image = null;
        TextView distance = null;
        TextView avg = null;
        TextView duration = null;
        TextView date = null;
    }
}