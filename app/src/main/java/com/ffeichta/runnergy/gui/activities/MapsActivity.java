package com.ffeichta.runnergy.gui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ffeichta.runnergy.R;
import com.ffeichta.runnergy.gui.listener.ConnectionFailed;
import com.ffeichta.runnergy.gui.listener.LocationListenerCompare;
import com.ffeichta.runnergy.model.Activity;
import com.ffeichta.runnergy.model.Coordinate;
import com.ffeichta.runnergy.model.DBAccessHelper;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final int FACTOR_BETWEEN_INTERVALS = 1 / 3;
    private final float FACTOR_DISPLACEMENT = 1 / 4;
    private final double FACTOR_ACTION_BAR = 8.3;
    // Value changes when the user presses the Start and Stop Button
    public Boolean startButtonEnabled = false;
    // Interval for location updates. Inexact. Updates may be more or less frequent
    public long updateIntervalInMilliseconds = -1;
    // Fastest rate for location updates. Exact. Updates will never be more frequent than this value
    public long fastestUpdateIntervalInMilliseconds = -1;
    // Minimum displacement between location updates in meters
    public float smallestDisplacementInMeter = -1;
    // Entry point to Google Play services
    public GoogleApiClient googleApiClient = null;
    // Request to the FusedLocationProviderApi
    protected LocationRequest locationRequest = null;
    // Listener which is called when the location changes
    protected LocationListenerCompare locationListener = null;
    // Listener which handles the states of the connection to the Play Services
    protected GoogleApiClient.ConnectionCallbacks connectionCallbacks = null;
    // Listener which is called when the connection to the Play Services failed
    protected GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener = null;
    // UI Widgets
    private GoogleMap map = null;
    private Button startStopComparison = null;
    // Coordinates of the route
    private ArrayList<Coordinate> coordinates = null;
    private TextView text = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_fragment);

        getActionBar().setDisplayShowTitleEnabled(false);

        text = (TextView) findViewById(R.id.time);

        startStopComparison = (Button) findViewById(R.id.mapsStartStop);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapsGoogleMap);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        Activity a = (Activity) intent.getSerializableExtra("activity");
        this.coordinates = DBAccessHelper.getInstance(this).getCoordinates(a);

        startStopComparison.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!startButtonEnabled) {
                    startButtonEnabled = true;
                    startStopComparison.setText(getResources().getString(R.string
                            .maps_activity_stop));
                    text.setVisibility(TextView.VISIBLE);
                    text.setText("");
                    locationListener = new LocationListenerCompare(map, MapsActivity.this,
                            coordinates.get(0).getActivity(), text);
                    // Start location updates
                    startLocationUpdates();
                    if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission
                            .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MapsActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MainActivity.REQUEST_CODE_ASK_PERMISSIONS);
                        return;
                    }
                    map.setMyLocationEnabled(true);
                } else {
                    startButtonEnabled = false;
                    startStopComparison.setText(getResources().getString(R.string
                            .maps_activity_start));
                    text.setVisibility(TextView.GONE);
                    text.setText("");
                    stopLocationUpdates();
                    if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission
                            .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MapsActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MainActivity.REQUEST_CODE_ASK_PERMISSIONS);
                        return;
                    }
                    map.setMyLocationEnabled(false);
                    onMapReady(map);
                }
            }
        });

        //Set up the Listener for the FusedLocationApi
        onConnectionFailedListener = new ConnectionFailed(this);

        setUpdateIntervalsAndDisplacement();

        buildGoogleApiClient();
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
        map.clear();
        // Set the map type
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences
                (getApplicationContext());
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

        // Holds all Polylines
        PolylineOptions polylineOptions;
        ArrayList<ArrayList<LatLng>> all = new ArrayList<>(0);
        ArrayList<LatLng> latLngGroup = new ArrayList<>();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        // Add the start Marker, the end Marker and the Polylines to the map
        for (int i = 0; i < coordinates.size(); i++) {
            Coordinate c = coordinates.get(i);
            LatLng latLng = new LatLng(c.getLatitude(), c.getLongitude());
            builder.include(latLng);
            latLngGroup.add(latLng);
            if (c.isPause()) {
                all.add(latLngGroup);
                latLngGroup = new ArrayList<>();
            }
            if (i == coordinates.size() - 1) {
                all.add(latLngGroup);
            }
            if (c.isStart()) {
                Marker marker = map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(getResources().getString(R.string.marker_start))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory
                                .HUE_GREEN)));
                // Only this InfoWindow is shown because only one info window can be displayed at
                // once
                marker.showInfoWindow();
            }
            if (c.isEnd()) {
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(getResources().getString(R.string.marker_end))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory
                                .HUE_RED)));
            }
        }
        for (ArrayList<LatLng> group : all) {
            polylineOptions = new PolylineOptions();
            polylineOptions.addAll(group).color(Color.MAGENTA);
            map.addPolyline(polylineOptions);
        }

        // width and height are uses to generate the area which is shown on the map
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        // Get the height of the ActionBar because the ActionBar covers over the Map
        int actionBarHeight = (int) (height / (100 / FACTOR_ACTION_BAR));
