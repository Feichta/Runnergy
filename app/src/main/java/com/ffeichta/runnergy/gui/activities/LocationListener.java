package com.ffeichta.runnergy.gui.activities;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;

import com.ffeichta.runnergy.R;
import com.ffeichta.runnergy.model.Activity;
import com.ffeichta.runnergy.model.Coordinate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * Created by Fabian on 31.12.2015.
 */
public class LocationListener implements com.google.android.gms.location.LocationListener {
    // Coordinates of Activity
    ArrayList<Coordinate> coordinates = null;
    // Current location
    LatLng actualLatLng = null;
    // Previous location
    LatLng previousLatLng = null;
    // Used for getRessources()
    private Context context = null;
    // UI Widgets
    private GoogleMap mMap = null;
    // Current Activity
    private Activity activity = null;

    public LocationListener(GoogleMap mMap, Context context) {
        this.mMap = mMap;
        this.context = context;

        coordinates = new ArrayList<>();

        activity = new Activity();
        activity.setDate(System.currentTimeMillis());
        activity.setCoordinates(coordinates);
    }

    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {
        this.previousLatLng = this.actualLatLng;
        this.actualLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        updateMap();
        addCoordinate();

    }

    private void addCoordinate() {
        Coordinate coordinate = new Coordinate();
        coordinate.setLatitude(actualLatLng.latitude);
        coordinate.setLongitude(actualLatLng.longitude);
        if (previousLatLng == null) {
            coordinate.setStart(true);
            coordinate.setTimeFromStart(0);
            coordinate.setDistanceFromPrevious(0);
        } else {
            float[] result = new float[1];
            Location.distanceBetween(previousLatLng.latitude, previousLatLng.longitude, actualLatLng.latitude, actualLatLng.longitude, result);
            coordinate.setDistanceFromPrevious(result[0]);
            coordinate.setTimeFromStart((int) ((System.currentTimeMillis() - activity.getDate()) / 1000));
        }
        coordinates.add(coordinate);
    }

    /**
     * Puts a Polyline or a Marker into the map
     */
    private void updateMap() {
        if (previousLatLng == null) {
            addStartMarker();
        } else {
            addPolyline();
        }
        Log.d("alkdsjf", "######" + actualLatLng.toString());

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(actualLatLng, 18));
    }

    private void addStartMarker() {
        mMap.addMarker(new MarkerOptions()
                .position(actualLatLng)
                .title(context.getResources().getString(R.string.maps_activity_marker_start))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
    }

    private void addPolyline() {
        // Instantiates a new Polyline object and adds points
        PolylineOptions polylineOptions = new PolylineOptions()
                .add(actualLatLng)
                .add(previousLatLng);
        // Get back the mutable Polyline
        polylineOptions.color(Color.MAGENTA);
        mMap.addPolyline(polylineOptions);
    }

    public Activity getActivity() {
        return activity;
    }
}
