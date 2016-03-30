package com.privatecar.privatecar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.dialogs.DateTimeDialog;
import com.privatecar.privatecar.dialogs.PaymentTypeDialog;
import com.privatecar.privatecar.models.entities.PrivateCarPlace;
import com.privatecar.privatecar.models.entities.User;
import com.privatecar.privatecar.models.enums.AddressType;
import com.privatecar.privatecar.models.enums.CarType;
import com.privatecar.privatecar.models.enums.PaymentType;
import com.privatecar.privatecar.models.requests.TripRequest;
import com.privatecar.privatecar.models.responses.TripRequestResponse;
import com.privatecar.privatecar.requests.CustomerRequests;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.DateUtil;
import com.privatecar.privatecar.utils.DialogUtils;
import com.privatecar.privatecar.utils.RequestListener;
import com.privatecar.privatecar.utils.Utils;

import java.util.Calendar;
import java.util.Locale;

public class CustomerVerifyTripActivity extends BasicBackActivity implements View.OnClickListener, RequestListener<TripRequestResponse> {
    private TripRequest tripRequest; // used to save the request parameters to be send to server
    private TextView tvPickupAddress;
    private TextView tvAddDetails;
    private TextView tvAddDropoff;
    private TextView tvPickupTime;
    private RadioGroup rgPickupTime;
    private RadioButton rbNow;
    private RadioButton rbLater;
    private RadioGroup rgCarType;
    private View layoutPaymentType;
    private TextView tvPaymentType;
    private TextView tvAddNotes;
    private TextView tvPromoCode;
    private Button btnGo;

