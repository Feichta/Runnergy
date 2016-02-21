package com.ffeichta.runnergy.gui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.ffeichta.runnergy.R;
import com.ffeichta.runnergy.gui.activities.ActivitiesActivity;
import com.ffeichta.runnergy.gui.adapter.TrackAdapter;
import com.ffeichta.runnergy.gui.dialogfactory.ChangeTrackDialogFactory;
import com.ffeichta.runnergy.gui.message.ToastFactory;
import com.ffeichta.runnergy.model.DBAccessHelper;
import com.ffeichta.runnergy.model.Track;

import java.util.ArrayList;

/**
 * Created by Fabian on 28.12.2015.
 */
public class TracksFragment extends Fragment {

    ArrayList<Track> selection = null;
    private ArrayList<Track> tracks = null;
    private ListView listView = null;
    private TrackAdapter trackAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tracks_fragment, container, false);
        listView = (ListView) v.findViewById(R.id.listviewTracks);
        tracks = DBAccessHelper.getInstance(getContext()).getTracks();
        selection = new ArrayList<>();
        if (tracks == null) {
            tracks = new ArrayList<>();
        }
        trackAdapter = new TrackAdapter(this.getActivity(), tracks);
        listView.setAdapter(trackAdapter);
        listView.setChoiceMode(ExpandableListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean
                    checked) {
                if (checked) {
                    selection.add(tracks.get(position));
                    mode.setTitle(listView.getCheckedItemCount() + getResources().getString(R
                            .string.select));
                } else {
                    selection.remove(tracks.get(position));
                    mode.setTitle(listView.getCheckedItemCount() + getResources().getString(R
                            .string.select));
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater menuInflater = getActivity().getMenuInflater();
                menuInflater.inflate(R.menu.contextual_action_bar_menu_delete_change, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        for (Track Item : selection) {
                            tracks.remove(Item);
                            if (DBAccessHelper.getInstance(getContext()).deleteTrack(Item) != 0) {
                                ToastFactory.makeToast(getContext(), getResources().getString(R
                                        .string.toast_delete_track_error));
                            } else {
                                item.setVisible(false);
                                selection.remove(Item);
                            }
                        }
                        onCreate(null);
                        trackAdapter.notifyDataSetChanged();
                        mode.finish();
                        break;
                    case R.id.change:

                        ChangeTrackDialogFactory changeTrackDialogFactory = new
                                ChangeTrackDialogFactory(getActivity(), selection.get(0), mode);
                        changeTrackDialogFactory.makeCustomInputDialog();
                        selection.clear();
                        break;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (tracks.get(position).getActivities() != null) {
                    Intent i = new Intent(getActivity(),
                            ActivitiesActivity.class);
                    i.putExtra("track", tracks.get(position));
                    startActivityForResult(i, 0);
                }
            }
        });
        return v;
    }

    /**
     * Called when an activity started by this Fragment is finished
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        tracks = DBAccessHelper.getInstance(getContext()).getTracks();
        if (tracks == null) {
            tracks = new ArrayList<>();
        }
        trackAdapter = new TrackAdapter(this.getActivity(), tracks);
        listView.setAdapter(trackAdapter);
    }
}