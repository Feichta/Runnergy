package com.ffeichta.runnergy.gui;


import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import com.ffeichta.runnergy.R;

/**
 * Created by Fabian on 28.12.2015.
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new PrefsFragment()).commit();
    }

    // This class is used becaus 'addPreferencesFromResource' in class 'PreferenceActivity' is deprecated
    public static class PrefsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Load the preferences from XML
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}