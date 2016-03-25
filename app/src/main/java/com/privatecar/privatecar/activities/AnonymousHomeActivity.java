package com.privatecar.privatecar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.privatecar.privatecar.R;
import com.privatecar.privatecar.models.entities.Config;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.ButtonHighlighterOnTouchListener;

public class AnonymousHomeActivity extends BaseActivity {

    Selection selection = Selection.CUSTOMER; //customer is the default selection
    private Button btnOneLift, btnSignIn, btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anonymous_home);

        RadioGroup rgTogglePerson = (RadioGroup) findViewById(R.id.rg_toggle_person);
        rgTogglePerson.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_customer:
                        selection = Selection.CUSTOMER;
                        btnOneLift.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb_driver:
                        selection = Selection.DRIVER;
                        btnOneLift.setVisibility(View.GONE);
                        break;
                }
            }
        });

        btnOneLift = (Button) findViewById(R.id.btn_one_lift);
        btnSignIn = (Button) findViewById(R.id.btn_sign_in);
        btnSignUp = (Button) findViewById(R.id.btn_sign_up);

        btnOneLift.setOnTouchListener(new ButtonHighlighterOnTouchListener(this, R.drawable.onelift_btn_bg));
        btnSignIn.setOnTouchListener(new ButtonHighlighterOnTouchListener(this, R.drawable.yellowish_start_rounded_corners_shape));
        btnSignUp.setOnTouchListener(new ButtonHighlighterOnTouchListener(this, R.drawable.petroleum_end_rounded_corners_shape));

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_one_lift:
                String customerServiceNumber = AppUtils.getConfigValue(getApplicationContext(), Config.KEY_CUSTOMER_SERVICE_NUMBER);
                if (customerServiceNumber != null) {
                    AppUtils.showCallCustomerServiceDialog(this);
                }
                break;
            case R.id.btn_sign_in:
                switch (selection) {
                    case CUSTOMER:
                        startActivity(new Intent(getApplicationContext(), CustomerSignInActivity.class));
                        break;
                    case DRIVER:
                        startActivity(new Intent(getApplicationContext(), DriverSigninActivity.class));
                        break;
                }
                break;
            case R.id.btn_sign_up:
                switch (selection) {
                    case CUSTOMER:
                        startActivity(new Intent(getApplicationContext(), CustomerSignupActivity.class));
                        break;
                    case DRIVER:
                        startActivity(new Intent(getApplicationContext(), DriverSignupActivity.class));
                        break;
                }
                break;
        }
    }

    private enum Selection {
        CUSTOMER, DRIVER
    }
}
