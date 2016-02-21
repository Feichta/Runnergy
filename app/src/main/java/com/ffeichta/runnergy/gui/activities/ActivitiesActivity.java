package com.ffeichta.runnergy.gui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ExpandableListView;

import com.ffeichta.runnergy.R;
import com.ffeichta.runnergy.gui.adapter.ActivityAdapter;
import com.ffeichta.runnergy.gui.message.ToastFactory;
import com.ffeichta.runnergy.model.DBAccessHelper;
import com.ffeichta.runnergy.model.Track;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Fabian on 29.12.2015.
 */
public class ActivitiesActivity extends Activity {

    // UI Widgets
    ExpandableListView expListView = null;

    // Used for the ExpandableListView
    List<String> parentStrings = null;
    List<com.ffeichta.runnergy.model.Activity> childActivities = null;
    Map<String, ArrayList<com.ffeichta.runnergy.model.Activity>> groupCollection = null;

    ArrayList<com.ffeichta.runnergy.model.Activity> selection = null;

    // Actual Track
    private Track track = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_activity);

        expListView = (ExpandableListView) findViewById(R.id.listViewActivities);
        // registerForContextMenu(expListView);
        selection = new ArrayList();

        Intent intent = getIntent();
        track = (Track) intent.getSerializableExtra("track");
        getActionBar().setTitle(track.getName());

        childActivities = track.getActivities();
        if (childActivities == null) {
            childActivities = new ArrayList<>();
        }

        setUpParentsAndChilds();

        final ActivityAdapter activityAdapter = new ActivityAdapter(this, parentStrings,
                groupCollection);
        expListView.setAdapter(activityAdapter);
        expListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        expListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean
                    checked) {
                if (expListView.getItemAtPosition(position) instanceof com
                        .ffeichta.runnergy.model.Activity) {
                    mode.getMenuInflater().inflate(R.menu.contextual_action_bar_menu_delete, mode
                            .getMenu());
                    if (checked) {
                        selection.add(childActivities.get(position - 1));
                    } else {
                        selection.remove(childActivities.get(position - 1));
                    }
                    mode.setTitle(expListView.getCheckedItemCount() + getResources().getString(R
                            .string.select));
                } else {
                    if (position == childActivities.size()) {
                        position--;
                    }
                    mode.getMenuInflater().inflate(R.menu.contextual_action_bar_empty, mode
                            .getMenu());
                    int idType = getResources().getIdentifier(childActivities.get(position)
                                    .getType
                                            ().toString().toLowerCase(),
                            "string", getPackageName());
                    mode.setTitle(getResources().getString(idType));
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if (item.getItemId() == R.id.delete) {
                    for (com.ffeichta.runnergy.model.Activity Item : selection) {
                        childActivities.remove(Item);
                        if (DBAccessHelper.getInstance(ActivitiesActivity.this).deleteActivity
                                (Item) != 0) {
                            ToastFactory.makeToast(ActivitiesActivity.this, getResources()
                                    .getString(R.string.toast_delete_activity_error));
                        } else {
                            item.setVisible(false);
                            setUpParentsAndChilds();
                        }
                    }
                    onCreate(null);
                    activityAdapter.notifyDataSetChanged();
                    mode.finish();
                    return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                selection.clear();
            }
        });
        // If there is only one group in the ListView, then expand it
        if (parentStrings.size() == 1) {
            expListView.expandGroup(0);
        }

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(ActivitiesActivity.this, MapsActivity.class);
                intent.putExtra("activity", groupCollection.get(parentStrings.get(groupPosition))
                        .get(childPosition));
                startActivity(intent);
                return true;
            }
        });

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup) {
                    expListView.collapseGroup(previousGroup);
                }
                previousGroup = groupPosition;
            }
        });
    }

    private void setUpParentsAndChilds() {
        parentStrings = new ArrayList<>();
        groupCollection = new LinkedHashMap<>();

        // Iterate all Activities
        for (com.ffeichta.runnergy.model.Activity a1 : childActivities) {
            int id1 = getResources().getIdentifier(a1.getType().toString().toLowerCase(),
                    "string", getPackageName());
            String typeAsString = getResources().getString(id1);
            // Add every Type of Activity once
            if (!parentStrings.contains(typeAsString)) {
                parentStrings.add(getResources().getString(id1));
                ArrayList<com.ffeichta.runnergy.model.Activity> temp = new ArrayList<>();
                // Iterate Activities again and link every Type with the right Activities
                for (com.ffeichta.runnergy.model.Activity a2 : childActivities) {
                    int id2 = getResources().getIdentifier(a2.getType().toString().toLowerCase(),
                            "string", getPackageName());
                    if (getResources().getString(id2).equals(typeAsString)) {
                        temp.add(a2);
                    }
                }
                // Set the ranking in the group
                DBAccessHelper.getInstance(this).setRankingForActivitiesInTrack(temp);
                // Set the coordinates for each Activity
                for (com.ffeichta.runnergy.model.Activity activity : temp) {
                    activity.setCoordinates(DBAccessHelper.getInstance(this).getCoordinates
                            (activity));
                }
                groupCollection.put(typeAsString, temp);
            }
        }
    }
}