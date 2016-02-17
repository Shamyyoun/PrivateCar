package com.privatecar.privatecar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.privatecar.privatecar.R;
import com.privatecar.privatecar.dialogs.CustomerCarTypeDialog;
import com.privatecar.privatecar.dialogs.CustomerPickupTimeDialog;

public class CustomerVerifyTripActivity extends BasicBackActivity implements View.OnClickListener {
    TextView textAddDetails;
    TextView textAddDropoff;
    RadioGroup groupPickupTime;
    TextView textCarType;
    TextView textPromoCode;

    CustomerPickupTimeDialog pickupTimeDialog;
    Selection selection = Selection.NOW; // now is the default selection
    CustomerCarTypeDialog carTypeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_verifty_trip);

        // customize add details textview
        textAddDetails = (TextView) findViewById(R.id.tv_add_details);
        textAddDetails.setOnClickListener(this);

        // customize add dropoff textview
        textAddDropoff = (TextView) findViewById(R.id.tv_add_dropoff);
        textAddDropoff.setOnClickListener(this);

        // customize picker time radio group
        groupPickupTime = (RadioGroup) findViewById(R.id.rg_pickup_time);
        groupPickupTime.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_now:
                        selection = Selection.NOW;
                        break;
                    case R.id.rb_later:
                        selection = Selection.LATER;
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

        // customize car type textview
        textCarType = (TextView) findViewById(R.id.tv_car_type);
        textCarType.setOnClickListener(this);

        // customize promo code textview
        textPromoCode = (TextView) findViewById(R.id.tv_promo_code);
        textPromoCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add_details:
                // open add details activity
                startActivity(new Intent(this, CustomerAddDetailsActivity.class));
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

            case R.id.tv_promo_code:
                // open add promo code activity
                startActivity(new Intent(this, CustomerAddPromoCodeActivity.class));
                break;
        }
    }

    private enum Selection {
        NOW, LATER
    }
}
