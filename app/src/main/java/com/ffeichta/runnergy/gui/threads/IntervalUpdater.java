package com.ffeichta.runnergy.gui.threads;

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
        if (activityFragment.getStartButtonEnabled() && !activityFragment.getPauseButtonEnabled()) {
            double speed;
            if (locationListener.getLocation() != null && locationListener.getLocation().getSpeed
                    () >= 0) {
                speed = locationListener.getLocation().getSpeed() * FACTOR_MS_TO_KMH;
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
            // Stop the LocationUpdates to modify the interval
            activityFragment.stopLocationUpdates();
            activityFragment.getLocationRequest().setInterval(seconds * 1000);
            activityFragment.getLocationRequest().setFastestInterval(seconds * 1000 *
                    ActivityFragment
                            .FACTOR_BETWEEN_INTERVALS);
            // Apply the changes on the interval
            activityFragment.startLocationUpdates();
        }
    }
}
