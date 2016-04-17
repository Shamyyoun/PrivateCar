package com.privateegy.privatecar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.privateegy.privatecar.Const;
import com.privateegy.privatecar.R;
import com.privateegy.privatecar.models.entities.PrivateCarPlace;
import com.privateegy.privatecar.utils.Utils;

public class CustomerAddDetailsActivity extends BasicBackActivity {
    private PrivateCarPlace pickupPlace;
    private String details;
    private TextView tvPickupAddress;
    private EditText etDetails;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_add_details);

        // get passed pickup place
        pickupPlace = (PrivateCarPlace) getIntent().getSerializableExtra(Const.KEY_PICKUP_PLACE);

        // init views
        tvPickupAddress = (TextView) findViewById(R.id.tv_pickup_address);
        etDetails = (EditText) findViewById(R.id.et_details);
        btnSave = (Button) findViewById(R.id.btn_save);

        // set the pickup address
        String address = pickupPlace.getName();
        if (!Utils.isNullOrEmpty(pickupPlace.getAddress())) {
            address += " - " + pickupPlace.getAddress();
        }
        tvPickupAddress.setText(address);

        // set the details
        details = getIntent().getStringExtra(Const.KEY_DETAILS);
        etDetails.setText(details);
        if (!Utils.isNullOrEmpty(details)) {
            etDetails.setSelection(details.length());
        }

        // add save click listener
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set the result with details text and finish
                details = Utils.getText(etDetails);
                Intent intent = new Intent();
                intent.putExtra(Const.KEY_DETAILS, details);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
