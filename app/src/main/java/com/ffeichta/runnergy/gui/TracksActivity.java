package com.ffeichta.runnergy.gui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ffeichta.runnergy.R;

/**
 * Created by Fabian on 28.12.2015.
 */
public class TracksActivity extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_tracks, container, false);
        return v;
    }
}
