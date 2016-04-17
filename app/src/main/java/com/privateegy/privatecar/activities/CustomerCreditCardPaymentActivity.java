package com.privateegy.privatecar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.privateegy.privatecar.R;

public class CustomerCreditCardPaymentActivity extends BasicBackActivity implements View.OnClickListener {
    Button buttonConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_credit_card_payment);

        // customize confirm button
        buttonConfirm = (Button) findViewById(R.id.btn_confirm);
        buttonConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                // open receipt activity
                startActivity(new Intent(this, CustomerReceiptActivity.class));
                break;
        }
    }
}
