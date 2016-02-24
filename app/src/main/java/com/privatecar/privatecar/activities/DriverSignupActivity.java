package com.privatecar.privatecar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.privatecar.privatecar.R;
import com.privatecar.privatecar.adapters.CountryAdapter;

public class DriverSignupActivity extends BasicBackActivity {

    private Button btnSignUp;
    EditText etFirstName, etLastName, etMobile, etEmail;
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
