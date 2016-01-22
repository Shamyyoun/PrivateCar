package com.privatecar.privatecar.activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.privatecar.privatecar.R;
import com.privatecar.privatecar.models.entities.Config;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.ButtonHighlighterOnTouchListener;

public class AnonymousHomeActivity extends Activity {

    private Button btnOneLift, btnSignIn, btnSignUp;

    private enum Selection {
        CUSTOMER, DRIVER
    }

    Selection selection = Selection.CUSTOMER; //customer is the default selection

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
                String customerServiceNumber = AppUtils.getConfigValue(getApplicationContext(), Config.CUSTOMER_SERVICE_NUMBER_KEY);
                if (customerServiceNumber != null) {
                    showCallDialog(customerServiceNumber);
                }
                break;
            case R.id.btn_sign_in:
                switch (selection) {
                    case CUSTOMER:
                        //TODO: customer sign in
                        break;
                    case DRIVER:
                        //TODO: driver sign in
                        break;
                }
                break;
            case R.id.btn_sign_up:
                switch (selection) {
                    case CUSTOMER:
                        //TODO: customer sign up
                        break;
                    case DRIVER:
                        //TODO: driver sign up
                        break;
                }
                break;
        }
    }

    private void showCallDialog(final String customerServiceNumber) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.call) + ": " + customerServiceNumber);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + customerServiceNumber));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.no, null);
        builder.show();
    }
}
