package com.ffeichta.runnergy.gui.activities;

import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Fabian on 31.12.2015.
 */
public class ConnectionFailed implements GoogleApiClient.OnConnectionFailedListener {

    protected static final String TAG = ConnectionServices.class.getSimpleName();

    /**
     * The connection to Google Play services failed
     *
     * @param result
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }
}
