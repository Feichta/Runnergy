package com.ffeichta.runnergy.gui;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ffeichta.runnergy.R;


/**
 * Created by Fabian on 28.12.2015.
 */
public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private SlidingTabLayout slidingTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load preferences from XML
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // Titles of the tabs
        CharSequence Titles[] = this.getResources().getStringArray(R.array.main_activity_tabs);

        // Use a Toolbar instead of ActionBar (only in this activity)
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the FragmentStatePagerAdapter
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles);

        // Set the adapter class for the ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(viewPagerAdapter);

        // Get the SlidingTabLayoutView
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.tabs);
        slidingTabLayout.setDistributeEvenly(true);

        // Set color for the line under the tabs
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorTabUnderline);
            }
        });
        // Set the ViewPager for the SlidingTabLayout
        slidingTabLayout.setViewPager(viewPager);
    }

    // Build the menu in the ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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