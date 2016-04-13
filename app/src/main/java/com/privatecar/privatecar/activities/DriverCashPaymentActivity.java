package com.privatecar.privatecar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.models.entities.DriverTripRequest;
import com.privatecar.privatecar.models.responses.EndTripResponse;
import com.privatecar.privatecar.requests.DriverRequests;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.DialogUtils;
import com.privatecar.privatecar.utils.RequestListener;
import com.privatecar.privatecar.utils.Utils;

import java.util.Locale;

public class DriverCashPaymentActivity extends BaseActivity implements RequestListener<EndTripResponse> {

    private TextView tvRideNo, tvPrice;
    private EditText etCash;

    private DriverTripRequest tripRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_cash_payment);

        // get trip request object
        tripRequest = (DriverTripRequest) getIntent().getSerializableExtra(Const.KEY_TRIP_REQUEST);

        tvRideNo = (TextView) findViewById(R.id.tv_ride_no);
        tvRideNo.setText(tripRequest.getCode());

        tvPrice = (TextView) findViewById(R.id.tv_price);
        int actualFare = getIntent().getIntExtra(Const.KEY_ACTUAL_FARE, 0);
        tvPrice.setText(String.format(Locale.ENGLISH, "%d %s", actualFare, getString(R.string.currency)));

        etCash = (EditText) findViewById(R.id.et_cash);
    }

    private void endTrip() {
        float cash;
        if (Utils.isEmpty(etCash)) {
            Utils.showLongToast(this, R.string.please_enter_cash_value);
            return;
        } else {
            cash = Float.parseFloat(etCash.getText().toString());
        }

        progressDialog = DialogUtils.showProgressDialog(this, R.string.ending_trip, false);

        int actualFare = getIntent().getIntExtra(Const.KEY_ACTUAL_FARE, 0);
        int tripDistance = getIntent().getIntExtra(Const.KEY_TRIP_DISTANCE, 0);
        int tripDuration = getIntent().getIntExtra(Const.KEY_TRIP_DURATION, 0);

        DriverRequests.endTrip(this, this, AppUtils.getCachedUser(this).getAccessToken()
                , tripRequest.getId(), actualFare, tripDistance / 1000.0f, tripDuration * 60, cash);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                //TODO: add confirmation
                endTrip();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

    @Override
    public void onSuccess(EndTripResponse response, String apiName) {
        if (response.isSuccess()) {
            progressDialog.dismiss();

            startActivity(new Intent(this, DriverRateTripActivity.class));
            finish();
        }
    }

    @Override
    public void onFail(String message, String apiName) {
        progressDialog.dismiss();

        Utils.showLongToast(this, message);
        Utils.LogE(message);
    }
}
