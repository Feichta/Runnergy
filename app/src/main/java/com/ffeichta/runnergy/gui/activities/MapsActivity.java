package com.ffeichta.runnergy.gui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
    private GoogleMap map = null;
    private Button startComparison = null;
    private ArrayList<Coordinate> coordinates = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        startComparison = (Button) findViewById(R.id.activityMapsComparison);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activityMapsGoogleMap);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        this.coordinates = (ArrayList<Coordinate>) intent.getSerializableExtra("coordinates");

        startComparison.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(this, "Not implemented yet", Toast.LENGTH_SHORT);
                Log.d("öldfkj", "#####");
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

        //map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        // Holds all Polylines
        PolylineOptions polylineOptions = new PolylineOptions();
        // Holds all coordinates
        ArrayList<LatLng> latLngs = new ArrayList<>();
        // Holds all Markers
        // Used to zoom into the map
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (Coordinate c : coordinates) {
            LatLng latLng = new LatLng(c.getLatitude(), c.getLongitude());
            latLngs.add(latLng);
            if (c.isStart()) {
                Marker marker = map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(getResources().getString(R.string.maps_activity_start))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                builder.include(marker.getPosition());
                marker.showInfoWindow();
            }
            if (c.isEnd()) {
                Marker marker = map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(getResources().getString(R.string.maps_activity_end))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                builder.include(marker.getPosition());
                marker.showInfoWindow();
            }
        }
        polylineOptions.addAll(latLngs).color(Color.MAGENTA);
        map.addPolyline(polylineOptions);

        LatLngBounds bounds = builder.build();
        // begin new code:
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels - getActionBar().getHeight();
        int padding = (int) (width * 0.12); // offset from edges of the map 12% of screen

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
// end of new code

        map.moveCamera(cu);
    }
}