    private DateTimeDialog pickupTimeDialog;
    private PaymentTypeDialog paymentTypeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_verifty_trip);

        // get passed params
        boolean pickupNow = getIntent().getBooleanExtra(Const.KEY_NOW, false);
        PrivateCarPlace pickupPlace = (PrivateCarPlace) getIntent().getSerializableExtra(Const.KEY_PICKUP_PLACE);
        CarType carType = (CarType) getIntent().getSerializableExtra(Const.KEY_CAR_TYPE);

        // create the trip request object with initial values
        tripRequest = new TripRequest();
        tripRequest.setPickupNow(pickupNow);
        tripRequest.setPickupPlace(pickupPlace);
        tripRequest.setCarType(carType);
        tripRequest.setPaymentType(PaymentType.CASH);
        tripRequest.setDestinationType(AddressType.LEAD_DRIVER);

        // init views
        tvPickupAddress = (TextView) findViewById(R.id.tv_pickup_address);
        tvAddDetails = (TextView) findViewById(R.id.tv_add_details);
        tvAddDropoff = (TextView) findViewById(R.id.tv_add_dropoff);
        tvPickupTime = (TextView) findViewById(R.id.tv_pickup_time);
        rgPickupTime = (RadioGroup) findViewById(R.id.rg_pickup_time);
        rbNow = (RadioButton) findViewById(R.id.rb_now);
        rbLater = (RadioButton) findViewById(R.id.rb_later);
        rgCarType = (RadioGroup) findViewById(R.id.rg_car_type);
        layoutPaymentType = findViewById(R.id.layout_payment_type);
        tvPaymentType = (TextView) findViewById(R.id.tv_payment_type);
        tvAddNotes = (TextView) findViewById(R.id.tv_add_notes);
        tvPromoCode = (TextView) findViewById(R.id.tv_promo_code);
        btnGo = (Button) findViewById(R.id.btn_go);

        // add check change listener to pickup time rg
        rgPickupTime.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_now:
                        oNowOption();
                        break;
                    case R.id.rb_later:
                        onLaterOption();
                        break;
                }
            }
        });

        // add check change listener to car type rg
        rgCarType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_economy:
                        tripRequest.setCarType(CarType.ECONOMY);
                        break;

                    case R.id.rb_business:
                        tripRequest.setCarType(CarType.BUSINESS);
                        break;
                }
            }
        });

        // update the ui
        updateUI();

        //  add click listeners
        tvAddDropoff.setOnClickListener(this);
        tvAddDetails.setOnClickListener(this);
        layoutPaymentType.setOnClickListener(this);
        tvAddNotes.setOnClickListener(this);
        tvPromoCode.setOnClickListener(this);
        btnGo.setOnClickListener(this);
    }

    /**
     * method, used to update pickup time to now
     */
    private void oNowOption() {
        // update request object & ui
        tripRequest.setPickupNow(true);
        tripRequest.setPickupTime(null);
        tvPickupTime.setText(R.string.as_soon_as_possible);
    }

    /**
     * method, used to show pickup time dialog and handle callbacks
     */
    private void onLaterOption() {
        // check to create new dialog
        if (pickupTimeDialog == null) {
            pickupTimeDialog = new DateTimeDialog(this, true, new DateTimeDialog.OnTimeSelectedListener() {
                @Override
                public void onTimeSelected(Calendar calendar) {
                    tripRequest.setPickupNow(false);
                    tripRequest.setPickupTime(calendar);
                    updateUI();
                }

                @Override
                public void onCancelled() {
                    // check now option
                    rbNow.setChecked(true);
                }
            });
        }

        // show select time dialog
        pickupTimeDialog.show();
    }

    /**
     * method, used to update the ui
     */
    private void updateUI() {
        // set the pickup address
        String pickupAddress = tripRequest.getPickupPlace().getName();
        if (!Utils.isNullOrEmpty(tripRequest.getPickupPlace().getAddress())) {
            pickupAddress += " - " + tripRequest.getPickupPlace().getAddress();
        }
        tvPickupAddress.setText(pickupAddress);

        // check pickup time
        if (tripRequest.isPickupNow()) {
            // set pickup time text
            tvPickupTime.setText(R.string.as_soon_as_possible);
        } else {
            rbLater.setChecked(true);

            // set pickup time text
            if (tripRequest.getPickupTime() != null) {
                if (DateUtil.isCurrentDate(tripRequest.getPickupTime())) {
                    tvPickupTime.setText(DateUtil.convertToString(tripRequest.getPickupTime(), "hh:mm a"));
                } else {
                    tvPickupTime.setText(DateUtil.convertToString(tripRequest.getPickupTime(), "d/M/yyyy hh:mm a"));
                }
            } else {
                Calendar calendar = Calendar.getInstance(Locale.getDefault());
                tvPickupTime.setText(DateUtil.convertToString(calendar, "hh:mm a"));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add_details:
                onAddDetails();
                break;

            case R.id.tv_add_dropoff:
                // open add drop off activity
                startActivity(new Intent(this, CustomerAddDropOffActivity.class));
                break;

            case R.id.layout_payment_type:
                onChangePaymentType();
                break;

            case R.id.tv_add_notes:
                onAddNotes();
                break;

            case R.id.tv_promo_code:
                // open add promo code activity
                startActivity(new Intent(this, CustomerAddPromoCodeActivity.class));
                break;

            case R.id.btn_go:
                requestTrip();
                break;
        }
    }

    /**
     * method, used to show payment type dialog and handle callbacks
     */
    private void onChangePaymentType() {
        // check to create new dialog
        if (paymentTypeDialog == null) {
            paymentTypeDialog = new PaymentTypeDialog(this, new PaymentTypeDialog.OnTypeSelectedListener() {
                @Override
                public void onTypeSelected(PaymentType type) {
                    // update trip request object
                    tripRequest.setPaymentType(type);

                    // update ui
                    String text;
                    if (type == PaymentType.CASH) {
                        tvPaymentType.setText(getString(R.string.cash));
                    } else {
                        tvPaymentType.setText(getString(R.string.account_credit));
                    }

                    paymentTypeDialog.dismiss();
                }
            });
        }

        // show payment dialog
        paymentTypeDialog.show();
    }

    /**
     * method, used to open add details activity
     */
    private void onAddDetails() {
        // open add details activity with pickup place
        Intent intent = new Intent(this, CustomerAddDetailsActivity.class);
        intent.putExtra(Const.KEY_PICKUP_PLACE, tripRequest.getPickupPlace());
        intent.putExtra(Const.KEY_DETAILS, tripRequest.getPickupDetails());
        startActivityForResult(intent, Const.REQUEST_ADD_DETAILS);
    }

    /**
     * method, used to open add details activity
     */
    private void onAddNotes() {
        // open add notes to captain activity with pickup place
        Intent intent = new Intent(this, CustomerAddNotesToCaptainActivity.class);
        intent.putExtra(Const.KEY_DETAILS, tripRequest.getNotes());
        startActivityForResult(intent, Const.REQUEST_ADD_NOTES);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check result code
        if (resultCode == RESULT_OK) {
            // check request code
            if (requestCode == Const.REQUEST_ADD_DETAILS) {
                // save details text
                String details = data.getStringExtra(Const.KEY_DETAILS);
                tripRequest.setPickupDetails(details);

                // change ui
                if (Utils.isNullOrEmpty(details)) {
                    tvAddDetails.setText(R.string.add_details);
                } else {
                    tvAddDetails.setText(R.string.change_details);
                }
            } else if (requestCode == Const.REQUEST_ADD_NOTES) {
                // save notes text
                String notes = data.getStringExtra(Const.KEY_DETAILS);
                tripRequest.setNotes(notes);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * method, used to validate and request trip from server
     */
    private void requestTrip() {
        // check internet connection
        if (!Utils.hasConnection(this)) {
            Utils.showShortToast(this, R.string.no_internet_connection);
            return;
        }

        // show progress dialog
        progressDialog = DialogUtils.showProgressDialog(this, R.string.finding_your_private_driver_please_wait);

        // create and send the request
        User user = AppUtils.getCachedUser(this);
        CustomerRequests.requestTrip(this, this, user.getAccessToken(), user.getCustomerAccountDetails().getId(), tripRequest);
    }

    @Override
    public void onSuccess(TripRequestResponse response, String apiName) {
        // dismiss progress dialog
        progressDialog.dismiss();

        // check response
        if (response.isStatus()) {
            // success
            // goto ride activity
//            Intent intent = new Intent(this, CustomerRideActivity.class);
//            intent.putExtra()
            DialogUtils.showAlertDialog(this, "Successful request", null);
        } else {
            // no drivers found
            // show dialog msg
            DialogUtils.showAlertDialog(this, R.string.no_drivers_found_now, null);
        }
    }

    @Override
    public void onFail(String message, String apiName) {
        progressDialog.dismiss();
        Utils.showLongToast(this, message);
        Log.e("ERROR", message);
    }
}
