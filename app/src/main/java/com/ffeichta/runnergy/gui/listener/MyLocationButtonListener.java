package com.ffeichta.runnergy.gui.listener;

import android.content.Context;
import android.location.LocationManager;

import com.ffeichta.runnergy.R;
import com.ffeichta.runnergy.gui.message.ToastFactory;
import com.google.android.gms.maps.GoogleMap;

/**
 * Created by Fabian on 07.02.2016.
 */
public class MyLocationButtonListener implements GoogleMap.OnMyLocationButtonClickListener {
    private Context context = null;

    public MyLocationButtonListener(Context context) {
        this.context = context;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            ToastFactory.makeToast(context, context.getResources().getString(R.string.toast_enable_gps));
        }
        return false;
    }
}
