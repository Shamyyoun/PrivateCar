package com.privatecar.privatecar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.privatecar.privatecar.R;

public class CustomerReceiptActivity extends BasicBackActivity implements View.OnClickListener {
    Button buttonOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_receipt);

        // customize ok button
        buttonOk = (Button) findViewById(R.id.btn_ok);
        buttonOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                // open rate activity
                startActivity(new Intent(this, CustomerRateActivity.class));
                break;
        }
    }
}
