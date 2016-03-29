package com.privatecar.privatecar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.utils.Utils;

public class CustomerAddNotesToCaptainActivity extends BasicBackActivity {
    private String details;
    private EditText etDetails;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_add_notes_to_captain);

        // init views
        etDetails = (EditText) findViewById(R.id.et_details);
        btnSave = (Button) findViewById(R.id.btn_save);

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
