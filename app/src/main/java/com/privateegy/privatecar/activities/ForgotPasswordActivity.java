package com.privateegy.privatecar.activities;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.privateegy.privatecar.R;
import com.privateegy.privatecar.models.responses.GeneralResponse;
import com.privateegy.privatecar.requests.CommonRequests;
import com.privateegy.privatecar.utils.DialogUtils;
import com.privateegy.privatecar.utils.RequestListener;
import com.privateegy.privatecar.utils.Utils;

/**
 * Created by Shamyyoun on 2/20/2016.
 */
public class ForgotPasswordActivity extends BasicBackActivity implements RequestListener<GeneralResponse> {
    private EditText etEmail;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // init views
        etEmail = (EditText) findViewById(R.id.et_email);
        btnSend = (Button) findViewById(R.id.btn_send);

        // add listeners
        etEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    restorePassword();
                    return true;
                }

                return false;
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restorePassword();
            }
        });
    }

    private void restorePassword() {
        // validate the email address
        if (Utils.isEmpty(etEmail)) {
            etEmail.setError(getString(R.string.enter_your_email_address));
            return;
        }
        if (!Utils.isValidEmail(Utils.getText(etEmail))) {
            etEmail.setError(getString(R.string.not_valid_email));
            return;
        }

        // hide the keyboard
        Utils.hideKeyboard(etEmail);

        // check internet connection
        if (!Utils.hasConnection(this)) {
            // show error toast
            Utils.showShortToast(this, R.string.no_internet_connection);
            return;
        }

        // show progress dialog
        progressDialog = DialogUtils.showProgressDialog(this, R.string.loading_please_wait);

        // create the request & send it
        CommonRequests.forgetPassword(this, this, Utils.getText(etEmail));
    }

    @Override
    public void onSuccess(GeneralResponse response, String apiName) {
        // dismiss progress
        progressDialog.dismiss();

        // check response
        if (response.isSuccess()) {
            // show success toast & finish
            Utils.showLongToast(this, R.string.please_check_your_inbox_and_follow_instructions);
            finish();
        } else {
            // show error toast
            Utils.showLongToast(this, R.string.please_enter_your_email_used_in_registration);
        }
    }

    @Override
    public void onFail(String message, String apiName) {
        // dismiss progress & show error toast
        progressDialog.dismiss();
        Utils.showLongToast(this, R.string.connection_error);
    }
}
