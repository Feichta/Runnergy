package com.ffeichta.runnergy.gui;

/**
 * Created by Fabian on 28.12.2015.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ffeichta.runnergy.R;

/**
 * Created by hp1 on 21-01-2015.
 */
public class ActivityActivity extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_activity, container, false);
        return v;
    }
}