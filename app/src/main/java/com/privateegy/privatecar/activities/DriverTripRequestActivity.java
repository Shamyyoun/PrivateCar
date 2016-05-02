package com.privateegy.privatecar.activities;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.privateegy.privatecar.Const;
import com.privateegy.privatecar.R;
import com.privateegy.privatecar.models.entities.Config;
import com.privateegy.privatecar.models.entities.DriverAccountDetails;
import com.privateegy.privatecar.models.entities.DriverTripRequest;
import com.privateegy.privatecar.models.entities.User;
import com.privateegy.privatecar.models.enums.PaymentType;
import com.privateegy.privatecar.models.responses.GeneralResponse;
import com.privateegy.privatecar.requests.DriverRequests;
import com.privateegy.privatecar.utils.AppUtils;
import com.privateegy.privatecar.utils.ButtonHighlighterOnTouchListener;
import com.privateegy.privatecar.utils.DialogUtils;
import com.privateegy.privatecar.utils.RequestListener;
import com.privateegy.privatecar.utils.Utils;

public class DriverTripRequestActivity extends BaseActivity implements RequestListener {
    public static DriverTripRequestActivity currentInstance;

    private DriverTripRequest tripRequest;
    private TextView tvOrderNo;
    private TextView tvRideNo;
    private TextView tvClientName;
    private TextView tvMobile;
    private TextView tvPickupAddress;
    private TextView tvDestination;
    private TextView tvNotes;
    private TextView tvPaymentType;
    Button btnAccept, btnDecline;

    MediaPlayer player;
    private Handler timeoutHandler;
    private Runnable timeoutRunnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_trip_request);
        currentInstance = this;

        // get trip request object
        tripRequest = (DriverTripRequest) getIntent().getSerializableExtra(Const.KEY_TRIP_REQUEST);

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

        // start timeout request after static time
        scheduleTimeoutRequest();
    }

    private void scheduleTimeoutRequest() {
        // cancel old before starting new one
        cancelTimeoutRequest();

        // schedule canceller thread after static time from the cache
        int timeoutDelay = Utils.convertToInteger(AppUtils.getConfigValue(this, Config.KEY_ACCEPT_TRIP_WINDOW)) * 1000;
        timeoutHandler = new Handler();
        timeoutRunnable = new Runnable() {
            @Override
            public void run() {
                timeout();
            }
        };
        timeoutHandler.postDelayed(timeoutRunnable, timeoutDelay);
    }

    private void cancelTimeoutRequest() {
        if (timeoutHandler != null && timeoutRunnable != null) {
            timeoutHandler.removeCallbacks(timeoutRunnable);
        }
     }

    /**
     * method, used to timeout the trip request
     */
    private void timeout() {
        player.stop();
        player.release();

        // check internet connection
        if (!Utils.hasConnection(this)) {
            return;
        }

        // show loading
        progressDialog = DialogUtils.showProgressDialog(this, R.string.you_are_timed_out_cancelling_the_request);

        // create & send the request
        User user = AppUtils.getCachedUser(this);
        DriverAccountDetails accountDetails = user.getDriverAccountDetails();
        DriverRequests.timeoutTripRequest(this, this, user.getAccessToken(), "" + accountDetails.getId(),
                "" + tripRequest.getId(), "" + accountDetails.getDefaultCarId(), tripRequest.isMidnightRequest());
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

    @Override
    protected void onStart() {
        super.onStart();

        // play the ringtone sound
        player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        player.setLooping(true);
        player.start();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_accept:
                player.stop();
                player.release();

                // cancel decline request
                cancelTimeoutRequest();

                acceptTrip();
                break;

            case R.id.btn_decline:
                player.stop();
                player.release();

                // cancel timeout request
                cancelTimeoutRequest();

                // goto decline activity
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
        DriverAccountDetails accountDetails = user.getDriverAccountDetails();
        DriverRequests.acceptTrip(this, this, user.getAccessToken(), "" + accountDetails.getId(), "" + tripRequest.getId(),
                "" + accountDetails.getDefaultCarId());
    }

    @Override
    public void onSuccess(Object response, String apiName) {
        // dismiss progress
        progressDialog.dismiss();

        // check the apiName
        if (apiName.equals(Const.MESSAGE_DRIVER_ACCEPT_TRIP)) {
            // this is accept trip request
            // cast the response
            GeneralResponse generalResponse = (GeneralResponse) response;

            // check if success
            if (generalResponse.isSuccess()) {
                // cancel decline request
                cancelTimeoutRequest();

                // goto trip info activity & finish
                Intent intent = new Intent(this, DriverTripInfoActivity.class);
                intent.putExtra(Const.KEY_TRIP_REQUEST, tripRequest);
                startActivity(intent);
                finish();
            } else {
                // show error msg
                Utils.showLongToast(this, R.string.error_accepting_this_trip);

                // start the decline request again
                scheduleTimeoutRequest();
            }
        } else {
            // this is timeout trip request
            // cast the response
            GeneralResponse generalResponse = (GeneralResponse) response;

            // check response
            if (generalResponse.isSuccess()) {
                // show message and finish
                Utils.showLongToast(this, R.string.you_are_timed_out_trip_automatically_cancelled);
                finish();
            } else {
                // start the timeout request again
                scheduleTimeoutRequest();
            }
        }
    }

    @Override
    public void onFail(String message, String apiName) {
        // // dismiss progress & show error toast
        progressDialog.dismiss();
        Utils.showLongToast(this, R.string.connection_error);

        // start the timeout request again
        scheduleTimeoutRequest();
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

    @Override
    protected void onDestroy() {
        //TODO: remove this deprecated code
        //TODO: make the activity unlock the screen

        // release screen lock
        KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();

        super.onDestroy();
    }
}
