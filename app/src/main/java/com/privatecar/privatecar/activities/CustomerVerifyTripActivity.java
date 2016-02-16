package com.privatecar.privatecar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.privatecar.privatecar.R;

public class CustomerVerifyTripActivity extends BasicBackActivity implements View.OnClickListener{
    TextView textAddDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_verifty_trip);

        textAddDetails = (TextView) findViewById(R.id.tv_add_details);
        textAddDetails.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add_details:
                // open add details activity
                startActivity(new Intent(this, CustomerAddDetailsActivity.class));

                break;
        }
    }
}
