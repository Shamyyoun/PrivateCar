package com.privatecar.privatecar.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.models.entities.Config;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.ButtonHighlighterOnTouchListener;
import com.privatecar.privatecar.utils.PermissionUtil;
import com.privatecar.privatecar.utils.Utils;

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
                    // check call permission
                    if (PermissionUtil.isGranted(this, Manifest.permission.CALL_PHONE)) {
                        // show call customer service dialog
                        AppUtils.showCallCustomerServiceDialog(this);
                    } else {
                        // not granted
                        // request the permission
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, Const.PERM_REQ_CALL);
                    }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Const.PERM_REQ_CALL:
                // check if granted
                if (PermissionUtil.isAllGranted(grantResults)) {
                    // granted
                    // show call customer service dialog
                    AppUtils.showCallCustomerServiceDialog(this);
                } else {
                    // show msg
                    Utils.showShortToast(this, R.string.we_need_call_permission_to_call_customer_service);
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
