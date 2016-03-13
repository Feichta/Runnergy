package com.ffeichta.runnergy.gui.listener;

import android.content.Context;

import com.ffeichta.runnergy.R;
import com.ffeichta.runnergy.gui.message.ToastFactory;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Fabian on 31.12.2015.
 */
public class ConnectionFailed implements GoogleApiClient.OnConnectionFailedListener {

    private Context context = null;

    public ConnectionFailed(Context context) {
        this.context = context;
    }

    /**
     * The connection to Google Play services failed
     *
     * @param result
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        ToastFactory.makeToast(context, context.getResources()
                .getString(R.string.toast_connection_lost) + result.getErrorCode());
    }
}