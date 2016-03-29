package com.privatecar.privatecar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.models.entities.User;
import com.privatecar.privatecar.models.enums.UserType;
import com.privatecar.privatecar.models.responses.AccessTokenResponse;
import com.privatecar.privatecar.requests.CustomerRequests;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.ButtonHighlighterOnTouchListener;
import com.privatecar.privatecar.utils.DialogUtils;
import com.privatecar.privatecar.utils.RequestListener;
import com.privatecar.privatecar.utils.Utils;

public class UserVerificationActivity extends BasicBackActivity implements RequestListener<AccessTokenResponse> {
    private String email;
    private String password;
    private EditText etVerificationCode;
    private Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_verification);

        // get passed params
        email = getIntent().getStringExtra(Const.KEY_EMAIL);
        password = getIntent().getStringExtra(Const.KEY_PASSWORD);

        // init views
        etVerificationCode = (EditText) findViewById(R.id.et_verification_code);
        btnDone = (Button) findViewById(R.id.btn_done);
        btnDone.setOnTouchListener(new ButtonHighlighterOnTouchListener(this, R.drawable.petroleum_rounded_corners_shape));

        // add done click listener to password edit text
        etVerificationCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    verify();
                    return true;
                }
                return false;
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_done:
                verify();
                break;
        }
    }

    private void verify() {
        // validate code
        if (Utils.isEmpty(etVerificationCode)) {
            etVerificationCode.setError(getString(R.string.required));
            return;
        }

        // hide the keyboard
        Utils.hideKeyboard(etVerificationCode);

        // show progress dialog
        progressDialog = DialogUtils.showProgressDialog(this, R.string.verifying_your_account_please_wait);

        // create & send the request
        CustomerRequests.verifyUser(this, this, email, Utils.getText(etVerificationCode));
    }

    @Override
    public void onSuccess(AccessTokenResponse response, String apiName) {
        // hide the progress dialog
        progressDialog.dismiss();

        // check the access token
        if (!Utils.isNullOrEmpty(response.getAccessToken())) {
            // successful request
            // create and cache new user object
            User user = new User();
            user.setAccessToken(response.getAccessToken());
            long expiryTimestamp = System.currentTimeMillis() + (response.getExpiresIn() * 1000);
            user.setExpiryTimestamp(expiryTimestamp);
            user.setType(UserType.CUSTOMER);
            user.setUserName(email);
            user.setPassword(password);
            AppUtils.cacheUser(this, user);

            // open customer home activity
            Intent intent = new Intent(this, CustomerHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } else {
            // prepare error msg
            String errorMsg = "";
            if (response.getValidation() != null) {
                for (int i = 0; i < response.getValidation().size(); i++) {
                    if (i == 0)
                        errorMsg += response.getValidation().get(i);
                    else
                        errorMsg += "\n" + response.getValidation().get(i);
                }
            }

            // show the suitable error dialog
            if (errorMsg.isEmpty()) {
                errorMsg = getString(R.string.unexpected_error_try_again);
            }
            DialogUtils.showAlertDialog(this, errorMsg, null);
        }
    }

    @Override
    public void onFail(String message, String apiName) {
        progressDialog.dismiss();

        Utils.showLongToast(this, message);
        Utils.LogE(message);
    }
}
