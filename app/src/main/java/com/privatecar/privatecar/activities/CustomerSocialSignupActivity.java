package com.privatecar.privatecar.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.adapters.CountryAdapter;
import com.privatecar.privatecar.models.entities.User;
import com.privatecar.privatecar.models.enums.UserType;
import com.privatecar.privatecar.models.responses.AccessTokenResponse;
import com.privatecar.privatecar.models.responses.GeneralResponse;
import com.privatecar.privatecar.requests.CommonRequests;
import com.privatecar.privatecar.requests.CustomerRequests;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.CountriesUtils;
import com.privatecar.privatecar.utils.DialogUtils;
import com.privatecar.privatecar.utils.RequestListener;
import com.privatecar.privatecar.utils.Utils;

public class CustomerSocialSignupActivity extends BasicBackActivity implements RequestListener<Object> {
    private Spinner spinner;
    private EditText etMobile;
    private ProgressDialog progressDialog;

    String firstName, lastName, email, id, token, provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_social_signup);

        Intent intent = getIntent();
        firstName = intent.getStringExtra(Const.KEY_FIRST_NAME);
        lastName = intent.getStringExtra(Const.KEY_LAST_NAME);
        email = intent.getStringExtra(Const.KEY_EMAIL);
        id = intent.getStringExtra(Const.KEY_ID);
        token = intent.getStringExtra(Const.KEY_TOKEN);
        provider = intent.getStringExtra(Const.KEY_PROVIDER);

        if (firstName == null || lastName == null || email == null || id == null || token == null || provider == null) {
            onBackPressed();
            return;
        }

        spinner = (Spinner) findViewById(R.id.spinner_countries);
        CountryAdapter adapter = new CountryAdapter(this);
        spinner.setAdapter(adapter);
        spinner.setSelection(Const.EGYPT_INDEX); //set egypt the default


        etMobile = (EditText) findViewById(R.id.et_mobile);
        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (spinner.getSelectedItemPosition() == Const.EGYPT_INDEX) {
                    if (!Utils.isValidEgyptianMobileNumber("0" + s.toString())) {
                        etMobile.setError(getString(R.string.not_valid_mobile));
                    } else {
                        etMobile.setError(null);
                    }
                }
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sign_up:
                customerSocialSignup();
                break;
        }
    }


    private boolean customerSocialSignupValidation() {
        boolean valid = true;

        if (spinner.getSelectedItemPosition() == Const.EGYPT_INDEX) {
            if (!Utils.isValidEgyptianMobileNumber("0" + Utils.getText(etMobile))) {
                etMobile.setError(getString(R.string.not_valid_mobile));
                valid = false;
            } else {
                etMobile.setError(null);
            }
        }

        return valid;
    }

    private void customerSocialSignup() {
        if (!customerSocialSignupValidation()) return;

        String code = new CountriesUtils().getCountryCodes()[spinner.getSelectedItemPosition()];
        progressDialog = DialogUtils.showProgressDialog(this, R.string.registering, true);
        Utils.hideKeyboard(etMobile);
        CustomerRequests.regCustomerSocial(this, this, firstName, lastName, email, code + Utils.getText(etMobile), provider, id, token);
    }

    @Override
    public void onSuccess(Object response, String apiName) {
        // check response
        if (response instanceof GeneralResponse) {
            // this was the social sign up request
            // cast the response
            GeneralResponse generalResponse = (GeneralResponse) response;

            // check the response
            if (generalResponse.isSuccess()) {
                // successful request
                // send access token request
                CommonRequests.socialLogin(this, this, id, token, provider);
            } else {
                // hide the progress dialog
                progressDialog.dismiss();

                // prepare error msg
                String errorMsg = "";
                if (generalResponse.getValidation() != null) {
                    for (int i = 0; i < generalResponse.getValidation().size(); i++) {
                        if (i == 0)
                            errorMsg += generalResponse.getValidation().get(i);
                        else
                            errorMsg += "\n" + generalResponse.getValidation().get(i);
                    }
                }

                // show the suitable error dialog
                if (errorMsg.isEmpty()) {
                    errorMsg = getString(R.string.unexpected_error_try_again);
                }
                DialogUtils.showAlertDialog(this, errorMsg, null);
            }
        } else {
            // this was the access token request
            // hide the progress dialog
            progressDialog.dismiss();

            // cast the response
            AccessTokenResponse accessTokenResponse = (AccessTokenResponse) response;

            // create and cache new user object
            User user = new User();
            user.setAccessToken(accessTokenResponse.getAccessToken());
            long expiryTimestamp = System.currentTimeMillis() + (accessTokenResponse.getExpiresIn() * 1000);
            user.setExpiryTimestamp(expiryTimestamp);
            user.setType(UserType.CUSTOMER);
            user.setSocialProvider(provider);
            user.setSocialToken(token);
            user.setSocialUserId(id);
            AppUtils.cacheUser(this, user);

            // open customer home activity
            Intent intent = new Intent(this, CustomerHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onFail(String message, String apiName) {
        progressDialog.dismiss();

        Utils.showLongToast(this, message);
        Utils.LogE(message);
    }
}
