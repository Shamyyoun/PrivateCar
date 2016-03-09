package com.privatecar.privatecar.activities;

import android.os.Bundle;
import android.view.View;

import com.privatecar.privatecar.R;

public class DriverAddCarConfirmationActivity extends BasicBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_add_car_confirmation);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                onBackPressed();
                break;
        }
    }
}
