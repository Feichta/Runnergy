package com.ffeichta.runnergy.gui.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ffeichta.runnergy.R;
import com.ffeichta.runnergy.model.DBAccessHelper;
import com.ffeichta.runnergy.model.Track;

import java.util.Hashtable;

/**
 * Created by Fabian on 31.12.2015.
 */
public class AddTrackDialogFragment extends DialogFragment {
    private EditText name = null;
    private TextView errorTextView = null;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View promptView = layoutInflater.inflate(R.layout.dialog_fragment_track, null);

        name = (EditText) promptView.findViewById(R.id.dialog_fragment_edit_text);
        Log.d("alsdkf", "####" + (name == null));
        errorTextView = (TextView) promptView.findViewById(R.id.dialog_fragment_error);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(layoutInflater.inflate(R.layout.dialog_fragment_track, null))
                // Add action buttons
                .setPositiveButton(R.string.dialog_fragment_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Track track = new Track();
                        String input = name.getText().toString();
                        if (input != null) {
                            track.setName(name.getText().toString());
                        }
                        int result = DBAccessHelper.getInstance(null).insertTrack(track);
                        if (result == 0) {
                            AddTrackDialogFragment.this.getDialog().cancel();
                        } else {
                            Hashtable<String, Integer> error = track.getError();
                            if (error != null) {
                                if (error.get("name") == Track.NAME_IS_NOT_SET) {
                                    errorTextView.setVisibility(View.VISIBLE);
                                    errorTextView.setText(getResources().getString(R.string.dialog_fragment_not_set));
                                }
                                if (error.get("name") == Track.NAME_ALREADY_EXISTS) {
                                    errorTextView.setVisibility(View.VISIBLE);
                                    errorTextView.setText(getResources().getString(R.string.dialog_fragment_already_set));
                                }
                            }
                        }
                    }
                })
                .setNegativeButton(R.string.dialog_fragment_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddTrackDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
