package com.ffeichta.runnergy.gui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.ffeichta.runnergy.R;
import com.ffeichta.runnergy.model.Activity;
import com.ffeichta.runnergy.model.Coordinate;
import com.ffeichta.runnergy.model.DBAccessHelper;
import com.ffeichta.runnergy.model.Track;
import com.ffeichta.runnergy.model.enums.ActivityTypes;

import java.util.ArrayList;

/**
 * Created by Fabian on 31.12.2015.
 */
public class SaveActivityActivity extends android.app.Activity {

    // Activity
    Activity activity = null;
    // UI Widgets
    protected Spinner spinnerTrack = null;
    private Spinner spinnerType = null;
    private Button newTrack = null;
    private TextView distance = null;
    private TextView duration = null;
    private TextView avg = null;
    private Button saveActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_activity);

        spinnerTrack = (Spinner) findViewById(R.id.activity_save_activity_track_spinner);
        spinnerType = (Spinner) findViewById(R.id.activity_save_activity_type_spinner);

        newTrack = (Button) findViewById(R.id.activity_save_activity_track_new);

        distance = (TextView) findViewById(R.id.activity_save_activity_distance);
        duration = (TextView) findViewById(R.id.activity_save_activity_duration);
        avg = (TextView) findViewById(R.id.activity_save_activity_avg);

        saveActivity = (Button) findViewById(R.id.activity_save_save);

        Intent intent = getIntent();
        activity = (Activity) intent.getSerializableExtra("activity");

        setUpSpinners();
        setUpTextViews();

        newTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddChangeTrack addChangeTrack = new AddChangeTrack(SaveActivityActivity.this);
                addChangeTrack.showInputDialog();
                Log.d("söadlkf", "####97");
            }
        });

        saveActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setType(ActivityTypes.Type.CYCLING);
                activity.setTrack(DBAccessHelper.getInstance(SaveActivityActivity.this).getTracks().get(0));
                Log.d("söadlk", "####" + activity.getTrack().toString());
                Log.d("asdfad", "####" + activity.toString());
                for (Coordinate c: activity.getCoordinates()) {
                    Log.d("asdfad", "####" + c.toString());
                }
                int result = DBAccessHelper.getInstance(SaveActivityActivity.this).insertActivity(activity);
                Log.d("asdfad", "####" + result);
            }
        });

    }

    /**
     * Called when an activity started by this Fragment is finished
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        setUpSpinners();
    }

    private void setUpTextViews() {
        distance.setText(activity.getFormattedDistance(getUnitFromSettings()));
        duration.setText(activity.getFormattedDuration());
        avg.setText(activity.getFormattedAvg(getUnitFromSettings()));
    }

    private String getUnitFromSettings() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        return sp.getString("unit", "km");
    }

     void setUpSpinners() {
        ArrayList<Track> tracks = DBAccessHelper.getInstance(this).getTracks();
        if (tracks != null) {
            ArrayList<String> names = new ArrayList<>();
            for (Track t : tracks) {
                names.add(t.getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            spinnerTrack.setAdapter(adapter);
        }
    }



}
