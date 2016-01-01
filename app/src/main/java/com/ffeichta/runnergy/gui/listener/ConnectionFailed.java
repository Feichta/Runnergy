package com.ffeichta.runnergy.gui.listener;

import com.ffeichta.runnergy.R;
import com.ffeichta.runnergy.gui.fragments.ActivityFragment;
import com.ffeichta.runnergy.gui.message.ToastFactory;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Fabian on 31.12.2015.
 */
public class ConnectionFailed implements GoogleApiClient.OnConnectionFailedListener {
    ActivityFragment activityFragment = null;

    public ConnectionFailed(ActivityFragment activityFragment) {
        this.activityFragment = activityFragment;
    }

    /**
     * The connection to Google Play services failed
     *
     * @param result
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        ToastFactory.makeToast(activityFragment.getContext(), activityFragment.getResources().getString(R.string.toast_connection_lost) + result.getErrorCode());
    }
}
