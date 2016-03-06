package com.privatecar.privatecar.fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.activities.DriverHomeActivity;
import com.privatecar.privatecar.models.entities.Trip;
import com.privatecar.privatecar.models.entities.User;
import com.privatecar.privatecar.models.responses.TripsResponse;
import com.privatecar.privatecar.requests.DriverRequests;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.DatePickerFragment;
import com.privatecar.privatecar.utils.DateUtil;
import com.privatecar.privatecar.utils.DialogUtils;
import com.privatecar.privatecar.utils.RequestListener;
import com.privatecar.privatecar.utils.Utils;

public class DriverStatementFragment extends BaseFragment implements View.OnClickListener,
        RequestListener<TripsResponse> {
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    private DriverHomeActivity activity;
    private TextView tvStatus;
    private TextView tvLastTripPrice;
    private TextView tvLastTripDate;
    private TextView tvFrom;
    private TextView tvTo;
    private ImageButton ibFrom;
    private ImageButton ibTo;
    private Button btnSearch;
    private DatePickerFragment fromPickerFragment;
    private DatePickerFragment toPickerFragment;
    private String fromDateStr;
    private String toDateStr;
    private ProgressDialog progressDialog;

    public DriverStatementFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (DriverHomeActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_driver_statement, container, false);

        // init views
        tvStatus = (TextView) fragment.findViewById(R.id.tv_status);
        tvLastTripPrice = (TextView) fragment.findViewById(R.id.tv_last_trip_price);
        tvLastTripDate = (TextView) fragment.findViewById(R.id.tv_last_trip_date);
        tvFrom = (TextView) fragment.findViewById(R.id.tv_from);
        tvTo = (TextView) fragment.findViewById(R.id.tv_to);
        ibFrom = (ImageButton) fragment.findViewById(R.id.ib_from);
        ibFrom.setOnClickListener(this);
        ibTo = (ImageButton) fragment.findViewById(R.id.ib_to);
        ibTo.setOnClickListener(this);
        btnSearch = (Button) fragment.findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(this);

        // update status
        User user = AppUtils.getCachedUser(activity);
        tvStatus.setText(getString(user.isOnline() ? R.string.online : R.string.offline));

        // load last trip
        loadLastTrip();

        return fragment;
    }

    private void loadLastTrip() {
        // show loading
        progressDialog = DialogUtils.showProgressDialog(activity, R.string.loading_please_wait);

        // create & send the request
        User user = AppUtils.getCachedUser(activity);
        DriverRequests.lastTrip(activity, this, user.getAccessToken());
    }

    @Override
    public void onSuccess(TripsResponse response, String apiName) {
        // hide loading
        progressDialog.dismiss();

        // check last trip
        if (response.getTrips() != null && response.getTrips().size() != 0) {
            // render response to the ui
            Trip trip = response.getTrips().get(response.getTrips().size() - 1);
            tvLastTripPrice.setText(trip.getEstimateFare() + " " + getString(R.string.currency));
            tvLastTripDate.setText(DateUtil.formatDate(trip.getPickupDateTime(), "yyyy-MM-dd hh:mm:ss", DATE_FORMAT));
        }
    }

    @Override
    public void onFail(String message, String apiName) {
        progressDialog.dismiss();

        // show error toast
        Utils.showLongToast(activity, message);
        Log.e(Const.LOG_TAG, message);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_from:
                // create new instance of from picker fragment
                fromPickerFragment = new DatePickerFragment();

                // pass from date if exists
                fromPickerFragment.setDate(fromDateStr, DATE_FORMAT);

                // show it
                fromPickerFragment.setDatePickerListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        fromDateStr = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        tvFrom.setText(fromDateStr);
                    }
                });
                fromPickerFragment.show(activity.getSupportFragmentManager(), "FromPickerFragment");
                break;

            case R.id.ib_to:
                // create new instance of to picker fragment
                toPickerFragment = new DatePickerFragment();

                // pass to date if exists
                toPickerFragment.setDate(toDateStr, DATE_FORMAT);

                // show it
                toPickerFragment.setDatePickerListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        toDateStr = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        tvTo.setText(toDateStr);
                    }
                });
                toPickerFragment.show(activity.getSupportFragmentManager(), "ToPickerFragment");
                break;
        }
    }
}
