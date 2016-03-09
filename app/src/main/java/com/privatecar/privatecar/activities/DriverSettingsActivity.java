package com.privatecar.privatecar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.privatecar.privatecar.R;

public class DriverSettingsActivity extends BasicBackActivity {
    private View layoutChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_settings);

        // init views
        layoutChangePassword = findViewById(R.id.layout_change_password);
        layoutChangePassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_change_password:
                // open change password activity
                startActivity(new Intent(this, ChangePasswordActivity.class));
                break;

            default:
                super.onClick(v);
        }
    }
}
