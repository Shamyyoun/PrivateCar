package com.privatecar.privatecar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.models.entities.CustomerTripRequest;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.SavePrefs;

public class CustomerCashPaymentActivity extends BaseActivity implements View.OnClickListener {
    private CustomerTripRequest tripRequest;
    private float cost;
    private TextView tvRideNo;
    private TextView tvCost;
    private Button buttonConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_cash_payment);

        // get trip request and cost
        tripRequest = AppUtils.getLastCachedTripRequest(this);
        cost = getIntent().getFloatExtra(Const.KEY_COST, 0);

        // init views
        tvRideNo = (TextView) findViewById(R.id.tv_ride_no);
        tvCost = (TextView) findViewById(R.id.tv_cost);
        buttonConfirm = (Button) findViewById(R.id.btn_confirm);

        // set data
        tvRideNo.setText(tripRequest.getCode());
        String costStr = String.format("%.0f", cost);
        tvCost.setText(costStr + " " + getString(R.string.currency));

        // add click listeners
        buttonConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                // start new customer receipt activity
                Intent intent = new Intent(this, CustomerReceiptActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }
}
