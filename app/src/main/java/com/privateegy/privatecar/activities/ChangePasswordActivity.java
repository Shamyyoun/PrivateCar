package com.privateegy.privatecar.activities;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.privateegy.privatecar.R;
import com.privateegy.privatecar.models.entities.User;
import com.privateegy.privatecar.models.responses.GeneralResponse;
import com.privateegy.privatecar.requests.CommonRequests;
import com.privateegy.privatecar.utils.AppUtils;
import com.privateegy.privatecar.utils.DialogUtils;
import com.privateegy.privatecar.utils.RequestListener;
import com.privateegy.privatecar.utils.Utils;

/**
 * Created by Shamyyoun on 2/20/2016.
 */
public class ChangePasswordActivity extends BasicBackActivity implements RequestListener<GeneralResponse> {
    private User user;
    private EditText etCurrentPassword;
    private EditText etNewPassword;
    private EditText etRePassword;
    private Button btnSave;
    private String newPassword; // used to hold the new password to update it in cached after successful request

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // get cached user
        user = AppUtils.getCachedUser(this);

        // init views
        etCurrentPassword = (EditText) findViewById(R.id.et_current_password);
        etNewPassword = (EditText) findViewById(R.id.et_new_password);
        etRePassword = (EditText) findViewById(R.id.et_re_password);
        btnSave = (Button) findViewById(R.id.btn_save);

        // add listeners
        etRePassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    changePassword();
                    return true;
                }

                return false;
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    private void changePassword() {
        // get strings from edit texts
        String currentPasswordText = etCurrentPassword.getText().toString().trim();
        newPassword = etNewPassword.getText().toString().trim();
        String rePasswordText = etRePassword.getText().toString().trim();

        // validate inputs
        // check current password
        if (currentPasswordText.isEmpty()) {
            etCurrentPassword.setError(getString(R.string.enter_your_current_password));
            return;
        }
        if (!currentPasswordText.equals(user.getPassword())) {
            etCurrentPassword.setError(getString(R.string.your_current_password_isnt_correct));
            return;
        }

        // check new password
        if (newPassword.isEmpty()) {
            etNewPassword.setError(getString(R.string.enter_the_new_password));
            return;
        }
        if (newPassword.length() < 3 || newPassword.length() > 32) {
            etNewPassword.setError(getString(R.string.password_length_error));
            return;
        }
        if (!newPassword.equals(rePasswordText)) {
            etRePassword.setError(getString(R.string.passwords_doesnt_match));
            return;
        }

        // hide the keyboard
        Utils.hideKeyboard(etRePassword);

        // check internet connection
        if (!Utils.hasConnection(this)) {
            // show error toast
            Utils.showShortToast(this, R.string.no_internet_connection);
            return;
        }

        // show progress dialog
        progressDialog = DialogUtils.showProgressDialog(this, R.string.changing_password_please_wait);

        // create the request & send it
        CommonRequests.changePassword(this, this, user.getAccessToken(), currentPasswordText, newPassword);
    }

    @Override
    public void onSuccess(GeneralResponse response, String apiName) {
        // dismiss progress
        progressDialog.dismiss();

        // check response
        if (response.isSuccess()) {
            // updated cached user
            user.setPassword(newPassword);
            AppUtils.cacheUser(this, user);

            // show success toast & finish
            Utils.showLongToast(this, R.string.your_password_changed_successfully);
            finish();
        } else {
            // show error toast
            Utils.showLongToast(this, R.string.error_while_changing_your_password);
        }
    }

    @Override
    public void onFail(String message, String apiName) {
        // dismiss progress & show error toast
        progressDialog.dismiss();
        Utils.showLongToast(this, R.string.connection_error);
    }
}
