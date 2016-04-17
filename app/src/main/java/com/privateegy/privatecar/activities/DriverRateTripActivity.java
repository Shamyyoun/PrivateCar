package com.privateegy.privatecar.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.privateegy.privatecar.Const;
import com.privateegy.privatecar.R;
import com.privateegy.privatecar.models.entities.DriverTripRequest;
import com.privateegy.privatecar.models.entities.Option;
import com.privateegy.privatecar.models.entities.TripInfo;
import com.privateegy.privatecar.models.entities.User;
import com.privateegy.privatecar.models.enums.OptionType;
import com.privateegy.privatecar.models.responses.GeneralResponse;
import com.privateegy.privatecar.models.responses.OptionsResponse;
import com.privateegy.privatecar.requests.CommonRequests;
import com.privateegy.privatecar.requests.DriverRequests;
import com.privateegy.privatecar.utils.AppUtils;
import com.privateegy.privatecar.utils.DialogUtils;
import com.privateegy.privatecar.utils.RequestListener;
import com.privateegy.privatecar.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DriverRateTripActivity extends BaseActivity implements RequestListener {
    private static final int OPTION_OTHER = 99999999;

    private TripInfo tripInfo;
    private DriverTripRequest tripRequest;
    private User user;
    private TextView tvRideNo;
    private TextView tvClientId;
    private RatingBar ratingBar;
    private View layoutOptions;
    private RadioGroup rgOptions;
    private EditText etOther;
    private Button btnRate;
    private List<Option> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_rate_trip);

        // get main objects
        tripRequest = (DriverTripRequest) getIntent().getSerializableExtra(Const.KEY_TRIP_REQUEST);
        tripInfo = AppUtils.getLastCachedTripInfo(this);
        user = AppUtils.getCachedUser(this);

        // init views
        tvRideNo = (TextView) findViewById(R.id.tv_ride_no);
        tvClientId = (TextView) findViewById(R.id.tv_client_id);
        ratingBar = (RatingBar) findViewById(R.id.rating_bar);
        layoutOptions = findViewById(R.id.layout_options);
        rgOptions = (RadioGroup) findViewById(R.id.rg_options);
        etOther = (EditText) findViewById(R.id.et_other);
        btnRate = (Button) findViewById(R.id.btn_rate);

        // set header info
        tvRideNo.setText(tripRequest.getCode());
//        tvClientId.setText("" + tripRequest.);

        // add listeners
        rgOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                etOther.setError(null);
            }
        });
        btnRate.setOnClickListener(this);

        // load available options
        loadOptions();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_rate:
                rate();
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
        CommonRequests.options(this, this, user.getAccessToken(), OptionType.DRIVER_RATING.getValue());
    }

    /**
     * method, used to send rate request to the server
     */
    private void rate() {
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
        DriverRequests.rateTrip(this, this, user.getAccessToken(), tripRequest.getId(), ratingBar.getRating(), reasonId, comment);
    }

    @Override
    public void onSuccess(Object response, String apiName) {
        // hide loading
        progressDialog.dismiss();

        // check response type
        if (response instanceof OptionsResponse) {
            // this options response
            OptionsResponse optionsResponse = (OptionsResponse) response;

            // prepare options list & update the ui
            if (optionsResponse.getOptions() == null) {
                options = new ArrayList<>();
            } else {
                options = optionsResponse.getOptions();
            }
            updateUI();
        } else if (response instanceof GeneralResponse) {
            // this was rate trip request
            GeneralResponse generalResponse = (GeneralResponse) response;
            if (generalResponse.isSuccess()) {
                // start new customer home activity
                Intent intent = new Intent(DriverRateTripActivity.this, DriverHomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                // show error msg
                Utils.showLongToast(this, R.string.error_submitting_your_rating_please_try_again);
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
        layoutOptions.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFail(String message, String apiName) {
        // hide progress
        progressDialog.dismiss();

        // check the apiName
        if (apiName.equals(Const.MESSAGE_GET_OPTIONS)) {
            showOptionsError();
        } else {
            // show error toast
            Utils.showLongToast(this, R.string.connection_error);
        }
    }

    /**
     * method, used to show confirm dialog to try loading options again or just finish
     */
    private void showOptionsError() {
        // show confirm dialog
        String errorMsg = getString(R.string.error_fetching_available_options) + "\n" + getString(R.string.try_again);
        DialogUtils.showConfirmDialog(this, errorMsg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadOptions();
                dialog.dismiss();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }
}
