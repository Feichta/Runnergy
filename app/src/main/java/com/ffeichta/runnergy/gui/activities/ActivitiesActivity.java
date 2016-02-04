package com.ffeichta.runnergy.gui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.ffeichta.runnergy.R;
import com.ffeichta.runnergy.gui.adapter.ActivityAdapter;
import com.ffeichta.runnergy.model.DBAccessHelper;
import com.ffeichta.runnergy.model.Track;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Fabian on 29.12.2015.
 */
public class ActivitiesActivity extends Activity {
    // UI Widgets
    ExpandableListView expListView = null;

    // Used for the ExpandableListView
    List<String> parentStrings = null;
    List<com.ffeichta.runnergy.model.Activity> childActivities = null;
    Map<String, ArrayList<com.ffeichta.runnergy.model.Activity>> groupCollection = null;

    // Actual Track
    private Track track = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_activity);

        expListView = (ExpandableListView) findViewById(R.id.listViewActivities);

        Intent intent = getIntent();
        track = (Track) intent.getSerializableExtra("track");
        getActionBar().setTitle(track.getName());

        childActivities = track.getActivities();
        if (childActivities == null) {
            childActivities = new ArrayList<>();
        }

        setUpParentsAndChilds();

        // Set the Coodinates for each Activity because the ActivityAdapter needs them
        for (com.ffeichta.runnergy.model.Activity activity : childActivities) {
            activity.setCoordinates(DBAccessHelper.getInstance(this).getCoordinates(activity));
        }
        ActivityAdapter activityAdapter = new ActivityAdapter(this, parentStrings, groupCollection);
        expListView.setAdapter(activityAdapter);

        // If there is only one group in the ListView, then expand it
        if (parentStrings.size() == 1) {
            expListView.expandGroup(0);
        }

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(ActivitiesActivity.this, MapsActivity.class);
                intent.putExtra("coordinates", groupCollection.get(parentStrings.get(groupPosition)).get(childPosition).getCoordinates());
                startActivity(intent);
                return true;
            }
        });

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup) {
                    expListView.collapseGroup(previousGroup);
                }
                previousGroup = groupPosition;
            }
        });
    }

    private void setUpParentsAndChilds() {
        parentStrings = new ArrayList<>();
        groupCollection = new LinkedHashMap<>();

        for (com.ffeichta.runnergy.model.Activity a : childActivities) {
            if (!parentStrings.contains(a.getType().toString())) {
                parentStrings.add(a.getType().toString());
            }
        }

        for (String title : parentStrings) {
            ArrayList<com.ffeichta.runnergy.model.Activity> temp = new ArrayList<>();
            for (com.ffeichta.runnergy.model.Activity a : childActivities) {
                if (a.getType().toString().equals(title)) {
                    temp.add(a);
                }
            }
            groupCollection.put(title, temp);
        }
    }
}