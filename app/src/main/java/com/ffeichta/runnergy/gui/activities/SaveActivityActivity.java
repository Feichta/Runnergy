package com.ffeichta.runnergy.gui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.ffeichta.runnergy.R;
import com.ffeichta.runnergy.gui.dialogfactory.AddTrackDialogFactory;
import com.ffeichta.runnergy.gui.message.ToastFactory;
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
    // UI Widgets
    public Spinner spinnerTrack = null;

    // Activity
    Activity activity = null;
    // All actual Tracks
    ArrayList<Track> tracks = null;
    private Spinner spinnerType = null;
    private Button add = null;
    private TextView distance = null;
    private TextView duration = null;
    private TextView avg = null;
    private Button save = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_activity_activity);

        spinnerTrack = (Spinner) findViewById(R.id.spinner_track);
        spinnerType = (Spinner) findViewById(R.id.spinner_type);
        add = (Button) findViewById(R.id.add);
        distance = (TextView) findViewById(R.id.distance);
        duration = (TextView) findViewById(R.id.duration);
        avg = (TextView) findViewById(R.id.avg);
        save = (Button) findViewById(R.id.save);

        // Get the Activity created in ActivityFragment
        activity = (Activity) (getIntent().getSerializableExtra("activity"));
        for (Coordinate c : activity.getCoordinates()) {
            Log.d("0000", c.toString());
        }

        setUpSpinners();
        setUpTextViews();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTrackDialogFactory addTrackDialogFactory = new AddTrackDialogFactory(SaveActivityActivity.this);
                addTrackDialogFactory.makeCustomInputDialog();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareActivity();
                int result = DBAccessHelper.getInstance(SaveActivityActivity.this).insertActivity(activity);
                if (result == 0) {
                    Intent intent = new Intent(SaveActivityActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    ToastFactory.makeToast(SaveActivityActivity.this, getResources().getString(R.string.toast_error_save_track));
                }
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

    public void setUpSpinners() {
        tracks = DBAccessHelper.getInstance(this).getTracks();
        setUpSpinnerTrack();
        setUpSpinnerType();

    }

    private void prepareActivity() {
        activity.setType(ActivityTypes.Type.values()[spinnerType.getSelectedItemPosition()]);
        activity.setTrack(tracks.get(spinnerTrack.getSelectedItemPosition()));
    }

    private void setUpSpinnerTrack() {
        if (tracks != null) {
            ArrayList<String> names = new ArrayList<>();
            for (Track t : tracks) {
                names.add(t.getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTrack.setAdapter(adapter);
        }
    }

    private void setUpSpinnerType() {
        ArrayList<String> types = new ArrayList<>();
        for (ActivityTypes.Type type : ActivityTypes.Type.values()) {
            types.add(type.toString(this));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.dialog_back_pressed_title))
                    .setMessage(getResources().getString(R.string.dialog_back_pressed_subtitle))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Close the dialog
                            dialog.dismiss();
                            // Finish the activity
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .create().show();
        }
        return super.onKeyDown(keyCode, event);
    }
}