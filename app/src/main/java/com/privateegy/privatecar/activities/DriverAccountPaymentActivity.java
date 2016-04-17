package com.privateegy.privatecar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.privateegy.privatecar.Const;
import com.privateegy.privatecar.R;
import com.privateegy.privatecar.models.entities.DriverTripRequest;

import java.util.Locale;

public class DriverAccountPaymentActivity extends BaseActivity {
    private DriverTripRequest tripRequest;
    private TextView tvRideNo, tvPrice;
    private Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_account_payment);

        // get trip request object
        tripRequest = (DriverTripRequest) getIntent().getSerializableExtra(Const.KEY_TRIP_REQUEST);

        tvRideNo = (TextView) findViewById(R.id.tv_ride_no);
        tvRideNo.setText(tripRequest.getCode());

        tvPrice = (TextView) findViewById(R.id.tv_price);
        int actualFare = getIntent().getIntExtra(Const.KEY_ACTUAL_FARE, 0);
        tvPrice.setText(String.format(Locale.ENGLISH, "%d %s", actualFare, getString(R.string.currency)));

        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open driver home activity with some flags
                Intent intent = new Intent(DriverAccountPaymentActivity.this, DriverRateTripActivity.class);
                intent.putExtra(Const.KEY_TRIP_REQUEST, tripRequest);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }
}
