package com.ffeichta.runnergy.gui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.ffeichta.runnergy.R;
import com.ffeichta.runnergy.model.Activity;
import com.ffeichta.runnergy.model.Coordinate;
import com.ffeichta.runnergy.model.DBAccessHelper;
import com.ffeichta.runnergy.model.Setting;
import com.ffeichta.runnergy.model.Track;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Date;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = com.ffeichta.runnergy.model.DBAccessHelper.class
            .getSimpleName();
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //int id = getResources().getIdentifier("RUNNING", "string", getPackageName());
        //String value = (String) getResources().getText(id);
        //Log.d(TAG, value);

        // TEST
        DBAccessHelper db = DBAccessHelper.getInstance(this);
        Track t = new Track();
        t.setName("Bruneck-Bozen");
        Log.d(TAG, "#### " + String.valueOf(db.insertTrack(t)));
        if (t.getError() != null) {
            Log.d(TAG, "#### " + String.valueOf(t.getError().get("name")));
        }
        ArrayList<Track> tracks = db.getTracks();
        if (tracks != null) {
            for (Track track : tracks) {
                Log.d(TAG, "#### " + track.toString());
                ArrayList<Activity> activities = db.getActivities(track);
                if (activities != null) {
                    for (Activity activity : activities) {
                        Log.d(TAG, "#### " + activity.toString());
                    }
                } else {
                    Log.d(TAG, "#### " + activities);
                }
            }
        }
        Log.d(TAG, "#### " + db.getActivity(2).toString());
        Log.d(TAG, "#### " + db.getActivity(100));

        Activity a = new Activity();
        a.setType(Activity.Type.RUNNING);
        a.setDate(new Date().getTime());
        a.setDuration(1259);
        a.setTrack(t);
        Coordinate c = new Coordinate();
        c.setLongitude(11.0003);
        c.setLatitude(59.00089);
        c.setStart(true);
        c.setEnd(false);
        c.setTimeFromStart(0);
        c.setDistanceFromPrevious(0);
        ArrayList<Coordinate> coordinatesInsert = new ArrayList<>();
        coordinatesInsert.add(c);
        c.setActivity(a);
        a.setCoordinates(coordinatesInsert);
        Log.d(TAG, "#### " + db.insertActivity(a));
        ArrayList<Coordinate> coordinates = db.getCoordinates(a);
        for (Coordinate coordinate : coordinates) {
            Log.d(TAG, "#### " + coordinate.toString());
        }
        Log.d(TAG, "#### " + db.getCoordinate(1).toString());

        Setting s = new Setting("interval", "60");
        Log.d(TAG, "#### " + db.insertSetting(s));
        ArrayList<Setting> settings = db.getSettings();
        for (Setting setting : settings) {
            Log.d(TAG, "#### " + setting.toString());
        }

        //Log.d(TAG, "#### " + db.deleteTrack(t));
        //Log.d(TAG, "#### " + db.deleteActivity(a));

        s.setValue("70");
        Log.d(TAG, "#### " + db.updateSetting(s));
        ArrayList<Setting> settingss = db.getSettings();
        for (Setting setting : settingss) {
            Log.d(TAG, "#### " + setting.toString());
        }

        t.setName("Bozen-Rom");
        Log.d(TAG, "#### " + db.updateTrack(t));
        ArrayList<Track> trackss = db.getTracks();
        for (Track track : trackss) {
            Log.d(TAG, "#### " + track.toString());
        }

        Activity tmp = new Activity();
        tmp.setId(1);
        ArrayList<Coordinate> coordinatess = db.getCoordinates(tmp);
        for (Coordinate coordinate : coordinatess) {
            Log.d(TAG, "#### " + coordinate.toString());
        }
        Log.d(TAG, "#### " + db.getIDOfClosestCoordinateInActivity(11.354921, 46.497891, 1));
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
