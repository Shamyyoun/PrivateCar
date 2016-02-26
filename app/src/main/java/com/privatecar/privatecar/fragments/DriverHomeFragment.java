package com.privatecar.privatecar.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.activities.DriverHomeActivity;
import com.privatecar.privatecar.dialogs.GpsOptionDialog;
import com.privatecar.privatecar.models.entities.DriverAccountDetails;
import com.privatecar.privatecar.models.entities.User;
import com.privatecar.privatecar.models.responses.DriverAccountDetailsResponse;
import com.privatecar.privatecar.requests.DriverRequests;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.ButtonHighlighterOnTouchListener;
import com.privatecar.privatecar.utils.DialogUtils;
import com.privatecar.privatecar.utils.RequestListener;
import com.privatecar.privatecar.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DriverHomeFragment extends BaseFragment implements OnMapReadyCallback, View.OnClickListener, RequestListener {
    private DriverHomeActivity activity;
    private Button btnBeActive;
    private TextView tvMessage;
    private TextView tvDate;
    private TextView tvTodayProfit;
    private TextView tvTotalTrips;
    private TextView tvTotalHours;

    private GpsOptionDialog gpsOptionDialog;
    private ProgressDialog progressDialog;

    public DriverHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (DriverHomeActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_driver_home, container, false);

        // init views
        btnBeActive = (Button) fragment.findViewById(R.id.btn_be_active);
        btnBeActive.setOnTouchListener(new ButtonHighlighterOnTouchListener(getActivity(), R.drawable.petroleum_bottom_rounded_corners_shape));
        btnBeActive.setOnClickListener(this);
        tvMessage = (TextView) fragment.findViewById(R.id.tv_message);
        tvDate = (TextView) fragment.findViewById(R.id.tv_date);
        tvTodayProfit = (TextView) fragment.findViewById(R.id.tv_today_profit);
        tvTotalTrips = (TextView) fragment.findViewById(R.id.tv_total_trips);
        tvTotalHours = (TextView) fragment.findViewById(R.id.tv_total_hours);

        // update today's date
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        tvDate.setText(dateFormat.format(calendar.getTime()));

        // load account details
        loadAccountDetails();

        return fragment;
    }

    /**
     * method, used to update UI and set new values
     */
    private void updateUI(DriverAccountDetails detailsDriverAccountDetails) {
        tvTodayProfit.setText(detailsDriverAccountDetails.getCredit() + " " + getString(R.string.currency));
        tvTotalTrips.setText(detailsDriverAccountDetails.getTotaltrips() + " " + getString(R.string.trips));
        tvTotalHours.setText(detailsDriverAccountDetails.getTodayhours() + " " + getString(R.string.hours));
    }

    @Override
    public void onStart() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        super.onStart();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        GoogleMap mMap = googleMap;
        mMap.getUiSettings().setAllGesturesEnabled(false);

//        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_be_active:
                beActive(true);
                break;
        }
    }

    /**
     * method, used to fetch driver's account details from server
     */
    private void loadAccountDetails() {
        // show progress dialog
        progressDialog = DialogUtils.showProgressDialog(activity, R.string.loading_please_wait);

        // get cached user & send the request
        User user = AppUtils.getCachedUser(activity);
        DriverRequests.accountDetails(activity, this, user.getAccessToken());
    }

    /**
     * method, used to validate gps & send be active request to server
     */
    private void beActive(boolean active) {
        // check internet connection
        if (!Utils.hasConnection(activity)) {
            Utils.showShortToast(activity, R.string.no_internet_connection);
            return;
        }

        // check gps if he wanna be active
        if (active && !Utils.isGpsEnabled(activity)) {
            // show enable gps dialog
            gpsOptionDialog = new GpsOptionDialog(this);
            gpsOptionDialog.show();

            return;
        }

        // TODO send goonline request
    }

    @Override
    public void onSuccess(Object response, String apiName) {
        // dismiss progress dialog
        progressDialog.dismiss();

        // check request type
        if (response instanceof DriverAccountDetailsResponse) {
            // account details response
            DriverAccountDetailsResponse detailsResponse = (DriverAccountDetailsResponse) response;

            // check status
            if (detailsResponse != null && detailsResponse.getDriverAccountDetails() != null) {
                // response is valid
                // update ui in the fragment
                updateUI(detailsResponse.getDriverAccountDetails());

                // update personal info in the navigation drawer
                activity.updatePersonalInfo(detailsResponse.getDriverAccountDetails());
            } else {
                // invalid response
                // show error toast & exit
                Utils.showLongToast(activity, R.string.unexpected_error_try_again);
                activity.finish();
            }
        } else {

        }
    }

    @Override
    public void onFail(String message, String apiName) {
        // check api name
        if (apiName.equals(Const.MESSAGE_DRIVER_ACCOUNT_DETAILS)) {
            // show error toast & exits
            Utils.showLongToast(activity, message);
            Log.e(Const.LOG_TAG, message);
            activity.finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check request code
        if (requestCode == Const.REQ_GPS_SETTINGS) {
            gpsOptionDialog.dismiss();
            beActive(true);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
