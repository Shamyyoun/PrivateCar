package com.privatecar.privatecar.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.models.entities.User;
import com.privatecar.privatecar.models.enums.UserType;
import com.privatecar.privatecar.models.responses.AccessTokenResponse;
import com.privatecar.privatecar.requests.CommonRequests;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.DialogUtils;
import com.privatecar.privatecar.utils.RequestListener;
import com.privatecar.privatecar.utils.Utils;

public class DriverSigninActivity extends BasicBackActivity implements View.OnClickListener, RequestListener<AccessTokenResponse> {
    private EditText etUsername, etPassword;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_signin);

        // init edit texts
        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);

        // add done click listener to password edit text
        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    signIn();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sign_in:
                signIn();
                break;
            case R.id.btn_forgot_password:
                // open forget password activity
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                break;
        }
    }

    /**
     * method, used to validate inputs then send sign in request to server
     */
    private void signIn() {
        // get inputs
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // validate inputs
        if (username.isEmpty()) {
            etUsername.setError(getString(R.string.required));
            return;
        }
        if (password.isEmpty()) {
            etPassword.setError(getString(R.string.required));
            return;
        }

        // hide keyboard
        Utils.hideKeyboard(etUsername);

        // check internet connection
        if (!Utils.hasConnection(this)) {
            Utils.showShortToast(this, R.string.no_internet_connection);
            return;
        }

        // show progress dialog
        progressDialog = DialogUtils.showProgressDialog(this, R.string.loading_please_wait);

        // all things is alright
        // send sign in request
        CommonRequests.normalLogin(this, this, username, password);
    }

    @Override
    public void onSuccess(AccessTokenResponse response, String apiName) {
        // dismiss progress dialog
        progressDialog.dismiss();

        // validate response
        if (response != null) {
            // check response
            if (response.getAccessToken() != null && !response.getAccessToken().isEmpty()) {
                // success
                // cache response
                User user = new User();
                user.setType(UserType.DRIVER);
                user.setUserName(Utils.getText(etUsername));
                user.setPassword(Utils.getText(etPassword));
                user.setAccessToken(response.getAccessToken());
                int expiryIn = response.getExpiresIn() * 1000; //in melli seconds
                user.setExpiryTimestamp(System.currentTimeMillis() + expiryIn);
                AppUtils.cacheUser(this, user);

                // goto home activity
                Intent intent = new Intent(this, DriverHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                // failed
                // prepare error msg
                String errorMsg = "";
                if (response.getValidation().size() == 0) {
                    errorMsg = getString(R.string.invalid_credentials);
                } else {
                    for (int i = 0; i < response.getValidation().size(); i++) {
                        if (i != 0) {
                            errorMsg += "\n";
                        }
                        errorMsg += response.getValidation().get(i);
                    }
                }

                // show the error msg
                Utils.showLongToast(this, errorMsg);
            }
        } else {
            // show error dialog
            Utils.showLongToast(this, R.string.unexpected_error_try_again);
        }
    }

    @Override
    public void onFail(String message, String apiName) {
        // dismiss progress dialog
        progressDialog.dismiss();

        // show error
        Utils.showLongToast(this, message);
        Log.e(Const.LOG_TAG, message);
    }
}
