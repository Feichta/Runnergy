package com.ffeichta.runnergy.gui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ffeichta.runnergy.R;
import com.ffeichta.runnergy.gui.adapter.ActivityAdapter;
import com.ffeichta.runnergy.model.DBAccessHelper;
import com.ffeichta.runnergy.model.Track;

import java.util.ArrayList;

/**
 * Created by Fabian on 29.12.2015.
 */
public class ActivitiesActivity extends Activity {

    private Track track = null;
    private ArrayList<com.ffeichta.runnergy.model.Activity> activities = null;
    private ListView listView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);
        listView = (ListView) findViewById(R.id.listViewActivity);

        Intent intent = getIntent();
        track = (Track) intent.getSerializableExtra("track");
        getActionBar().setTitle(track.getName());

        activities = track.getActivities();
        if (activities == null) {
            activities = new ArrayList<>();
        }
        for (com.ffeichta.runnergy.model.Activity activity : activities) {
            activity.setCoordinates(DBAccessHelper.getInstance(this).getCoordinates(activity));
        }
        ActivityAdapter activityAdapter = new ActivityAdapter(this, activities);
        listView.setAdapter(activityAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ActivitiesActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }
}