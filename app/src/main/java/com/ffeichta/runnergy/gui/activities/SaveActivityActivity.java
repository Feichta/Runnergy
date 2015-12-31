package com.ffeichta.runnergy.gui.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ffeichta.runnergy.R;
import com.ffeichta.runnergy.model.Activity;
import com.ffeichta.runnergy.model.DBAccessHelper;
import com.ffeichta.runnergy.model.Track;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Fabian on 31.12.2015.
 */
public class SaveActivityActivity extends android.app.Activity {

    // Activity
    Activity activity = null;
    // UI Widgets
    private Spinner spinnerTrack = null;
    private Spinner spinnerType = null;
    private Button newTrack = null;
    private TextView distance = null;
    private TextView duration = null;
    private TextView avg = null;

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

        Intent intent = getIntent();
        activity = (Activity) intent.getSerializableExtra("activity");

        setUpSpinners();
        setUpTextViews();

        newTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
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

    private void setUpSpinners() {
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


    protected void showInputDialog() {
        LayoutInflater layoutInflater = getLayoutInflater();
        final View promptView = layoutInflater.inflate(R.layout.dialog_fragment_track, null);

        final AlertDialog d = new AlertDialog.Builder(this)
                .setPositiveButton(R.string.dialog_fragment_ok, null)
                .setNegativeButton(R.string.dialog_fragment_cancel, null)
                .setTitle(R.string.dialog_fragment_title_add)
                .setView(promptView)
                .create();
        d.show();
        d.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                EditText name = (EditText) promptView.findViewById(R.id.dialog_fragment_edit_text);
                TextView errorTextView = (TextView) promptView.findViewById(R.id.dialog_fragment_error);

                Track track = new Track();
                String input = name.getText().toString();
                if (input != null) {
                    track.setName(name.getText().toString());
                }
                int result = DBAccessHelper.getInstance(null).insertTrack(track);
                if (result == 0) {
                    d.dismiss();
                } else {
                    Hashtable<String, Integer> error = track.getError();
                    if (error != null) {
                        if (error.get("name") == Track.NAME_IS_NOT_SET) {
                            errorTextView.setVisibility(View.VISIBLE);
                            errorTextView.setText(getResources().getString(R.string.dialog_fragment_not_set));
                        }
                        if (error.get("name") == Track.NAME_ALREADY_EXISTS) {
                            errorTextView.setVisibility(View.VISIBLE);
                            errorTextView.setText(getResources().getString(R.string.dialog_fragment_already_set));
                        }
                    }
                }

                //d.dismiss();
            }
        });
    }
}
