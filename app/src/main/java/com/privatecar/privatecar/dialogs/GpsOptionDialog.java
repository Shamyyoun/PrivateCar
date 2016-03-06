package com.privatecar.privatecar.dialogs;

import android.app.Activity;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;

/**
 * Created by Shamyyoun on 2/17/2016.
 */
public class GpsOptionDialog extends ParentDialog {
    private Fragment fragment;
    private Activity activity;

    public GpsOptionDialog(Fragment fragment) {
        super(fragment.getActivity());
        this.fragment = fragment;
        init();
    }

    public GpsOptionDialog(Activity activity) {
        super(activity);
        this.activity = activity;
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_gps_option);

        // init views
        Button btnCancel = (Button) findViewById(R.id.btn_cancel);
        Button btnSettings = (Button) findViewById(R.id.btn_settings);

        // add click listeners
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dismiss dialog
                dismiss();
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open gps settings
                if (fragment != null)
                    fragment.startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), Const.REQUEST_GPS_SETTINGS);
                else
                    activity.startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), Const.REQUEST_GPS_SETTINGS);
            }
        });
    }
}
