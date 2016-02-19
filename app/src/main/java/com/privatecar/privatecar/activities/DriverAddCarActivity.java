package com.privatecar.privatecar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.privatecar.privatecar.R;

public class DriverAddCarActivity extends BasicBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_add_car);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_car:
                startActivity(new Intent(this, DriverAddCarConfirmationActivity.class));
                break;
        }
    }
}
