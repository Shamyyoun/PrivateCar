package com.privatecar.privatecar.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.activities.CustomerHomeActivity;
import com.privatecar.privatecar.activities.CustomerPickupActivity;
import com.privatecar.privatecar.dialogs.GpsOptionDialog;
import com.privatecar.privatecar.models.entities.CustomerAccountDetails;
import com.privatecar.privatecar.models.entities.User;
import com.privatecar.privatecar.models.responses.CustomerAccountDetailsResponse;
import com.privatecar.privatecar.requests.CustomerRequests;
import com.privatecar.privatecar.utils.AppUtils;
import com.privatecar.privatecar.utils.DialogUtils;
import com.privatecar.privatecar.utils.RequestListener;
import com.privatecar.privatecar.utils.Utils;

public class BookALiftFragment extends BaseFragment implements OnMapReadyCallback, RequestListener<CustomerAccountDetailsResponse> {
    public static final String TAG = BookALiftFragment.class.getName();

    private CustomerHomeActivity activity;
    private View rootView;
    private TextView tvUserName, tvUserID;
    private View layoutPickNow, layoutPickLater;
    private GpsOptionDialog gpsOptionDialog;
    private boolean now; // boolean flag used to hold now value to re call pickup method after return from gps settings

    public BookALiftFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (CustomerHomeActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // check root view
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_book_alift, container, false);

            tvUserName = (TextView) rootView.findViewById(R.id.tv_user_name);
            tvUserID = (TextView) rootView.findViewById(R.id.tv_user_id);
            layoutPickNow = rootView.findViewById(R.id.layout_pick_now);
            layoutPickLater = rootView.findViewById(R.id.layout_pick_later);

            // add click listeners
            layoutPickNow.setOnClickListener(this);
            layoutPickLater.setOnClickListener(this);

            // load account details
            loadAccountDetails();
        }

        return rootView;
    }

    /**
     * method, used to fetch customer's account details from server
     */
    private void loadAccountDetails() {
        // show progress dialog
        progressDialog = DialogUtils.showProgressDialog(activity, R.string.loading_please_wait);

        // get cached user & send the request
        User user = AppUtils.getCachedUser(activity);
        CustomerRequests.accountDetails(activity, this, user.getAccessToken());
    }

    @Override
    public void onSuccess(CustomerAccountDetailsResponse response, String apiName) {
        // dismiss progress dialog
        progressDialog.dismiss();

        // check account details
        if (response.getAccountDetails() != null) {
            // response is valid
            // update ui in the fragment
            updateUI(response.getAccountDetails());

            // update personal info in the navigation drawer
            activity.updatePersonalInfo(response.getAccountDetails());

            // update cached user
            User user = AppUtils.getCachedUser(activity);
            user.setCustomerAccountDetails(response.getAccountDetails());
            AppUtils.cacheUser(activity, user);
        } else {
            // invalid response
            // show error toast & exit
            Utils.showLongToast(activity, R.string.unexpected_error_try_again);
            activity.onBackPressed();
        }
    }

    @Override
    public void onFail(String message, String apiName) {
        // dismiss progress dialog
        progressDialog.dismiss();

        // show error toast & exit
        Utils.showLongToast(activity, message);
        Log.e(Const.LOG_TAG, message);
        activity.onBackPressed();
    }

    /**
     * method, used to update UI and set new values
     */
    private void updateUI(CustomerAccountDetails accountDetails) {
        tvUserName.setText(accountDetails.getFullname());
        tvUserID.setText("" + accountDetails.getId());
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
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_pick_now:
                pickUp(true);
                break;

            case R.id.layout_pick_later:
                pickUp(false);
                break;
        }
    }

    /**
     * method, used to check GPS options and options pickup activity with now flag
     *
     * @param now boolean flag that is true if should pickup now and false otherwise
     */
    private void pickUp(boolean now) {
        this.now = now;

        // check internet connection
        if (!Utils.hasConnection(activity)) {
            // show error toast
            Utils.showShortToast(activity, R.string.no_internet_connection);
            return;
        }

        // check if gps is enabled
        if (!Utils.isGpsEnabled(activity)) {
            // show enable gps dialog
            gpsOptionDialog = new GpsOptionDialog(this);
            gpsOptionDialog.show();

            return;
        }

        // gps is enabled, open pickup activity
        Intent intent = new Intent(activity, CustomerPickupActivity.class);
        intent.putExtra(Const.KEY_NOW, now);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Const.REQUEST_GPS_SETTINGS) {
            gpsOptionDialog.dismiss();

            // check gps
            if (Utils.isGpsEnabled(activity)) {
                // enabled, re call pick up
                pickUp(now);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
