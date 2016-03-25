package com.privatecar.privatecar.activities;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.privatecar.privatecar.R;
import com.privatecar.privatecar.models.entities.User;
import com.privatecar.privatecar.models.responses.GeneralResponse;
import com.privatecar.privatecar.models.responses.PromoCodeResponse;
import com.privatecar.privatecar.requests.CustomerRequests;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.DialogUtils;
import com.privatecar.privatecar.utils.RequestListener;
import com.privatecar.privatecar.utils.Utils;

public class CustomerAddPromoCodeActivity extends BasicBackActivity implements RequestListener<PromoCodeResponse>{
    private EditText etPromoCode;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_add_promo_code);

        // init views
        etPromoCode = (EditText) findViewById(R.id.et_promo_code);
        btnSave = (Button) findViewById(R.id.btn_save);

        // add listeners
        etPromoCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addPromoCode();
                    return true;
                }
                return false;
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPromoCode();
            }
        });
    }

    /**
     * method, used to validate and send promo code to the server
     */
    private void addPromoCode() {
        // validate promo code
        if (Utils.isEmpty(etPromoCode)) {
            // show error
            etPromoCode.setError(getString(R.string.required));
            return;
        }

        // hide keyboard
        Utils.hideKeyboard(etPromoCode);

        // check internet connection
        if (!Utils.hasConnection(this)) {
            // show error msg
            Utils.showShortToast(this, R.string.no_internet_connection);
            return;
        }

        // show progress dialog
        progressDialog = DialogUtils.showProgressDialog(this, R.string.activating_promo_code_please_wait, false);

        // create and send the request
        User user = AppUtils.getCachedUser(this);
        CustomerRequests.activatePromoCodde(this, this, user.getAccessToken(), user.getCustomerAccountDetails().getId(), Utils.getText(etPromoCode));
    }

    @Override
    public void onSuccess(PromoCodeResponse response, String apiName) {
        // hide progress dialog
        progressDialog.dismiss();

        // check response
        if (response.isSuccess()) {
            // show success msg & finish
            Utils.showLongToast(this, R.string.promo_code_activated_successfully);
            finish();
        } else {
            // prepare error msg
            String errorMsg = response.getValidation();
            if (Utils.isNullOrEmpty(errorMsg)) {
                errorMsg = getString(R.string.error_activating_promo_code);
            }

            // show error msg
            Utils.showLongToast(this, errorMsg);
        }
    }

    @Override
    public void onFail(String message, String apiName) {
        // hide progress dialog & show error msg
        progressDialog.dismiss();
        Utils.showLongToast(this, R.string.connection_error);
        Utils.LogE("ERROR: " + message);
    }
}
