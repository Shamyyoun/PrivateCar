package com.privateegy.privatecar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.privateegy.privatecar.R;
import com.privateegy.privatecar.dialogs.AdDialog;
import com.privateegy.privatecar.models.entities.CustomerTripRequest;
import com.privateegy.privatecar.models.entities.User;
import com.privateegy.privatecar.models.responses.AdResponse;
import com.privateegy.privatecar.requests.CustomerRequests;
import com.privateegy.privatecar.utils.AppUtils;
import com.privateegy.privatecar.utils.RequestHelper;
import com.privateegy.privatecar.utils.RequestListener;

public class CustomerReceiptActivity extends BaseActivity implements RequestListener<AdResponse> {
    private CustomerTripRequest tripRequest;
    private TextView tvRideNo;
    private TextView tvReceiptNo;
    private Button btnOk;

    private RequestHelper randomAdRequest;
    private AdDialog adDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_receipt);

        // get trip request info
        tripRequest = AppUtils.getLastCachedTripRequest(this);

        // init views
        tvRideNo = (TextView) findViewById(R.id.tv_ride_no);
        tvReceiptNo = (TextView) findViewById(R.id.tv_receipt_no);
        btnOk = (Button) findViewById(R.id.btn_ok);

        // set data
        tvRideNo.setText(tripRequest.getCode());
        tvReceiptNo.setText("" + tripRequest.getId());

        // load random ad
        loadRandomAd();

        // add click listeners
        btnOk.setOnClickListener(this);
    }

    /**
     * method, used to load a random ad in the background
     */
    private void loadRandomAd() {
        // create & send the request
        User user = AppUtils.getCachedUser(this);
        randomAdRequest = CustomerRequests.randomAd(this, this, user.getAccessToken());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                // start new customer arte activity
                Intent intent = new Intent(this, CustomerRateActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

    @Override
    public void onSuccess(AdResponse response, String apiName) {
        // check the ad object
        if (response.getAd() != null) {
            // open ad dialog with this ad
            adDialog = new AdDialog(this, response.getAd());
            adDialog.show();
        }
    }

    @Override
    public void onFail(String message, String apiName) {
        // log the message
        Log.e("ERROR", message);
    }

    @Override
    protected void onDestroy() {
        // cancel the random ad request
        randomAdRequest.cancel(false);
        super.onDestroy();
    }
}
