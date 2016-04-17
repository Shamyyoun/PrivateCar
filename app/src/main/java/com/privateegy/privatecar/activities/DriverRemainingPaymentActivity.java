package com.privateegy.privatecar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.privateegy.privatecar.Const;
import com.privateegy.privatecar.R;
import com.privateegy.privatecar.models.entities.DriverTripRequest;
import com.privateegy.privatecar.models.responses.GeneralResponse;
import com.privateegy.privatecar.requests.DriverRequests;
import com.privateegy.privatecar.utils.AppUtils;
import com.privateegy.privatecar.utils.DialogUtils;
import com.privateegy.privatecar.utils.RequestListener;
import com.privateegy.privatecar.utils.Utils;

import java.util.Locale;

public class DriverRemainingPaymentActivity extends BaseActivity implements RequestListener<GeneralResponse> {

    private TextView tvRideNo, tvPrice, tvRemaining;
    private EditText etCash;

    private DriverTripRequest tripRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_remaining_payment);

        // get trip request object
        tripRequest = (DriverTripRequest) getIntent().getSerializableExtra(Const.KEY_TRIP_REQUEST);

        tvRideNo = (TextView) findViewById(R.id.tv_ride_no);
        tvRideNo.setText(tripRequest.getCode());

        tvPrice = (TextView) findViewById(R.id.tv_price);
        int actualFare = getIntent().getIntExtra(Const.KEY_ACTUAL_FARE, 0);
        tvPrice.setText(String.format(Locale.ENGLISH, "%d %s", actualFare, getString(R.string.currency)));

        tvRemaining = (TextView) findViewById(R.id.tv_remaining);
        float remainingValue = getIntent().getIntExtra(Const.KEY_REMAINING_VALUE, 0);
        tvPrice.setText(String.format("%.0f", remainingValue) + " " + getString(R.string.currency));

        etCash = (EditText) findViewById(R.id.et_cash);
    }

    private void confirmPayRemaining() {
        float cash;
        if (Utils.isEmpty(etCash)) {
            Utils.showLongToast(this, R.string.please_enter_cash_value);
            return;
        } else {
            cash = Float.parseFloat(etCash.getText().toString());
        }

        progressDialog = DialogUtils.showProgressDialog(this, R.string.ending_trip, false);

        DriverRequests.payRemaining(this, this, AppUtils.getCachedUser(this).getAccessToken(), tripRequest.getId(), cash);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                //TODO: add confirmation
                confirmPayRemaining();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

    @Override
    public void onSuccess(GeneralResponse response, String apiName) {
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
