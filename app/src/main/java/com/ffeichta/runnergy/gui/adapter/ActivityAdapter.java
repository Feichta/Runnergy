package com.ffeichta.runnergy.gui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ffeichta.runnergy.R;
import com.ffeichta.runnergy.model.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Fabian on 29.12.2015.
 */
public class ActivityAdapter extends BaseExpandableListAdapter {

    private android.app.Activity context;
    private Map<String, ArrayList<Activity>> activityCollection;
    private List<String> activities;

    public ActivityAdapter(android.app.Activity context, List<String> activities,
                           Map<String, ArrayList<Activity>> activityCollection) {
        this.context = context;
        this.activityCollection = activityCollection;
        this.activities = activities;
    }

    @Override
    public int getGroupCount() {
        return activities.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return activityCollection.get(activities.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return activities.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return activityCollection.get(activities.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String laptopName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_row,
                    null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.heading);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(laptopName);
        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View ret = convertView;
        ActivityHolder activityHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_item, null);
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
        Activity a = (Activity) getChild(groupPosition, childPosition);

        // The outputs in the TextViews are based on the Settings
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

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

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
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