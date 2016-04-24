package com.privateegy.privatecar.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.privateegy.privatecar.Const;
import com.privateegy.privatecar.R;
import com.privateegy.privatecar.models.entities.Fare;
import com.privateegy.privatecar.models.entities.User;
import com.privateegy.privatecar.models.responses.FaresResponse;
import com.privateegy.privatecar.requests.CommonRequests;
import com.privateegy.privatecar.utils.AppUtils;
import com.privateegy.privatecar.utils.DateUtil;
import com.privateegy.privatecar.utils.PermissionUtil;
import com.privateegy.privatecar.utils.RequestHelper;
import com.privateegy.privatecar.utils.RequestListener;
import com.privateegy.privatecar.utils.Utils;

import java.util.Locale;

public class CustomerPricesFragment extends ProgressFragment implements RequestListener<FaresResponse> {
    public static final String TAG = CustomerPricesFragment.class.getName();

    private Activity activity;
    private View layoutMain;
    private RadioGroup rgTripType;
    private TextView tvCounterStartFare;
    private TextView tvKMFare;
    private TextView tvMinWaitFare;
    private TextView tvDesc;
    private View layoutCallUs;
    private Button btnCall;

    private int selectedRadioPosition; // used to hold the selected radio button item's position
    private RequestHelper requestHelper; // used to hold request helper object to cancel it if required

    public CustomerPricesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // create parent view
        super.onCreateView(inflater, container, savedInstanceState);

        // init views
        layoutMain = rootView.findViewById(R.id.layout_main);
        rgTripType = (RadioGroup) rootView.findViewById(R.id.rg_trip_type);
        tvCounterStartFare = (TextView) rootView.findViewById(R.id.tv_counter_start_fare);
        tvKMFare = (TextView) rootView.findViewById(R.id.tv_km_fare);
        tvMinWaitFare = (TextView) rootView.findViewById(R.id.tv_min_wait_fare);
        tvDesc = (TextView) rootView.findViewById(R.id.tv_desc);
        layoutCallUs = rootView.findViewById(R.id.layout_call_us);
        btnCall = (Button) rootView.findViewById(R.id.btn_call);

        // add radio group listener
        rgTripType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // get selected item index
                switch (checkedId) {
                    case R.id.rb_economy:
                        selectedRadioPosition = 0;
                        showFullDayView(false);
                        loadFares();
                        break;

                    case R.id.rb_business:
                        selectedRadioPosition = 1;
                        showFullDayView(false);
                        loadFares();
                        break;

                    case R.id.rb_full_day:
                        showFullDayView(true);
                        break;
                }
            }
        });

        // add call button click listener
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check call permission
                if (PermissionUtil.isGranted(activity, Manifest.permission.CALL_PHONE)) {
                    // show call customer service dialog
                    AppUtils.showCallCustomerServiceDialog(activity);
                } else {
                    // not granted
                    // request the permission
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, Const.PERM_REQ_CALL);
                }
            }
        });

        // load the first fares
        loadFares();

        return rootView;
    }

    /**
     * method, used to show / hide call layout &  main layout
     *
     * @param show
     */
    private void showFullDayView(boolean show) {
        layoutCallUs.setVisibility(show ? View.VISIBLE : View.GONE);
        layoutMain.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    /**
     * method, used to fetch fares from the server
     */
    private void loadFares() {
        // check the internet connection
        if (!Utils.hasConnection(activity)) {
            // show error view
            showError(R.string.no_internet_connection);
            return;
        }

        // show progress
        tvCounterStartFare.setText("0");
        tvKMFare.setText("0");
        tvMinWaitFare.setText("0");
        showProgress();

        // create & send the request
        User user = AppUtils.getCachedUser(activity);
        requestHelper = CommonRequests.fares(activity, this, user.getAccessToken(), "" + (selectedRadioPosition + 1), DateUtil.getCurrentTime());
    }

    @Override
    public void onSuccess(FaresResponse response, String apiName) {
        // check the fares
        if (!Utils.isNullOrEmpty(response.getFares())) {
            // update the ui
            updateUI(response.getFares().get(response.getFares().size() - 1));
            showMain();
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
            showError(errorMsg);
        }
    }

    /**
     * method, used to update the ui
     *
     * @param fare
     */
    private void updateUI(Fare fare) {
        tvCounterStartFare.setText(fare.getOpenFare() + " " + getString(R.string.currency));
        tvKMFare.setText(fare.getKilometerFare() + " " + getString(R.string.currency));
        tvMinWaitFare.setText(fare.getMinuteWaitFare() + " " + getString(R.string.currency));

        tvDesc.setText(Utils.getAppLanguage().equals(Locale.ENGLISH.getLanguage()) ? fare.getEnDesc() : fare.getArDesc());
    }

    @Override
    public void onFail(String message, String apiName) {
        // show error view
        showError(R.string.connection_error);
        Utils.LogE("ERROR: " + message);
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.fragment_customer_prices;
    }

    @Override
    protected int getMainViewResId() {
        return R.id.scroll_view;
    }

    @Override
    protected View.OnClickListener getRefreshListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFares();
            }
        };
    }

    @Override
    public void onDestroy() {
        // cancel request if still running
        if (requestHelper != null) requestHelper.cancel(false);
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Const.PERM_REQ_CALL:
                // check if granted
                if (PermissionUtil.isAllGranted(grantResults)) {
                    // granted
                    // show call customer service dialog
                    AppUtils.showCallCustomerServiceDialog(activity);
                } else {
                    // show msg
                    Utils.showShortToast(activity, R.string.we_need_call_permission_to_call_customer_service);
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
