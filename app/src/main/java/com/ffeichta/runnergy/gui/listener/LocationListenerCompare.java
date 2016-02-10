package com.ffeichta.runnergy.gui.listener;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.TextView;

import com.ffeichta.runnergy.R;
import com.ffeichta.runnergy.model.Activity;
import com.ffeichta.runnergy.model.Coordinate;
import com.ffeichta.runnergy.model.DBAccessHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Fabian on 09.02.2016.
 */
public class LocationListenerCompare implements com.google.android.gms.location.LocationListener,
        TextToSpeech.OnInitListener {
    TextToSpeech textToSpeech = null;
    Activity activity = null;
    // Current location
    LatLng actualLatLng = null;
    // Previous location
    LatLng previousLatLng = null;
    // Coordinates of Activity
    ArrayList<Coordinate> coordinates = null;
    TextView text = null;
    // UI Widgets
    private GoogleMap map = null;
    // Used for getRessources()
    private Context context = null;
    private long time = 0;

    public LocationListenerCompare(GoogleMap map, Context context, Activity activity, TextView
            text) {
        this.map = map;
        this.context = context;
        this.activity = activity;
        this.text = text;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (previousLatLng == null) {
            time = System.currentTimeMillis() / 1000;
        }
        this.previousLatLng = this.actualLatLng;
        this.actualLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        updateMap();
        int id = DBAccessHelper.getInstance(context).getIDOfClosestCoordinateInActivity(location
                .getLongitude(), location.getLatitude(), activity.getId());
        Coordinate c = DBAccessHelper.getInstance(context).getCoordinate(id);
       /* float[] result = new float[1];
        Location.distanceBetween(c.getLongitude(), c.getLatitude(), location.getLongitude(),
        location.getLatitude(), result);
        Log.d("++++", result[0] + " ");*/
        Log.d("++++", c.getTimeFromStart() - ((System.currentTimeMillis() / 1000) - time) + " ");
        long difference = c.getTimeFromStart() - ((System.currentTimeMillis() / 1000) - time);
        if (difference < 0) {
            if (difference == -1) {
                text.setText(-difference + "Sekunde langsamer");
            } else {
                text.setText(-difference + " Sekunden langsamer");
            }
        } else {
            if (difference > 0) {
                if (difference == 1) {
                    text.setText(difference + " Sekunde schneller");
                } else {
                    text.setText(difference + " Sekunden schneller");
                }
            } else {
                text.setText("gleich schnell");
            }
        }
       /* textToSpeech = new TextToSpeech(context,this);
        if(previousLatLng == null) {
            Log.d("++++", "adsfklja");
           speech();
        }*/

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
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(actualLatLng, 18));
    }

    private void addStartMarker() {
        map.addMarker(new MarkerOptions()
                .position(actualLatLng)
                .title(context.getResources().getString(R.string.marker_start))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
    }

    private void addPolyline() {
        // Instantiates a new Polyline object and adds points
        PolylineOptions polylineOptions = new PolylineOptions()
                .add(actualLatLng)
                .add(previousLatLng);
        // Get back the mutable Polyline
        polylineOptions.color(Color.RED);
        map.addPolyline(polylineOptions);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Log.d("++++", "adsfklja988");
            textToSpeech.setLanguage(Locale.GERMAN);
        }
    }

    /*private void speech() {
        Log.d("++++", "adsfklja4");
        textToSpeech.speak("Hannes is gay", TextToSpeech.QUEUE_FLUSH, null, null);
        Log.d("++++", "adsfklja5");
    }*/
}
