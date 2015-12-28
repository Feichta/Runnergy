package com.ffeichta.runnergy.gui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Fabian on 28.12.2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private final int NUMBER_TABS = 2;
    private CharSequence TitlesOfTabs[];

    public ViewPagerAdapter(FragmentManager fragmentManager, CharSequence mTitles[]) {
        super(fragmentManager);
        this.TitlesOfTabs = mTitles;
    }

    // Return a fragment object for the tabs
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            ActivityActivity activityActivity = new ActivityActivity();
            return activityActivity;
        } else {
            TracksActivity tracksActivity = new TracksActivity();
            return tracksActivity;
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