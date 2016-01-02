package com.ffeichta.runnergy.gui.listener;

import android.os.Bundle;

import com.ffeichta.runnergy.R;
import com.ffeichta.runnergy.gui.fragments.ActivityFragment;
import com.ffeichta.runnergy.gui.message.ToastFactory;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Fabian on 31.12.2015.
 */
public class ConnectionServices implements GoogleApiClient.ConnectionCallbacks {
    ActivityFragment activityFragment = null;

    public ConnectionServices(ActivityFragment activityFragment) {
        this.activityFragment = activityFragment;
    }

    /**
     * Called when a GoogleApiClient object successfully connects
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // If user pressed the Start button before GoogleApiClient connects, we start getting
        // location updates
        if (activityFragment.startButtonEnabled) {
            activityFragment.startLocationUpdates();
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
        ToastFactory.makeToast(activityFragment.getContext(), activityFragment.getResources().getString(R.string.toast_connection_lost) + cause);
        activityFragment.googleApiClient.connect();
    }
}