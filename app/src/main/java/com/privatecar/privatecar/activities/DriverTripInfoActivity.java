package com.privatecar.privatecar.activities;

import android.os.Bundle;
import android.widget.RatingBar;

import com.privatecar.privatecar.R;

public class DriverTripInfoActivity extends BaseActivity {

    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_trip_info);

        ratingBar = (RatingBar) findViewById(R.id.rating_bar);

    }

}
