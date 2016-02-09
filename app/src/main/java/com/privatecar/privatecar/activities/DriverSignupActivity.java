package com.privatecar.privatecar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.privatecar.privatecar.R;
import com.privatecar.privatecar.adapters.CountryAdapter;
import com.privatecar.privatecar.utils.ButtonHighlighterOnTouchListener;

public class DriverSignupActivity extends BasicBackActivity {

    private Button btnSignUp;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_signup);

        spinner = (Spinner) findViewById(R.id.spinner_countries);
        CountryAdapter adapter = new CountryAdapter(this);
        spinner.setAdapter(adapter);
        spinner.setSelection(61); //set egypt the default

        btnSignUp = (Button) findViewById(R.id.btn_sign_up);
        btnSignUp.setOnTouchListener(new ButtonHighlighterOnTouchListener(this, R.drawable.petroleum_rounded_corners_shape));
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sign_up:
                startActivity(new Intent(this, DriverSignupConfirmationActivity.class));
                finish();
                break;
        }
    }
}
