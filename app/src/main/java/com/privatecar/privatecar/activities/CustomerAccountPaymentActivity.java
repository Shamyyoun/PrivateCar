package com.privatecar.privatecar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.privatecar.privatecar.R;

public class CustomerAccountPaymentActivity extends BasicBackActivity implements View.OnClickListener {
    Button buttonConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_account_payment);

        // customize confirm button
        buttonConfirm = (Button) findViewById(R.id.btn_confirm);
        buttonConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                // open credit card payment activity
                startActivity(new Intent(this, CustomerCreditCardPaymentActivity.class));
                break;
        }
    }
}
