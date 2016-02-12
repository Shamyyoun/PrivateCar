package com.privatecar.privatecar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.privatecar.privatecar.R;
import com.privatecar.privatecar.utils.ButtonHighlighterOnTouchListener;

public class DriverSigninActivity extends BasicBackActivity {

    private Button btnSignIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_signin);


        btnSignIn = (Button) findViewById(R.id.btn_sign_in);
        btnSignIn.setOnTouchListener(new ButtonHighlighterOnTouchListener(this, R.drawable.petroleum_rounded_corners_shape));

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sign_in:
                startActivity(new Intent(this, DriverHomeActivity.class));
                break;
            case R.id.btn_forgot_password:

                break;
        }
    }
}
