package com.privateegy.privatecar.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.privateegy.privatecar.Const;
import com.privateegy.privatecar.R;
import com.privateegy.privatecar.models.entities.User;
import com.privateegy.privatecar.models.enums.UserType;
import com.privateegy.privatecar.models.responses.AccessTokenResponse;
import com.privateegy.privatecar.requests.CommonRequests;
import com.privateegy.privatecar.utils.AppUtils;
import com.privateegy.privatecar.utils.DialogUtils;
import com.privateegy.privatecar.utils.RequestListener;
import com.privateegy.privatecar.utils.Utils;

public class DriverSigninActivity extends BasicBackActivity implements View.OnClickListener, RequestListener<Object> {
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
    public void onSuccess(Object response, String apiName) {
        // dismiss progress dialog
        progressDialog.dismiss();

        // cast the response
        AccessTokenResponse accessTokenResponse = (AccessTokenResponse) response;

        // validate response
        if (accessTokenResponse != null) {
            // check response
            if (accessTokenResponse.getAccessToken() != null && !accessTokenResponse.getAccessToken().isEmpty()) {
                // success
                // cache response
                User user = new User();
                user.setType(UserType.DRIVER);
                user.setUserName(Utils.getText(etUsername));
                user.setPassword(Utils.getText(etPassword));
                user.setAccessToken(accessTokenResponse.getAccessToken());
                int expiryIn = accessTokenResponse.getExpiresIn() * 1000; //in melli seconds
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
                if (accessTokenResponse.getValidation().size() == 0) {
                    errorMsg = getString(R.string.invalid_credentials);
                } else {
                    for (int i = 0; i < accessTokenResponse.getValidation().size(); i++) {
                        if (i != 0) {
                            errorMsg += "\n";
                        }
                        errorMsg += accessTokenResponse.getValidation().get(i);
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
