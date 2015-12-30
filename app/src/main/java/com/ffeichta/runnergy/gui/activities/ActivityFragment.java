package com.ffeichta.runnergy.gui.activities;

/**
 * Created by Fabian on 28.12.2015.
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ffeichta.runnergy.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by hp1 on 21-01-2015.
 */
public class ActivityFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap map = null;
    private Button startStopActivity = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_activity, container, false);

        startStopActivity = (Button) v.findViewById(R.id.activityActivityStartStop);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        FragmentActivity activity = getActivity();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.activityActivityGoogleMap);
        mapFragment.getMapAsync(this);

        startStopActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startStopActivity.getText().equals(getResources().getString(R.string.activity_fragment_start))) {
                    startStopActivity.setText(getResources().getString(R.string.activity_fragment_stop));
                } else {
                    startStopActivity.setText(getResources().getString(R.string.activity_fragment_start));
                }
            }
        });
        return v;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        // Set the map type
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        int value = Integer.valueOf(sp.getString("type", "1"));
        int type;
        switch (value) {
            case 0:
                type = GoogleMap.MAP_TYPE_NORMAL;
                break;
            case 1:
                type = GoogleMap.MAP_TYPE_HYBRID;
                break;
            case 2:
                type = GoogleMap.MAP_TYPE_SATELLITE;
                break;
            case 3:
                type = GoogleMap.MAP_TYPE_TERRAIN;
                break;
            case 4:
                type = GoogleMap.MAP_TYPE_NONE;
                break;
            default:
                type = GoogleMap.MAP_TYPE_NORMAL;
                break;
        }
        map.setMapType(type);
    }
}