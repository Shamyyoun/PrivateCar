package com.privatecar.privatecar.activities;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.models.entities.DriverAccountDetails;
import com.privatecar.privatecar.models.entities.TripRequest;
import com.privatecar.privatecar.models.entities.User;
import com.privatecar.privatecar.models.enums.PaymentType;
import com.privatecar.privatecar.models.responses.GeneralResponse;
import com.privatecar.privatecar.requests.DriverRequests;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.ButtonHighlighterOnTouchListener;
import com.privatecar.privatecar.utils.DialogUtils;
import com.privatecar.privatecar.utils.RequestListener;
import com.privatecar.privatecar.utils.Utils;

public class TripRequestActivity extends BaseActivity implements RequestListener<GeneralResponse> {
    public static TripRequestActivity currentInstance;

    private TripRequest tripRequest;
    private TextView tvOrderNo;
    private TextView tvRideNo;
    private TextView tvClientName;
    private TextView tvMobile;
    private TextView tvPickupAddress;
    private TextView tvDestination;
    private TextView tvNotes;
    private TextView tvPaymentType;
    Button btnAccept, btnDecline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_request);
        currentInstance = this;

        // get trip request object
        tripRequest = (TripRequest) getIntent().getSerializableExtra(Const.KEY_TRIP_REQUEST);

        // init views
        tvOrderNo = (TextView) findViewById(R.id.tv_order_no);
        tvRideNo = (TextView) findViewById(R.id.tv_ride_no);
        tvClientName = (TextView) findViewById(R.id.tv_client_name);
        tvMobile = (TextView) findViewById(R.id.tv_mobile);
        tvPickupAddress = (TextView) findViewById(R.id.tv_pickup_address);
        tvDestination = (TextView) findViewById(R.id.tv_destination);
        tvNotes = (TextView) findViewById(R.id.tv_notes);
        tvPaymentType = (TextView) findViewById(R.id.tv_payment_type);

        btnAccept = (Button) findViewById(R.id.btn_accept);
        btnAccept.setOnTouchListener(new ButtonHighlighterOnTouchListener(this, R.drawable.greenish_start_rounded_corners_shape));

        btnDecline = (Button) findViewById(R.id.btn_decline);
        btnDecline.setOnTouchListener(new ButtonHighlighterOnTouchListener(this, R.drawable.reddish_end_rounded_corners_shape));

        updateUI();

        // wake up the device
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock.acquire();
    }

    /**
     * method, used to render the ui
     */
    private void updateUI() {
        tvRideNo.setText("" + tripRequest.getCode());
        tvOrderNo.setText("" + tripRequest.getId());
        tvClientName.setText(tripRequest.getCustomer());
        tvMobile.setText(tripRequest.getMobile());
        tvPickupAddress.setText(tripRequest.getPickAddress());
        tvDestination.setText(tripRequest.getDestinationAddress());
        tvNotes.setText(tripRequest.getNotes() == null ? "" : tripRequest.getNotes());

        // set payment type text
        if (tripRequest.getPaymentType().equals(PaymentType.CASH.getValue())) {
            tvPaymentType.setText(R.string.cash);
        } else {
            tvPaymentType.setText(R.string.account_credit);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_accept:
                acceptTrip();
                break;

            case R.id.btn_decline:
                Intent declineIntent = new Intent(this, DriverDeclineTripReasonActivity.class);
                declineIntent.putExtra(Const.KEY_TRIP_ID, tripRequest.getId());
                startActivity(declineIntent);
                break;
        }
    }

    /**
     * method, used to send accept trip request to the server
     */
    private void acceptTrip() {
        // check internet connection
        if (!Utils.hasConnection(this)) {
            Utils.showShortToast(this, R.string.no_internet_connection);
            return;
        }

        // show loading
        progressDialog = DialogUtils.showProgressDialog(this, R.string.loading_please_wait);

        // create & send the request
        User user = AppUtils.getCachedUser(this);
        DriverAccountDetails accountDetails = user.getAccountDetails();
        DriverRequests.acceptTrip(this, this, user.getAccessToken(), "" + accountDetails.getId(), "" + tripRequest.getId(),
                "" + accountDetails.getDefaultCarId());
    }

    @Override
    public void onSuccess(GeneralResponse response, String apiName) {
        // dismiss progress
        progressDialog.dismiss();

        // check if success
        if (response.isSuccess()) {
            // goto trip info activity & finish
            Intent intent = new Intent(this, DriverTripInfoActivity.class);
            intent.putExtra(Const.KEY_TRIP_REQUEST, tripRequest);
            startActivity(intent);
            finish();
        } else {
            // show error msg
            Utils.showLongToast(this, R.string.error_accepting_this_trip);
        }
    }

    @Override
    public void onFail(String message, String apiName) {
        // // dismiss progress & show error toast
        progressDialog.dismiss();
        Utils.showLongToast(this, R.string.connection_error);
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

    @Override
    protected void onDestroy() {
        // release screen lock
        KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();

        super.onDestroy();
    }
}
