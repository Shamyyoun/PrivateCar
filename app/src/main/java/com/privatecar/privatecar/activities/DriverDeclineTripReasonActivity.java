package com.privatecar.privatecar.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.models.entities.DriverAccountDetails;
import com.privatecar.privatecar.models.entities.Option;
import com.privatecar.privatecar.models.entities.User;
import com.privatecar.privatecar.models.enums.OptionType;
import com.privatecar.privatecar.models.responses.GeneralResponse;
import com.privatecar.privatecar.models.responses.OptionsResponse;
import com.privatecar.privatecar.requests.CommonRequests;
import com.privatecar.privatecar.requests.DriverRequests;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.DialogUtils;
import com.privatecar.privatecar.utils.RequestListener;
import com.privatecar.privatecar.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DriverDeclineTripReasonActivity extends BasicBackActivity implements RequestListener<Object> {
    private static final int OPTION_OTHER = 99999999;

    private int tripId;
    private View layoutContent;
    private RadioGroup rgOptions;
    private EditText etOther;
    private Button btnSend;
    private List<Option> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_decline_trip_reason);

        // get trip id
        tripId = getIntent().getIntExtra(Const.KEY_TRIP_ID, 0);

        // init views
        layoutContent = findViewById(R.id.layout_content);
        rgOptions = (RadioGroup) findViewById(R.id.rg_options);
        etOther = (EditText) findViewById(R.id.et_other);
        btnSend = (Button) findViewById(R.id.btn_send);

        // add listeners
        rgOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                etOther.setError(null);
            }
        });
        btnSend.setOnClickListener(this);

        // load available options
        loadOptions();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                decline();
                break;

            default:
                super.onClick(v);
        }
    }

    /**
     * method, used to load available options from server
     */
    private void loadOptions() {
        // show loading
        progressDialog = DialogUtils.showProgressDialog(this, R.string.loading_please_wait);

        // create & send the request
        User user = AppUtils.getCachedUser(this);
        CommonRequests.options(this, this, user.getAccessToken(), OptionType.DECLINE_TRIP.getValue());
    }

    /**
     * method, used to send decline request to the server
     */
    private void decline() {
        // check internet connection
        if (!Utils.hasConnection(this)) {
            Utils.showShortToast(this, R.string.no_internet_connection);
            return;
        }

        // hide keyboard
        Utils.hideKeyboard(etOther);

        // get reason id & comment
        int selectedOptionIndex = rgOptions.getCheckedRadioButtonId();
        String reasonId = null;
        String comment = null;
        if (selectedOptionIndex == OPTION_OTHER) {
            // other option is selected
            // must have comment
            if (etOther.getText().toString().trim().isEmpty()) {
                // show error
                etOther.setError(getString(R.string.enter_your_comment));
                return;
            }

            comment = etOther.getText().toString().trim();
        } else {
            reasonId = "" + options.get(selectedOptionIndex).getId();
        }

        // show loading
        progressDialog = DialogUtils.showProgressDialog(this, R.string.loading_please_wait);

        // create & send the request
        User user = AppUtils.getCachedUser(this);
        DriverAccountDetails accountDetails = user.getDriverAccountDetails();
        DriverRequests.declineTrip(this, this, user.getAccessToken(), "" + accountDetails.getId(), "" + tripId,
                "" + accountDetails.getDefaultCarId(), reasonId, comment);
    }

    @Override
    public void onSuccess(Object response, String apiName) {
        // hide loading
        progressDialog.dismiss();

        // check response type
        if (response instanceof OptionsResponse) {
            // this options response
            OptionsResponse optionsResponse = (OptionsResponse) response;

            /*
            // ====DUMMY====
            String res = "{\n" +
                    "\"status\": true,\n" +
                    "\"content\": [\n" +
                    "{ \"id\": 1, \"name\": \"Reason 1\", \"name_ar\": \"Reason 1\" },\n" +
                    "{ \"id\": 1, \"name\": \"Reason 2\", \"name_ar\": \"Reason 2\" },\n" +
                    "{ \"id\": 1, \"name\": \"Reason 2\", \"name_ar\": \"Reason 2\" }\n" +
                    "],\n" +
                    "\"validation\": null\n" +
                    "}";
            Gson gson = new Gson();
            optionsResponse = gson.fromJson(res, OptionsResponse.class);
            */

            // prepare options list & update the ui
            if (optionsResponse.getOptions() == null) {
                options = new ArrayList<>();
            } else {
                options = optionsResponse.getOptions();
            }
            updateUI();
        } else if (response instanceof GeneralResponse) {
            // this was decline trip request
            GeneralResponse generalResponse = (GeneralResponse) response;
            if (generalResponse.isSuccess()) {
                // show msg and finish this & trip request activity
                Utils.showLongToast(this, R.string.trip_declined_successfully);
                DriverTripRequestActivity.currentInstance.finish();
                finish();
            } else {
                // show error msg
                Utils.showLongToast(this, R.string.error_decline_this_trip);
            }
        }
    }

    private void updateUI() {
        // get app language
        String lang = Utils.getAppLanguage();

        // loop to add radio buttons with available  options
        for (int i = 0; i < options.size(); i++) {
            // create new radio button with this option
            Option option = options.get(i);
            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(i);
            radioButton.setText(lang.equals(Locale.ENGLISH.getLanguage()) ? option.getName() : option.getNameAr());

            // add to the radio group
            rgOptions.addView(radioButton);
        }

        // create & add the other button
        final RadioButton radioButton = new RadioButton(this);
        radioButton.setText(R.string.other);
        radioButton.setId(OPTION_OTHER);

        // check options size
        if (options.size() == 0) {
            // check other button
            radioButton.setChecked(true);
        } else {
            // check first button by default
            rgOptions.check(0);
        }

        // add to the radio group
        rgOptions.addView(radioButton);

        // show content layout
        layoutContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFail(String message, String apiName) {
        // hide progress
        progressDialog.dismiss();

        // check the apiName
        if (apiName.equals(Const.MESSAGE_GET_OPTIONS)) {
            // show error toast & finish
            Utils.showLongToast(this, R.string.error_fetching_available_options);
            finish();
        } else {
            // show error toast
            Utils.showLongToast(this, R.string.connection_error);
        }
    }
}
