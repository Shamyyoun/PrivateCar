package com.privatecar.privatecar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.dialogs.DateTimeDialog;
import com.privatecar.privatecar.dialogs.PaymentTypeDialog;
import com.privatecar.privatecar.models.entities.DistanceMatrixElement;
import com.privatecar.privatecar.models.entities.Fare;
import com.privatecar.privatecar.models.entities.PrivateCarPlace;
import com.privatecar.privatecar.models.entities.User;
import com.privatecar.privatecar.models.enums.AddressType;
import com.privatecar.privatecar.models.enums.CarType;
import com.privatecar.privatecar.models.enums.PaymentType;
import com.privatecar.privatecar.models.requests.TripRequest;
import com.privatecar.privatecar.models.responses.DistanceMatrixResponse;
import com.privatecar.privatecar.models.responses.FaresResponse;
import com.privatecar.privatecar.models.responses.TripRequestResponse;
import com.privatecar.privatecar.requests.CommonRequests;
import com.privatecar.privatecar.requests.CustomerRequests;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.DateUtil;
import com.privatecar.privatecar.utils.DialogUtils;
import com.privatecar.privatecar.utils.RequestListener;
import com.privatecar.privatecar.utils.Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CustomerVerifyTripActivity extends BasicBackActivity implements View.OnClickListener, RequestListener {
    private TripRequest tripRequest; // used to save the request parameters to be send to server
    private TextView tvPickupAddress;
    private TextView tvAddDetails;
    private TextView tvAddDropoff;
    private TextView tvDestinationAddress;
    private TextView tvPickupTime;
    private RadioGroup rgPickupTime;
    private RadioButton rbNow;
    private RadioButton rbLater;
    private RadioGroup rgCarType;
    private View layoutPaymentType;
    private TextView tvPaymentType;
    private View layoutEstimate;
    private TextView tvEstimation;
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
        tvDestinationAddress = (TextView) findViewById(R.id.tv_destination_address);
        tvPickupTime = (TextView) findViewById(R.id.tv_pickup_time);
        rgPickupTime = (RadioGroup) findViewById(R.id.rg_pickup_time);
        rbNow = (RadioButton) findViewById(R.id.rb_now);
        rbLater = (RadioButton) findViewById(R.id.rb_later);
        rgCarType = (RadioGroup) findViewById(R.id.rg_car_type);
        layoutPaymentType = findViewById(R.id.layout_payment_type);
        tvPaymentType = (TextView) findViewById(R.id.tv_payment_type);
        layoutEstimate = findViewById(R.id.layout_estimate);
        tvEstimation = (TextView) findViewById(R.id.tv_estimation);
        tvAddNotes = (TextView) findViewById(R.id.tv_add_notes);
        tvPromoCode = (TextView) findViewById(R.id.tv_promo_code);
        btnGo = (Button) findViewById(R.id.btn_go);

        // set estimate layout disabled
        layoutEstimate.setEnabled(false);

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
        layoutEstimate.setOnClickListener(this);
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
        updateUI();
    }

    /**
     * method, used to show pickup time dialog and handle callbacks
     */
    private void onLaterOption() {
        // zero all estimations
        tripRequest.setEstimateFare(0);
        tripRequest.setEstimateDistance(0);
        tripRequest.setEstimateTime(0);

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

        // check destination place to customize estimate ui
        if (tripRequest.getDestinationPlace() != null) {
            // update estimate ui
            tvEstimation.setText(R.string.click_to_estimate);
            layoutEstimate.setEnabled(true);
        } else {
            // update estimate ui
            tvEstimation.setText(R.string.add_dropoff_to_estimate);
            layoutEstimate.setEnabled(false);
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
                startActivityForResult(new Intent(this, CustomerAddDropOffActivity.class), Const.REQUEST_ADD_DROP_OFF);
                break;

            case R.id.layout_payment_type:
                onChangePaymentType();
                break;

            case R.id.layout_estimate:
                estimateTripFares();
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
     * method, used to send request to google apis to estimate time and distance then estimate fares if success
     */
    private void estimateTripFares() {
        // check internet connection
        if (!Utils.hasConnection(this)) {
            // show error msg
            Utils.showShortToast(this, R.string.no_internet_connection);
            return;
        }

        // show progress dialog
        progressDialog = DialogUtils.showProgressDialog(this, R.string.estimating_your_trip_fares_please_wait);

        // create and send the time and distance estimation request from google
        String origin = tripRequest.getPickupPlace().getLocation().getLat() + "," + tripRequest.getPickupPlace().getLocation().getLng();
        LatLng destination = new LatLng(tripRequest.getDestinationPlace().getLocation().getLat(), tripRequest.getDestinationPlace().getLocation().getLng());
        CommonRequests.getTravelTimeByDistanceMatrixApi(this, this, origin, destination);
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

                // change ui
                if (Utils.isNullOrEmpty(notes)) {
                    tvAddNotes.setText(R.string.notes_to_captain);
                } else {
                    tvAddNotes.setText(R.string.change_notes);
                }
            } else if (requestCode == Const.REQUEST_ADD_DROP_OFF) {
                // get the selected place
                PrivateCarPlace destinationPlace = (PrivateCarPlace) data.getSerializableExtra(Const.KEY_DROP_OFF_PLACE);
                tripRequest.setDestinationPlace(destinationPlace);

                // update trip request object
                tripRequest.setEstimateDistance(0);
                tripRequest.setEstimateFare(0);
                tripRequest.setEstimateTime(0);

                // check the place
                if (destinationPlace == null) {
                    // he will guide the captain
                    // update trip request object
                    tripRequest.setDestinationType(AddressType.LEAD_DRIVER);

                    // update ui
                    tvAddDropoff.setText(R.string.add_dropoff);
                    tvDestinationAddress.setText(R.string.i_will_guide_the_captain);
                    tvEstimation.setText(R.string.add_dropoff_to_estimate);
                    layoutEstimate.setEnabled(false);
                } else {
                    // user has selected a place
                    // update trip request object
                    tripRequest.setDestinationType(AddressType.ADDRESS);

                    // update the ui
                    tvAddDropoff.setText(R.string.change_drop_off);
                    tvDestinationAddress.setText(destinationPlace.getFullAddress());
                    tvEstimation.setText(R.string.click_to_estimate);
                    layoutEstimate.setEnabled(true);
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccess(Object response, String apiName) {
        // check response
        if (response instanceof TripRequestResponse) {
            // this is the request trip message
            // dismiss progress dialog
            progressDialog.dismiss();

            // cast the response
            TripRequestResponse requestResponse = (TripRequestResponse) response;

            // check response
            if (requestResponse.isStatus()) {
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
        } else if (response instanceof DistanceMatrixResponse) {
            // this was the estimation response
            // cast the response
            DistanceMatrixResponse estimationResponse = (DistanceMatrixResponse) response;

            // check response
            if (estimationResponse.isOk()) {
                // get element and check it
                DistanceMatrixElement element = estimationResponse.getRows().get(0).getElements().get(0);
                if (element.isOk()) {
                    // get values
                    float duration = element.getDuration().getValue(); // in seconds
                    float distance = element.getDistance().getValue(); // in meters
                    distance = distance / 1000f; // in kilo meters
                    tripRequest.setEstimateTime(duration);
                    tripRequest.setEstimateDistance(distance);

                    // prepare params
                    User user = AppUtils.getCachedUser(this);
                    String theClass = tripRequest.getCarType().getValue();
                    String pickupTime = DateUtil.convertToString(tripRequest.isPickupNow() ? Calendar.getInstance(Locale.getDefault())
                            : tripRequest.getPickupTime(), "hh:mm:ss");

                    // create and send fares request to the server
                    CustomerRequests.fares(this, this, user.getAccessToken(), theClass, pickupTime);
                } else {
                    // dismiss progress & show error
                    progressDialog.dismiss();
                    Utils.showLongToast(getApplicationContext(), R.string.cant_estimate_fares);
                }
            } else {
                // dismiss progress & show error
                progressDialog.dismiss();
                Utils.showLongToast(getApplicationContext(), R.string.cant_estimate_fares);
            }
        } else if (response instanceof FaresResponse) {
            // this was the fares request
            // dismiss the progress
            progressDialog.dismiss();

            // cast the response
            FaresResponse faresResponse = (FaresResponse) response;

            // check response
            if (faresResponse.isStatus() && !Utils.isNullOrEmpty(faresResponse.getFares())) {
                // get values
                Fare fare = faresResponse.getFares().get(faresResponse.getFares().size() - 1);

                // calc and save the trip fare
                float tripFare = AppUtils.calculateFare(this, tripRequest.getEstimateDistance(), fare.getOpenFare(), fare.getKilometerFare());
                tripRequest.setEstimateFare(tripFare);

                // update the ui
                Log.e("Trip Fare", "" + tripFare);
                String tripFareStr = String.format("%.0f", tripFare);
                tvEstimation.setText(tripFareStr + " " + getString(R.string.currency));
                layoutEstimate.setEnabled(false);
            } else {
                // show error msg
                Utils.showLongToast(getApplicationContext(), R.string.cant_estimate_fares);
            }
        }
    }

    @Override
    public void onFail(String message, String apiName) {
        // dismiss progress dialog if visible
        if (progressDialog != null) progressDialog.dismiss();

        // show and log the message
        Utils.showLongToast(this, R.string.connection_error);
        Log.e("ERROR", message);
    }
}
