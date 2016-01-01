package com.ffeichta.runnergy.gui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ffeichta.runnergy.R;
import com.ffeichta.runnergy.model.Coordinate;
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
    // Value changes when the user presses the Start and Stop Button
    public Boolean startButtonEnabled = false;
    // UI Widgets
    private GoogleMap map = null;
    private Button startStopComparison = null;
    // Coordinates of the route
    private ArrayList<Coordinate> coordinates = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_fragment);

        startStopComparison = (Button) findViewById(R.id.mapsStartStop);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapsGoogleMap);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        this.coordinates = (ArrayList<Coordinate>) intent.getSerializableExtra("coordinates");

        startStopComparison.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!startButtonEnabled) {
                    startButtonEnabled = true;
                    startStopComparison.setText(getResources().getString(R.string.maps_activity_stop));
                    Toast.makeText(MapsActivity.this, "Start comparison not implemented yet", Toast.LENGTH_SHORT).show();
                } else {
                    startButtonEnabled = false;
                    Toast.makeText(MapsActivity.this, "Stop comparison not implemented yet", Toast.LENGTH_SHORT).show();
                    startStopComparison.setText(getResources().getString(R.string.maps_activity_start));
                }
            }
        });

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
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
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
        PolylineOptions polylineOptions = new PolylineOptions();
        // Holds all coordinates
        ArrayList<LatLng> latLngs = new ArrayList<>();
        // Holds all Markers
        // Used to zoom into the map
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        // Add the start Marker, the end Marker and the Polylines to the map
        for (Coordinate c : coordinates) {
            LatLng latLng = new LatLng(c.getLatitude(), c.getLongitude());
            builder.include(latLng);
            latLngs.add(latLng);
            if (c.isStart()) {
                Marker marker = map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(getResources().getString(R.string.marker_start))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                // Only this InfoWindow is shown because only one info window can be displayed at
                // once
                marker.showInfoWindow();
            }
            if (c.isEnd()) {
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(getResources().getString(R.string.marker_end))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
        }
        polylineOptions.addAll(latLngs).color(Color.MAGENTA);
        map.addPolyline(polylineOptions);

        // width and height are uses to generate the area which is shown on the map
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        // Get the height of the ActionBar because the ActionBar covers over the Map
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        // padding is 12% of the width of screen
        int padding = (int) (width * 0.12);
        // subtract the height of the ActinBar
        height = height - actionBarHeight;

        // Contains all Coordinates where I want to zoom
        LatLngBounds bounds = builder.build();

        // Zoom into the map, so every Marker and polyline is visible on the map
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        map.moveCamera(cu);
    }
}