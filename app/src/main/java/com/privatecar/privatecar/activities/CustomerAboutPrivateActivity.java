package com.privatecar.privatecar.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.privatecar.privatecar.R;
import com.privatecar.privatecar.models.entities.Config;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.Utils;

/**
 * Created by Shamyyoun on 2/20/2016.
 */
public class CustomerAboutPrivateActivity extends BasicBackActivity {
    private TextView tvAppVersion;
    private Button btnUpdate;
    private TextView tvWebsite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_about_private);

        // init views
        tvAppVersion = (TextView) findViewById(R.id.tv_app_version);
        btnUpdate = (Button) findViewById(R.id.btn_update);
        tvWebsite = (TextView) findViewById(R.id.tv_website);

        // get current app version from cached configs and set it
        int configAppVersion = 0;
        try {
            configAppVersion = Integer.parseInt(AppUtils.getConfigValue(this, Config.KEY_CURRENT_APP_VERSION));
        } catch (Exception e) {
        }
        tvAppVersion.setText("" + configAppVersion);

        // get installed app version & compare it
        int installedAppVersion = Utils.getAppVersion(this);
        if (configAppVersion > installedAppVersion) {
            // not the latest version
            // add click listener to update button
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // open app page in google play
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                }
            });
        } else {
            // the latest version is installed
            // disable update button
            btnUpdate.setEnabled(false);
        }
    }
}
