package com.privateegy.privatecar.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.privateegy.privatecar.Const;
import com.privateegy.privatecar.R;
import com.privateegy.privatecar.activities.CustomerHomeActivity;
import com.privateegy.privatecar.activities.CustomerPickupActivity;
import com.privateegy.privatecar.dialogs.GpsOptionDialog;
import com.privateegy.privatecar.models.entities.Config;
import com.privateegy.privatecar.models.entities.CustomerAccountDetails;
import com.privateegy.privatecar.models.entities.User;
import com.privateegy.privatecar.models.responses.CustomerAccountDetailsResponse;
import com.privateegy.privatecar.requests.CustomerRequests;
import com.privateegy.privatecar.utils.AppUtils;
import com.privateegy.privatecar.utils.DialogUtils;
import com.privateegy.privatecar.utils.PermissionUtil;
import com.privateegy.privatecar.utils.PlayServicesUtils;
import com.privateegy.privatecar.utils.RequestListener;
import com.privateegy.privatecar.utils.Utils;

public class CustomerBookALiftFragment extends BaseFragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, RequestListener<CustomerAccountDetailsResponse> {
    public static final String TAG = CustomerBookALiftFragment.class.getName();

    private CustomerHomeActivity activity;
    private View rootView;
    private TextView tvUserName, tvUserID;
    private View layoutPickNow, layoutPickLater;
    private GpsOptionDialog gpsOptionDialog;

    private GoogleMap map;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequestCoarse;

    //create coarse location request for updating the heat map
    private void createCoarseLocationRequest() {
        locationRequestCoarse = new LocationRequest();
        String intervalString = AppUtils.getConfigValue(getActivity(), Config.KEY_MAP_REFRESH_RATE);
        int interval = 1000 * (intervalString != null ? Integer.parseInt(intervalString) : Const.LOCATION_UPDATE_DURATION); //in milli sec
        locationRequestCoarse.setInterval(interval);
        locationRequestCoarse.setFastestInterval(interval);
        locationRequestCoarse.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    public CustomerBookALiftFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!PlayServicesUtils.isPlayServicesAvailable(getActivity())) {
            getActivity().onBackPressed();
            return;
        }

        // Create an instance of GoogleAPIClient.
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        createCoarseLocationRequest();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (CustomerHomeActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        super.onStart();

        googleApiClient.connect();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment.getView() != null) {
            mapFragment.getView().setClickable(false); //disable click events for lite mode map
            mapFragment.getView().setAlpha(0.5f); //set the map transparent
        }


        mapFragment.getMapAsync(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        googleApiClient.disconnect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setAllGesturesEnabled(false);
        map.getUiSettings().setMapToolbarEnabled(false);
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
    private void pickUp(final boolean now) {
        // check internet connection
        if (!Utils.hasConnection(activity)) {
            // show error toast
            Utils.showShortToast(activity, R.string.no_internet_connection);
            return;
        }

        // check if the user enabled the coarse location in the settings, if successful open CustomerPickupActivity

        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder().addLocationRequest(locationRequestCoarse);
        PendingResult<LocationSettingsResult> pendingResult =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());

        pendingResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates states = result.getLocationSettingsStates();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location requests here.

                        // location is enabled, open pickup activity
                        Intent intent = new Intent(activity, CustomerPickupActivity.class);
                        intent.putExtra(Const.KEY_NOW, now);
                        CustomerBookALiftFragment.this.startActivityForResult(intent, Const.REQUEST_TRIP_FULL_DAY);
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().
                            status.startResolutionForResult(getActivity(), Const.REQUEST_COARSE_LOCATION_PERMISSION); //CustomerHomeActivity receives the result
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the settings so we won't show the dialog.
                        break;
                }

            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
        //request locationRequestCoarse

        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder().addLocationRequest(locationRequestCoarse);
        PendingResult<LocationSettingsResult> pendingResult =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());

        pendingResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates states = result.getLocationSettingsStates();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location requests here.
                        getLastLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().
                            status.startResolutionForResult(getActivity(), Const.REQUEST_COARSE_LOCATION_PERMISSION); //CustomerHomeActivity receives the result
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    private void getLastLocation() {
        //TODO: support api 23 permission model
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation != null) {
                Utils.LogE(">>>>> Last Location: " + lastLocation.getLatitude() + ", " + lastLocation.getLongitude());

                updateMapLocation(lastLocation);
            } else {
                Utils.LogE("lastLocation == null");
            }
        }
    }

    private void updateMapLocation(Location location) {
        if (map != null) {
            LatLng curLocation = new LatLng(location.getLatitude(), location.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLng(curLocation));
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.REQUEST_TRIP_FULL_DAY && resultCode == Activity.RESULT_OK) {
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
