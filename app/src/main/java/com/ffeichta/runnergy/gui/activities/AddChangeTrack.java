package com.ffeichta.runnergy.gui.activities;

import android.app.AlertDialog;
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
public class AddChangeTrack {

    private SaveActivityActivity saveActivityActivity = null;

    public AddChangeTrack(SaveActivityActivity saveActivityActivity) {
        this.saveActivityActivity = saveActivityActivity;

    }

    public void showInputDialog() {
        LayoutInflater layoutInflater = saveActivityActivity.getLayoutInflater();
        final View promptView = layoutInflater.inflate(R.layout.dialog_fragment_track, null);

        final AlertDialog d = new AlertDialog.Builder(saveActivityActivity)
                .setPositiveButton(R.string.dialog_fragment_ok, null)
                .setNegativeButton(R.string.dialog_fragment_cancel, null)
                .setTitle(R.string.dialog_fragment_title_add)
                .setView(promptView)
                .create();
        d.show();
        Log.d("adfdadf", "####1");
        d.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                EditText name = (EditText) promptView.findViewById(R.id.dialog_fragment_edit_text);
                TextView errorTextView = (TextView) promptView.findViewById(R.id.dialog_fragment_error);

                Track track = new Track();
                String input = name.getText().toString();
                if (input != null) {
                    track.setName(name.getText().toString());
                }
                int result = DBAccessHelper.getInstance(null).insertTrack(track);
                if (result == 0) {
                    saveActivityActivity.setUpSpinners();
                    saveActivityActivity.spinnerTrack.setSelection(saveActivityActivity.spinnerTrack.getAdapter().getCount()-1);
                    d.dismiss();

                } else {
                    Hashtable<String, Integer> error = track.getError();
                    if (error != null) {
                        if (error.get("name") == Track.NAME_IS_NOT_SET) {
                            errorTextView.setVisibility(View.VISIBLE);
                            errorTextView.setText(saveActivityActivity.getResources().getString(R.string.dialog_fragment_not_set));
                        }
                        if (error.get("name") == Track.NAME_ALREADY_EXISTS) {
                            errorTextView.setVisibility(View.VISIBLE);
                            errorTextView.setText(saveActivityActivity.getResources().getString(R.string.dialog_fragment_already_set));
                        }
                    }
                }
            }
        });
    }

}
