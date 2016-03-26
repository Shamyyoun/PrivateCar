package com.privatecar.privatecar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.dialogs.CustomerCarTypeDialog;
import com.privatecar.privatecar.dialogs.CustomerFullDayCarTypeDialog;
import com.privatecar.privatecar.dialogs.CustomerPickupTimeDialog;
import com.privatecar.privatecar.models.entities.PrivateCarPlace;
import com.privatecar.privatecar.models.entities.TripRequest;
import com.privatecar.privatecar.utils.Utils;

public class CustomerVerifyTripActivity extends BasicBackActivity implements View.OnClickListener {
    private boolean now; // boolean indicate now or later
    private PrivateCarPlace pickupPlace; // pickup place
    private TextView tvAddDetails;
    private TextView tvAddDropoff;
    private RadioGroup rgPickupTime;
    private TextView tvCarType;
    private TextView tvFullDay;
    private TextView tvPromoCode;
    private Button btnGo;

    private CustomerPickupTimeDialog pickupTimeDialog;
    private CustomerCarTypeDialog carTypeDialog;
    private CustomerFullDayCarTypeDialog fullDayCarTypeDialog;

    private TripRequest tripRequest; // used to save the request parameters to be send to sever

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_verifty_trip);

        // get passed params
        now = getIntent().getBooleanExtra(Const.KEY_NOW, false);
        pickupPlace = (PrivateCarPlace) getIntent().getSerializableExtra(Const.KEY_PICKUP_PLACE);

        // init views
        tvAddDetails = (TextView) findViewById(R.id.tv_add_details);
        tvAddDropoff = (TextView) findViewById(R.id.tv_add_dropoff);
        rgPickupTime = (RadioGroup) findViewById(R.id.rg_pickup_time);
        tvCarType = (TextView) findViewById(R.id.tv_car_type);
        tvFullDay = (TextView) findViewById(R.id.tv_full_day);
        tvPromoCode = (TextView) findViewById(R.id.tv_promo_code);
        btnGo = (Button) findViewById(R.id.btn_go);

        // create the trip request object
        tripRequest = new TripRequest();

        // add check change listener to pickup time rg
        rgPickupTime.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_now:
                        break;
                    case R.id.rb_later:
                        // check to create new dialog
                        if (pickupTimeDialog == null) {
                            pickupTimeDialog = new CustomerPickupTimeDialog(CustomerVerifyTripActivity.this);
                        }

                        // show select time dialog
                        pickupTimeDialog.show();
                        break;
                }
            }
        });

        //  add click listeners
        tvAddDropoff.setOnClickListener(this);
        tvAddDetails.setOnClickListener(this);
        tvCarType.setOnClickListener(this);
        tvFullDay.setOnClickListener(this);
        tvPromoCode.setOnClickListener(this);
        btnGo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add_details:
                // open add details activity with pickup place
                Intent detailsIntent = new Intent(this, CustomerAddDetailsActivity.class);
                detailsIntent.putExtra(Const.KEY_PICKUP_PLACE, pickupPlace);
                startActivityForResult(detailsIntent, Const.REQUEST_ADD_DETAILS);
                break;

            case R.id.tv_add_dropoff:
                // open add dropoff activity
                startActivity(new Intent(this, CustomerAddDropoffActivity.class));
                break;

            case R.id.tv_car_type:
                // check car type dialog
                if (carTypeDialog == null) {
                    carTypeDialog = new CustomerCarTypeDialog(this);
                }

                // show it
                carTypeDialog.show();
                break;

            case R.id.tv_full_day:
                // check full day car type dialog
                if (fullDayCarTypeDialog == null) {
                    fullDayCarTypeDialog = new CustomerFullDayCarTypeDialog(this);
                }

                // show it
                fullDayCarTypeDialog.show();
                break;

            case R.id.tv_promo_code:
                // open add promo code activity
                startActivity(new Intent(this, CustomerAddPromoCodeActivity.class));
                break;

            case R.id.btn_go:
                // open ride activity
                startActivity(new Intent(this, CustomerRideActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check result code
        if (resultCode == RESULT_OK) {
            // check request code
            if (requestCode == Const.REQUEST_ADD_DETAILS) {
                // save details text
                String details = data.getStringExtra(Const.KEY_DETAILS);
                tripRequest.setPickNotes(details);

                // change ui
                if (Utils.isNullOrEmpty(details)) {
                    tvAddDetails.setText(R.string.add_details);
                } else {
                    tvAddDetails.setText(R.string.change_details);
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
