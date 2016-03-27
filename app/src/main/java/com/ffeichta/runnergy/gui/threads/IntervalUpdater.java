package com.ffeichta.runnergy.gui.threads;

import android.util.Log;

import com.ffeichta.runnergy.gui.fragments.ActivityFragment;
import com.ffeichta.runnergy.gui.listener.LocationListener;

/**
 * Created by Fabian on 27.03.2016.
 */
public class IntervalUpdater implements Runnable {

    private static final double FACTOR_MS_TO_KMH = 3.6;

    private LocationListener locationListener = null;
    private ActivityFragment activityFragment = null;

    public IntervalUpdater(LocationListener locationListener, ActivityFragment activityFragment) {
        this.locationListener = locationListener;
        this.activityFragment = activityFragment;
    }

    @Override
    public void run() {
        Log.d("####", "1");
        if (activityFragment.getStartButtonEnabled() && !activityFragment.getPauseButtonEnabled()) {
            double speed;
            if (locationListener.getLocation() != null && locationListener.getLocation().getSpeed
                    () >= 0) {
                Log.d("####", "2");
                speed = locationListener.getLocation().getSpeed() * FACTOR_MS_TO_KMH;
                Log.d("####", "speed = " + speed);
                if (speed < 3) {
                    if (activityFragment.getLocationRequest().getFastestInterval() != 15) {
                        updateInterval(15);
                    }
                }
                if (speed >= 3 && speed < 30) {
                    if (activityFragment.getLocationRequest().getFastestInterval() != 5) {
                        updateInterval(5);
                    }
                }
                if (speed >= 30) {
                    if (activityFragment.getLocationRequest().getFastestInterval() != 2) {
                        updateInterval(2);
                    }
                }
            }
        }
    }

    private void updateInterval(long seconds) {
        if (activityFragment.googleApiClient.isConnected() && activityFragment.getLocationRequest
                () !=
                null) {
            activityFragment.stopLocationUpdates();

            activityFragment.getLocationRequest().setInterval(4000);
            activityFragment.getLocationRequest().setFastestInterval(4000);

            activityFragment.googleApiClient.disconnect();
            activityFragment.googleApiClient.connect();

            activityFragment.startLocationUpdates();
        }
    }
}
