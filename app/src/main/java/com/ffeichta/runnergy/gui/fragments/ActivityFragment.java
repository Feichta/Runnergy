package com.ffeichta.runnergy.gui.fragments;

/**
 * Created by Fabian on 28.12.2015.
 */

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ffeichta.runnergy.R;
import com.ffeichta.runnergy.gui.activities.SaveActivityActivity;
import com.ffeichta.runnergy.gui.listener.ConnectionFailed;
import com.ffeichta.runnergy.gui.listener.ConnectionServices;
import com.ffeichta.runnergy.gui.listener.LocationListener;
import com.ffeichta.runnergy.model.Activity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by hp1 on 21-01-2015.
 */
public class ActivityFragment extends Fragment implements OnMapReadyCallback {
    private static final int FACTOR_BETWEEN_INTERVALS = 1 / 3;
    private static final float FACTOR_DISPLACEMENT = 1 / 4;
    // Interval for location updates. Inexact. Updates may be more or less frequent
    public long updateIntervalInMilliseconds = -1;
    // Fastest rate for location updates. Exact. Updates will never be more frequent than this value
    public long fastestUpdateIntervalInMilliseconds = -1;
    // Minimum displacement between location updates in meters
    public float smallestDisplacementInMeter = -1;
    // Entry point to Google Play services
    public GoogleApiClient googleApiClient = null;
    // Value changes when the user presses the Start and Stop Button
    public Boolean startButtonEnabled = false;
    // Value changes when the user presses the Pause and Resume Button
    public Boolean pauseButtonEnabled = false;
    // Request to the FusedLocationProviderApi
    protected LocationRequest locationRequest = null;
    // Listener which is called when the location changes
    protected LocationListener locationListener = null;
    // Listener which handles the states of the connection to the Play Services
    protected GoogleApiClient.ConnectionCallbacks connectionCallbacks = null;
    // Listener which is called when the connection to the Play Services failed
    protected GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener = null;
    // UI Widgets
    private Button startStopButton = null;
    private Button pauseResumeButton = null;
    // Google Map
    private GoogleMap map = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_fragment, container, false);

        // Locate UI Widgets
        startStopButton = (Button) v.findViewById(R.id.activityStartStop);
        pauseResumeButton = (Button) v.findViewById(R.id.activityPauseResume);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.activityGoogleMap);
        mapFragment.getMapAsync(this);

        startStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!startButtonEnabled) {
                    startButtonEnabled = true;
                    startStopButton.setText(getResources().getString(R.string.activity_fragment_stop));
                    pauseResumeButton.setVisibility(View.VISIBLE);
                    // Get a new LocationListener
                    locationListener = new LocationListener(map, getContext());
                    // Start location updates
                    startLocationUpdates();
                } else {
                    startButtonEnabled = false;
                    startStopButton.setText(getResources().getString(R.string.activity_fragment_stop));
                    // Stop location updates
                    stopLocationUpdates();
                    // Get the Activity object from the listener...
                    Activity activity = locationListener.getActivity();
                    // ... and set the duration
                    activity.setDuration((int) ((System.currentTimeMillis() - activity.getDate()) / 1000));
                    // Set the last coordinate as end point
                    activity.getCoordinates().get(activity.getCoordinates().size() - 1).setEnd(true);
                    // Start Activity where the user can save the Activity
                    Intent intent = new Intent(getActivity(), SaveActivityActivity.class);
                    intent.putExtra("activity", locationListener.getActivity());
                    startActivityForResult(intent, 1);
                }
            }
        });

        pauseResumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pauseButtonEnabled) {
                    pauseButtonEnabled = true;
                    pauseResumeButton.setText(getResources().getString(R.string.activity_fragment_resume));
                    stopLocationUpdates();
                } else {
                    pauseButtonEnabled = false;
                    pauseResumeButton.setText(getResources().getString(R.string.activity_fragment_pause));
                    startLocationUpdates();
                }
            }
        });

        // Set up the Listener for the FusedLocationApi
        connectionCallbacks = new ConnectionServices(this);
        onConnectionFailedListener = new ConnectionFailed(this);

        setUpdateIntervalsAndDisplacement();

        buildGoogleApiClient();
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

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        // Shows the current position with the famous blue point with the circle in the map.
        // Enables also the 'Current Location Button'
        map.setMyLocationEnabled(true);
    }


    /**
     * Builds a GoogleApiClient. Uses the addApi method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(connectionCallbacks)
                .addOnConnectionFailedListener(onConnectionFailedListener)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * ACCESS_COARSE_LOCATION and ACCESS_FINE_LOCATION
     * These settings control the accuracy of the current location. This sample uses
     * ACCESS_FINE_LOCATION, as defined in the AndroidManifest.xml.
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet
     */
    protected void createLocationRequest() {
        locationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        locationRequest.setInterval(1000);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        locationRequest.setFastestInterval(1000);

        // Sets the minimum displacement between location updates in meters.
        // If the displacement is too small the updates will be suppressed
        locationRequest.setSmallestDisplacement(smallestDisplacementInMeter);

        // Accuracy must be high for tracking a route
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Requests location updates from the FusedLocationApi
     */
    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        // LocationListener is fired every x seconds
        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, locationRequest, locationListener);
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, locationListener);
    }

    /**
     * Sets up the two intervals and the displacement for the LocationRequest
     */
    private void setUpdateIntervalsAndDisplacement() {
        // ???
        // Never get more updates than this interval?
        fastestUpdateIntervalInMilliseconds = getIntervalFromSettingsInMilliseconds();
        // ???
        updateIntervalInMilliseconds = fastestUpdateIntervalInMilliseconds * FACTOR_BETWEEN_INTERVALS;
        // Example: If the user wants location updates every 10 seconds, he gets them only if
        // he moves at least 2,5 meters in 10 seconds
        smallestDisplacementInMeter = fastestUpdateIntervalInMilliseconds / 1000 * FACTOR_DISPLACEMENT;
    }

    /**
     * Gets the interval from Settings in milliseconds
     *
     * @return
     */
    private int getIntervalFromSettingsInMilliseconds() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        return Integer.valueOf(sp.getString("interval", "1")) * 1000;

    }

    /**
     * Called when the fragment starts
     */
    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    /**
     * Called when user resumes to display the map on display
     */
    @Override
    public void onResume() {
        super.onResume();
        if (googleApiClient.isConnected() && locationRequest != null && startButtonEnabled) {
            locationRequest.setInterval(1000);
            locationRequest.setFastestInterval(1000);
            // apply the changes on the interval
            startLocationUpdates();
        }
    }

    /**
     * Called when user doesn't see the map on display
     */
    @Override
    public void onPause() {
        super.onPause();
        if (googleApiClient.isConnected() && locationRequest != null && startButtonEnabled) {
            locationRequest.setInterval(updateIntervalInMilliseconds);
            locationRequest.setFastestInterval(fastestUpdateIntervalInMilliseconds);
            // apply the changes on the interval
            startLocationUpdates();
        }
    }
}