package com.privatecar.privatecar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.models.entities.CustomerTripRequest;
import com.privatecar.privatecar.models.entities.TripCostInfo;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.SavePrefs;

public class CustomerAccountPaymentActivity extends BaseActivity implements View.OnClickListener {
    private CustomerTripRequest tripRequest;
    private TripCostInfo tripCostInfo;
    private TextView tvRideNo;
    private TextView tvCurrentStatement;
    private TextView tvCost;
    private TextView tvCurrentBalance;
    private Button buttonConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_account_payment);

        // get trip request info and trip cost info
        tripRequest = AppUtils.getLastCachedTripRequest(this);
        tripCostInfo = (TripCostInfo) getIntent().getSerializableExtra(Const.KEY_TRIP_COST_INFO);

        // init views
        tvRideNo = (TextView) findViewById(R.id.tv_ride_no);
        tvCurrentStatement = (TextView) findViewById(R.id.tv_current_statement);
        tvCost = (TextView) findViewById(R.id.tv_cost);
        tvCurrentBalance = (TextView) findViewById(R.id.tv_current_balance);
        buttonConfirm = (Button) findViewById(R.id.btn_confirm);

        // set data
        tvRideNo.setText(tripRequest.getCode());
        tvCurrentStatement.setText(String.format("%.0f", tripCostInfo.getAvalCredit()));
        tvCost.setText(String.format("%.0f", tripCostInfo.getActualFare()));
        tvCurrentBalance.setText(String.format("%.0f", tripCostInfo.getCurrentCredit()));

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
