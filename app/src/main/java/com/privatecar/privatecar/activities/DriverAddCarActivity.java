package com.privatecar.privatecar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.privatecar.privatecar.R;

public class DriverAddCarActivity extends BasicBackActivity {
    private ImageButton ibCarPhoto;
    private TextView tvCarLicenceFront;
    private ImageButton ibBrowseCarLicenceFront;
    private ImageButton ibCaptureCarLicenceFront;
    private TextView tvCarLicenceBack;
    private ImageButton ibBrowseCarLicenceBack;
    private ImageButton ibCaptureCarLicenceBack;
    private Button btnAddCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_add_car);

        // init views
        ibCarPhoto = (ImageButton) findViewById(R.id.ib_car_photo);
        tvCarLicenceFront = (TextView) findViewById(R.id.tv_car_licence_front);
        ibBrowseCarLicenceFront = (ImageButton) findViewById(R.id.ib_browse_car_licence_front);
        ibCaptureCarLicenceFront = (ImageButton) findViewById(R.id.ib_capture_car_licence_front);
        tvCarLicenceBack = (TextView) findViewById(R.id.tv_car_licence_back);
        ibBrowseCarLicenceBack = (ImageButton) findViewById(R.id.ib_browse_car_licence_back);
        ibCaptureCarLicenceBack = (ImageButton) findViewById(R.id.ib_capture_car_licence_back);
        btnAddCar = (Button) findViewById(R.id.btn_add_car);

        // add click listeners
        ibCarPhoto.setOnClickListener(this);
        ibBrowseCarLicenceFront.setOnClickListener(this);
        ibCaptureCarLicenceFront.setOnClickListener(this);
        ibBrowseCarLicenceBack.setOnClickListener(this);
        ibCaptureCarLicenceBack.setOnClickListener(this);
        btnAddCar.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_car_photo:
                break;

            case R.id.btn_add_car:
                startActivity(new Intent(this, DriverAddCarConfirmationActivity.class));
                break;
        }
    }
}
