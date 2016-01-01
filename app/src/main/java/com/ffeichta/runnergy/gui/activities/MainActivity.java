package com.ffeichta.runnergy.gui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ffeichta.runnergy.R;
import com.ffeichta.runnergy.gui.adapter.ViewPagerAdapter;
import com.ffeichta.runnergy.gui.tabs.SlidingTabLayout;


/**
 * Created by Fabian on 28.12.2015.
 */
public class MainActivity extends AppCompatActivity {
    // UI Widgets
    private Toolbar toolbar;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private SlidingTabLayout slidingTabLayout;

    // Titles for the tabs
    private CharSequence titles[] = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Set color of the tabs
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // Titles of the tabs
        titles = this.getResources().getStringArray(R.array.tabs_titles);

        // Use a Toolbar instead of ActionBar (only in this activity)
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        // Set the adapter class for the ViewPager
        viewPager = (ViewPager) findViewById(R.id.pager);
        // Get the SlidingTabLayoutView
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.tabs);

        // Create the FragmentStatePagerAdapter
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), titles);
        // Set the adapter
        viewPager.setAdapter(viewPagerAdapter);

        slidingTabLayout.setDistributeEvenly(true);
        // Set the ViewPager for the SlidingTabLayout
        slidingTabLayout.setViewPager(viewPager);

        // Set color for the line under the tabs
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return ContextCompat.getColor(getApplication(), R.color.colorTabUnderline);
            }
        });
    }

    // Build the menu in the ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // Called when an Item in the menu is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean ret;
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent settings = new Intent(this, SettingsActivity.class);
                startActivity(settings);
                ret = true;
                break;
            case R.id.menu_about:
                Intent about = new Intent(this, AboutActivity.class);
                startActivity(about);
                ret = true;
                break;
            default:
                ret = super.onOptionsItemSelected(item);
        }
        return ret;
    }
}