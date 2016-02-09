package com.ffeichta.runnergy.gui.listener;

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
    // Current location
    private LatLng actualLatLng = null;
    // Previous location
    private LatLng previousLatLng = null;
    // Coordinates of Activity
    private ArrayList<Coordinate> coordinates = null;
    // UI Widgets
    private GoogleMap map = null;
    // Used for getRessources()
    private Context context = null;
    // Current Activity
    private Activity activity = null;

    private Coordinate actualCoordinate = null;
    private Coordinate previousCoordinate = null;
    private long startfirstpause = 0;

    private boolean firstisset = false;


    private boolean lastCoordinateIsPause = false;

    public LocationListener(GoogleMap map, Context context) {
        this.map = map;
        this.context = context;
        // Initialize objects
        coordinates = new ArrayList<>();
        activity = new Activity();
        // Set current date(when activity starts) as the date for the activity
        activity.setDate(System.currentTimeMillis());
        // Set the coordinates for the activity
        activity.setCoordinates(coordinates);

    }

    public boolean isLastCoordinateIsPause() {
        return lastCoordinateIsPause;
    }

    public void setLastCoordinateIsPause(boolean lastCoordinateIsPause) {
        this.lastCoordinateIsPause = lastCoordinateIsPause;
    }

    /**
     * Callback that fires when the location changes forced by
     */
    @Override
    public void onLocationChanged(Location location) {
        Log.d("0000", "new coordinate + " + lastCoordinateIsPause);
        this.previousLatLng = this.actualLatLng;
        this.previousCoordinate = this.actualCoordinate;
        this.actualLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        Coordinate c = new Coordinate();
        c.setLongitude(location.getLongitude());
        c.setLatitude(location.getLatitude());
        this.actualCoordinate = c;
        addCoordinate();
        updateMap();
    }

    private void addCoordinate() {
        Log.d("0000", "add coordinate");
        if (previousLatLng == null) {
            actualCoordinate.setStart(true);
            actualCoordinate.setTimeFromStart(0);
            actualCoordinate.setDistanceFromPrevious(0);
        } else {
            if (lastCoordinateIsPause) {
                Log.d("0000", "1stcoordinate");
                // 1st pause
                /*previousCoordinate.setPause(true);
                previousCoordinate.setTimeFromStart((int) ((System.currentTimeMillis() - activity.getDate()) / 1000));
                float[] result = new float[1];
                Location.distanceBetween(previousLatLng.latitude, previousLatLng.longitude, actualLatLng.latitude, actualLatLng.longitude, result);
                actualCoordinate.setDistanceFromPrevious(result[0]);
                startfirstpause = System.currentTimeMillis();
                lastCoordinateIsPause = false;
                firstisset = true;*/
                coordinates.get(coordinates.size() - 1).setPause(true);
                previousLatLng = null;

                actualCoordinate.setDistanceFromPrevious(0);
                /*actualCoordinate.setTimeFromStart((int) ((System.currentTimeMillis() - activity.getDate() - System.currentTimeMillis() - startfirstpause) / 1000));
                actualCoordinate.setPause(true);*/
                actualCoordinate.setTimeFromStart((int) ((System.currentTimeMillis() - activity.getDate()) / 1000));

                lastCoordinateIsPause = false;
                firstisset = true;
            } else {
                float[] result = new float[1];
                Location.distanceBetween(previousLatLng.latitude, previousLatLng.longitude, actualLatLng.latitude, actualLatLng.longitude, result);
                actualCoordinate.setDistanceFromPrevious(result[0]);
                actualCoordinate.setTimeFromStart((int) ((System.currentTimeMillis() - activity.getDate()) / 1000));

            }
        }
        actualCoordinate.setActivity(activity);
        coordinates.add(actualCoordinate);
    }

    /**
     * Puts a Polyline or a Marker into the map
     */
    private void updateMap() {
        if (previousLatLng == null && coordinates.size() == 1) {
            addStartMarker();
        } else {
            if (!coordinates.get(coordinates.size() - 1).isPause()) {
                if (actualLatLng != null && previousLatLng != null) {
                    addPolyline();
                }
            }
        }
        if (actualLatLng != null) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(actualLatLng, 18));
        }
    }

    private void addStartMarker() {
        map.addMarker(new MarkerOptions()
                .position(actualLatLng)
                .title(context.getResources().getString(R.string.marker_start))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
    }

    private void addPolyline() {
        // Instantiates a new Polyline object and adds points
        PolylineOptions polylineOptions = new PolylineOptions()
                .add(actualLatLng)
                .add(previousLatLng);
        // Get back the mutable Polyline
        polylineOptions.color(Color.MAGENTA);
        map.addPolyline(polylineOptions);
    }

    public Activity getActivity() {
        return activity;
    }
}