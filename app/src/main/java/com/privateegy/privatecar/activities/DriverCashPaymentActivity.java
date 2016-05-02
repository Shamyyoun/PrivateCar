package com.privateegy.privatecar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.privateegy.privatecar.Const;
import com.privateegy.privatecar.R;
import com.privateegy.privatecar.models.entities.DriverTripRequest;
import com.privateegy.privatecar.models.responses.EndTripResponse;
import com.privateegy.privatecar.requests.DriverRequests;
import com.privateegy.privatecar.utils.AppUtils;
import com.privateegy.privatecar.utils.DialogUtils;
import com.privateegy.privatecar.utils.RequestListener;
import com.privateegy.privatecar.utils.Utils;

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

            // start rate activity as new task
            Intent intent = new Intent(this, DriverRateTripActivity.class);
            intent.putExtra(Const.KEY_TRIP_REQUEST, tripRequest);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
        }
    }

    @Override
    public void onFail(String message, String apiName) {
        progressDialog.dismiss();

        Utils.showLongToast(this, message);
        Utils.LogE(message);
    }
}
