package com.privateegy.privatecar.activities;

import android.os.Bundle;
import android.view.View;

import com.privateegy.privatecar.R;

public class DriverSignupConfirmationActivity extends BasicBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_signup_confirmation);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                finish();
                break;
        }
    }
}
