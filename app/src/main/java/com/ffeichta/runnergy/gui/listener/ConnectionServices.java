package com.ffeichta.runnergy.gui.listener;

import android.os.Bundle;
import android.util.Log;

import com.ffeichta.runnergy.gui.fragments.ActivityFragment;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Fabian on 31.12.2015.
 */
public class ConnectionServices implements GoogleApiClient.ConnectionCallbacks {

    protected static final String TAG = ConnectionServices.class.getSimpleName();

    ActivityFragment activityFragment = null;

    public ConnectionServices(ActivityFragment activityFragment) {
        this.activityFragment = activityFragment;
    }

    /**
     * Called when a GoogleApiClient object successfully connects
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");
        // If the user presses the Start Updates button before GoogleApiClient connects, we set
        // startButtonEnabled to true (see startUpdatesButtonHandler()). Here, we check
        // the value of startButtonEnabled and if it is true, we start location updates.
        if (activityFragment.startButtonEnabled) {
            activityFragment.startLocationUpdates();
        } else {
            Log.i(TAG, "Just wait a moment...");
        }
    }

    /**
     * The connection to Google Play services was lost for some reason
     *
     * @param cause
     */
    @Override
    public void onConnectionSuspended(int cause) {
        // Call connect() to attempt to re-establish the connection
        Log.i(TAG, "Connection suspended");
        activityFragment.mGoogleApiClient.connect();
    }


}
