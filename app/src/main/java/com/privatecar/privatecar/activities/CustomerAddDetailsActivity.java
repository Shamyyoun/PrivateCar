package com.privatecar.privatecar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.models.entities.PrivateCarPlace;
import com.privatecar.privatecar.utils.Utils;

public class CustomerAddDetailsActivity extends BasicBackActivity {
    private PrivateCarPlace pickupPlace;
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
        tvPickupAddress.setText(pickupPlace.getAddress());

        // add save click listener
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set the result with details text and finish
                String details = Utils.getText(etDetails);
                Intent intent = new Intent();
                intent.putExtra(Const.KEY_DETAILS, details);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