//        TypedValue tv = new TypedValue();
//        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
//            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources()
//                    .getDisplayMetrics());
//        }
        // padding is 18% of the width of screen
        int padding = (int) (width * 0.18);
        // subtract the height of the ActinBar
        height = height - actionBarHeight;

        // Contains all Coordinates where I want to zoom
        LatLngBounds bounds = builder.build();


        // Zoom into the map, so every Marker and polyline is visible on the map
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        map.moveCamera(cu);
    }


    /**
     * Builds a GoogleApiClient. Uses the addApi method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getBaseContext())
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
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission
                .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MainActivity.REQUEST_CODE_ASK_PERMISSIONS);
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
        updateIntervalInMilliseconds = fastestUpdateIntervalInMilliseconds *
                FACTOR_BETWEEN_INTERVALS;
        // Example: If the user wants location updates every 10 seconds, he gets them only if
        // he moves at least 2,5 meters in 10 seconds
        smallestDisplacementInMeter = fastestUpdateIntervalInMilliseconds / 1000 *
                FACTOR_DISPLACEMENT;
    }

    /**
     * Gets the interval from Settings in milliseconds
     */
    private int getIntervalFromSettingsInMilliseconds() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MapsActivity.this);
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
     * Called when user resumes to display the MainActivity with the two fragments
     * Example: User comes back from SettingsActivity or comes back from another app
     */
    @Override
    public void onResume() {
        super.onResume();
        // Set again the type of the map because he could have changed
        //setMapType();
        // Set the interval of the location requests to one second if the MainActivity
        // (and thus the map) is visible.
        // This is because setMyLocationEnabled() is true and the blue point updates every second,
        // then we should draw every second a Polyline into the map
        if (googleApiClient.isConnected() && locationRequest != null && startButtonEnabled) {
            // Stop the LocationUpdates to modify the interval
            stopLocationUpdates();
            locationRequest.setInterval(1000);
            locationRequest.setFastestInterval(1000);
            // Apply the changes on the interval
            startLocationUpdates();
        }
    }

    /**
     * Called when user doesn't see the MainActivity with the two fragments
     */
    @Override
    public void onPause() {
        super.onPause();
        // Set the interval of the location requests to the set value if the MainActivity
        // (and thus the map) is not visible, for example when the user locks his device
        if (googleApiClient.isConnected() && locationRequest != null && startButtonEnabled) {
            // Stop the LocationUpdates to modify the interval
            stopLocationUpdates();
            locationRequest.setInterval(updateIntervalInMilliseconds);
            locationRequest.setFastestInterval(fastestUpdateIntervalInMilliseconds);
            // Apply the changes on the interval
            startLocationUpdates();
        }
    }
}