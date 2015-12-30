package com.ffeichta.runnergy.gui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ffeichta.runnergy.gui.activities.ActivityFragment;
import com.ffeichta.runnergy.gui.activities.TracksFragment;

/**
 * Created by Fabian on 28.12.2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private static final int NUMBER_TABS = 2;
    private CharSequence TitlesOfTabs[];

    public ViewPagerAdapter(FragmentManager fragmentManager, CharSequence mTitles[]) {
        super(fragmentManager);
        this.TitlesOfTabs = mTitles;
    }

    // Return a fragment object for the tabs
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            ActivityFragment activityFragment = new ActivityFragment();
            return activityFragment;
        } else {
            TracksFragment tracksFragment = new TracksFragment();
            return tracksFragment;
        }
    }

    // Return the titles for the tabs
    @Override
    public CharSequence getPageTitle(int position) {
        return TitlesOfTabs[position];
    }

    // Return the count of tabs
    @Override
    public int getCount() {
        return NUMBER_TABS;
    }
}