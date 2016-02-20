package com.privatecar.privatecar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.privatecar.privatecar.R;
import com.privatecar.privatecar.dialogs.MessageDialog;

public class CustomerRateActivity extends BasicBackActivity implements View.OnClickListener {
    Button buttonRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_rate_trip);

        // customize confirm button
        buttonRate = (Button) findViewById(R.id.btn_rate);
        buttonRate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_rate:
                // show message dialog
                new MessageDialog(this)
                        .setDialogTitle(R.string.thank_you)
                        .setMessage("Thanks Hossam Eleraky for using Private app and have a nice day.")
                        .setPositiveButtonText(R.string.ok)
                        .show();
                break;
        }
    }
}
