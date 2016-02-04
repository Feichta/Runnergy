package com.ffeichta.runnergy.gui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.ffeichta.runnergy.R;
import com.ffeichta.runnergy.gui.adapter.ActivityAdapter;
import com.ffeichta.runnergy.model.DBAccessHelper;
import com.ffeichta.runnergy.model.Track;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Fabian on 29.12.2015.
 */
public class ActivitiesActivity extends Activity {
    // UI Widgets


    List<String> titles = null;
    // private ArrayList<com.ffeichta.runnergy.model.Activity> activities = null;
    List<com.ffeichta.runnergy.model.Activity> activities = null;
    Map<String, ArrayList<com.ffeichta.runnergy.model.Activity>> laptopCollection = null;
    ExpandableListView expListView = null;
    private Track track = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_activity);

        expListView = (ExpandableListView) findViewById(R.id.listViewActivities);


        Intent intent = getIntent();
        track = (Track) intent.getSerializableExtra("track");
        getActionBar().setTitle(track.getName());

        activities = track.getActivities();
        if (activities == null) {
            activities = new ArrayList<>();
        }

        titles = new ArrayList<>();

        laptopCollection = new LinkedHashMap<>();

        for (com.ffeichta.runnergy.model.Activity a : activities) {
            if (!titles.contains(a.getType().toString())) {
                titles.add(a.getType().toString());
            }
        }

        for (String title : titles) {
            ArrayList<com.ffeichta.runnergy.model.Activity> temp = new ArrayList<>();
            for (com.ffeichta.runnergy.model.Activity a : activities) {
                if (a.getType().toString().equals(title)) {
                    temp.add(a);
                }
            }
            laptopCollection.put(title, temp);
        }

        for (String s : titles) {
            Log.d("#####0", s);
        }
        Log.d("####", Arrays.toString(laptopCollection.entrySet().toArray()));
        final ExpandableListAdapter expListAdapter = new ActivityAdapter(this, titles, laptopCollection);
        expListView.setAdapter(expListAdapter);


        // Set the Coodinates for each Activity because the ActivityAdapter needs them
        for (com.ffeichta.runnergy.model.Activity activity : activities) {
            activity.setCoordinates(DBAccessHelper.getInstance(this).getCoordinates(activity));
        }
        ActivityAdapter activityAdapter = new ActivityAdapter(this, titles, laptopCollection);
        expListView.setAdapter(activityAdapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Log.d("#####", "vaganal");
                Intent intent = new Intent(ActivitiesActivity.this, MapsActivity.class);

                intent.putExtra("coordinates", laptopCollection.get(titles.get(groupPosition)).get(childPosition).getCoordinates());
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
}