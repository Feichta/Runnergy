package com.ffeichta.runnergy.gui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ffeichta.runnergy.R;
import com.ffeichta.runnergy.model.Track;

import java.util.ArrayList;

/**
 * Created by Fabian on 29.12.2015.
 */
public class TrackAdapter extends ArrayAdapter<Track> {
    public TrackAdapter(Activity context, ArrayList<Track> tracks) {
        super(context, R.layout.item_track, tracks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ret = convertView;
        TrackHolder trackHolder = null;
        if (ret == null) {
            LayoutInflater layoutInflater = ((Activity) getContext())
                    .getLayoutInflater();
            ret = layoutInflater.inflate(R.layout.item_track, parent, false);
            trackHolder = new TrackHolder();
            trackHolder.name = (TextView) ret.findViewById(R.id.item_track_name);
            trackHolder.count = (TextView) ret.findViewById(R.id.item_track_count);
            ret.setTag(trackHolder);
        } else {
            trackHolder = (TrackHolder) ret.getTag();
        }
        Track t = getItem(position);
        trackHolder.name.setText(t.getName());
        ArrayList<com.ffeichta.runnergy.model.Activity> activities = t.getActivities();
        if (activities == null) {
            trackHolder.count.setText("0" + getContext().getString(R.string.adapter_track_plural));
        } else {
            int count = activities.size();
            if (count == 0) {
                trackHolder.count.setText(String.valueOf(count) + getContext().getString(R.string.adapter_track_plural));
            } else {
                trackHolder.count.setText(String.valueOf(count) + getContext().getString(R.string.adapter_track_plural));
            }
        }

        return ret;
    }

    // GUI components
    private class TrackHolder {
        TextView name = null;
        TextView count = null;
    }
}
